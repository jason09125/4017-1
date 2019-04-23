package server.ServerGUI;

import server.chat.ChatServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerWindow {

	private HashMap<String, ArrayList<String>> group_info = null;
	private ChatServer cs;

	private JLabel welcome_label;
	private JLabel num_client;
	private JLabel connected_client;

	private JTextArea chat_record;
	private JScrollPane scroll_for_chat;

	private JList group_List;
	private JScrollPane scroll_for_group;

	private JButton exit_btn;
	private JButton remove_client;
	private JButton add_client;

	private JFrame frame;

//    @Override // Observer interface's implemented method
//    public void update(Observable o, Object data) {
//        label.setText((String) data); // displays new text in JLabel
//    }

	public ServerWindow(ChatServer cs) {

		this.group_info = group_info;
		this.cs = cs;

		frame = new JFrame("Server Window");

		addSerWinComponents(frame.getContentPane());

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
				notice(0);
			}
		});

		// close window to exit
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// set window sie, make it not resizable and visible
		frame.setSize(800, 600);
		frame.setResizable(false);
		frame.setLocation(200, 200);
		frame.setVisible(true);
	}

	private void notice(int action) {
		switch (action) {
		case 0:
			String[] options = { "YES", "NO" };

			int choice = JOptionPane.showOptionDialog(null, "Are you sure to leave now?", "Notification",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);

			String option = options[choice];

			if (option.equals(options[0])) {
				close();
			}
			break;
		default:
			break;
		}
	}

	private void close() {
		frame.dispose();
		System.exit(0);
	}

	private void addSerWinComponents(Container contentPane) {
		contentPane.setLayout(null);

		welcome_label = new JLabel("Welcome Admin");
		welcome_label.setFont(new Font(welcome_label.getFont().getName(), welcome_label.getFont().getStyle(), 20));
		Dimension welcome_size = welcome_label.getPreferredSize();
		welcome_label.setBounds(15, 15, welcome_size.width, welcome_size.height);
		contentPane.add(welcome_label);

		connected_client = new JLabel("Client(s) connected to this Server.");
		connected_client
				.setFont(new Font(connected_client.getFont().getName(), connected_client.getFont().getStyle(), 13));
		Dimension connected_client_size = connected_client.getPreferredSize();
		connected_client.setBounds(550, 15, connected_client_size.width, connected_client_size.height);
		contentPane.add(connected_client);

		num_client = new JLabel("0");
		num_client.setFont(new Font(num_client.getFont().getName(), num_client.getFont().getStyle(), 13));
//        Dimension num_client_size = connected_client.getPreferredSize();
		num_client.setSize(25, connected_client_size.height);
//        Dimension num_client_size = num_client.getPreferredSize();
		num_client.setBounds(525, 15, 50, connected_client_size.height);
		contentPane.add(num_client);

		chat_record = new JTextArea(5, 30);
		scroll_for_chat = new JScrollPane(chat_record);
		scroll_for_chat.setBounds(300, 65, 380, 350);
		chat_record.setLineWrap(true);
		chat_record.setWrapStyleWord(true);
		chat_record.setEditable(false);
		scroll_for_chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scroll_for_chat);

		exit_btn = new JButton("Close Server");
		exit_btn.setBounds(575, 450, 150, 50);
		contentPane.add(exit_btn);

		exit_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				notice(0);
			}
		});

		add_client = new JButton("Add Client");
		add_client.setBounds(50, 450, 150, 50);
		contentPane.add(add_client);

		remove_client = new JButton("Remove Client");
		remove_client.setBounds(300, 450, 150, 50);
		contentPane.add(remove_client);

//		Object[] members = group_info.keySet().toArray();
//		group_List = new JList(members);
//		group_List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		scroll_for_group = new JScrollPane(group_List);
//		scroll_for_group.setBounds(50, 65, 150, 350);
//		scroll_for_group.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		contentPane.add(scroll_for_group);
//
//		group_List.addListSelectionListener(new ListSelectionListener() {
//			@Override
//			public void valueChanged(ListSelectionEvent e) {
//				update_data("", 2);
//
//				refreshChat();
//			}
//		});
		
	}

	protected String get_selected_group() {
		return group_List.getSelectedValue().toString();
	}

	private void refreshChat() {

//		String chat = cs.get_chat_record(group_List.getSelectedValue().toString());

//		update_data(chat, 1);

	}

	public void update_data(String data, int option) {
		switch (option) {
		case 0:
			num_client.setText(data);
			chat_record.setCaretPosition(chat_record.getDocument().getLength());
//                System.out.println("connected clients: " + num_client.getText());
			break;
		case 1:
			chat_record.append(data);
			chat_record.setCaretPosition(chat_record.getDocument().getLength());
			break;
		case 2:
			chat_record.setText("");
			break;
		default:
			break;
		}
	}
}
//        quit_button.setFont(new Font("Dialog", Font.PLAIN, 20));