package test;

import server.user.UserManager;
import shared.AsymmetricKeyManager;
import shared.DataConverter;

import java.security.KeyPair;

public class Test {

  public static KeyPair createUser() {
    //
    // 1. user generate key pair

    /* Actual step in production:
     *  1. User generates key pair
     *  2. User keeps the private key
     *  3. User sends username, password, and the public key to the server
     *  4. User retrieves the 2FA secret from server and stores it in app like "Google Authenticator"
     *  5. Register completed
     * */

    String username = "Eric";
    String plainPassword = "123456";

    KeyPair keyPair = AsymmetricKeyManager.generateKeyPair();

    System.out.println("Client: Please store the following credentials secretly");
    System.out.printf("------ Private Key ---------\n%s\n\n", DataConverter.keyToString(keyPair.getPrivate()));

    System.out.println("Client: Start to ask server to register");
    String twoFactorAuthSecret = UserManager.register(username, plainPassword, DataConverter.keyToString(keyPair.getPublic()));

    System.out.printf("Server: New user registered: %s\n", username);
    System.out.println("Server: Please store this in app like Google Authenticator");
    System.out.printf("------ 2FA Secret Key ------\n%s\n\n", twoFactorAuthSecret);

    return keyPair;
  }

  public static void authUser(int token, byte[] privKey) {
    String username = "Eric";
    String plainPassword = "123456";

    byte[] signed = AsymmetricKeyManager.signData("CHALLENGE_COMP4017".getBytes(), privKey);

    boolean isAuthenticated = UserManager.auth(username, plainPassword, token, signed);
    System.out.println("Server: authentication status is " + isAuthenticated);
  }

  public static void main(String[] args) {
    // clean up before testing
//    UserManager.delete("Eric");
//
//    KeyPair kp = createUser(); // this creates a user

    int token = 450087;
    byte[] privKey = DataConverter.stringToKeyBytes("MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUICzNP88jxRETok7dgRGPbT4aUm8=");
    authUser(token, privKey); // this authenticates a user
  }
}
