package client.chat;

import client.auth.ClientAuthenticator;

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
  ClientAuthenticator clientAuthenticator;

  public ChatClient(String serverName, int serverPort) {
    this.clientAuthenticator = new ClientAuthenticator("./client-config/config.properties");

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

          if (line.matches("^\\.login .*$")) { // ---- send authentication request ----
            System.out.println("Logging in");
            String[] items = line.split("\\s+");
            String username = items[1];
            String password = items[2];
            int token = Integer.parseInt(items[3]);
            String cmd = clientAuthenticator.getLoginCommand(username, password, token);
            streamOut.writeUTF(cmd);
            streamOut.flush();
            continue;
          }

          streamOut.writeUTF(line);
          streamOut.flush();
        } catch (IOException ioe) {
          System.out.println("Sending error: " + ioe.getMessage());
          stop();
        }
      }
  }

  void handle(String msg) {
    if (msg.equals(".bye")) {
      System.out.println("Good bye. Press RETURN to exit ...");
      stop();
    } else
      System.out.println(msg);
  }

  private void start() throws IOException {
    console = new BufferedReader(new InputStreamReader(System.in));
    streamOut = new DataOutputStream(socket.getOutputStream());
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
