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

    byte[] signed = AsymmetricCrypto.signData("CHALLENGE_COMP4017".getBytes(), privKey);

    boolean isAuthenticated = UserManager.auth(username, plainPassword, token, signed);
    System.out.println("Server: authentication status is " + isAuthenticated);
  }

  public static void authServer() {
    String challenge = "kdlghfasdm9r04c90rmq49mc9dsm-fadsf";
    byte[] signed = ServerAuthenticator.signChallenge(challenge);

    try (InputStream input = new FileInputStream("./client-config/config.properties")) {
      Properties prop = new Properties();
      prop.load(input);
      byte[] publicKey = DataConverter.stringToKeyBytes(prop.getProperty("SERVER_MASTER_PUBLIC_KEY"));
      boolean verified = AsymmetricCrypto.verifyData(challenge.getBytes(), signed, publicKey);
      System.out.println("Server authentication result: " + verified);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
//    UserManager.delete("Eric"); // clean up before testing
//    KeyPair kp = createUser(); // this creates a user

//    System.out.println(DataConverter.keyToString(kp.getPrivate()));

    // This is the secret for 2FA------->>> JUA7N3KIJAQKEKXD
    int token =500383;
    byte[] privKey = DataConverter.stringToKeyBytes("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJSJfdWQfCEb41SSSg2VG2P4En8C1Y6plTZFvYnrNkRGvZscf1GH8AhcF7wfSH6jR/ikLPFA+B61Oqmr6EynbtfeAbROJTKuo+Mll2n2y7Q1/4le1UL5aVO7G0WliTNn0yQ4eP1pdjzNhXwPUNtubidO5eYUBv/t5+m/iBM0NjNvAgMBAAECgYBJqPVSF1i3QpE1u2Yl3i+7H6ZsfgdRvB9Wzrbz0kUTDtGwPi2VTQhn3OOYUxssUeS7FQ+EhGeHMvBoe+uzN8TrXpxaqs7V+SDppVpDRQmLfzz2tl20aLGHGeBEoyO/3E14ymizZki3K8p0qfb0LLdsneaElIQe23dD4GxnaeSNoQJBAMi4BCEv6yGCMsEt3mk7BdTLO5HKP8d+OFdd4v5CphVnzeh1kexd8n/o2hcifhe1UOKM7pFqXTIKQZMpaMeyieMCQQC9clKy6r/xRZYAoM4CGw8KBNaEopL6KoCItAoO9uCAeRjvvuBb0mUMmUH+ZCGiYAnVVoVtr2tQqPMh8EOYoBYFAkAtNIR5lPk3ysLzjwkQWiKuEjeQViSXIW4+/v4olYoiOAa/2/rJaT88X4z+uN39KPDWlTcFuRbUNksegaz/jM5RAkEAoG7MchAy9FQFsAp963KWzdlDAZfb+Fc9+obdbcbMYIAtCfsPbTNDt+Oh65lIkoXaTfyziivgKbKqE7ewxvPrMQJAPequMIu4gQPE+KzC6Q/a4sou+rzcWKcWYg0ZOSvbV4LThztvBlckTu4sO0K03k75fz2UMG19p3jWVEEkAMzwww==");
    authUser(token, privKey); // this authenticates a user

//    authServer();
  }
}
