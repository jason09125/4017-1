package shared;

import java.security.Key;
import java.util.Base64;

public class DataConverter {
  public static String keyToBase64(Key key) {
    return bytesToBase64(key.getEncoded());
  }

  public static String bytesToBase64(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  public static byte[] base64ToBytes(String str) {
    return Base64.getDecoder().decode(str);
  }
}
