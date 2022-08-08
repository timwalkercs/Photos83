//Timothy Walker tpw32
//Hasin Choudhury hmc94
package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;
import javax.imageio.ImageIO;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Controller class for the Search Results window
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class SearchController {
	
	/**
	 * Button that puts results into an Album
	 */
	@FXML Button ResultsIntoAlbum;
	
	/**
	 * ListView of search results
	 */
	@FXML ListView<String> ResultsList;
	
	/**
	 * Text that displays number of results
	 */
	@FXML Text ResultsCount;
	
	/**
	 * ImageView that displays the selected image
	 */
	@FXML ImageView SelectedImage;
	
	/**
	 * Current User
	 */
	private User crntUser;
	
	/**
	 * ObservableList
	 */
	private ObservableList<String> obsList;
	
	/**
	 * ObservableList of album names
	 */
	private ObservableList<String> albums;
	
	/**
	 * ArrayList of search result Photos
	 */
	private ArrayList<Photo> ResultPhotos;
	
	/**
	 * Start method, initializes window
	 * @param albums ObservableList
	 * @throws Exception
	 */
	public void start(ObservableList<String> albums) throws Exception{
		this.albums = albums;
		
		ResultsList.setItems(obsList);
		ResultsList.getSelectionModel().selectedItemProperty()
		.addListener((obs, oldVal, newVal) -> displayImage());
		
		ResultsCount.setText(String.valueOf(obsList.size()));
		
		ResultsIntoAlbum.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					TextInputDialog popup = new TextInputDialog();
					popup.setTitle("Add New Album");
					popup.setContentText("Enter new Album's title.");
					Optional<String> result = popup.showAndWait();
					if (!result.isEmpty()) {
						if(result.get().trim().equals("")) {//Blank input
							showAlert("Error", "Input cannot be blank");
						}else if(albums.contains(result.get().trim())) {//Album exists
							showAlert("Error", "Album of that name already exists.");
						}else {
							Album toAdd = new Album(result.get());		
							toAdd.setPhotos(ResultPhotos);
							crntUser.albumList.add(toAdd);
							albums.add(toAdd.getAlbumName());
						}						
					} 
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Method to search for Photos by LocalDate
	 * @param From from Date
	 * @param To to Date
	 * @param choice boolean of whether Date range is used
	 * @throws Exception
	 */
	public void searchDates(LocalDate From, LocalDate To, boolean choice) throws Exception{
		crntUser = LoginController.crntUser;
		obsList = FXCollections.observableArrayList();
		ResultPhotos = new ArrayList<Photo>();
		//obsList.add((crntUser.getAlbums()).get(0).getAlbumName());
		
		for(int i = 0; i < (crntUser.getAlbums()).size(); i++){
			 if((crntUser.getAlbums()).isEmpty()) break;
			 for(int j = 0; j < crntUser.getAlbums().get(i).getPhotos().size(); j++){
				 if(crntUser.getAlbums().get(i).getPhotos().isEmpty()) break;
				 Photo photo = crntUser.getAlbums().get(i).getPhotos().get(j);
				 LocalDate date = LocalDateTime.ofInstant(photo.getCreationDate().toInstant(), ZoneId.systemDefault()).toLocalDate();

				 if(choice) {
					 if( (!date.isBefore(From)) && (date.isBefore(To.plusDays(1))) ){
						 addToResult(photo);
					 }
				 }
				 else {
					 if(date.isEqual(From)){
						 addToResult(photo);
					 }
				 }
			 }
			 
			 
		 }
	}
	
	/**
	 * Method to search for Photos by tag
	 * @param firstInput String
	 * @param secondInput String
	 * @param junction String, contains AND or OR
	 * @throws Exception
	 */
	public void searchTags(String firstInput, String secondInput, String junction) throws Exception{
		crntUser = LoginController.crntUser;
		obsList = FXCollections.observableArrayList();
		ResultPhotos = new ArrayList<Photo>();
		//obsList.add((crntUser.getAlbums()).get(0).getAlbumName());
		
		for(int i = 0; i < (crntUser.getAlbums()).size(); i++){
			 if((crntUser.getAlbums()).isEmpty()) break;
			 for(int j = 0; j < crntUser.getAlbums().get(i).getPhotos().size(); j++){
				 if(crntUser.getAlbums().get(i).getPhotos().isEmpty()) break;
				 Photo photo = crntUser.getAlbums().get(i).getPhotos().get(j);
				
				 String key1 = null;
				 String value1 = null;
				 String key2 = null;
				 String value2 = null;
				 
				 if(firstInput!=null && !firstInput.isEmpty()){
					 key1 = firstInput.split("=")[0].trim();
					 value1 = firstInput.split("=")[1].trim();
				 }

				 if(secondInput!=null && !secondInput.isEmpty()){
					 key2 = secondInput.split("=")[0].trim();
					 value2 = secondInput.split("=")[1].trim();
				 }

				 HashMap<String, ArrayList<String>> tags = photo.getTags();
				 
				 if(tags.containsKey(key1) && key2==null) {
					 if(tags.get(key1).contains(value1)) {
						 addToResult(photo);
					 }
				 }
				 else if(tags.containsKey(key2) && key1==null) {
					 if(tags.get(key2).contains(value2)) {
						 addToResult(photo);
					 }
				 }
					 
				 else if(junction.equals("AND") && tags.containsKey(key1) && tags.containsKey(key2)) {
					 if(tags.get(key1).contains(value1) && tags.get(key2).contains(value2)) {
						 addToResult(photo);
					 }
				 }
				 
				 else if(junction.equals("OR")) {
					 
					 if(tags.containsKey(key1)) {
						 if(tags.get(key1).contains(value1)){
							 addToResult(photo);
						 }
					 }
					 
					 if(tags.containsKey(key2)) {
						 if(tags.get(key2).contains(value2)){
							 if(!obsList.contains(photo.getCaption())){
								 addToResult(photo);
							 }
						 }
					 }
					 
				 }
			 
			 }
			 
			 
		 }
	}
	

	/**
	 * Method that shows an Error Alert
	 * @param header Alert header
	 * @param content Alert contents
	 */
	public void showAlert(String header, String content) {
    	Alert alert = new Alert(AlertType.ERROR);
    	alert.setTitle("ERROR");
    	alert.setHeaderText(header);
    	alert.setContentText(content);
    	alert.showAndWait();
	}
	
	/**
	 * Getter method that returns albums
	 * @return list of Albums
	 */
	public ObservableList<String> getAlbums(){
		return albums;
	}
	
	/**
	 * Method that adds search results to observable list and view
	 * @param photo Photo instance
	 */
	public void addToResult(Photo photo) {
		if(!ResultPhotos.contains(photo)) {
			String caption = photo.getCaption();
			int index;
			obsList.add(caption);
			index = obsList.indexOf(caption);
			obsList.set(index, index+1 + ". " + caption);
			ResultPhotos.add(photo);
		}
		
	}
	
	/**
	 * Method that displays the selected image in the ImageView
	 */
	private void displayImage() {
		int index = ResultsList.getSelectionModel().getSelectedIndex();
		SelectedImage.setImage(ResultPhotos.get(index).getPhoto());
		
	}
}
