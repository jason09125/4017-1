package shared;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AsymmetricCrypto {

  public static KeyPair generateKeyPair() {
    KeyPairGenerator keyGen = null;
    try {
      keyGen = KeyPairGenerator.getInstance("RSA");
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
      PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privKey));
      Signature rsa = Signature.getInstance("SHA256WithRSA");
      rsa.initSign(privateKey);
      rsa.update(data);
      return rsa.sign();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static boolean verifyData(byte[] data, byte[] digitalSignature, byte[] pubKey) {
    if (pubKey == null) return false;

    try {
      PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
      Signature signature = Signature.getInstance("SHA256WithRSA");
      signature.initVerify(publicKey);
      signature.update(data);
      return signature.verify(digitalSignature);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  // only for short data, e.g. session key, NOT for message itself
  public static byte[] decryptWithPublicKey(byte[] encrypted, byte[] pubKey) {
    try {
      PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, publicKey);
      return cipher.doFinal(encrypted);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // only for short data, e.g. session key, NOT for message itself
  public static byte[] encryptWithPrivateKey(byte[] plain, byte[] privKey) {
    try {
      PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privKey));
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, privateKey);
      return cipher.doFinal(plain);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // only for short data, e.g. session key, NOT for message itself
  public static byte[] decryptWithPrivateKey(byte[] encrypted, byte[] privKey) {
    try {
      PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privKey));
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      return cipher.doFinal(encrypted);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // only for short data, e.g. session key, NOT for message itself
  public static byte[] encryptWithPublicKey(byte[] plain, byte[] pubKey) {
    try {
      PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      return cipher.doFinal(plain);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
