package test;

import server.auth.ServerAuthenticator;
import server.user.UserManager;
import shared.AsymmetricCrypto;
import shared.DataConverter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.util.Properties;

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

    KeyPair keyPair = AsymmetricCrypto.generateKeyPair();

    System.out.println("Client: Please store the private key and 2FA secret safely");
    System.out.printf("------ Private Key ---------\n%s\n\n", DataConverter.keyToBase64(keyPair.getPrivate()));

    System.out.printf("------ Public Key ---------\n%s\n\n", DataConverter.keyToBase64(keyPair.getPublic()));

    System.out.println("Client: Start to ask server to register");
    String twoFactorAuthSecret = UserManager.register(username, plainPassword, DataConverter.keyToBase64(keyPair.getPublic()));

    System.out.printf("Server: New user registered: %s\n", username);
    System.out.println("Server: Please store this in app like Google Authenticator");
    System.out.printf("------ 2FA Secret Key ------\n%s\n\n", twoFactorAuthSecret);

    return keyPair;
  }

  public static void authUser(int token, byte[] privKey) {
    String username = "Eric";
    String plainPassword = "123456";

    byte[] signed = AsymmetricCrypto.signData("CHALLENGE_COMP4017".getBytes(), privKey);

    boolean isAuthenticated = UserManager.auth(username, plainPassword, token, "CHALLENGE_COMP4017", signed);
    System.out.println("Server: authentication status is " + isAuthenticated);
  }

  public static void authServer() {
    String challenge = "kdlghfasdm9r04c90rmq49mc9dsm-fadsf";
    byte[] signed = ServerAuthenticator.signChallenge(challenge);

    try (InputStream input = new FileInputStream("./client-config/config.properties")) {
      Properties prop = new Properties();
      prop.load(input);
      byte[] publicKey = DataConverter.base64ToBytes(prop.getProperty("SERVER_MASTER_PUBLIC_KEY"));
      boolean verified = AsymmetricCrypto.verifyData(challenge.getBytes(), signed, publicKey);
      System.out.println("Server authentication result: " + verified);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
//    UserManager.delete("Eric"); // clean up before testing
//    KeyPair kp = createUser(); // this creates a user

//    System.out.println(DataConverter.keyToBase64(kp.getPublic()));
//    System.out.println(DataConverter.keyToBase64(kp.getPrivate()));

    // This is the secret for 2FA------->>> 4ZC2262UODCPBI3A
//    int token =500383;
//    byte[] privKey = DataConverter.base64ToBytes("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI48+BwLiYLbrMLe0K3xNtGpFgLa3VLCbluYBb2MFaFbGUCpRebbsBJQajViEmcCHW7zg7V2agxxjwpS1m0kX/sXDoilxZYe7WDjluEj6cqCSNXYHGuByBKJRfyazq3FvjvQh6n/Q2RFmf3HdNuJ9ZCrGMfYUxV5X06FR+ZK9cjXAgMBAAECgYB0ev9f0B7jV8xJpThVSbTvyz0oR1152ZmQTpVc3SwVgEnUxwpkfMHarZncb5zMWFIMO0U/xGIiIJjYBnBs3p3t+XJyiaZ/J6D6YaTW/3dc1cwBtyUiPIWdBdQEQMni12OJrUiQHzeZPNvk1r3+ZNde+WVmNsgLGCRUKnlV91KLyQJBAPfZUvZ3Kuz486YhqGVLJeUJyVOs464pZHO0NFe634G1ImmcWrpmXIg2JHfwYMFLeodh99lJRUooa9ia5QECXUUCQQCS6n4qBf4HX1/WlmTQH7GAgM4tgOapBRa/8roYmtTPEa9T/aJL2k3tiGXcGphQyVI5KHzayj/DTRfO6Y2UkelrAkAWajIllhtsuQsYAD1Bg+1WbG8nwSAKNTYffLGrKXxjN6V4Fari5rUBoJvluPiXIqNfMQ4AOa8piMRQH5oMYFFdAkBGAS50374XzT5hhfArq65syPN1g0Jlr2MTu5kpOD3HHWop32WCN1eCo8fFhXamqAdh7QTxTAXuDcIWeftYm95ZAkEAhvP8l+HhqJGylxssqJ3OpGtCPD+yNGSpkejIH+MpajIv8cGeKRSg00uXM2G4nx+iWHuMvO1+aBYaJyxLT5N4pg==");
//    authUser(token, privKey); // this authenticates a user

//    authServer();
  }
}
