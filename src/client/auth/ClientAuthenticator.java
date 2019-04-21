package client.auth;

import shared.AsymmetricCrypto;
import shared.DataConverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Properties;

public class ClientAuthenticator {
  private byte[] selfPublicKey;
  private byte[] selfPrivateKey;
  private byte[] serverPublicKey;
  private byte[] sessionKey;

  public byte[] getSelfPublicKey() {
    return selfPublicKey;
  }

  public byte[] getSelfPrivateKey() {
    return selfPrivateKey;
  }

  public byte[] getServerPublicKey() {
    return serverPublicKey;
  }

  public byte[] getSessionKey() {
    return sessionKey;
  }


  public static void generateKeyPair() { // helper function for new users
    KeyPair keyPair = AsymmetricCrypto.generateKeyPair();
    PublicKey pubKey = keyPair.getPublic();
    PrivateKey privKey = keyPair.getPrivate();
    System.out.printf("------------ Public Key --------------\n%s\n\n", DataConverter.keyToString(pubKey));
    System.out.printf("------------ Private Key -------------\n%s\n\n", DataConverter.keyToString(privKey));
  }

  public ClientAuthenticator(String configPath) { // sample path: "./client-config/config.properties"
    try (InputStream input = new FileInputStream(configPath)) {
      Properties prop = new Properties();
      prop.load(input);
      this.selfPublicKey = DataConverter.stringToKeyBytes(prop.getProperty("CLIENT_MASTER_PUBLIC_KEY"));
      this.selfPrivateKey = DataConverter.stringToKeyBytes(prop.getProperty("CLIENT_MASTER_PRIVATE_KEY"));
      this.serverPublicKey = DataConverter.stringToKeyBytes(prop.getProperty("SERVER_MASTER_PUBLIC_KEY"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void login(String username, String plainPassword, int token) {
    byte[] signed = AsymmetricCrypto.signData("CHALLENGE_COMP4017".getBytes(), this.selfPrivateKey);
    // todo: send data through network and retrieve session key
    this.sessionKey = null;
  }

  private boolean isEmptyString(String str) {
    return str == null || str.length() == 0;
  }
}
