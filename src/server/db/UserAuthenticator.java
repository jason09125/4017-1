package server.db;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.warrenstrange.googleauth.GoogleAuthenticator;

public class UserAuthenticator {
  public static boolean auth(String username, String plainPassword, int token) {
    String[] items = DatabaseMock.getUser(username);
    String encryptedPassword = items[0];
    String tfaSecret = items[1];

    BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), encryptedPassword);
    boolean isPasswordValid = result.verified;

    GoogleAuthenticator gAuth = new GoogleAuthenticator();
    boolean isTokenValid = gAuth.authorize(tfaSecret, token);

    System.out.println("> Logging in: " + username);
    System.out.println(">>> Password OK: " + isPasswordValid);
    System.out.println(">>> Token OK: " + isTokenValid);

    return isPasswordValid && isTokenValid;
  }

  public static void main(String[] args) {
    UserAuthenticator.auth("Eric", "123456", 807139);
  }
}
