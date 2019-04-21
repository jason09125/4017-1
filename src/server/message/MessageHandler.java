package server.message;

import server.auth.ServerAuthenticator;
import server.user.UserManager;
import shared.SymmetricCrypto;

public class MessageHandler {
  // todo: split into two functions, so no repeated server signing
  public static byte[] parseFromClientAndGetDeliverable(String fromUsername, String toUsername, byte[] encryptedWithSessionKey) {
    byte[] senderSessionKey = UserManager.getSessionKey(fromUsername, false);
    byte[] fromSigned = SymmetricCrypto.decrypt(encryptedWithSessionKey, senderSessionKey);
    byte[] serverSigned = ServerAuthenticator.encryptWithPrivateKey(fromSigned);

    byte[] receiverSessionKey = UserManager.getSessionKey(toUsername, false);
    return SymmetricCrypto.encrypt(serverSigned, receiverSessionKey);
  }
}
