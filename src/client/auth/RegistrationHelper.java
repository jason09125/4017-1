package client.auth;

import shared.AsymmetricCrypto;
import shared.DataConverter;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RegistrationHelper {
  public static void generateKeyPair() { // helper function for new users
    KeyPair keyPair = AsymmetricCrypto.generateKeyPair();
    PublicKey pubKey = keyPair.getPublic();
    PrivateKey privKey = keyPair.getPrivate();
    System.out.printf("------------ Public Key --------------\n%s\n\n", DataConverter.keyToBase64(pubKey));
    System.out.printf("------------ Private Key -------------\n%s\n\n", DataConverter.keyToBase64(privKey));
  }

  public static void main(String[] args) {
    generateKeyPair();
  }
}
