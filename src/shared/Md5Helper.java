package shared;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Helper {
  public static String digest(byte[] msg) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(msg);
      BigInteger no = new BigInteger(1, messageDigest);
      String hash = no.toString(16);
      while (hash.length() < 32) {
        hash = "0" + hash;
      }
      return hash.toUpperCase();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static boolean verify(byte[] msg, String md5) {
    String msgMd5 = digest(msg);
    return msgMd5 != null && msgMd5.equals(md5.toUpperCase());
  }
}
