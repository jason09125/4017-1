package server.auth;

import shared.AsymmetricCrypto;
import shared.DataConverter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

public class ServerAuthenticator {
  private static HashMap<String, String> propertiesCache = new HashMap<>();

  private static String readProp(String name) {
    String cached = propertiesCache.get(name);
    if (cached != null && cached.length() > 0) {
      return cached;
    }

    try (InputStream input = new FileInputStream("./server-config/config.properties")) {
      Properties prop = new Properties();
      prop.load(input);
      String result = prop.getProperty(name);
      propertiesCache.put(name, result);
      return result;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] signChallenge(String challenge) {
    try {
      String privateKeyStr = readProp("SERVER_MASTER_PRIVATE_KEY");
      return AsymmetricCrypto.signData(challenge.getBytes("utf-8"), DataConverter.base64ToBytes(privateKeyStr));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] encryptWithPrivateKey(byte[] data) {
    String privateKeyStr = readProp("SERVER_MASTER_PRIVATE_KEY");
    return AsymmetricCrypto.encryptWithPrivateKey(data, DataConverter.base64ToBytes(privateKeyStr));
  }
}
