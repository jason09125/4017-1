package client.auth;

import shared.DataConverter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class PublicKeysStorage {
  private HashMap<String, byte[]> userPublicKeyMap = new HashMap<>();

  public byte[] getUserPublicKey(String username) {
    byte[] publicKey = userPublicKeyMap.get(username);
    if (publicKey != null) return publicKey;

    String str = FileHelper.readFrom(username);
    if (str == null || str.length() == 0) return null;

    byte[] keyReadFromFile = DataConverter.base64ToBytes(str);
    userPublicKeyMap.put(username, keyReadFromFile);

    return keyReadFromFile;
  }

  public void setUserPublicKeyMap(String username, byte[] publicKey) {
    userPublicKeyMap.put(username, publicKey);
    FileHelper.writeTo(username, DataConverter.bytesToBase64(publicKey));
  }

  public PublicKeysStorage() {

  }
}

class FileHelper {
  static private String DIR_PATH = Paths.get(".", "client-cache").toString();

  static String readFrom(String name) {
    if (name == null) return null;

    Path path = Paths.get(DIR_PATH, name + ".pub");
    try {
      return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    } catch (FileNotFoundException e) {
    } catch (NoSuchFileException e) {
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  static void writeTo(String name, String value) {
    Path path = Paths.get(DIR_PATH, name + ".pub");
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(path.toString()));
      writer.write(value);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (writer != null) writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  static boolean delete(String name) {
    String path = Paths.get(DIR_PATH, name).toString();
    File file = new File(path);
    return file.delete();
  }
}