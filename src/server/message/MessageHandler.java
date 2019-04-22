package server.message;

import server.auth.ServerAuthenticator;
import server.user.UserManager;
import shared.AsymmetricCrypto;
import shared.DataConverter;
import shared.SymmetricCrypto;

public class MessageHandler {
  public static byte[] parseFromClientAndSign(String senderUsername, byte[] senderPublicKey, String cipherText) {
    byte[] encryptedWithSessionKey = DataConverter.base64ToBytes(cipherText);
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
    return DataConverter.bytesToBase64(SymmetricCrypto.encrypt(signedByServer, receiverSessionKey));
  }
}
