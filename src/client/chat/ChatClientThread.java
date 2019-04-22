package client.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClientThread extends Thread {
  private Socket socket = null;
  private ChatClient client = null;
  private DataInputStream streamIn = null;
  private volatile Thread thread = null;

  public ChatClientThread(ChatClient _client, Socket _socket) {
    client = _client;
    socket = _socket;
    open();
    start();
  }

  private void open() {
    try {
      streamIn = new DataInputStream(socket.getInputStream());
    } catch (IOException ioe) {
      System.out.println("Error getting input stream: " + ioe);
      client.stop();
    }
  }

  void close() {
    try {
      if (streamIn != null) streamIn.close();
    } catch (IOException ioe) {
      System.out.println("Error closing input stream: " + ioe);
    }
  }

  public void run() {
    Thread thisThread = Thread.currentThread();
    while (thread == thisThread) {
      try {
        client.handle(streamIn.readUTF());
      } catch (IOException ioe) {
        System.out.println("Listening error: " + ioe.getMessage());
        System.out.println(">> You might be seeing this message because:\n\t1) Server is down at the moment; or\n\t2) You have been inactive for a while and server disconnects you");
        client.stop();
      }
    }
  }

  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  void stopThread() {
    thread = null;
  }
}
