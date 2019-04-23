package client.message;

import client.auth.ClientAuthenticator;
import client.auth.PublicKeysStorage;
import shared.AsymmetricCrypto;
import shared.DataConverter;
import shared.Md5Helper;
import shared.SymmetricCrypto;

public class MessageHandler {
  private final ClientAuthenticator clientAuthenticator;
  private final PublicKeysStorage publicKeysStorage;

  public MessageHandler(ClientAuthenticator ca, PublicKeysStorage pks) {
    this.clientAuthenticator = ca;
    this.publicKeysStorage = pks;
  }

  public String getDeliverable(String plainText) {
    if (plainText == null) return null;

    byte[] plainTextBytes = plainText.getBytes();
    System.out.println(">>> Signing with client private key");
    byte[] signature = AsymmetricCrypto.signData(plainTextBytes, clientAuthenticator.getSelfPrivateKey());
    byte[] combined = DataConverter.combineByteArrays(plainTextBytes, signature);

    System.out.println(">>> Encrypting with session key");
    byte[] sessionKeyEncrypted = SymmetricCrypto.encrypt(combined, clientAuthenticator.getSessionKey());

    System.out.println(">>> Generating checksum");
    String md5 = Md5Helper.digest(sessionKeyEncrypted);
    System.out.println(">>> MD5 checksum: " + md5);
    byte[] withMd5 = DataConverter.combineByteArrays(sessionKeyEncrypted, md5.getBytes());
    return DataConverter.bytesToBase64(withMd5);
  }

  public String parseIncoming(String senderUsername, String cipherText) {
    byte[] withChecksum = DataConverter.base64ToBytes(cipherText);
    String md5 = new String(DataConverter.getChecksumFromBytes(withChecksum));
    byte[] encryptedWithSessionKey = DataConverter.getDataFromBytesWithChecksum(withChecksum);

    System.out.println(">>> Verifying MD5: " + md5);
    boolean isChecksumVerified = Md5Helper.verify(encryptedWithSessionKey, md5);
    System.out.println(">>> Checksum OK: " + isChecksumVerified);
    if (!isChecksumVerified) {
      return null; // todo: return warning
    }

    System.out.println(">>> Decrypting with session key");
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
