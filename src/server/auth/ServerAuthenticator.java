package server.auth;

import shared.AsymmetricCrypto;
import shared.DataConverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class ServerAuthenticator {
  private static String readProp(String name) {
    try (InputStream input = new FileInputStream("./server-config/config.properties")) {
      Properties prop = new Properties();
      prop.load(input);
      return prop.getProperty(name);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] signChallenge(String challenge) {
    try {
      String privateKeyStr = readProp("SERVER_MASTER_PRIVATE_KEY");
      return AsymmetricCrypto.signData(challenge.getBytes("utf-8"), DataConverter.stringToKeyBytes(privateKeyStr));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
