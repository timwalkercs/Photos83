//Timothy Walker tpw32
//Hasin Choudhury hmc94
package app;

import java.io.*;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.AdminController;
import view.LoginController;

/**
 * 
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class Photos83 extends Application implements Serializable {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Login Controller
	 */
	private LoginController loginController;
	
	/**
	 * ArrayList of all users
	 */
	public ArrayList<User> userList;
	
	/**
	 * String of data storage directory
	 */
	public static final String storageDirectory = "data";
	
	/**
	 * String of file to save user info
	 */
	public static final String storageFile = "users.data";
	

	
	@Override
	/**
	 * Start Method which sets the initial stage
	 */
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(getClass().getResource("/view/loginwindow.fxml"));
		VBox root = (VBox)loader.load(); //looks at .fxml file

		// Get controller instance from loader and call start
		loginController = loader.getController();
		loginController.start(primaryStage);
		
		//NOTE: DO NOT CREATE A CONTROLLER INSTANCE WITH new
		//		it will not be connected to FXML widgets
		//		you must call getController() on FXMLLoader after you call load()

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show(); 

	}

	/**
	 * Main method to initiate the entire project
	 * @param args typical main method parameter
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Photos83 session = new Photos83();
		
		//If previous data found
		if(load() != null) {
			session = load();
			AdminController.userList = session.userList;
		}
		launch(args);
		session.userList = AdminController.getUsers();
		save(session);

	}
	
	/**
	 * Method that saves the data from the current session, to be loaded upon next open
	 * @param session a session of the app
	 */
	public static void save(Photos83 session) {
		ObjectOutputStream outStream;
		
		try {
			outStream = new ObjectOutputStream(new FileOutputStream(storageDirectory + File.separator + storageFile));
			outStream.writeObject(session);
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();			
		}
	}
	
	/**
	 * Method that loads data from the previous session
	 * @return session
	 */
	public static Photos83 load() {
		ObjectInputStream inStream = null;
		Photos83 session = null;
	
		try {
			inStream = new ObjectInputStream(new FileInputStream(storageDirectory + File.separator + storageFile));
			session = (Photos83)inStream.readObject();
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			try { 
				inStream.close(); 
			} catch (IOException e1) {}
			return null;
		}
		try { 
			inStream.close(); 
		} catch (IOException e1) {}
		return session;
	}

}
