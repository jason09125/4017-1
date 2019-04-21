package server.chat;

import server.user.UserManager;
import shared.DataConverter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ChatServer implements Runnable {
  private ChatServerThread clients[] = new ChatServerThread[50];
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

    if (input.matches("^COMMAND .*$")) {
      String items[] = input.split(" ");
      if (items.length < 2) {
        return;
      }
      String action = items[1];
      if (action.equals("LOGIN")) {
        String username = items[2];
        String password = items[3];
        int token = Integer.parseInt(items[4]);
        byte[] signature = DataConverter.base64ToBytes(items[5]);
        String challenge = clientChallengeMap.get(ID);
        boolean authenticated = UserManager.auth(username, password, token, challenge, signature);
        if (authenticated) {
          byte[] key = UserManager.generateSessionKey(username);
          String keyStr = DataConverter.bytesToBase64(key);
          clients[findClient(ID)].send("RESPONSE AUTH 200 " + keyStr);
        } else {
          clients[findClient(ID)].send("RESPONSE AUTH 401");
        }
        return;
      }
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
