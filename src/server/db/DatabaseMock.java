package server.db;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class DatabaseMock {

  public static void addUser(String username, String plainPassword, String plainSecret) {
    String hash = BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    FileManager.appendUser(username, hash, plainSecret);
  }

  public static String[] getUser(String username) {
    return FileManager.getByKey(username);
  }

  public static void main(String[] args) {
  }
}
