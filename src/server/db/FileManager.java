package server.db;

import server.user.User;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;

class FileManager {
  static private String FILE_PATH = Paths.get(".", "db").toString();
  static private HashMap<String, User> userMap = new HashMap<>();

  static User getByKey(String username) throws IOException, ClassNotFoundException {
    User user = userMap.get(username);
    if (user != null) return userMap.get(username);
    String path = Paths.get(FILE_PATH, username).toString();
    FileInputStream fileIn = new FileInputStream(path);
    ObjectInputStream objectIn = new ObjectInputStream(fileIn);
    user = (User) objectIn.readObject();
    userMap.put(username, user);
    fileIn.close();
    objectIn.close();
    return user;
  }

  static void addUser(User user) {
    try {
      String path = Paths.get(FILE_PATH, user.getUsername()).toString();
      FileOutputStream fileOut = new FileOutputStream(path, false);
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(user);
      fileOut.close();
      objectOut.close();
      System.out.println("> New user added: " + user.getUsername());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static boolean deleteUser(String username) {
    String path = Paths.get(FILE_PATH, username).toString();
    File file = new File(path);

    return file.delete();
  }
}
