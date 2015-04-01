package reserv;

import grupp4uppgift.ClientController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;Femton
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 * 
 * @author Alex
 *
 */
// Ta Bort all logik
// Ta bort connect
// Sätt ditt SendPM knapp
// Skapa FileChooser
// Sätt ditt response metod

public class ClientGUI extends JFrame implements ActionListener, KeyListener {
	private ClientController ClientController;
	private JFileChooser filechooser = new JFileChooser(); // Implement
	private JTextPane textArea1 = new JTextPane();
	private JTextArea textArea2 = new JTextArea();
	private JTextField textField = new JTextField();;
	private JButton send = new JButton("Skicka");
	private JButton sendImage = new JButton("Send Image");
	private JButton PM = new JButton("Send PM");
	private JButton disconnect = new JButton("Disconnect");
	private JPanel p = new JPanel();
	private String textArea = "Chat has started \n";

	/**
	 * 
	 * @param userName
	 */
	public ClientGUI(ClientController ClientController, String user) {
		this.ClientController = ClientController;
		
		JFrame frame = new JFrame(user);
		frame.setSize(500, 400);
		frame.setLocationRelativeTo(null);	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		textArea1.setBackground(new Color(255, 255, 255));
		textArea2.setBackground(new Color(255, 255, 255));
		textField.setBackground(new Color(255, 255, 255));
		textArea1.setEditable(false);
		textArea2.setEditable(false);

		JScrollPane txtAScroll1 = new JScrollPane(textArea1);
		txtAScroll1
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JScrollPane txtAScroll2 = new JScrollPane(textArea2);
		txtAScroll2
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		p.setLayout(null);

		p.add(txtAScroll1);
		p.add(txtAScroll2);
		p.add(textField);
		p.add(send);
		p.add(sendImage);
		p.add(PM);
		p.add(disconnect);

		txtAScroll1.setBounds(5, 5, 350, 300);
		txtAScroll2.setBounds(355, 5, 135, 300);

		textField.setBounds(5, 305, 275, 65);
		send.setBounds(280, 305, 75, 65);
		sendImage.setBounds(357, 327, 132, 20);
		PM.setBounds(357, 305, 132, 20);
		disconnect.setBounds(357, 349, 132, 20);

		send.addActionListener(this);
		sendImage.addActionListener(this);
		PM.addActionListener(this);
		disconnect.addActionListener(this);
		
		frame.add(p);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(send);
		

	}



	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == send) {
			try {
				ClientController.sendMessage(textField.getText());
				textField.setText("");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == disconnect) {
			try {
				ClientController.disconnect();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
		if (e.getSource() == sendImage) {
			if (filechooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	        	try {
					ClientController.sendImage(filechooser.getSelectedFile().getPath());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
		if (e.getSource() == PM) {
			String setUser;
			LinkedList pm = new LinkedList();
			setUser = JOptionPane.showInputDialog("Skicka PM till: ");
			pm.add(setUser);
			pm.add(textField.getText());
			textField.setText("");
			try {
				ClientController.sendPM(pm);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

	}
	
	public void setResponse(String response){
		textArea += response;
		textArea1.setText(textArea);
		textArea1.setCaretPosition(textArea1.getDocument().getLength());
	}
	
	public void setResponse(User user){
		textArea2.setText(user.getList());
		textArea2.setCaretPosition(textArea2.getDocument().getLength());
	}
	
	public void setResponse(Icon iconResponse) {
		textArea1.insertIcon(iconResponse);
		textArea1.setCaretPosition(textArea1.getDocument().getLength());
	}
	

	/**
 * 
 */
	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				ClientController.sendMessage(textField.getText());
				textField.setText("");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub

	}





	/*
	 * MAIN METODEN---------------------------------------------------
	 */

	/*
	 * MAIN METODEN---------------------------------------------------
	 */

}