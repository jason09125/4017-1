package server.ServerGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import server.chat.ChatServer;

public class ServerSetupWindow {
	private JFrame frame;

	private JLabel welcoming;
	private JLabel IP_address;
	private JLabel port_label;
	private JTextField port_setup;
	private JButton create;
	private JLabel bg;

	private String server_IP;

	public ServerSetupWindow(String IP) {
		this.server_IP = IP;

		frame = new JFrame("Server Setup");

		addSerWinComponents(frame.getContentPane());

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
		frame.setSize(300, 350);
		frame.setResizable(false);
		frame.setLocation(200, 200);
		frame.setVisible(true);
	}
	
	private void addSerWinComponents(Container contentPane) {
		contentPane.setLayout(null);

		welcoming = new JLabel("Server Setup");
		welcoming.setFont(new Font(welcoming.getFont().getName(), welcoming.getFont().getStyle(), 20));
		Dimension welcome_size = welcoming.getPreferredSize();
		welcoming.setBounds(30, 20, welcome_size.width, welcome_size.height);
		contentPane.add(welcoming);

		IP_address = new JLabel("Server IP: " + server_IP);
		Dimension IP_address_size = IP_address.getPreferredSize();
		IP_address.setBounds(30, 60, IP_address_size.width, IP_address_size.height);
		contentPane.add(IP_address);

		port_label = new JLabel("Port");
		Dimension port_label_size = port_label.getPreferredSize();
		port_label.setBounds(30, 85, port_label_size.width, IP_address_size.height);
		contentPane.add(port_label);

// 		Enter Port Number here
		port_setup = new JTextField("1080", 30);
		port_setup.setBounds(30, 105, 200, IP_address_size.height);
		contentPane.add(port_setup);

		create = new JButton("Create");
		create.setBounds(85, 200, 120, 45);
		contentPane.add(create);

		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int port = Integer.valueOf(port_setup.getText());
					ChatServer server = new ChatServer(port);
					frame.dispose();
				} catch (NumberFormatException nfe) {
					notice(1, "Please input the port as Integer.");
				}
			}
		});

		String file_path = System.getProperty("user.dir");
		ImageIcon background = new ImageIcon(file_path + "\\src\\server\\image\\server_setup.png");
		Image image = background.getImage(); // transform it
		Image newimg = image.getScaledInstance(300, 300,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		background = new ImageIcon(newimg);
		bg = new JLabel(background);
		bg.setBounds(0, 0, 300, 300);
		contentPane.add(bg);
	}

	private void notice(int action, String msg) {
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
		case 1:
			String[] agree = { "OK" };

			int agreement = JOptionPane.showOptionDialog(null, msg, "Notification",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, agree, agree[0]);
			break;
		default:
			break;
		}
	}

	private void close() {
		frame.dispose();
		System.exit(0);
	}
}
