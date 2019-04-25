package server.chat;

import server.auth.ServerAuthenticator;
import server.group.GroupHandler;
import server.message.MessageHandler;
import server.user.UserManager;
import shared.DataConverter;
import shared.Md5Helper;
import server.ServerGUI.ServerSetupWindow;
import server.ServerGUI.ServerWindow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ChatServer implements Runnable {
    private ChatServerThread clients[] = new ChatServerThread[50];
    private Map<Integer, String> authenticatedClientUsernameMap = Collections.synchronizedMap(new HashMap<Integer, String>());
    private HashMap<Integer, String> clientChallengeMap = new HashMap<>();
    private ServerSocket server = null;
    private volatile Thread thread = null;
    private int clientCount = 0;
    private ServerWindow serWin;

    public ChatServer(int port) {
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            GroupHandler.scanRegisterUser();
            GroupHandler.addGroup("abc", "Eric");
            start();
        } catch (IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public void run() {
        Thread thisThread = Thread.currentThread();
        serWin = new ServerWindow(this);
        while (thread == thisThread) {
            try {
//        System.out.println("Waiting for a client ...");
                addThread(server.accept());
            } catch (IOException ioe) {
                System.out.println("Server accept error: " + ioe);
                stop();
            }
        }
    }

    private void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    private void stop() {
        if (thread != null) {
            thread = null;
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++)
            if (clients[i].getID() == ID)
                return i;
        return -1;
    }

    synchronized void handle(int ID, String input) {
        System.out.printf("Handling %s: %s\n", ID, input);
        serWin.update_data(authenticatedClientUsernameMap.get(ID) + ": " + input, 1);
        // check machine command
        if (input.matches("^COMMAND .*$")) {
            String items[] = input.split(" ");
            if (items.length < 2) {
                return;
            }
            String action = items[1];
            if (action.equals("SERVER_AUTH")) {
                String challenge = items[2];
                System.out.printf("\t> Challenged by client %s: %s\n", ID, challenge);
                String signatureStr = DataConverter.bytesToBase64(ServerAuthenticator.signChallenge(challenge));
                System.out.println("\t>>> Digital signature generated, sending back: " + signatureStr + "\n");
                clients[findClient(ID)].send("RESPONSE SERVER_SIGNATURE 200 " + signatureStr);
            }

            if (action.equals("LOGIN")) {
                if (items.length != 6) {
                    clients[findClient(ID)].send("RESPONSE AUTH 400 Invalid_argument_number");
                    return;
                }
                String username = items[2];
                String password = items[3];
                int token = Integer.parseInt(items[4]);
                byte[] signature = DataConverter.base64ToBytes(items[5]);

                // check if it has already logged in
                Object[] clientIds = authenticatedClientUsernameMap.keySet().toArray();
                for (Object obj : clientIds) {
                    int clientId = (int) obj;
                    String name = authenticatedClientUsernameMap.get(clientId);
                    if (username.equals(name)) {
                        clients[findClient(ID)].send("RESPONSE AUTH 409 Already_logged_in");
                        clients[findClient(clientId)].send("SERVER_MSG===Server noticed and blocked a login attempt of your account from another client portal");
                        return;
                    }
                }

                System.out.println("\n\t-------------------- AUTHENTICATING --------------------\n");
                String challenge = clientChallengeMap.get(ID);
                boolean authenticated = UserManager.auth(username, password, token, challenge, signature);
                System.out.println("\n\t^^^^^^^^^^^^^^^^^^^^ AUTHENTICATING ^^^^^^^^^^^^^^^^^^^^\n");
                if (authenticated) {
                    UserManager.generateSessionKey(username);
                    byte[] key = UserManager.getSessionKey(username, true);
                    String sessionKeyStr = DataConverter.bytesToBase64(key);

                    StringBuilder onlineUsersSb = new StringBuilder();
                    for (int i = 0; i < clientCount; i++) {
                        String onlineUsername = authenticatedClientUsernameMap.get(clients[i].getID());
                        if (onlineUsername != null && onlineUsername.length() > 0) {
                            onlineUsersSb.append(onlineUsername);
                            onlineUsersSb.append("===");
                        }
                    }

                    System.out.println("\n\t-------------------- AUTH OK --------------------\n");
                    System.out.println("\t\t>>> Exchanging session key");
                    System.out.println("\t\t>>> Sending back usernames of online clients for public key exchange process");
                    clients[findClient(ID)].send(String.format("RESPONSE AUTH 200 %s %s %s", username, sessionKeyStr, onlineUsersSb.toString()));
                    authenticatedClientUsernameMap.put(ID, username);

                    // broadcast client's public key
                    String pubKeyStr = DataConverter.bytesToBase64(UserManager.getPublicKey(username));
                    System.out.printf("\t\t>>> Broadcasting user's public key to other connected clients: %s %s\n", username, pubKeyStr);
                    serWin.update_data(username + " has locked in.", 1);
                    for (int i = 0; i < clientCount; i++) {
                        // check authentication before sending this, filter out those not authenticated
                        String broadcastTo = authenticatedClientUsernameMap.get(clients[i].getID());
                        if (broadcastTo == null) {
                            continue;
                        }
                        System.out.printf("\t\t>>> Broadcasting to %s\n", broadcastTo);
                        clients[i].send("COMMAND NEW_USER " + username + " " + pubKeyStr);
                    }
                    System.out.println("\n\t^^^^^^^^^^^^^^^^^^^^ AUTH OK ^^^^^^^^^^^^^^^^^^^^\n");

                } else {
                    clients[findClient(ID)].send("RESPONSE AUTH 403 Invalid_credentials");
                }
            }

            if (action.equals("GET_GROUP_LIST")) {
                String senderUsername = items[2];
                if (!senderUsername.equals(authenticatedClientUsernameMap.get(ID)) || authenticatedClientUsernameMap.get(ID) == null) {
                    clients[findClient(ID)].send("RESPONSE SEND_MESSAGE 401 Unauthorized");
                    return;
                }
                clients[findClient(ID)].send("COMMAND GROUP_LIST " + GroupHandler.getClientGroupList(senderUsername));
                return;
            }

            if (action.equals("SEND_MESSAGE")) {
                String senderUsername = items[2];
                if (!senderUsername.equals(authenticatedClientUsernameMap.get(ID)) || authenticatedClientUsernameMap.get(ID) == null) {
                    clients[findClient(ID)].send("RESPONSE SEND_MESSAGE 401 Unauthorized");
                    return;
                }
                String cipherText = items[3];
                System.out.println("\n\t-------------------- HANDLING NEW INCOMING MESSAGE --------------------");
                System.out.println("\n\t\t> Sender is " + senderUsername);
                byte[] signedByServer = MessageHandler.parseFromClientAndSign(senderUsername, UserManager.getPublicKey(senderUsername), cipherText);

                if (items[4].equals("All")) {
                    for (int i = 0; i < clientCount; i++) {
                        // filter out those not authenticated
                        if (authenticatedClientUsernameMap.get(clients[i].getID()) == null) {
                            continue;
                        }
                        String receiverUsername = authenticatedClientUsernameMap.get(clients[i].getID());
                        System.out.println("\n\t\t> Delivering to " + receiverUsername);
                        String deliverableMessage = MessageHandler.getDeliverable(receiverUsername, signedByServer);

                        clients[i].send("COMMAND DELIVER_MESSAGE " + senderUsername + " " + deliverableMessage + " " + "All");
                        System.out.println();
                    }
                } else {
                    String toGroup = items[4];
                    ArrayList groupMemberList = GroupHandler.getMembers(toGroup);

                    for (int i = 0; i < groupMemberList.size(); i++) {
                        for (int j = 0; j < clientCount; j++) {
                            // filter out those not authenticated
                            if (authenticatedClientUsernameMap.get(clients[i].getID()) == null) {
                                continue;
                            }
                            String receiverUsername = authenticatedClientUsernameMap.get(clients[i].getID());
                            if(receiverUsername.equals(groupMemberList.get(i))){
                                System.out.println("\n\t\t> Delivering to " + receiverUsername);
                                String deliverableMessage = MessageHandler.getDeliverable(receiverUsername, signedByServer);

                                clients[i].send("COMMAND DELIVER_MESSAGE " + senderUsername + " " + deliverableMessage + " " +toGroup);
                                System.out.println();
                            }
                        }
                    }

                }
                System.out.println("\t^^^^^^^^^^^^^^^^^^^^ HANDLING NEW INCOMING MESSAGE ^^^^^^^^^^^^^^^^^^^^\n");
            }

            if (action.equals("QUIT")) {
                clients[findClient(ID)].send("RESPONSE QUIT");
                remove(ID);
            }

            if (action.equals("PUB_KEY_REQUEST")) {
                if (items.length != 3) return;

                String[] usernames = items[2].split("===");
                System.out.println("\n\t-------------------- PUBLIC KEY EXCHANGE --------------------\n");
                for (String username : usernames) {
                    if (username != null && username.length() > 0) {
                        byte[] bytes = UserManager.getPublicKey(username);
                        if (bytes != null && bytes.length > 0) {
                            String publicKey = DataConverter.bytesToBase64(bytes);
                            String signature = DataConverter.bytesToBase64(ServerAuthenticator.signMessage(bytes));
                            String checksum = Md5Helper.digest(bytes);
                            System.out.printf("\t\t> Distributing public key of %s: %s\n", username, publicKey);
                            System.out.printf("\t\t>>> Server signature: %s\n", signature);
                            System.out.printf("\t\t>>> Checksum: %s\n", checksum);
                            clients[findClient(ID)].send(String.format("RESPONSE PUB_KEY %s %s %s %s", username, publicKey, signature, checksum));
                        }
                    }
                }
                System.out.println("\n\t^^^^^^^^^^^^^^^^^^^^ PUBLIC KEY EXCHANGE ^^^^^^^^^^^^^^^^^^^^");
            }
        }
        clients[findClient(ID)].send("RESPONSE LOGOUT 200");
    }

    synchronized void remove(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ChatServerThread toTerminate = clients[pos];
            int idToRemove = clients[pos].getID();
            clientChallengeMap.remove(idToRemove);
            authenticatedClientUsernameMap.remove(idToRemove);

            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount - 1)
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            clientCount--;
            try {
                toTerminate.close();
            } catch (IOException ioe) {
                System.out.println("Error closing thread: " + ioe);
            }
            toTerminate.stopThread();
            serWin.update_data(String.valueOf(clientCount), 0);
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ChatServerThread(this, socket);
            try {
                socket.setSoTimeout(300000); // 5 minutes
            } catch (SocketException e) {
                int clientIdToRemove = clients[clientCount].getID();
                remove(clientIdToRemove);
            }
            try {
                clients[clientCount].open();
                clients[clientCount].start();

                // ---- send challenge ----
                String challenge = Double.toString(Math.random());
                int clientId = clients[clientCount].getID();
                clientChallengeMap.put(clientId, challenge);
                clients[clientCount].send("COMMAND CHALLENGE " + challenge);
                System.out.printf("\t> Challenging client %s: %s\n\n", clientId, challenge);

                clientCount++;
                serWin.update_data(String.valueOf(clientCount), 0);
            } catch (IOException ioe) {
                System.out.println("Error opening thread: " + ioe);
            }
        } else
            System.out.println("Client refused: maximum " + clients.length + " reached.");
    }

    public static void main(String args[]) {
//    ChatServer server = null;
//    if (args.length != 1) {
//      System.out.println("Usage: java ChatServer port");
//    } else {
//      server = new ChatServer(Integer.parseInt(args[0]));
//    }
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getLocalHost();
            String server_IP = inetAddress.getHostAddress();
            System.out.println("IP Address: " + inetAddress.getHostAddress());
            ServerSetupWindow ssw = new ServerSetupWindow(server_IP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

