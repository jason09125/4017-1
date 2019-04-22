package client.chat;

import client.auth.ClientAuthenticator;
import client.auth.PublicKeysStorage;
import client.message.MessageHandler;
import shared.AsymmetricCrypto;
import shared.DataConverter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient implements Runnable {
  private Socket socket = null;
  private volatile Thread thread = null;
  private BufferedReader console = null;
  private DataOutputStream streamOut = null;
  private ChatClientThread client = null;
  private ClientAuthenticator clientAuthenticator;
  private PublicKeysStorage publicKeysStorage;
  private MessageHandler messageHandler;
  private String challengeForServer;
  private String challengeFromServer;
  private String username;

  public ChatClient(String serverName, int serverPort) {
    this.clientAuthenticator = new ClientAuthenticator("./client-config/config.properties");
    this.publicKeysStorage = new PublicKeysStorage();
    this.messageHandler = new MessageHandler(this.clientAuthenticator, this.publicKeysStorage);

    System.out.println("Establishing connection. Please wait ...");
    try {
      socket = new Socket(serverName, serverPort);
      System.out.println("Connected: " + socket);
      start();
    } catch (UnknownHostException uhe) {
      System.out.println("Host unknown: " + uhe.getMessage());
    } catch (IOException ioe) {
      System.out.println("Unexpected exception: " + ioe.getMessage());
    }
  }

  public void run() {
    Thread thisThread = Thread.currentThread();
    while (thread == thisThread)
      while (thread != null) {
        try {
          String line = console.readLine();

          // check human command
          if (line.matches("^\\.login .*$")) { // ---- send authentication request ----
            System.out.println("Logging in");
            String[] items = line.split("\\s+");
            String username = items[1];
            String password = items[2];
            int token = Integer.parseInt(items[3]);
            String cmd = clientAuthenticator.getLoginCommand(username, password, token, this.challengeFromServer);
            streamOut.writeUTF(cmd);
            streamOut.flush();
            continue;
          }

          // send message
          String cipherText = messageHandler.getDeliverable(line);
          streamOut.writeUTF(String.format("COMMAND SEND_MESSAGE %s %s", this.username, cipherText));
          streamOut.flush();
        } catch (IOException ioe) {
          System.out.println("Sending error: " + ioe.getMessage());
          stop();
        }
      }
  }

  void handle(String msg) {
    System.out.println("Handling: " + msg);

    // check machine command
    if (msg.matches("^COMMAND .*$")) {
      String[] items = msg.split("\\s+");
      String action = items[1];
      if (action.equals("CHALLENGE")) {
        System.out.println("Challenge from server received, will provide digital signature when login");
        this.challengeFromServer = items[2];
      }

      if (action.equals("NEW_USER")) {
        String username = items[2];
        String publicKey = items[3];
        System.out.printf("New user (%s) joined, public key: %s\n", username, publicKey);
        publicKeysStorage.setUserPublicKeyMap(username, DataConverter.base64ToBytes(publicKey));
      }

      if (action.equals("DELIVER_MESSAGE")) {
        String senderUsername = items[2];
        String encryptedWithSessionKey = items[3];
        String plainText = messageHandler.parseIncoming(senderUsername, encryptedWithSessionKey);
        System.out.println(plainText);
      }
      return;
    }

    // check machine response
    if (msg.matches("^RESPONSE .*$")) {
      String[] items = msg.split("\\s+");
      String action = items[1];
      if (action.equals("AUTH")) {
        String result = items[2];
        if (result.equals("200")) {
          System.out.println(">> [Server]: Authenticated");
          String username = items[3];
          String sessionKey = items[4];
          this.username = username;
          byte[] encryptedWithSelfPublicKey = DataConverter.base64ToBytes(sessionKey);
          clientAuthenticator.setSessionKey(encryptedWithSelfPublicKey, true);
        } else {
          System.out.println(">> [Server]: Authentication failed - " + result);
        }
      }
      if (action.equals("SERVER_SIGNATURE")) {
        String result = items[2];
        if (result.equals("200")) {
          byte[] signature = DataConverter.base64ToBytes(items[3]);
          boolean verified = AsymmetricCrypto.verifyData(this.challengeForServer.getBytes(), signature, clientAuthenticator.getServerPublicKey());
          if (verified) {
            System.out.println("Server is verified by digital signature, safe to log in");
          } else {
            System.out.println("[WARNING] Server has INVALID digital signature, this server could be forged");
          }
        }
      }
      if (action.equals("SEND_MESSAGE")) {
        String result = items[2];
        if (!result.equals("200")) {
          System.out.println("Failed to send message, server responds " + result + " " + items[3]);
        }
      }
      return;
    }

    if (msg.equals(".bye")) {
      System.out.println("Good bye. Press RETURN to exit ...");
      stop();
    } else {
      System.out.println(msg);
    }
  }

  private void start() throws IOException {
    console = new BufferedReader(new InputStreamReader(System.in));
    streamOut = new DataOutputStream(socket.getOutputStream());

    // ---- request to authenticate server ----
    this.challengeForServer = Double.toString(Math.random());
    String cmd = "COMMAND SERVER_AUTH " + this.challengeForServer;
    System.out.println("Challenging server, checking it's identity");
    streamOut.writeUTF(cmd);

    if (thread == null) {
      client = new ChatClientThread(this, socket);
      thread = new Thread(this);
      thread.start();
    }
  }

  void stop() {
    if (thread != null) {
      thread = null;
    }
    try {
      if (console != null) console.close();
      if (streamOut != null) streamOut.close();
      if (socket != null) socket.close();
    } catch (IOException ioe) {
      System.out.println("Error closing ...");
    }
    client.close();
    client.stopThread();
  }

  public static void main(String args[]) {
    ChatClient client = null;
    if (args.length != 2)
      System.out.println("Usage: java ChatClient host port");
    else
      client = new ChatClient(args[0], Integer.parseInt(args[1]));
  }
}
