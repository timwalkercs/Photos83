//Timothy Walker tpw32
//Hasin Choudhury hmc94
package view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import app.Album;
import app.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller class for the Photo Details window
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class PhotoViewController {
	
	/**
	 * Back Button to return to previous window
	 */
	@FXML Button Back;
	
	/**
	 * MenuBar with File and Edit options
	 */
	@FXML MenuBar Menu;
	
	/**
	 * MenuItem to delete a photo
	 */
	@FXML MenuItem Delete;
	
	/**
	 * MenuItem to edit a caption
	 */
	@FXML MenuItem EditCaption;
	
	/**
	 * ImageView to display the current photo
	 */
	@FXML ImageView PhotoView;
	
	/**
	 * Text to display the Photo's details
	 */
	@FXML Text PhotoDetails;
	
	/**
	 * Label that displays the Photo's caption
	 */
	@FXML Label CaptionDisplay;
	
	/**
	 * Text that displays the Photo's date
	 */
	@FXML Text DateDisplay;
	
	/**
	 * ListView that displays the Photo's tags
	 */
	@FXML ListView<String> TagListView;
	
	/**
	 * Button that allows for tag altering
	 */
	@FXML Button TagButton;

	/**
	 * ObservableList to fill the ListView
	 */
	private ObservableList<String> obsList = FXCollections.observableArrayList();
	
	
	/**
	 * Start method that initializes the window with a Photo, also stores current album
	 * @param crntAlbum current Album
	 * @param photo Photo
	 * @throws Exception
	 */
	public void start(Album crntAlbum, Photo photo) throws Exception {
		
		//Display Photo, date, caption//
		PhotoView.setImage(photo.getPhoto());
		CaptionDisplay.setText(photo.getCaption());
		SimpleDateFormat simp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String date = simp.format(photo.getCreationDate().getTime());
		DateDisplay.setText(date);
		
		if(photo.getTags() != null) {
			for (String i : photo.getTags().keySet()) {
				obsList.add(i + " --> " + String.join(", ", photo.getTags().get(i)));
			}
		}
		TagListView.setItems(obsList);
		
		TagButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					Stage stage = new Stage();
					stage.setResizable(false);
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("addingtagsview.fxml"));
					VBox root = (VBox)loader.load();
					TagsController tagsController = loader.getController();
					tagsController.start(photo.getTags());
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.initOwner(TagButton.getScene().getWindow());
					stage.showAndWait();
					
					photo.setTags(tagsController.getTags());
					
					obsList.clear();
					for (String i : photo.getTags().keySet()) {
						obsList.add(i + " --> " + String.join(", ", photo.getTags().get(i)));
					}
					TagListView.setItems(obsList);
					
				}
				catch (Exception q){
					q.printStackTrace();
				}
			}
		});
		
		EditCaption.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				TextInputDialog popup = new TextInputDialog();
				popup.setTitle("Edit Caption");
				popup.setContentText("Enter photo's new caption.");
				Optional<String> result = popup.showAndWait();
				if(result.isEmpty()) {
					return;
				}else {
					photo.setCaption(result.get());
					try {
						TagListView.getItems().clear();
						start(crntAlbum, photo);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}			
			}
		});
		
		
		Back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					((Node)e.getSource()).getScene().getWindow().hide();
					Stage stage = new Stage();
					stage.setResizable(false);
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("albumview.fxml"));
					VBox root = (VBox)loader.load();							
					AlbumViewController albumViewController = loader.getController();
					albumViewController.start(crntAlbum);						
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});
		
		Delete.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("DELETE");
				alert.setContentText("Are you sure you want to delete "
						+ "this photo from " + crntAlbum.getAlbumName() + "?");
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK){				
					crntAlbum.photos.remove(photo);
					Back.fire();
				}
			}
		});
	}
}
