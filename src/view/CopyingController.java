//Timothy Walker tpw32
//Hasin Choudhury hmc94
package view;

import java.util.ArrayList;

import app.Album;
import app.Photo;
import app.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller class for the Copy and Move Photo window
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class CopyingController {

	/**
	 * ChoiceBox to select destination album
	 */
	@FXML ChoiceBox<String> AlbumChoiceBox;

	/**
	 * Label to display the current album the User is in
	 */
	@FXML Text CurrentAlbumLabel;

	/**
	 * ImageView to display the Photo you wish to move
	 */
	@FXML ImageView ImagePreview;

	/**
	 * Back Button, returns to previous window
	 */
	@FXML Button Back;

	/**
	 * Copy Button, copies the Photo to destination Album
	 */
	@FXML Button Copy;

	/**
	 * Move Button, moves the Photo to destination Album, deleting it from current Album
	 */
	@FXML Button Move;


	/**
	 * ObservableList of albums, besides the current one
	 */
	private ObservableList<String> obsAlbumsList;

	/**
	 * ArrayList of all of the User's albums
	 */
	private ArrayList<Album> albums;


	/**
	 * Start method, initializes the window
	 * @param currentAlbum current Album
	 * @param currentPhoto current Photo
	 * @param index index of currentPhoto in currentAlbum
	 */
	public void start(Album currentAlbum, Photo currentPhoto, int index) {
		//Establish current user
		User crntUser = LoginController.crntUser;

		//Title
		CurrentAlbumLabel.setText("Current Album: " + currentAlbum.getAlbumName());

		//List of Albums
		obsAlbumsList = FXCollections.observableArrayList();
		AlbumChoiceBox.setItems(obsAlbumsList);
		albums = crntUser.getAlbums();
		//Fill in obsList with User's Albums, create ListView
		for(int i = 0; i < albums.size(); i++){
			 String name = crntUser.getAlbums().get(i).getAlbumName();
			 if((crntUser.getAlbums()).isEmpty()) break;
			 if(!name.equals(currentAlbum.getAlbumName())) obsAlbumsList.add(name);
		}

		AlbumChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if(newVal!=null && !newVal.isEmpty()) {
				Move.setDisable(false);
				Copy.setDisable(false);
			}
			else {
				Move.setDisable(true);
				Copy.setDisable(true);
			}
		});

		//show photo
		ImagePreview.setImage(currentPhoto.getPhoto());

		Move.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					if(copy(e, crntUser, currentAlbum, currentPhoto)) {
						currentAlbum.getPhotos().remove(currentPhoto);
						back(e, currentAlbum);
					}


				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});

		Copy.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					if(copy(e, crntUser, currentAlbum, currentPhoto)) {
						back(e, currentAlbum);
					}
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});

		//back
		Back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				back(e, currentAlbum);
			}
		});


	}

	/**
     * Method that copies a photo to another album
     * @param e ActionEvent
     * @param crntUser current User
     * @param currentAlbum current Album
     * @param currentPhoto selected Photo
     * @return was copy successful?
     */
	public boolean copy(ActionEvent e, User crntUser, Album currentAlbum, Photo currentPhoto) {
		Album album = albums.get(crntUser.searchAlbum(AlbumChoiceBox.getValue()));
		if( album.getPhotos().contains(currentPhoto) ) {
			showAlert("Existing Photo","Photo already exists in the destination album!");
			return false;
		}
		album.addPhoto(currentPhoto);
		return true;
	}

    /**
     * Method that returns to the previous window
     * @param e ActionEvent
     * @param curr current Album
     */
    public void back(ActionEvent e, Album curr) {
		try {
			((Node)e.getSource()).getScene().getWindow().hide();
			Stage stage = new Stage();
			stage.setResizable(false);
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("albumview.fxml"));
			VBox root = (VBox)loader.load();
			AlbumViewController albumViewController = loader.getController();
			albumViewController.start(curr);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		catch(Exception q){
			q.printStackTrace();
		}
	}

	/**
	 * Method to show a popup ERROR Alert
	 * @param header String of Alert header
	 * @param content String of Alert content
	 */
	public void showAlert(String header, String content) {
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("ERROR");
    	alert.setHeaderText(header);
    	alert.setContentText(content);
    	alert.showAndWait();
	}


}
