package reserv;

import grupp4uppgift.ClientController;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import reserv.Client;
import reserv.User;

public class ChatClient implements Client{
	private ClientController ClientController;
	private Socket socket;
	FUCKING MENJIE
	private ObjectOutputStream oos;
    //asjfoasfasf
	private ObjectInputStream ois;
	private String userName;
	private User user;

	/**
	 * Konstruktorn som initierar Socket, ObjectOutputStream, ObjectInputStream och User.
	 * @param ip adress till socketen.
	 * @param port port till socketen.
	 * @param name namn till användaren.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	
	public ChatClient(String ip, int port, String name) throws UnknownHostException, IOException{
		socket = new Socket(ip,port);
		oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		user = new User(name); 
		new Listener().start();
	}
	
	/**
	 * Metod som initierar ClientController med parametern.
	 * @param ClientController tar emot en ClientController.
	 */
	public void setClientController(ClientController ClientController) {
		this.ClientController = ClientController;
		
	}


	/**
	 * Metod som skickar medelande genom ObjectOutputStream.
	 * @param message string som skickas.
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		oos.writeObject(this.userName + ": " + message);
		oos.flush();
		
	}

	/**
	 * Metod som skickar medelande genom ObjectOutputStream.
	 * @param message LinkedList som skickas.
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendPM(LinkedList message) throws IOException {
		message.add(this.userName);
		oos.writeObject(message);
		oos.flush();
		
	}
	
	/**
	 * Metod som tar emot en string och initierar name samt skickar namn över ObjectOutputStream.
	 * @param name String som innehåller namn.
	 * @throws IOException
	 */
	public void connect(String name) throws IOException{

		user.setName(name);
		oos.writeObject(user);
		oos.flush();
		oos.writeObject(name + " has connected");
		oos.flush();
	}
	
	/**
	 * Metod som tar emot String som är plats för bild. Skickar användarnamn samt bilden. 
	 * @param pathway Sträng med platsen för filen.
	 * @throws IOException
	 */
	public void sendIcon(String pathway) throws IOException {
		oos.writeObject(this.userName + ": ");
		oos.flush();
		oos.writeObject(new ImageIcon(pathway));
		oos.flush();
		
	}

	/**
	 * Metod som skickar en sträng till servern att användaren kopplar från.
	 * stänger socketen.
	 * @throws IOException
	 */
	public void exit() throws IOException{

		oos.writeObject(this.userName + " HAS DISCONNECTED");
//		oos.writeObject(".SHUTDOWN.");
		oos.flush();
		if(socket != null){
			socket.close();
	
		}
	}
	/**
	 * Metod som tar emot String och initierar username.
	 * @param username String med användarnamn.
	 */
	public void setUserName(String username) {
		this.userName = username;
		
	}

	/**
	 * Privat som lissnar på ObjectInputStream. if sats som ser vilket sorts object det är,
	 * och använder metod därefter.
	 * @author Hugobergstrom
	 *
	 */
	private class Listener extends Thread{
		public void run(){
			String textResponse;
			Icon iconResponse;
			Object object;
			User user;
			try{
				while(true){
					object = ois.readObject();
					if(object instanceof String){
						textResponse = (String) object;
						ClientController.newResponse(textResponse);
					}
					else if(object instanceof Icon){
						iconResponse = (Icon) object;
						ClientController.newResponse(iconResponse);
					}
					
					
					else if(object instanceof User){
						user = (User) object;
						ClientController.newResponse(user);
					}
				}
			}catch(IOException e){
				ClientController.newResponse("Du har kopplat ner");
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			}
		}
		
	}	
}