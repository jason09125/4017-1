package client.auth;

import java.util.HashMap;

public class PublicKeysStorage {
  private HashMap<String, byte[]> userPublicKeyMap = new HashMap<>();

  public byte[] getUserPublicKey(String username) {
    byte[] publicKey = userPublicKeyMap.get(username);
    if (publicKey != null) return publicKey;

    return null; //fixme

  }

  public void setUserPublicKeyMap(String username, byte[] publicKey) {
    userPublicKeyMap.put(username, publicKey);
  }

  public PublicKeysStorage() {

  }
}
