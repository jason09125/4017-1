package server.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import server.db.DatabaseMock;
import shared.AsymmetricCrypto;
import shared.DataConverter;
import shared.SymmetricCrypto;

import java.util.HashMap;

public class UserManager {
  private static HashMap<String, byte[]> userSessionKeyMap = new HashMap<>();
  public static boolean delete(String username) {
    return DatabaseMock.deleteUser(username);
  }

  public static boolean auth(String username, String plainPassword, int token, String challenge, byte[] signedData) {
    User user = DatabaseMock.getUser(username);
    String encryptedPassword = user.getPasswordHash();
    String tfaSecret = user.getTfaSecret();
    byte[] publicKey = getPublicKey(username);

    BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), encryptedPassword);
    boolean isPasswordValid = result.verified;

    GoogleAuthenticator gAuth = new GoogleAuthenticator();
    boolean isTokenValid = gAuth.authorize(tfaSecret, token);

    boolean isDigitalSignatureValid = AsymmetricCrypto.verifyData(challenge.getBytes(), signedData, publicKey);

    System.out.println("\t\t> Logging in: " + username);
    System.out.println("\t\t>>> Password OK: " + isPasswordValid);
    System.out.println("\t\t>>> Token OK: " + isTokenValid);
    System.out.println("\t\t>>> Digital Signature OK: " + isDigitalSignatureValid);

    return isPasswordValid && isTokenValid && isDigitalSignatureValid;
  }

  public static String register(String username, String plainPassword, String publicKey) {
    if (username == null || username.equals("")) {
      System.out.println("> Failed to register: username cannot be empty");
      return null;
    }
    if (username.contains(" ") || username.contains("=")) {
      System.out.println("> Failed to register: username cannot contain empty space or special characters");
    }
    if (DatabaseMock.isExisting(username)) {
      System.out.println("> Failed to register: username already exists");
      return null;
    }
    if (plainPassword == null || plainPassword.length() < 6) {
      System.out.println("> Failed to register: password should have at least 6 characters");
      return null;
    }
    if (publicKey == null || publicKey.length() != 216) {
      System.out.println("> Failed to register: public key is not a valid base64 string");
      return null;
    }
    String hash = BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    GoogleAuthenticator gAuth = new GoogleAuthenticator();
    String tfaSecret = gAuth.createCredentials().getKey();

    DatabaseMock.addUser(new User(username, hash, publicKey, tfaSecret));

    return tfaSecret;
  }

  public static void generateSessionKey(String username) {
    byte[] key = SymmetricCrypto.generateSecretKey();
    userSessionKeyMap.put(username, key);
  }

  public static byte[] getSessionKey(String username, boolean shouldEncrypt) {
    byte[] sessionKey = userSessionKeyMap.get(username);
    if (!shouldEncrypt) {
      return sessionKey;
    }
    User user = DatabaseMock.getUser(username);
    if (user == null) {
      return null;
    }
    byte[] userPubKey = DataConverter.base64ToBytes(user.getPublicKey());
    return AsymmetricCrypto.encryptWithPublicKey(sessionKey, userPubKey);
  }

  public static byte[] getPublicKey(String username) {
    User user = DatabaseMock.getUser(username);
    return DataConverter.base64ToBytes(user.getPublicKey());
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Usage: java UserManager username password public-key");
    } else {
      String username = args[0];
      String password = args[1];
      String publicKey = args[2];
      String tfaSecret = UserManager.register(username, password, publicKey);
      if (tfaSecret != null) {
        System.out.printf("> User Manager on server: New user registered: %s\n", username);
        System.out.println("> User Manager on server: Please store this in app like Google Authenticator");
        System.out.printf("------ 2FA Secret Key ------\n%s\n\n", tfaSecret);
      }
    }
  }
}
