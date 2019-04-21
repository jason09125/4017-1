package shared;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AsymmetricCrypto {

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

  public static byte[] signData(byte[] data, byte[] privKey) {
    try {
      PrivateKey privateKey = KeyFactory.getInstance("DSA").generatePrivate(new PKCS8EncodedKeySpec(privKey));
      Signature dsa = Signature.getInstance("SHA256WithDSA");
      dsa.initSign(privateKey);
      dsa.update(data);
      return dsa.sign();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static boolean verifyData(byte[] data, byte[] digitalSignature, byte[] pubKey) {
    try {
      PublicKey publicKey = KeyFactory.getInstance("DSA").generatePublic(new X509EncodedKeySpec(pubKey));
      Signature signature = Signature.getInstance("SHA256WithDSA");
      signature.initVerify(publicKey);
      signature.update(data);
      return signature.verify(digitalSignature);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
