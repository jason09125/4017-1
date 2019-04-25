package client.ClientGUI;

import client.chat.ChatClient;
import server.chat.ChatServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientChatWindow {

    private JFrame frame;
    private JLabel welcomimg;
    private JLabel user_name;
    private JTextField user_input;
    private JTextArea chat_record;
    private JScrollPane scroll_for_chat;
    private JButton send_btn;
    private JButton add_group_btn;
    private JButton add_member_btn;
    private DefaultListModel listModle;
    private JList groupList;
    private JLabel bg;

    private ChatClient cs;

    public ClientChatWindow(ChatClient cs) {
        this.cs = cs;

        frame = new JFrame("Chat Window");

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
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocation(900, 200);
        frame.setVisible(true);
    }

    private void addClientWinComponents(Container contentPane) {

        welcomimg = new JLabel("Welcome: ");
        welcomimg.setFont(new Font(welcomimg.getFont().getName(), welcomimg.getFont().getStyle(), 20));
        Dimension welcome_size = welcomimg.getPreferredSize();
        welcomimg.setBounds(15, 15, welcome_size.width, welcome_size.height);
        contentPane.add(welcomimg);

        user_name = new JLabel(cs.get_Name());
        user_name.setFont(new Font(user_name.getFont().getName(), user_name.getFont().getStyle(), 20));
        Dimension user_name_size = user_name.getPreferredSize();
        user_name.setBounds(welcome_size.width + 15, 15, user_name_size.width, welcome_size.height);
        contentPane.add(user_name);

        listModle = new DefaultListModel();
        listModle.addElement("All");
        groupList = new JList(listModle);
        groupList.setBounds(15, 65, 265, 350);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.setSelectedIndex(0);
        contentPane.add(groupList);

        chat_record = new JTextArea(5, 30);
        scroll_for_chat = new JScrollPane(chat_record);
        scroll_for_chat.setBounds(300, 65, 380, 350);
        chat_record.setLineWrap(true);
        chat_record.setWrapStyleWord(true);
        chat_record.setEditable(false);
        scroll_for_chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentPane.add(scroll_for_chat);

        user_input = new JTextField("", 100);
        user_input.setBounds(50, 450, 400, welcome_size.height);
        contentPane.add(user_input);

        user_input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user_input.getText().isEmpty()) {
                    notice(1, "Cannot send nothing.");
                } else {
                    cs.sendMsg(user_input.getText(), groupList.getSelectedValue().toString());
                    user_input.setText("");
                }
            }
        });

        send_btn = new JButton("SEND");
        send_btn.setBounds(470, 445, 120, 45);
        contentPane.add(send_btn);

        send_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (user_input.getText().isEmpty()) {
                    notice(1, "Cannot send nothing.");
                } else {
                    cs.sendMsg(user_input.getText(), groupList.getSelectedValue().toString());
                    user_input.setText("");
                }
            }
        });

        add_group_btn = new JButton("CREATE GROUP");
        add_group_btn.setBounds(50, 500, 240, 45);
        contentPane.add(add_group_btn);

        add_group_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user_input.getText().isEmpty()) {
                    notice(1, "Cannot send nothing.");
                } else {
                    cs.sendMsg(".ADD_GROUP", user_input.getText());
                    user_input.setText("");
                }
            }
        });

        add_member_btn = new JButton("ADD MEMBER");
        add_member_btn.setBounds(320, 500, 240, 45);
        contentPane.add(add_member_btn);

        add_member_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user_input.getText().isEmpty()) {
                    notice(1, "Cannot send nothing.");
                } else {
                    cs.sendMsg(".ADD_MEMBER " + user_input.getText(), groupList.getSelectedValue().toString());
                    user_input.setText("");
                }
            }
        });

        String file_path = System.getProperty("user.dir");
        ImageIcon background = new ImageIcon(file_path + "\\src\\client\\image\\chat_room_bg.jpeg");
        Image image = background.getImage(); // transform it
        Image newimg = image.getScaledInstance(1000, 664, Image.SCALE_SMOOTH);
        background = new ImageIcon(newimg);
        bg = new JLabel(background);
        bg.setBounds(0, 0, 1000, 664);
        contentPane.add(bg);
    }

    public void update_data(String data, int option) {
        switch (option) {
            case 0:
                listModle.addElement(data);
                break;
            case 1:
                chat_record.append(data + "\n");
                chat_record.setCaretPosition(chat_record.getDocument().getLength());
                break;
            default:
                break;
        }
    }

    public void close(Integer option) {
        switch (option) {
            case 0:
                cs.sendMsg(".bye", "All");
                frame.dispose();
                System.exit(0);
                break;
            case 1:
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
            case 2:
                String[] choose = {"OK"};

                int dialog = JOptionPane.showOptionDialog(
                        null,
                        noti_msg,
                        "Notification",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        choose,
                        choose[0]);
                close(1);
                break;
            default:
                break;
        }
    }
}
