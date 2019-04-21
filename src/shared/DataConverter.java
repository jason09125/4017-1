package shared;

import java.security.Key;
import java.util.Base64;

public class DataConverter {
  public static String keyToString(Key key) {
    return bytesToString(key.getEncoded());
  }

  public static String bytesToString(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  public static byte[] stringToBytes(String str) {
    return Base64.getDecoder().decode(str);
  }
}
