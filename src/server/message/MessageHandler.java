package server.message;

import server.auth.ServerAuthenticator;
import server.user.UserManager;
import shared.AsymmetricCrypto;
import shared.DataConverter;
import shared.Md5Helper;
import shared.SymmetricCrypto;

public class MessageHandler {
  public static byte[] parseFromClientAndSign(String senderUsername, byte[] senderPublicKey, String cipherText) {
    byte[] withChecksum = DataConverter.base64ToBytes(cipherText);
    String md5 = new String(DataConverter.getChecksumFromBytes(withChecksum));
    byte[] encryptedWithSessionKey = DataConverter.getDataFromBytesWithChecksum(withChecksum);

    System.out.println("\t\t>>> Verifying MD5: " + md5);
    boolean isChecksumVerified = Md5Helper.verify(encryptedWithSessionKey, md5);
    System.out.println("\t\t>>> Checksum OK: " + isChecksumVerified);
    if (!isChecksumVerified) {
      return null; // todo: show better warning
    }

    System.out.printf("\t\t>>> Decrypting using sender's session key (%s)\n", senderUsername);
    byte[] senderSessionKey = UserManager.getSessionKey(senderUsername, false);
    byte[] senderSigned = SymmetricCrypto.decrypt(encryptedWithSessionKey, senderSessionKey);

    byte[] dataWithoutSenderSignature = DataConverter.getDataFromSigned(senderSigned);
    byte[] senderSignature = DataConverter.getSignatureFromSigned(senderSigned);
    boolean isSenderSignatureValid = AsymmetricCrypto.verifyData(dataWithoutSenderSignature, senderSignature, senderPublicKey);
    System.out.println("\t\t>>> Sender (" + senderUsername + ") signature OK: " + isSenderSignatureValid);
    if (!isSenderSignatureValid) {
      return null; // todo: show better warning
    }

    System.out.println("\t\t>>> Signing with server private key");
    // server signs sender-signed message
    byte[] signature = ServerAuthenticator.signMessage(senderSigned);
    return DataConverter.combineByteArrays(senderSigned, signature);
  }

  public static String getDeliverable(String toUsername, byte[] signedByServer) {
    System.out.printf("\t\t>>> Encrypting with receiver's session key (%s)\n", toUsername);
    byte[] receiverSessionKey = UserManager.getSessionKey(toUsername, false);
    byte[] encrypted = SymmetricCrypto.encrypt(signedByServer, receiverSessionKey);

    System.out.println("\t\t>>> Generating checksum");
    String md5 = Md5Helper.digest(encrypted);
    System.out.println("\t\t>>> MD5 checksum: " + md5);
    byte[] withMd5 = DataConverter.combineByteArrays(encrypted, md5.getBytes());
    return DataConverter.bytesToBase64(withMd5);
  }
}
