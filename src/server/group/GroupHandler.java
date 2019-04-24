package server.group;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupHandler {

    private static ArrayList<String> authedUser = new ArrayList<>();
    private static HashMap<String, ArrayList<String>> groupClientsMap = new HashMap<>();

    public static void scanRegisterUser() {
        addPreUser(new File(".", "db"));
    }

    private static void addPreUser(File file) {
        File[] children = file.listFiles();
        for (File child : children) {
            char[] userFileName = child.toString().toCharArray();
            String name = "";
            for (int i = 5; i < userFileName.length; i++) {
                name += userFileName[i];
            }
            authedUser.add(name);
            // debug
            System.out.printf("User: %s added.\n", name);
        }
    }

    public static boolean isAddUserAuthed(String username) {
        return authedUser.contains(username);
    }

    public static boolean addGroup(String groupname, String username) {
        // check no duplicate group name
        for (String i : groupClientsMap.keySet()) {
            if (groupname.equals(i)) {
                return false;
            }
        }

        ArrayList newGroupList = new ArrayList<>();
        newGroupList.add(username);
        groupClientsMap.put(groupname, newGroupList);
        // debug
        System.out.println(groupClientsMap);
        return true;
    }

    public static boolean addMember(String groupname, String username) {
        if (isAddUserAuthed(username)) {
            return groupClientsMap.get(groupname).add(username);
        } else {
            return false;
        }
    }

    public static boolean quitGroup(String groupname, String username) {
        return groupClientsMap.get(groupname).remove(username);
    }

    public static String getClientGroupList(String username) {
        String groupString = "";
        for (String i : groupClientsMap.keySet()) {
            for (String j : groupClientsMap.get(i)) {
                if (username.equals(j)) {
                    groupString = groupString + i + "|";
                }
            }
        }
        return groupString;
    }

    public static void main(String args[]) {
        // testing
        scanRegisterUser();
        addGroup("abc", "jason");
        addMember("abc", "dfsb");
        addMember("abc", "Eric");
        addGroup("asdv", "fgdb");
        addGroup("asv", "jason");
        quitGroup("abc", "Joe");
        addGroup("xzcv", "7685");
        addGroup("abc", "ytm");
        System.out.println(getClientGroupList("jason"));
    }

}
