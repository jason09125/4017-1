package shared;

import java.security.Key;
import java.util.Base64;

public class DataConverter {
  public static String keyToString(Key key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  public static byte[] stringToKeyBytes(String str) {
    return Base64.getDecoder().decode(str);
  }
}
