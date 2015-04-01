package reserv;

import grupp4uppgift.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import javax.swing.Icon;

/**
 * Klassen är en "Chat Server" som kan lagra och skicka ut meddelande till anslutna klienter
 * @author Stefan Tran
 *
 */

public class ChatServer implements Runnable {
	/**
	 * Nödvändinga instansvariabler deklareras
	 */
	private ServerSocket serverSocket;
	private Thread server = new Thread(this);
	private ArrayList<User> offlineList;
	private ArrayList<ClientHandler> alch;
	private ArrayList<User> onlineList;
	private LinkedList<Object> chatLog = new LinkedList<Object>();
	private User user;
	private ClientHandler ClientHandler;
	private static Calendar calendar;
	private String mainlog = getTime() + "SERVER START" + "\n";
	
	/**
	 * Konstruktor som tar emot en int i form av port
	 * @param port en int för port
	 * @throws IOException
	 */

	public ChatServer(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		alch = new ArrayList<ClientHandler>();
		onlineList = new ArrayList<User>();
		offlineList = new ArrayList<User>();
		server.start();
	}

	@Override
	
	/**
	 * Metoden startar servern och skapar en ArrayList som lagrar inkommande klienters sockets
	 */
	public void run() {
		System.out.println("ChatServer Online");
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ClientHandler ch = new ClientHandler(socket);
				this.ClientHandler = ch;
				alch.add(ch);
				ch.start();
			} catch (IOException e) {
			}
		}

	}
/**
 * En inre klass som kan hantera de olika klienter som kopplar in sig till servern
 * @author Girondins
 *
 */
	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
/**
 * Konstruktor som tar emot rätt socket från klient
 * @param socket från ansluten klient
 * @throws IOException
 */
		public ClientHandler(Socket socket) throws IOException {
			this.socket = socket;
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		}
		
/**
 * Metod som startar hantering av inkommande och utgående object
 * 
 */
		public void run() {
			Object object, request;
			Icon iconRequest;
			String list = "";
			LinkedList pm;

			try {
				while (true) {

					object = ois.readObject();
					chatLog.add(object);
					request = chatLog.pop();

					if (object instanceof String) {
						request = (String) object;
						mainChat(request);
						mainLog(request);
					} else if (object instanceof Icon) {
						iconRequest = (Icon) object;
						sendImage(iconRequest);
						mainLog(iconRequest);
					} else if (object instanceof LinkedList) {
						pm = (LinkedList) object;
						sendPM(pm);
						/**
						 * Om objektet är en instans av User så lagras varje ny User i en ArrayList
						 * Antingen i onlinelist eller offlinelist beroende på ifall User är ansluten eller inte
						 * Ansluter en redan existerad användare till servern så hämtas den existerad log till användaren
						 * Loggar en användare ut så flyttas användaren från onlineList till offlineList
						 */
					} else if (object instanceof User) {
						user = (User) object;
						user.setId(ClientHandler.getId());
						if (offlineList.contains(user)) {
							offlineList.remove(user);
						}
						onlineList.add(user);
						if (onlineList.contains(user)
								|| offlineList.contains(user)) {
							readLog(user);
						}
						oos.writeObject(user.getUserLog());
						oos.flush();
						for (int i = 0; i < onlineList.size(); i++) {
							list += onlineList.get(i).getName() + "\n";
						}
						connectedUsers(list);
					}
				}
			} catch (IOException e) {
				try {
					oos.close();
					ois.close();
					socket.close();
				} catch (IOException e1) {
					for (int i = alch.size(); --i >= 0;) {
						ClientHandler ch = alch.get(i);
						if (ch.socket.isClosed()) {
							alch.remove(i);
							offlineList.add(onlineList.get(i));
							onlineList.remove(i);
						}
					}
				} finally {
					for (int i = alch.size(); --i >= 0;) {
						ClientHandler ch = alch.get(i);
						if (ch.socket.isClosed()) {
							alch.remove(i);
							offlineList.add(onlineList.get(i));
							onlineList.remove(i);

						}
					}

				}
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			}

			try {
				socket.close();
			} catch (Exception e) {
				System.out.println(e);
			}

		}
		/**
		 * Metod som hanterar PM(Privata meddelanden)
		 * Genom att ta emot en LinkedList objekt som innehåller:
		 * I element 1 = Mottagande Användare
		 * I element 2 = Meddelandet som ska skickas
		 * I element 3 = Användaren som skickar meddelandet
		 * @param PM en LinkedList som innehåller information för att kunna skicka PM
		 * @throws IOException
		 */
		@SuppressWarnings("rawtypes")
		public void sendPM(LinkedList PM) throws IOException {
			LinkedList pm = PM;
			String reciever = (String) pm.pop();
			String message = (String) pm.pop();
			String sender = (String) pm.pop();
			User recievingUser;
			User sendingUser = null;

			for (int i = 0; i < onlineList.size(); i++) {
				String checkSender = onlineList.get(i).getName();
				if (sender.equals(checkSender)) {
					sendingUser = onlineList.get(i);
				}
			}

			for (int i = 0; i < onlineList.size(); i++) {
				String onlineUser = onlineList.get(i).getName();
				if (reciever.equals(onlineUser)) {
					recievingUser = onlineList.get(i);
					for (int j = 0; j < alch.size(); j++) {
						if (alch.get(j).getId() == recievingUser.getId()) {
							ClientHandler ch = alch.get(j);
							userLog(getTime() + "[PM:] " + sender + ": " + message + "\n", recievingUser);
							userLog(getTime() + "[PM:] " + sender + ": " + message + "\n", sendingUser);
							mainLog("[PM:] " + sender + ": " + message + "\n" + "->" + recievingUser.getName());
							ch.oos.writeObject(getTime() + "[PM:] " + sender + ": " + message + "\n");
							ch.oos.flush();
							oos.writeObject(getTime() + "[PM] " + sender + ": " + message + "\n");
							oos.flush();
						}
					}
				} else {
					for (int y = 0; y < offlineList.size(); y++) {
						String offUser = offlineList.get(i).getName();
						if (reciever.equals(offUser)) {
							recievingUser = offlineList.get(i);
							userLog(getTime() + "[PM:] " + sender + ": "+ message + "\n", recievingUser);
							userLog(getTime() + "[PM:] " + sender + ": "+ message + "\n", sendingUser);
							mainLog("[PM:] " + sender + ": " + message + "->" + recievingUser.getName());
							oos.writeObject(getTime() + "[PM] " + sender + ": " + message + "\n");
							oos.flush();
						} else {
							userLog(getTime() + "[ERROR] User does not exist", sendingUser);
							mainLog(getTime() + sender + ": " + "[ERROR] User does not exist");
							oos.writeObject(getTime() + "[ERROR] User does not exist");
							oos.flush();
						}

					}

				}
			}

		}

	}
	/**
	 * Metod som hanterar loggning för all aktivitet i servern
	 * @param message meddelanden som skickas genom server och klienter
	 */
	public void mainLog(Object message) {
		BufferedWriter bw = null;
		File f = new File("/Users/Girondins/Desktop/Skool/chatLog/mainLog.txt");
		mainlog += getTime() + message + "\n";
		try {
			bw = new BufferedWriter(new FileWriter(f));
			bw.write(mainlog);
			bw.flush();
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}
	}
	/**
	 * Metod som retunerar nuvarande tid i format HH/MM/SS
	 * @return en sträng som innnehpller tid i format HH/MM/SS
	 */
	public String getTime() {
		String time;
		Calendar cal = Calendar.getInstance();

		if (cal.get(Calendar.SECOND) < 10) {
			time = "[" + cal.get(Calendar.HOUR_OF_DAY) + ":"
					+ cal.get(Calendar.MINUTE) + ":0"
					+ cal.get(Calendar.SECOND) + "]";
		} else {
			time = "[" + cal.get(Calendar.HOUR_OF_DAY) + ":"
					+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND)
					+ "]";
		}
		return time;
	}
	/**
	 * Metod som loggar aktivitet för varje ansluten klient
	 * @param message meddelande som klient mottager
	 * @param user den anslutna användaren
	 */
	public void userLog(Object message, User user) {
		String messageReqeust = (String) message;
		user.setUserLog(messageReqeust);
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(
					"/Users/Girondins/Desktop/Skool/chatLog/" + user.getName()
							+ "mainLog.txt"));
			bw.write(user.getUserLog());
			bw.flush();
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}
	}
	/**
	 * Metod som läser av existerad log för ansluten användaren
	 * @param user den anslutna användaren
	 */
	public void readLog(User user) {
		String read;
		BufferedReader br = null;
		File f = new File("/Users/Girondins/Desktop/Skool/chatLog/"
				+ user.getName() + "mainLog.txt");

		if (f.exists()) {
			try {
				br = new BufferedReader(new FileReader(
						"/Users/Girondins/Desktop/Skool/chatLog/"
								+ user.getName() + "mainLog.txt"));
				read = br.readLine();
				while (read != null) {
					user.setUserLog(read);
					read = br.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Metod som hanterar Icon objektet
	 * @param icon tar emot Icon objekt som skickas ut
	 * @throws IOException
	 */
	public void sendImage(Icon icon) throws IOException {
		for (int i = alch.size(); --i >= 0;) {
			ClientHandler ch = alch.get(i);
			ch.oos.writeObject(icon);
			ch.oos.flush();
		}
	}
	/**
	 * Metod som tar emot meddelande och skickar till alla anslutna användare
	 * @param message meddelande som skickas ut
	 * @throws IOException
	 */
	public void mainChat(Object message) throws IOException {

		for (int i = alch.size(); --i >= 0;) {
			ClientHandler ch = alch.get(i);
			for (int j = 0; j < onlineList.size(); j++) {
				if (ch.getId() == (onlineList.get(j).getId())) {
					userLog(getTime() + message, onlineList.get(j));
					ch.oos.writeObject(getTime() + message);
					ch.oos.writeObject("\n");
					ch.oos.flush();
				}

			}
		}
	}
	/**
	 * Metod som skickar ut alla anslutna användare till alla anslutna klienter
	 * @param list anslutna klienter
	 * @throws IOException
	 */
	public void connectedUsers(String list) throws IOException {
		user.setList(list);
		User user;
		for (int i = alch.size(); --i >= 0;) {
			ClientHandler ch = alch.get(i);
			if(onlineList.get(i).getId()==(ch.getId())){
				user = onlineList.get(i);
				user.setList(list);
				ch.oos.writeObject(user);
				ch.oos.flush();
			}
			ch.oos.writeObject(this.user);
			ch.oos.flush();

		}
	}
	/**
	 * Metod som startar servern
	 * @param args argument
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new ChatServer(3001);

	}
}
