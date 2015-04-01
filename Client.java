package reserv;

import grupp4uppgift.ClientController;

import java.io.IOException;
import java.util.LinkedList;

	/**
	 * Ett interface för abstrakta metoder
	 * @author Stefan Tran
	 *
	 */
public interface Client {
	public void setClientController(ClientController ClientController);
	public void sendMessage(String message) throws IOException;
	public void sendIcon(String pathway) throws IOException;
	public void sendPM(LinkedList pm) throws IOException;
	public void exit() throws IOException;
	public void setUserName(String username);
	public void connect(String name) throws IOException;

}
