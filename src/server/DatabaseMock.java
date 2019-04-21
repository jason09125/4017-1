package server;

import server.file.FileManager;

public class DatabaseMock {
  public DatabaseMock() {
    FileManager.init();
  }

  public void addUser(String username, String plainPassword, String plainSecret) {
    FileManager.appendUser(username, plainPassword, plainSecret);
  }

  public String[] getUser(String username) {
    return FileManager.getByKey(username);
  }

  public static void main(String[] args) {
  }
}
