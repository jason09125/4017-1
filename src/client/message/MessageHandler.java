package client.message;

import client.auth.ClientAuthenticator;
import client.auth.PublicKeysStorage;
import shared.AsymmetricCrypto;
import shared.DataConverter;
import shared.SymmetricCrypto;

public class MessageHandler {
  private final ClientAuthenticator clientAuthenticator;
  private final PublicKeysStorage publicKeysStorage;

  public MessageHandler(ClientAuthenticator ca, PublicKeysStorage pks) {
    this.clientAuthenticator = ca;
    this.publicKeysStorage = pks;
  }

  public String getDeliverable(String plainText) {
    byte[] plainTextBytes = plainText.getBytes();
    byte[] signature = AsymmetricCrypto.signData(plainTextBytes, clientAuthenticator.getSelfPrivateKey());
    byte[] combined = DataConverter.combineByteArrays(plainTextBytes, signature);

    byte[] sessionKeyEncrypted = SymmetricCrypto.encrypt(combined, clientAuthenticator.getSessionKey());
    return DataConverter.bytesToBase64(sessionKeyEncrypted);
  }

  public String parseIncoming(String senderUsername, String cipherText) {
    byte[] encryptedWithSessionKey = DataConverter.base64ToBytes(cipherText);
    byte[] serverSigned = SymmetricCrypto.decrypt(encryptedWithSessionKey, clientAuthenticator.getSessionKey());

    byte[] dataWithoutServerSignature = DataConverter.getDataFromSigned(serverSigned);
    byte[] serverSignature = DataConverter.getSignatureFromSigned(serverSigned);

    boolean isServerSignatureValid = AsymmetricCrypto.verifyData(dataWithoutServerSignature, serverSignature, clientAuthenticator.getServerPublicKey());

    System.out.println(">>> Server signature OK: " + isServerSignatureValid);
    if (!isServerSignatureValid) {
      return null; // todo: return warning
    }

    byte[] dataWithoutSenderSignature = DataConverter.getDataFromSigned(dataWithoutServerSignature);
    byte[] senderSignature = DataConverter.getSignatureFromSigned(dataWithoutServerSignature);

    boolean isSenderSignatureValid = AsymmetricCrypto.verifyData(dataWithoutSenderSignature, senderSignature, publicKeysStorage.getUserPublicKey(senderUsername));

    System.out.println(">>> Sender (" + senderUsername + ") signature OK: " + isSenderSignatureValid);
    if (!isSenderSignatureValid) {
      return null; // todo: return warning
    }

    return new String(dataWithoutSenderSignature);
  }
}
