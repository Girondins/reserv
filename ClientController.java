package reserv;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import reserv.ClientGUI;
import reserv.User;


	/**
	 * Klass som hanterar all logik för klienter
	 * @author Stefan Tran
	 *
	 */
public class ClientController {
	/**
	 * Deklaration av nödvändiga instansvariabler
	 */
	private ClientGUI ClientGUI;
	private ChatClient client;
	private static String name;
	/**
	 * Konstruktor som tar emot klient som ska hanteras
	 * @param client klient som ska hanteras
	 * @throws IOException
	 */
	public ClientController(ChatClient client) throws IOException {

		this.client = client;
		String name = JOptionPane.showInputDialog("Ange namn");
		client.setUserName(name);
		this.name = name;
		ClientGUI = new ClientGUI(this, name);
		client.setClientController(this);
		connect();


	}
	/**
	 * Metod som ansluter användaren till server
	 * @throws IOException
	 */
	public void connect() throws IOException{
		client.connect(this.name);
	}
	/**
	 * Metod som hanterar meddelande från klient till server
	 * @param message meddelande som ska skickas
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException{
		client.sendMessage(message);
	}
	/**
	 * Metod som hanterar bilder som ska skickas till server
	 * @param pathway sökvägen för bilden
	 * @throws IOException
	 */
	public void sendImage(String pathway) throws IOException{
		client.sendIcon(pathway);
	}
	/**
	 * Metod som hanterar PM meddelanden
	 * @param pm det PM som ska skickas
	 * @throws IOException
	 */
	public void sendPM(LinkedList pm) throws IOException{
		client.sendPM(pm);
	}
	/**
	 * Metod som hanterar bort koppling av klienter från server
	 * @throws IOException
	 */
	public void disconnect() throws IOException{
		client.exit();
	}
	
	/**
	 * Metod som leverar meddelande från server till klient
	 * @param response meddelandet som ska levereras
	 */
	public void newResponse(String response) {
		ClientGUI.setResponse(response);
		
	}
	/**
	 * Metod som ska leverera bild från server till klient
	 * @param iconResponse bild som ska levereras
	 */
	public void newResponse(Icon iconResponse) {
		ClientGUI.setResponse(iconResponse);
	
}
	/**
	 * Metod som hanterar användare till klient
	 * @param user användar som ska skickas till klient
	 */
	public void newResponse(User user){
		ClientGUI.setResponse(user);
	}
	
	
	/**
	 * Metod som startar klienterna
	 * @param args argument
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void main(String []args) throws UnknownHostException, IOException{
		
		
		ChatClient b = new ChatClient("127.0.0.1",3001,name);
		new ClientController(b);

	}


	
}

