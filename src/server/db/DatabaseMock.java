package server.db;

import server.user.User;

public class DatabaseMock {

  public static void addUser(User user) {
    FileManager.addUser(user);
  }

  public static User getUser(String username) {
    return FileManager.getByKey(username);
  }

  public static void main(String[] args) {
  }
}
