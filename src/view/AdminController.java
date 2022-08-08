//Timothy Walker tpw32
//Hasin Choudhury hmc94
package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import app.User;
//import view.PrimaryController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller class for the admin window, can add and delete users
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class AdminController {
	
	/**
	 * Button to add new User
	 */
	@FXML Button AddUser;
	
	/**
	 * Button to delete selected User
	 */
	@FXML Button DeleteUser;
	
	/**
	 * TextField to input a new User's name
	 */
	@FXML TextField NewUserField;
	
	/**
	 * ListView to display the list of User's
	 */
	@FXML ListView<String> UsersView;
	
	/**
	 * Button to log out and return to the home window
	 */
	@FXML Button Logout;
	
	
	/**
	 * ArrayList of all Users
	 */
	public static ArrayList<User> userList;
	
	/**
	 * ObservableList that will be used to display the Users
	 */
	private ObservableList<String> obsList;

	/**
	 * Start method, initializes the window
	 */
	public void start() {
		
		//BASIC STARTUP PROTOCOL
		 if(userList == null) {userList = new ArrayList<User>();}
		 obsList = FXCollections.observableArrayList();
		 //fill obsList with usernames of all the Users in UserList
		 for(int i = 0; i < userList.size(); i++){
			 if(userList.isEmpty()) break;
			 obsList.add(userList.get(i).getUsername());
		 }
		 
		 //Set ListView's items and autoselect #1
		 UsersView.setItems(obsList);		 
		 UsersView.getSelectionModel().select(0);
		 UsersView.getSelectionModel().selectedItemProperty();
		 
		 
		 //START OF FUNCTIONALITY BELOW
		 
		 //Logout Button
		 Logout.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("LOGOUT");
					alert.setContentText("Do you wish to confirm?");
					
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get() == ButtonType.OK){
						((Node)e.getSource()).getScene().getWindow().hide();
						try {
							Stage stage = new Stage();
							stage.setResizable(false);
							FXMLLoader loader = new FXMLLoader();
							loader.setLocation(getClass().getResource("loginwindow.fxml"));
							VBox root = (VBox)loader.load();							
							LoginController loginController = loader.getController();
							loginController.start(stage);						
							Scene scene = new Scene(root);
							stage.setScene(scene);
							stage.show();
						}
						catch(Exception q){
							q.printStackTrace();
						}
					}
					else{
						return;
					}
				}
			});	
		 
		 //Add Button
		 AddUser.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if(NewUserField.getText().trim().isEmpty()){ //No input
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setContentText("Must input a new username, try again...");
						alert.show();
					}else if(searchUser(NewUserField.getText()) != -1
							|| NewUserField.getText().equalsIgnoreCase("admin")) { //User exists
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("ERROR");
						alert.setContentText("Invalid Input: User already exists.");
						alert.show();
					}else { //Valid input
						userList.add(new User(NewUserField.getText()));
						start();
						NewUserField.clear();
					}
				}
			});
		 
		 //Delete Button
		 DeleteUser.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if(obsList.isEmpty()){
						return;
					}else{
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("DELETE");
						alert.setContentText("You want to delete "+UsersView.getSelectionModel().getSelectedItem()+"?");
						Optional<ButtonType> result = alert.showAndWait();
						if(result.get() == ButtonType.OK){
							int index = searchUser(UsersView.getSelectionModel().getSelectedItem());
							userList.remove(index);
							start();
						}
					}
				}
			});
		 
}
	
	/**
	 * Getter method that returns a list of Users
	 * @return userList
	 */
	public static ArrayList<User> getUsers(){
		return userList;
	}
	
	/**
	 * Method that searches for an existing User and returns their index
	 * @param username User's name
	 * @return index, negative if not found
	 */
	public int searchUser(String username){
		if(userList == null) return -1;
		for(int i = 0; i < userList.size(); i++){
			if(userList.get(i).getUsername().equals(username)){
				return i;
			}
		}
		return -1;
	}
}
