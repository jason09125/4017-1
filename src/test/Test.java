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

  public static void authUser(byte[] privKey) {
    String username = "Eric";
    String plainPassword = "123456";

    // get this from your Google Authenticator
    int token = 123456;

    // use private key to sign data, better do this locally on client, never transfer private key to server
//    byte[] privateKey = DataConverter.stringToKeyBytes("3082014B0201003082012C06072A8648CE3804013082011F02818100FD7F53811D75122952DF4A9C2EECE4E7F611B7523CEF4400C31E3F80B6512669455D402251FB593D8D58FABFC5F5BA30F6CB9B556CD7813B801D346FF26660B76B9950A5A49F9FE8047B1022C24FBBA9D7FEB7C61BF83B57E7C6A8A6150F04FB83F6D3C51EC3023554135A169132F675F3AE2B61D72AEFF22203199DD14801C70215009760508F15230BCCB292B982A2EB840BF0581CF502818100F7E1A085D69B3DDECBBCAB5C36B857B97994AFBBFA3AEA82F9574C0B3D0782675159578EBAD4594FE67107108180B449167123E84C281613B7CF09328CC8A6E13C167A8B547C8D28E0A3AE1E2BB3A675916EA37F0BFA213562F1FB627A01243BCCA4F1BEA8519089A883DFE15AE59F06928B665E807B552564014C3BFECF492A041602142E9A2E7727E2FD822629D88253C47C89DFA9319C");
    byte[] signed = AsymmetricKeyManager.signData("CHALLENGE_COMP4017".getBytes(), privKey);

    boolean isAuthenticated = UserManager.auth(username, plainPassword, token, signed);
    System.out.println("Server: authentication status is " + isAuthenticated);
  }

  public static void main(String[] args) {
    KeyPair kp = createUser(); // this creates a user
    authUser(kp.getPrivate().getEncoded()); // this authenticates a user
  }
}
