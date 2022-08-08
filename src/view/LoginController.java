//Timothy Walker tpw32
//Hasin Choudhury hmc94
package view;

import java.io.IOException;
import java.util.ArrayList;

import app.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.LoginController;
import javafx.scene.Node;


/**
 * 
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class LoginController {

	/**
	 * TextField where you enter the username to login
	 */
	@FXML TextField UsernameField;
	
	/**
	 * Button to login and start a session with the User
	 */
	@FXML Button LoginButton;
	
	/**
	 * List of all the User's stored
	 */
	ArrayList<User> userList = AdminController.getUsers();
	
	/**
	 * Keeps track of User that is currently logged in
	 */
	static User crntUser;
		
	/**
	 * Start method to initialize the Login window
	 * @param mainStage Stage
	 */
	public void start(Stage mainStage) {
		mainStage.setResizable(false);
		LoginButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String loginAttempt = UsernameField.getText();
				
				//no input in field
				if(loginAttempt.equals("") || loginAttempt == null){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("ERROR");
					alert.setContentText("No username detected, try again.");
					alert.show();
					return;
				}
				
				//Admin login
				if(loginAttempt.equalsIgnoreCase("admin")){
					try {
						Stage stage = new Stage();
						stage.setResizable(false);
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("adminview.fxml"));
						VBox root = (VBox)loader.load();						
						AdminController adminController = loader.getController();
						adminController.start();
						Scene scene = new Scene(root);
						stage.setScene(scene);
						((Node)e.getSource()).getScene().getWindow().hide();
						stage.show();	
						
					} catch (IOException m) {
						m.printStackTrace();
					}
				}else if(searchUser(userList, loginAttempt) != -1) { //User login
					crntUser = userList.get(searchUser(userList, loginAttempt));
					try {
						Stage stage = new Stage();
						stage.setResizable(false);
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("albumlist.fxml"));
						VBox root = (VBox)loader.load();						
						AlbumListController albumListController = loader.getController();
						albumListController.start();
						Scene scene = new Scene(root);
						stage.setScene(scene);
						((Node)e.getSource()).getScene().getWindow().hide();
						stage.show();	
						
					} catch (IOException m) {
						m.printStackTrace();
					}
				}else if(searchUser(userList, loginAttempt) == -1) {//User not found
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("ERROR");
					alert.setContentText("Requested User not found.");
					alert.show();
				}			
			}
		});	
		
	}
	
	/**
	 * Method to search through list of User's, looking for one by name
	 * @param list ArrayList of Users
	 * @param name Username
	 * @return index of User, negative if not found
	 */
	public int searchUser(ArrayList<User> list, String name){
		if(list == null) return -1;
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getUsername().equals(name)){
				return i;
			}
		}
		return -1;
	}
	
	

}
