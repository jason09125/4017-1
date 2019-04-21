package server.db;

import server.user.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FileManager {
  static private String FILE_PATH = "./db";
  static private HashMap<String, User> userMap = new HashMap<>();

  protected static User getByKey(String username) {
    User user = userMap.get(username);
    if (user != null) return userMap.get(username);

    try {
      FileInputStream fileIn = new FileInputStream(FILE_PATH + '/' + username);
      ObjectInputStream objectIn = new ObjectInputStream(fileIn);
      user = (User) objectIn.readObject();
      userMap.put(username, user);
      fileIn.close();
      objectIn.close();
      return user;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  protected static void addUser(User user) {
    try {
      FileOutputStream fileOut = new FileOutputStream(FILE_PATH + '/' + user.getUsername(), true);
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(user);
      fileOut.close();
      objectOut.close();
      System.out.println("> New user added: " + user.getUsername());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
