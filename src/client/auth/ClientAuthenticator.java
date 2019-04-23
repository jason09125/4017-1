package client.auth;

import client.ClientGUI.ClientLoginWindow;
import shared.AsymmetricCrypto;
import shared.DataConverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

  public ClientAuthenticator(String configPath, ClientLoginWindow login_win) { // sample path: "./client-config/config.properties"
    try (InputStream input = new FileInputStream(configPath)) {
      Properties prop = new Properties();
      prop.load(input);
      this.selfPublicKey = DataConverter.base64ToBytes(prop.getProperty("CLIENT_MASTER_PUBLIC_KEY"));
      this.selfPrivateKey = DataConverter.base64ToBytes(prop.getProperty("CLIENT_MASTER_PRIVATE_KEY"));
      this.serverPublicKey = DataConverter.base64ToBytes(prop.getProperty("SERVER_MASTER_PUBLIC_KEY"));
    } catch (IOException e) {
      login_win.notice(1, "File not found. \nPlease place it in the client-config file");
      e.printStackTrace();
    }
  }

  public String signChallenge(String challenge) {
    byte[] signature = AsymmetricCrypto.signData(challenge.getBytes(), this.selfPrivateKey);
    return DataConverter.bytesToBase64(signature);
  }

  public void setSessionKey(byte[] key, boolean shouldDecrypt) {
    if (!shouldDecrypt) {
      this.sessionKey = key;
      return;
    }

    this.sessionKey = AsymmetricCrypto.decryptWithPrivateKey(key, this.selfPrivateKey);
  }
}
