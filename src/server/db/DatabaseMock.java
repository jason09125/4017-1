package server.db;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.warrenstrange.googleauth.GoogleAuthenticator;

public class DatabaseMock {

  public static String registerUser(String username, String plainPassword) {
    String hash = BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());

    GoogleAuthenticator gAuth = new GoogleAuthenticator();
    String tfaSecret = gAuth.createCredentials().getKey();

    FileManager.appendUser(username, hash, tfaSecret);

    return tfaSecret;
  }

  public static String[] getUser(String username) {
    return FileManager.getByKey(username);
  }

  public static void main(String[] args) {
  }
}
