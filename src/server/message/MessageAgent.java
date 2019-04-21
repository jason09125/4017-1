package server.message;

import server.auth.ServerAuthenticator;
import server.user.UserManager;
import shared.AsymmetricCrypto;
import shared.SymmetricCrypto;

public class MessageAgent {
  public static byte[] parseFromClientAndGetDeliverable(String fromUsername, String toUsername, byte[] encryptedWithSessionKey) {
    byte[] senderSessionKey = UserManager.getSessionKey(fromUsername, false);
    byte[] receiverPublicKey = UserManager.getPublicKey(toUsername);
    byte[] fromSigned = SymmetricCrypto.decrypt(encryptedWithSessionKey, senderSessionKey);
    byte[] serverSigned = ServerAuthenticator.encryptWithPrivateKey(fromSigned);
    return AsymmetricCrypto.encryptWithPublicKey(serverSigned, receiverPublicKey);
  }
}
