package shared;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

// cipher text strings or keys are base64 string, and should use this converter
// for plain text strings, use getBytes() to convert to bytes
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

  public static byte[] combineByteArrays(byte[] one, byte[] two) {
    byte[] combined = new byte[one.length + two.length];

    for (int i = 0; i < combined.length; ++i) {
      combined[i] = i < one.length ? one[i] : two[i - one.length];
    }
    return combined;
  }

  public static byte[] getDataFromSigned(byte[] combined) {
    return Arrays.copyOfRange(combined, 0, combined.length - 128);
  }

  public static byte[] getSignatureFromSigned(byte[] combined) {
    return Arrays.copyOfRange(combined, combined.length - 128, combined.length);
  }
}
