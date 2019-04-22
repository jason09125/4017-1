package server.db;

import server.user.User;

import java.io.IOException;

public class DatabaseMock {
  public static boolean isExisting(String username) {
    try {
      User user = FileManager.getByKey(username);
      if (user != null && user.getUsername().toUpperCase().equals(username.toUpperCase())) {
        return true;
      }
    } catch (Exception e) {
    }
    return false;
  }

  public static void addUser(User user) {
    FileManager.addUser(user);
  }

  public static User getUser(String username) {
    try {
      return FileManager.getByKey(username);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void main(String[] args) {
  }

  public static boolean deleteUser(String username) {
    return FileManager.deleteUser(username);
  }
}
