package server.ClientGUI;

import client.chat.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientLoginWindow {

    private JFrame frame;
    private JLabel welcomimg;
    private JLabel server_IP;
    private JLabel port_num;
    private JLabel token_lab;
    private JTextField serverIP;
    private JTextField port;
    private JTextField user_token;
    private JButton connect;
    private JLabel bg;
//    private JOptionPane popUP = null;

    public ClientLoginWindow() {
        frame = new JFrame("Login Window");

        addClientWinComponents(frame.getContentPane());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
                notice(0, "");
            }
        });

        // close window to exit
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // set window sie, make it not resizable and visible
        frame.setSize(300, 400);
        frame.setResizable(false);
        frame.setLocation(800, 200);
        frame.setVisible(true);
    }

    private void addClientWinComponents(Container contentPane) {
        contentPane.setLayout(null);

        welcomimg = new JLabel("Welcome to use this JCS");
        welcomimg.setFont(new Font(welcomimg.getFont().getName(), welcomimg.getFont().getStyle(), 20));
        Dimension welcomimg_size = welcomimg.getPreferredSize();
        welcomimg.setBounds(20, 50, welcomimg_size.width, welcomimg_size.height);
        contentPane.add(welcomimg);

        server_IP = new JLabel("Server IP address:");
        Dimension server_IP_size = server_IP.getPreferredSize();
        server_IP.setBounds(80, 120, server_IP_size.width, server_IP_size.height);
        contentPane.add(server_IP);

        // please remove the default data --> text
        serverIP = new JTextField("localhost", 30);
        serverIP.setBounds(45, 140, 200, server_IP_size.height);
        contentPane.add(serverIP);

        port_num = new JLabel("Port number:");
        Dimension port_num_size = port_num.getPreferredSize();
        port_num.setBounds(95, 170, port_num_size.width, port_num_size.height);
        contentPane.add(port_num);

        // please remove the default data --> text
        port = new JTextField("1080", 30);
        port.setBounds(45, 190, 200, server_IP_size.height);
        contentPane.add(port);

        token_lab = new JLabel("Properties file name:");
        Dimension token_lab_size = token_lab.getPreferredSize();
        token_lab.setBounds(80, 220, token_lab_size.width, server_IP_size.height);
        contentPane.add(token_lab);

        user_token = new JTextField("", 30);
        user_token.setBounds(45, 245, 200, server_IP_size.height);
        contentPane.add(user_token);

        connect = new JButton("Connect");
        connect.setBounds(85, 280, 120, 45);
        contentPane.add(connect);

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (serverIP.getText().isEmpty() || port.getText().isEmpty() || user_token.getText().isEmpty()) {
                    notice(1, "Please fill in all info. first");
                } else {
                    ChatClient client = new ChatClient(serverIP.getText(), Integer.parseInt(port.getText()), "./client-config/" + user_token.getText() + ".properties");
                }
            }
        });

        String file_path = System.getProperty("user.dir");
        ImageIcon background = new ImageIcon(file_path + "\\src\\client\\image\\client_login.jpg");
        Image image = background.getImage(); // transform it
        Image newimg = image.getScaledInstance(920, 440, Image.SCALE_SMOOTH); // scale it the smooth way
        background = new ImageIcon(newimg);
        bg = new JLabel(background);
        bg.setBounds(-320, -20, 920, 440);
        contentPane.add(bg);
    }

//    protected String getUserName() {
//        return user_name.getText();
//    }

    public void close(Integer option) {
        switch (option) {
            case 0:
            frame.dispose();
            System.exit(0);
            break;
            default:
                frame.dispose();
                break;
        }

    }

    public void notice(int action, String noti_msg) {
        switch (action) {
            case 0:
                String[] options = {"YES", "NO"};

                int choice = JOptionPane.showOptionDialog(
                        null,
                        "Are you sure to leave now?",
                        "Notification",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);

                String option = options[choice];

                if (option.equals(options[0])) {
                    close(0);
                }
                break;
            case 1:
                String[] agreement = {"OK"};

                int agree = JOptionPane.showOptionDialog(
                        null,
                        noti_msg,
                        "Notification",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        agreement,
                        agreement[0]);

                break;
            default:
                break;
        }
    }
}
