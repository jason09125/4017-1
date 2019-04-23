package client.ClientGUI;

import client.chat.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientTokenWindow {

    private JFrame frame;
    private JLabel respon;
    private JLabel user_lab;
    private JLabel pw_lab;
    private JLabel token_lab;
    private JTextField user_name;
    private JTextField user_token;
    private JPasswordField password;
    private JButton login_but;
    private JLabel bg;
    private ChatClient chatClient;

    public ClientTokenWindow(ChatClient chatClient) {
        this.chatClient = chatClient;

        frame = new JFrame("Token Window");

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

        respon = new JLabel("Server verified, safe to login.");
        respon.setFont(new Font(respon.getFont().getName(), respon.getFont().getStyle(), 16));
        Dimension respon_size = respon.getPreferredSize();
        respon.setBounds(15, 50, respon_size.width, respon_size.height);
        contentPane.add(respon);

        user_lab = new JLabel("Username:");
        Dimension user_lab_size = user_lab.getPreferredSize();
        user_lab.setBounds(110, 120, user_lab_size.width, user_lab_size.height);
        contentPane.add(user_lab);

        // please remove the default data --> text
        user_name = new JTextField("Eric", 30);
        user_name.setBounds(45, 140, 200, user_lab_size.height);
        contentPane.add(user_name);

        pw_lab = new JLabel("Password:");
        Dimension pw_lab_size = pw_lab.getPreferredSize();
        pw_lab.setBounds(110, 170, pw_lab_size.width, user_lab_size.height);
        contentPane.add(pw_lab);

        // please remove the default data --> text
        password = new JPasswordField("123456", 30);
        password.setBounds(45, 195, 200, user_lab_size.height);
        contentPane.add(password);

        token_lab = new JLabel("Token:");
        Dimension token_lab_size = token_lab.getPreferredSize();
        token_lab.setBounds(120, 220, token_lab_size.width, user_lab_size.height);
        contentPane.add(token_lab);

        user_token = new JTextField("", 30);
        user_token.setBounds(45, 245, 200, user_lab_size.height);
        contentPane.add(user_token);

        login_but = new JButton("Connect");
        login_but.setBounds(85, 280, 120, 45);
        contentPane.add(login_but);

        login_but.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user_name.getText().isEmpty() || password.getText().isEmpty() || user_token.getText().isEmpty()) {
                    notice(1, "Please fill in all info. first");
                } else {
                    chatClient.sendMsg(".login " + user_name.getText() + " " + password.getText() + " " + user_token.getText());
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

    protected void close() {
        frame.dispose();
        System.exit(0);
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
                    close();
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
