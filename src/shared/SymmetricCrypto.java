package shared;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SymmetricCrypto {
  private static final String ALGORITHM = "AES";
  private static final int KEY_BIT_SIZE = 128; // Java supports only 128-bit encryption

  public static byte[] generateSecretKey() {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
      SecureRandom secureRandom = new SecureRandom();

      keyGenerator.init(KEY_BIT_SIZE, secureRandom);
      return keyGenerator.generateKey().getEncoded();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] encrypt(byte[] plainText, byte[] key) {
    try {
      SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);

      return cipher.doFinal(plainText);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] decrypt(byte[] cipherText, byte[] key) {
    try {
      SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return cipher.doFinal(cipherText);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
