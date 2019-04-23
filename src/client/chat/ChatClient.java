package client.chat;

import client.auth.ClientAuthenticator;
import client.auth.PublicKeysStorage;
import client.message.MessageHandler;
import shared.AsymmetricCrypto;
import shared.DataConverter;
import shared.Md5Helper;
import client.ClientGUI.ClientLoginWindow;
import client.ClientGUI.ClientTokenWindow;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ChatClient implements Runnable {
    private Socket socket = null;
    private volatile Thread thread = null;
    private BufferedReader console = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread client = null;
    private ClientAuthenticator clientAuthenticator;
    private PublicKeysStorage publicKeysStorage;
    private MessageHandler messageHandler;
    private String challengeForServer;
    private String challengeFromServer;
    private String username;
    private boolean hasAuthenticated;
    private ClientTokenWindow tokenWindow;
    private static ClientLoginWindow login_win;
    //  private ClientWindow clientWin;

    public ChatClient(String serverName, int serverPort, String configFile) {
        this.clientAuthenticator = new ClientAuthenticator(configFile, login_win);
        this.publicKeysStorage = new PublicKeysStorage();
        this.messageHandler = new MessageHandler(this.clientAuthenticator, this.publicKeysStorage);

        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            start();
        } catch (UnknownHostException uhe) {
            login_win.notice(1, "Please check the server IP and Port.\nOr the server may not started yet.");
//      System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            login_win.notice(1, "Please check the server IP and Port.\nOr the server may not started yet.");
//      System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void run() {
        Thread thisThread = Thread.currentThread();
//    while (thread == thisThread)
    }

    public boolean sendMsg(String msg) {
//    while (thread != null) {
        try {
//        String line = console.readLine();

//        if (line == null || line.equals("")) {
//          continue;
//        }

            // check human command
            if (msg.matches("^\\.login .*$")) { // ---- send authentication request ----
                System.out.println("Logging in...");
                String[] items = msg.split("\\s+");
                if (items.length != 4) {
                    System.out.println("Invalid parameter number, usage: '.login [username] [password] [token]'");
                    return true;
//            continue;
                }
                String username = items[1];
                String password = items[2];
                int token = 0;
                try {
                    token = Integer.parseInt(items[3]);
                } catch (NumberFormatException e) {
                    tokenWindow.notice(1, "Token should be a number");
                    return false;
//            System.out.println("Invalid token type, token should be a number");
//            continue;
                }
                System.out.println("Token: " + token);
                String signature = clientAuthenticator.signChallenge(this.challengeFromServer);
                String cmd = String.format("COMMAND LOGIN %s %s %s %s", username, password, token, signature);
                streamOut.writeUTF(cmd);
                streamOut.flush();
                return true;
//          continue;
            }

            if (msg.matches("^\\.bye$")) { // ---- send authentication request ----
                System.out.println("Quiting...");
                streamOut.writeUTF("COMMAND QUIT");
                streamOut.flush();
                // ============need to close the window==================
                return true;
//          continue;
            }

            if (this.hasAuthenticated) {
                // send message
                String cipherText = messageHandler.getDeliverable(msg);
                streamOut.writeUTF(String.format("COMMAND SEND_MESSAGE %s %s", this.username, cipherText));
                streamOut.flush();
                return true;
            } else {
                System.out.println("Unauthorized, use '.login [username] [password] [token]' to login first");
                return false;
            }
        } catch (IOException ioe) {
            System.out.println("Sending error: " + ioe.getMessage());
            stop();
            return false;
        }
//    }
    }

    void handle(String msg) {
        System.out.println("Handling: " + msg);

        // check machine command
        if (msg.matches("^COMMAND .*$")) {
            String[] items = msg.split("\\s+");
            if (items.length < 2) return;
            String action = items[1];
            if (action.equals("CHALLENGE")) {
                if (items.length != 3) return;
                System.out.println("Challenge from server received, will provide digital signature when login");
                this.challengeFromServer = items[2];
            }

            if (action.equals("NEW_USER")) {
                if (items.length != 4) return;
                String username = items[2];
                String publicKey = items[3];
                System.out.printf("New user (%s) joined, public key: %s\n", username, publicKey);
                publicKeysStorage.setUserPublicKeyMap(username, DataConverter.base64ToBytes(publicKey));
            }

            if (action.equals("DELIVER_MESSAGE")) {
                if (items.length != 4) return;
                String senderUsername = items[2];
                String encryptedWithSessionKey = items[3];
                String plainText = messageHandler.parseIncoming(senderUsername, encryptedWithSessionKey);
                if (plainText == null) {
                    System.out.printf("[Verification Failed] Message from %s is not trusted\n", senderUsername);
                    return;
                }
                System.out.printf("\n[%s]: %s\n", senderUsername, plainText);
            }

            return;
        }

        // check server message
        if (msg.matches("^SERVER_MSG===.*$")) {
            String[] items = msg.split("===");
            if (items.length >= 2) {
                String serverMessage = items[1];
                System.out.println(">> [Server message]: " + serverMessage);
            }
            return;
        }

        // check machine response
        if (msg.matches("^RESPONSE .*$")) {
            String[] items = msg.split("\\s+");
            if (items.length < 2 || items[1] == null || items[1].equals("")) return;

            String action = items[1];
            if (action.equals("AUTH")) {
                if (items.length < 3) return;
                String statusCode = items[2];
                if (statusCode.equals("200")) {
                    if (items.length < 5) return;

                    System.out.println(">> [Server]: Authenticated");
                    String username = items[3];
                    String sessionKey = items[4];
                    if (items.length >= 6) {
                        String[] onlineUsers = items[5].split("===");
                        if (onlineUsers.length > 0) {
                            Object[] strObjs = Arrays.stream(onlineUsers).filter(n -> n != null && n.length() > 0 && !n.equals(username) && publicKeysStorage.getUserPublicKey(n) == null).toArray();
                            String[] usersToRequest = Arrays.copyOf(strObjs, strObjs.length, String[].class);
                            if (usersToRequest.length > 0) {
                                String cmdForGettingPublicKey = String.format("COMMAND PUB_KEY_REQUEST %s", String.join("===", usersToRequest));
                                try {
                                    streamOut.writeUTF(cmdForGettingPublicKey);
                                    streamOut.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    this.username = username;
                    byte[] encryptedWithSelfPublicKey = DataConverter.base64ToBytes(sessionKey);
                    clientAuthenticator.setSessionKey(encryptedWithSelfPublicKey, true);
                    this.hasAuthenticated = true;
                } else {
                    String statusMessage = items.length >= 4 ? items[3] : "";
                    System.out.println(">> [Server]: Authentication failed - " + statusCode + " " + statusMessage);
                }
            }
            if (action.equals("SERVER_SIGNATURE")) {
                if (items.length != 4) return;
                String result = items[2];
                if (result.equals("200")) {
                    byte[] signature = DataConverter.base64ToBytes(items[3]);
                    boolean verified = AsymmetricCrypto.verifyData(this.challengeForServer.getBytes(), signature, clientAuthenticator.getServerPublicKey());
                    if (verified) {
                        login_win.close(1);
                        tokenWindow = new ClientTokenWindow(this);
//            System.out.println("Server is verified by digital signature, safe to log in");
                    } else {
                        login_win.notice(1, "[WARNING] Server has INVALID digital signature, this server could be forged");
//                        System.out.println("[WARNING] Server has INVALID digital signature, this server could be forged");
                    }
                }
            }

            if (action.equals("SEND_MESSAGE")) {
                if (items.length < 3) return;
                String statusCode = items[2];
                if (!statusCode.equals("200")) {
                    if (items.length >= 4) {
                        System.out.println("Failed to send message, server responds " + statusCode + " " + items[3]);
                    }
                }
            }

            if (action.equals("PUB_KEY")) {
                if (items.length != 6) return;

                String name = items[2];
                System.out.println("> Public key of user " + name + " received from server, verifying");
                byte[] publicKey = DataConverter.base64ToBytes(items[3]);
                byte[] serverSignature = DataConverter.base64ToBytes(items[4]);
                String checksum = items[5];
                boolean isSignatureValid = AsymmetricCrypto.verifyData(publicKey, serverSignature, clientAuthenticator.getServerPublicKey());
                boolean isChecksumVerified = Md5Helper.verify(publicKey, checksum);
                System.out.println(">>> Server signature OK: " + isSignatureValid);
                System.out.println(">>> Checksum OK: " + isChecksumVerified);
                if (isSignatureValid && isChecksumVerified) {
                    System.out.printf(">>> Public key of [%s] is confirmed\n", name);
                    publicKeysStorage.setUserPublicKeyMap(name, publicKey);
                } else {
                    System.out.printf(">>> Public key of [%s] is NOT trusted", name);
                }
            }

            if (action.equals("QUIT")) {
                this.hasAuthenticated = false;
                System.out.println("Good bye. Press RETURN to exit ...");
                stop();
            }
        }
    }

    private void start() throws IOException {
        console = new BufferedReader(new InputStreamReader(System.in));
        streamOut = new DataOutputStream(socket.getOutputStream());

        // ---- request to authenticate server ----
        this.challengeForServer = Double.toString(Math.random());
        String cmd = "COMMAND SERVER_AUTH " + this.challengeForServer;
        System.out.println("Challenging server, checking it's identity");
        streamOut.writeUTF(cmd);

        if (thread == null) {
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }

    void stop() {
        if (thread != null) {
            thread = null;
        }
        try {
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
        client.close();
        client.stopThread();
    }

    public static void main(String args[]) {
//        ChatClient client = null;
//        if (args.length != 3) {
//            System.out.println("Usage: java ChatClient [host] [port] [config-file]");
//        } else {
//            client = new ChatClient(args[0], Integer.parseInt(args[1]), args[2]);
//        }
        login_win = new ClientLoginWindow();
    }
}
