package client.auth;

import java.util.HashMap;

public class PublicKeysStorage {
  private HashMap<String, byte[]> userPublicKeyMap = new HashMap<>();

  public byte[] getUserPublicKey(String username) {
    return userPublicKeyMap.get(username);
  }

  public void setUserPublicKeyMap(String username, byte[] publicKey) {
    userPublicKeyMap.put(username, publicKey);
  }

  public PublicKeysStorage() {

  }
}
