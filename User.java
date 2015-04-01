package reserv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
	/**asdasdasdasd
	 * Klass för användare
	 * @author Stefan Tran
	 *
	 */
public class User implements Serializable{
	/**
	 * Deklaration av nödvändiga instansvariabler
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private long id;
	private String list;
	private String userLog = "";
	/**
	 * Konstruktor som tar emot ett användarnamn
	 * @param name användarnamn
	 */
	public User(String name) {
		this.name = name;

	}
	/**
	 * Metod som sätter ett ID för användaren
	 * @param id användarens ID
	 */
	public void setId(long id){
		this.id = id;
	}
	/**
	 * Metod som retunerar användarens ID
	 * @return användarens ID
	 */
	public long getId(){
		return this.id;
	}
	/**
	 * Metod som sätter ett namn för användaren
	 * @param name användar namn
	 */
	public void setName(String name){
		this.name = name;
	}
	/**
	 * Metod som hämtar användarens namn
	 * @return användarens namn
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Metod som sätter in information om vilka användare som är anslutna till servern
	 * @param list användarna som är anslutna till server
	 */
	public void setList(String list){
		this.list = list;
	}
	/**
	 * Metod som retunerar anslutna användare till servern
	 * @return användare som är anslutna till server
	 */
	public String getList(){
		return this.list;
	}
	/**
	 * Metod som sätter ihop din privata log
	 * @param message text rad för uppbyggnad av log
	 */
	public void setUserLog(String message){
		userLog += message + "\n";
	}
	/**
	 * Metod som retunerar din log
	 * @return din log
	 */
	public String getUserLog(){
		return this.userLog;
	}


}