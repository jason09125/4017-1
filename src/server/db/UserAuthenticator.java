package server.db;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserAuthenticator {
  public static boolean auth(String username, String plainPassword, String token) {
    String[] items = DatabaseMock.getUser(username);
    String encryptedPassword = items[0];
    String tfaSecret = items[1];

    BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), encryptedPassword);
    boolean validPassword = result.verified;

    boolean validToken = true;

    return validPassword && validToken;
  }

  public static void main(String[] args) {
  }
}
