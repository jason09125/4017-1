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

  public static boolean deleteUser(String username) {
    return FileManager.deleteUser(username);
  }
}
