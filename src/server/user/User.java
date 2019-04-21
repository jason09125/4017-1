package server.user;

import java.io.Serializable;

public class User implements Serializable {
  public User(String username, String passwordHash, String publicKey, String tfaSecret) {
    this.username = username;
    this.passwordHash = passwordHash;
    this.publicKey = publicKey;
    this.tfaSecret = tfaSecret;
  }

  public String getUsername() {
    return username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public String getTfaSecret() {
    return tfaSecret;
  }

  private String username;
  private String passwordHash;
  private String publicKey;
  private String tfaSecret;
}
