//Timothy Walker tpw32
//Hasin Choudhury hmc94
package view;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class AlbumListController {
	
	/**
	 * Button to open the selected Album
	 */
	@FXML Button Open;
	
	/**
	 * Button to search for Photo's by date or tags
	 */
	@FXML Button Search;
	
	/**
	 * ComboBox that contains optional search parameters
	 */
	@FXML ComboBox<String> SearchByDropdown;
	
	/**
	 * TextField for first search input
	 */
	@FXML TextField SearchInput1;
	
	/**
	 * ComboBox for optional juction choice
	 */
	@FXML ComboBox<String> JunctionChoice;
	
	/**
	 * TextField for second search input
	 */
	@FXML TextField SearchInput2;
	
	/**
	 * DatePicker to select a date to search by
	 */
	@FXML DatePicker FromPicker;
	
	/**
	 * DatePicker to select a second date to search by, creating a range
	 */
	@FXML DatePicker ToPicker;
	
	/**
	 * CheckBox, decides whether you search by single date or a range
	 */
	@FXML CheckBox DateChoice;
	
	/**
	 * ListView that shows User's Albums
	 */
	@FXML ListView<String> AlbumView;
	
	/**
	 * Logout Button
	 */
	@FXML Button Logout;
	
	/**
	 * Text that displays User's name
	 */
	@FXML Text UsernameDisplay;
	
	/**
	 * Text that displays number of Photos in selected Album
	 */
	@FXML Text PhotoCount;
	
	/**
	 * Text that displays ranges of dates
	 */
	@FXML Text DateRange;
	
	/**
	 * ImageView that displays Album's thumbnail
	 */
	@FXML ImageView Thumbnail;
	
	/**
	 * MenuBar with File and Edit options
	 */
	@FXML MenuBar Menu;
	
	/**
	 * MenuItem to create a new Album 
	 */
	@FXML MenuItem NewAlbum;
	
	/**
	 * MenuItem to delete a selected Album
	 */
	@FXML MenuItem DeleteAlbum;
	
	/**
	 * MenuItem to edit a selected Album
	 */
	@FXML MenuItem EditAlbum;
	
	
	/**
	 * ObservableList to display Albums in ListView
	 */
	private ObservableList<String> obsList;
		
	/**
	 * Start method to initialize the window
	 */
	public void start() {
		
		//Establish current user
		User crntUser = LoginController.crntUser;
		UsernameDisplay.setText(crntUser.getUsername());
		
		obsList = FXCollections.observableArrayList();
		//Fill in obsList with User's Albums, create ListView
		for(int i = 0; i < (crntUser.getAlbums()).size(); i++){
			 if((crntUser.getAlbums()).isEmpty()) break;
			 obsList.add((crntUser.getAlbums()).get(i).getAlbumName());
		 }
		AlbumView.setItems(obsList);
		
		AlbumView.getSelectionModel().selectedItemProperty()
		.addListener((obs, oldVal, newVal) -> displayAlbumDetails(crntUser));
		
		ObservableList<String> searchOptions = FXCollections.observableArrayList();
		searchOptions.add("Search by date");
		searchOptions.add("Search by tag");
		SearchByDropdown.setItems(searchOptions);
		
		ObservableList<String> junctionOptions = FXCollections.observableArrayList();
		junctionOptions.add("AND");
		junctionOptions.add("OR");
		JunctionChoice.setItems(junctionOptions);
		JunctionChoice.getSelectionModel().select(0);
		
		addSearchListeners();
		
		//Create Album
		NewAlbum.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				TextInputDialog popup = new TextInputDialog();
				popup.setTitle("Add New Album");
				popup.setContentText("Enter new Album's title.");
				Optional<String> result = popup.showAndWait();
				if (!result.isEmpty()) {
					if(result.get().trim().equals("")) {//Blank input
						showAlert("Error", "Input cannot be blank");
					}else if(obsList.contains(result.get().trim())) {//Album exists
						showAlert("Error", "Album of that name already exists.");
					}else {
						Album toAdd = new Album(result.get());						
						crntUser.albumList.add(toAdd);
						start();
					}						
				} 
			}
		});
		 
		//Delete Album
		DeleteAlbum.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String toDel = AlbumView.getSelectionModel().getSelectedItem();
				if (toDel != null) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setHeaderText("DELETE");
					alert.setContentText("Do you wish to delete " + toDel + "?");
					Optional<ButtonType> result = alert.showAndWait();
					if(result.get() == ButtonType.OK){
						int index = crntUser.searchAlbum(AlbumView.getSelectionModel().getSelectedItem());
						crntUser.albumList.remove(index);
						obsList.remove(0);
						start();
					}
				}
				AlbumView.getSelectionModel().select(null);
				Open.setDisable(true);
				Thumbnail.setImage(null);
				PhotoCount.setText("");
				DateRange.setText("");
				if(obsList.isEmpty()) Open.setDisable(true);
			}
		});
		 
		//Edit Album
		EditAlbum.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				TextInputDialog popup = new TextInputDialog();
				popup.setTitle("Edit Album Title");
				popup.setContentText("Enter Album's new title.");
				Optional<String> result = popup.showAndWait();
				if (!result.isEmpty()) {
					if(result.get().trim().equals("")) {//Blank input
						showAlert("Error", "Input cannot be blank.");
					}else if(obsList.contains(result.get().trim())) {//Album exists
						showAlert("Error", "Album of that name already exists.");
					}else {
						int index = crntUser.searchAlbum(AlbumView.getSelectionModel().getSelectedItem());
						crntUser.albumList.get(index).setAlbumName(result.get());					
						start();
					}						
				}
				AlbumView.getSelectionModel().select(null);
				Open.setDisable(true);
				Thumbnail.setImage(null);
				PhotoCount.setText("");
				DateRange.setText("");
			}
		});
		 
		 
		
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
		 
		//Open Button
		 Open.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					int index = crntUser.searchAlbum(AlbumView.getSelectionModel().getSelectedItem());
					Album crntAlbum = crntUser.getAlbums().get(index);
					((Node)e.getSource()).getScene().getWindow().hide();
					try {
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
		 
		 //Search Button
		 Search.setOnAction(new EventHandler<ActionEvent>() {
			 public void handle(ActionEvent e) {
				 //System.out.println("careful... youre pushing my buttons");
				 String searchType = SearchByDropdown.getValue();
				 
				 try {
					 Stage stage = new Stage();
					 stage.setResizable(false);
					 FXMLLoader loader = new FXMLLoader();
					 loader.setLocation(getClass().getResource("searchview.fxml"));
					 VBox root = (VBox)loader.load();
					 SearchController searchController = loader.getController();
					 
					 
					 if(searchType=="Search by date") { //can make dynamic
						 if(checkDateInputs(FromPicker.getValue(), ToPicker.getValue(), DateChoice.isSelected())) return;
						 searchController.searchDates(FromPicker.getValue(), ToPicker.getValue(), DateChoice.isSelected());
					 }
					 else {
						 SearchInput1.setText(SearchInput1.getText().trim());
						 SearchInput2.setText(SearchInput2.getText().trim());
						 if(checkTagInputs(SearchInput1.getText(), SearchInput2.getText(), JunctionChoice.getValue())) return;
						 searchController.searchTags(SearchInput1.getText(), SearchInput2.getText(), JunctionChoice.getValue());
					 }
					 
					 searchController.start(obsList);
					 Scene scene = new Scene(root);
					 stage.setScene(scene);

					 stage.initModality(Modality.APPLICATION_MODAL);
					 stage.initOwner(Search.getScene().getWindow());
					 stage.showAndWait();
					 
					 obsList = searchController.getAlbums();
					 
				 }
				 catch(Exception q) {
					 q.printStackTrace();
				 }
				 
			 }
		 });
		 
		 
	}

	/**
	 * Method to display the Album's details on the screen
	 * @param crntUser the current User
	 */
	private void displayAlbumDetails(User crntUser) {
		Open.setDisable(false);
		int index = crntUser.searchAlbum(AlbumView.getSelectionModel().getSelectedItem());
		if(index != -1) {
			String toText = String.valueOf(crntUser.albumList.get(index).getPhotoCount());
			PhotoCount.setText(toText);
			if(!PhotoCount.getText().equals("0")) {
				Thumbnail.setImage(crntUser.albumList.get(index).photos.get(0).getPhoto());
			}else {
				Thumbnail.setImage(null);
			}
			
			//SET DATE RANGE DISPLAY
			DateRange.setText(crntUser.albumList.get(index).getDateRange());
			
		}
		
	}
	
	/**
	 * Method to add listeners for Search
	 */
	private void addSearchListeners() {
		SearchByDropdown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			 if(newVal=="Search by date") {
				FromPicker.setVisible(true);
				DateChoice.setVisible(true);
				
				SearchInput1.setVisible(false);
				JunctionChoice.setVisible(false);
				SearchInput2.setVisible(false);
				
				Search.setVisible(true);
			 }
			 else {
				SearchInput1.setVisible(true);
				JunctionChoice.setVisible(true);
				SearchInput2.setVisible(true);
				
				Search.setVisible(false);
				FromPicker.setVisible(false);
				ToPicker.setVisible(false);
				DateChoice.setVisible(false);
			 }

		});

		SearchInput1.textProperty().addListener((obs, oldVal, newVal) -> {
			if((newVal.isEmpty() || newVal==null) && (SearchInput2.getText().isEmpty() || SearchInput2.getText()==null) ) Search.setVisible(false);
			else { Search.setVisible(true);}
		});
		
		SearchInput2.textProperty().addListener((obs, oldVal, newVal) -> {
			if((newVal.isEmpty() || newVal==null) && (SearchInput1.getText().isEmpty() || SearchInput1.getText()==null)) Search.setVisible(false);
			else { Search.setVisible(true);}
		});
		
		DateChoice.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if(newVal) {
				ToPicker.setVisible(true);
			}
			else {
				ToPicker.setVisible(false);
			}
		});
		
		
		
	}
	
	
	/**
	 * Method to check whether date Inputs are valid
	 * @param from a LocalDate
	 * @param to another LocalDate
	 * @param willRange are you using a date range?
	 * @return true if valid, false if not
	 */
	public boolean checkDateInputs(LocalDate from, LocalDate to, boolean willRange) {
		if(from==null) {
			showAlert("Incorrect Date Input", "Please input from date correctly!");
			return true;
		}
		if(willRange) {
			if(to==null) {
				showAlert("Incorrect Date Input", "Please input to date correctly!");
				return true;
			}
			if(to.isBefore(from)) {
				showAlert("Incorrect Date Ranges", "Please make sure to date is LATER than (or the same as) from date!");
				return true;
			}
			if(from.isAfter(to)) {
				showAlert("Incorrect Date Ranges", "Please make sure from date is EARLIER than (or the same as) to date!");
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Method to check whether tag inputs are valid
	 * @param first a tag
	 * @param second optional second tag
	 * @param junction String, either AND or OR
	 * @return true if valid, false if not
	 */
	public boolean checkTagInputs(String first, String second, String junction) {
		
		if(first.split("=").length!=2 && first!=null && !first.isEmpty()) {
			showAlert("Incorrect Tag Input", "Please enter only 1 '=' sign following a value for inputs(s) ex: person=sesh");
			return true;
		}
		
		if(second.split("=").length!=2 && second!=null && !second.isEmpty()) {
			showAlert("Incorrect Tag Input", "Please enter only 1 '=' sign following a value for inputs(s) ex: person=sesh");
			return true;
		}
		
		
		return false;
	}
	
	/**
	 * Method to show an alert on the screen
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