package server.chat;

import server.auth.ServerAuthenticator;
import server.message.MessageHandler;
import server.user.UserManager;
import shared.DataConverter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ChatServer implements Runnable {
  private ChatServerThread clients[] = new ChatServerThread[50];
  private HashMap<Integer, String> clientUsernameMap = new HashMap<>();
  private HashMap<Integer, Boolean> clientAuthMap = new HashMap<>();
  private HashMap<Integer, String> clientChallengeMap = new HashMap<>();
  private ServerSocket server = null;
  private volatile Thread thread = null;
  private int clientCount = 0;

  public ChatServer(int port) {
    try {
      System.out.println("Binding to port " + port + ", please wait  ...");
      server = new ServerSocket(port);
      System.out.println("Server started: " + server);
      start();
    } catch (IOException ioe) {
      System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
    }
  }

  public void run() {
    Thread thisThread = Thread.currentThread();
    while (thread == thisThread) {
      try {
        System.out.println("Waiting for a client ...");
        addThread(server.accept());
      } catch (IOException ioe) {
        System.out.println("Server accept error: " + ioe);
        stop();
      }
    }
  }

  private void start() {
    if (thread == null) {
      thread = new Thread(this);
      thread.start();
    }
  }

  private void stop() {
    if (thread != null) {
      thread = null;
    }
  }

  private int findClient(int ID) {
    for (int i = 0; i < clientCount; i++)
      if (clients[i].getID() == ID)
        return i;
    return -1;
  }

  synchronized void handle(int ID, String input) {
    System.out.printf(">>> Handling %s: %s\n", ID, input);

    // check machine command
    if (input.matches("^COMMAND .*$")) {
      String items[] = input.split(" ");
      if (items.length < 2) {
        return;
      }
      String action = items[1];
      if (action.equals("SERVER_AUTH")) {
        String challenge = items[2];
        System.out.printf("Challenged by client %s: %s\n", ID, challenge);
        String signatureStr = DataConverter.bytesToBase64(ServerAuthenticator.signChallenge(challenge));
        System.out.println("Digital signature generated: " + signatureStr);
        clients[findClient(ID)].send("RESPONSE SERVER_SIGNATURE 200 " + signatureStr);
      }

      if (action.equals("LOGIN")) {
        String username = items[2];
        String password = items[3];
        int token = Integer.parseInt(items[4]);
        byte[] signature = DataConverter.base64ToBytes(items[5]);
        String challenge = clientChallengeMap.get(ID);
        boolean authenticated = UserManager.auth(username, password, token, challenge, signature);
        if (authenticated) {
          UserManager.generateSessionKey(username);
          byte[] key = UserManager.getSessionKey(username, true);
          String sessionKeyStr = DataConverter.bytesToBase64(key);
          clients[findClient(ID)].send(String.format("RESPONSE AUTH 200 %s %s", username, sessionKeyStr));
          clientUsernameMap.put(ID, username);
          clientAuthMap.put(ID, true);

          // broadcast client's public key
          String pubKeyStr = DataConverter.bytesToBase64(UserManager.getPublicKey(username));
          System.out.printf("Authenticated - Broadcasting user's public key to other connected clients: %s %s\n", username, pubKeyStr);
          for (int i = 0; i < clientCount; i++) {
            // check authentication before sending this, filter out those not authenticated
            if (!clientAuthMap.get(clients[i].getID())) {
              continue;
            }
            clients[i].send("COMMAND NEW_USER " + username + " " + pubKeyStr);
          }

        } else {
          clients[findClient(ID)].send("RESPONSE AUTH 401");
        }
      }

      if (action.equals("SEND_MESSAGE")) {
        String senderUsername = items[2];
        if (!senderUsername.equals(clientUsernameMap.get(ID)) || !clientAuthMap.get(ID)) {
          clients[findClient(ID)].send("RESPONSE SEND_MESSAGE 401 Unauthorized");
          return;
        }
        String cipherText = items[3];
        byte[] signedByServer = MessageHandler.parseFromClientAndSign(senderUsername, UserManager.getPublicKey(senderUsername), cipherText);
        for (int i = 0; i < clientCount; i++) {
          // filter out those not authenticated
          if (!clientAuthMap.get(clients[i].getID())) {
            continue;
          }
          String deliverableMessage = MessageHandler.getDeliverable(clientUsernameMap.get(clients[i].getID()), signedByServer);

          clients[i].send("COMMAND DELIVER_MESSAGE " + senderUsername + " " + deliverableMessage);
        }
      }
      return;
    }

    if (input.equals(".bye")) {
      clients[findClient(ID)].send(".bye");
      remove(ID);
    } else
      for (int i = 0; i < clientCount; i++) {
        clients[i].send(ID + ": " + input);
      }
  }

  synchronized void remove(int ID) {
    int pos = findClient(ID);
    if (pos >= 0) {
      ChatServerThread toTerminate = clients[pos];
      int idToRemove = clients[pos].getID();
      clientChallengeMap.remove(idToRemove);
      clientAuthMap.remove(idToRemove);
      clientUsernameMap.remove(idToRemove);

      System.out.println("Removing client thread " + ID + " at " + pos);
      if (pos < clientCount - 1)
        for (int i = pos + 1; i < clientCount; i++) {
          clients[i - 1] = clients[i];
        }
      clientCount--;
      try {
        toTerminate.close();
      } catch (IOException ioe) {
        System.out.println("Error closing thread: " + ioe);
      }
      toTerminate.stopThread();
    }
  }

  private void addThread(Socket socket) {
    if (clientCount < clients.length) {
      System.out.println("Client accepted: " + socket);
      clients[clientCount] = new ChatServerThread(this, socket);
      try {
        clients[clientCount].open();
        clients[clientCount].start();

        // ---- send challenge ----
        String challenge = Double.toString(Math.random());
        int clientId = clients[clientCount].getID();
        clientChallengeMap.put(clientId, challenge);
        clients[clientCount].send("COMMAND CHALLENGE " + challenge);
        System.out.printf(">>> Challenging client %s: %s\n", clientId, challenge);

        clientCount++;
      } catch (IOException ioe) {
        System.out.println("Error opening thread: " + ioe);
      }
    } else
      System.out.println("Client refused: maximum " + clients.length + " reached.");
  }

  public static void main(String args[]) {
    ChatServer server = null;
    if (args.length != 1) {
      System.out.println("Usage: java ChatServer port");
    } else {
      server = new ChatServer(Integer.parseInt(args[0]));
    }
  }
}
