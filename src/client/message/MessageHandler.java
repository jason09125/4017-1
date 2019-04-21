package client.message;

import client.auth.ClientAuthenticator;
import client.auth.PublicKeysStorage;
import shared.AsymmetricCrypto;
import shared.SymmetricCrypto;

public class MessageHandler {
  private final ClientAuthenticator clientAuthenticator;
  private final PublicKeysStorage publicKeysStorage;

  public MessageHandler(ClientAuthenticator ca, PublicKeysStorage pks) {
    this.clientAuthenticator = ca;
    this.publicKeysStorage = pks;
  }

  public byte[] getDeliverable(byte[] plain) {
    byte[] selfSigned = AsymmetricCrypto.encryptWithPrivateKey(plain, clientAuthenticator.getSelfPrivateKey());
    return SymmetricCrypto.encrypt(selfSigned, clientAuthenticator.getSessionKey());
  }

  public byte[] parseIncoming(String senderUsername, byte[] data) {
    byte[] serverSigned = SymmetricCrypto.decrypt(data, clientAuthenticator.getSessionKey());
    byte[] senderSigned = AsymmetricCrypto.decryptWithPublicKey(serverSigned, clientAuthenticator.getServerPublicKey());
    return AsymmetricCrypto.decryptWithPublicKey(senderSigned, publicKeysStorage.getUserPublicKey(senderUsername)); // plain message
  }
}
