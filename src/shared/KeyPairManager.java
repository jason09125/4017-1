package shared;

import java.security.*;

public class KeyPairManager {

  public static KeyPair generateKeyPair() {
    KeyPairGenerator keyGen = null;
    try {
      keyGen = KeyPairGenerator.getInstance("DSA");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    if (keyGen != null) {
      return keyGen.generateKeyPair();
    }
    return null;
  }

  public static byte[] signData(byte[] data, PrivateKey privateKey) {
    try {
      Signature dsa = Signature.getInstance("SHA256WithDSA");
      dsa.initSign(privateKey);
      dsa.update(data);
      return dsa.sign();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static boolean verifyData(byte[] data, PublicKey publicKey, byte[] digitalSignature) {
    try {
      Signature signature = Signature.getInstance("SHA256WithDSA");
      signature.initVerify(publicKey);
      signature.update(data);
      return signature.verify(digitalSignature);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private static String convertBytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02X", b));
    }
    return sb.toString();
  }
}
