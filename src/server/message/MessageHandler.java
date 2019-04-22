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

    boolean isChecksumVerified = Md5Helper.verify(encryptedWithSessionKey, md5);
    System.out.println(">>> Checksum OK: " + isChecksumVerified);
    if (!isChecksumVerified) {
      return null; // todo: show better warning
    }

    byte[] senderSessionKey = UserManager.getSessionKey(senderUsername, false);
    byte[] senderSigned = SymmetricCrypto.decrypt(encryptedWithSessionKey, senderSessionKey);

    byte[] dataWithoutSenderSignature = DataConverter.getDataFromSigned(senderSigned);
    byte[] senderSignature = DataConverter.getSignatureFromSigned(senderSigned);
    boolean isSenderSignatureValid = AsymmetricCrypto.verifyData(dataWithoutSenderSignature, senderSignature, senderPublicKey);
    System.out.println(">>> Sender (" + senderUsername + ") signature OK: " + isSenderSignatureValid);
    if (!isSenderSignatureValid) {
      return null; // todo: show better warning
    }

    // still encrypt the message signed by sender, instead of the data without its signature
    byte[] signature = ServerAuthenticator.signMessage(senderSigned);
    return DataConverter.combineByteArrays(senderSigned, signature);
  }

  public static String getDeliverable(String toUsername, byte[] signedByServer) {
    byte[] receiverSessionKey = UserManager.getSessionKey(toUsername, false);
    byte[] encrypted = SymmetricCrypto.encrypt(signedByServer, receiverSessionKey);

    String md5 = Md5Helper.digest(encrypted);
    byte[] withMd5 = DataConverter.combineByteArrays(encrypted, md5.getBytes());
    return DataConverter.bytesToBase64(withMd5);
  }
}
