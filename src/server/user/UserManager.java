package server.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import server.db.DatabaseMock;
import shared.AsymmetricKeyManager;
import shared.DataConverter;

public class UserManager {
  private static final byte[] CHALLENGE = "CHALLENGE_COMP4017".getBytes();

  public static boolean auth(String username, String plainPassword, int token, byte[] signedData) {
    User user = DatabaseMock.getUser(username);
    String encryptedPassword = user.getPasswordHash();
    String tfaSecret = user.getTfaSecret();
    byte[] publicKey = DataConverter.stringToKeyBytes(user.getPublicKey());

    BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), encryptedPassword);
    boolean isPasswordValid = result.verified;

    GoogleAuthenticator gAuth = new GoogleAuthenticator();
    boolean isTokenValid = gAuth.authorize(tfaSecret, token);

    boolean isDigitalSignatureValid = AsymmetricKeyManager.verifyData(CHALLENGE, signedData, publicKey);

    System.out.println("> Logging in: " + username);
    System.out.println(">>> Password OK: " + isPasswordValid);
    System.out.println(">>> Token OK: " + isTokenValid);
    System.out.println(">>> Digital Signature OK: " + isDigitalSignatureValid);

    return isPasswordValid && isTokenValid && isDigitalSignatureValid;
  }

  public static String register(String username, String plainPassword, String publicKey) {
    String hash = BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    GoogleAuthenticator gAuth = new GoogleAuthenticator();
    String tfaSecret = gAuth.createCredentials().getKey();

    DatabaseMock.addUser(new User(username, hash, publicKey, tfaSecret));

    return tfaSecret;
  }
}
