package server.db;

import java.io.*;
import java.util.HashMap;

public class FileManager {
  static private String FILE = "./db.txt";
  // username -> [password, tfaSecret]
  static private HashMap<String, String[]> userMap = new HashMap<>();

  static {
    String line;

    BufferedReader bufferReader;
    try {
      bufferReader = new BufferedReader(new FileReader(FILE));
      while ((line = bufferReader.readLine()) != null) {
        String[] items = line.split("\\s*,\\s*");
        String username = items[0];
        String password = items[1];
        String tfaSecret = items[2];
        userMap.put(username, new String[]{password, tfaSecret});
        System.out.printf("[%s]: %s, %s\n", username, password, tfaSecret);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String[] getByKey(String username) {
    return userMap.get(username);
  }

  public static void appendUsers(String[] users) throws IOException {
    for (String line : users) {
      String[] items = line.split("/s*,/s*");
      String username = items[0];
      String password = items[1];
      String tfaSecret = items[2];
      appendUser(username, password, tfaSecret);
    }
  }

  public static void appendUser(String username, String password, String tfaSecret) {
    try {
      write(username, password, tfaSecret, true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void write(String username, String password, String tfaSecret, boolean append) throws IOException {
    FileWriter fileWriter = new FileWriter(FILE, append);
    PrintWriter printWriter = new PrintWriter(fileWriter);
    printWriter.printf("%s, %s, %s", username, password, tfaSecret);
    printWriter.println();
    printWriter.close();
  }

}
