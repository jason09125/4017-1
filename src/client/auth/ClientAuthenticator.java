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
    System.out.printf("------------ Public Key --------------\n%s\n\n", DataConverter.keyToBase64(pubKey));
    System.out.printf("------------ Private Key -------------\n%s\n\n", DataConverter.keyToBase64(privKey));
  }

  public ClientAuthenticator(String configPath) { // sample path: "./client-config/config.properties"
    try (InputStream input = new FileInputStream(configPath)) {
      Properties prop = new Properties();
      prop.load(input);
      this.selfPublicKey = DataConverter.base64ToBytes(prop.getProperty("CLIENT_MASTER_PUBLIC_KEY"));
      this.selfPrivateKey = DataConverter.base64ToBytes(prop.getProperty("CLIENT_MASTER_PRIVATE_KEY"));
      this.serverPublicKey = DataConverter.base64ToBytes(prop.getProperty("SERVER_MASTER_PUBLIC_KEY"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getLoginCommand(String username, String plainPassword, int token, String challenge) {
    byte[] signature = AsymmetricCrypto.signData(challenge.getBytes(), this.selfPrivateKey);
    String signatureStr = DataConverter.bytesToBase64(signature);

    return "COMMAND LOGIN " + username + " " + plainPassword + " " + token + " " + signatureStr;
  }

  public void setSessionKey(byte[] key, boolean shouldDecrypt) {
    if (!shouldDecrypt) {
      this.sessionKey = key;
      return;
    }

    this.sessionKey = AsymmetricCrypto.decryptWithPrivateKey(key, this.selfPrivateKey);
  }

  private boolean isEmptyString(String str) {
    return str == null || str.length() == 0;
  }
}
