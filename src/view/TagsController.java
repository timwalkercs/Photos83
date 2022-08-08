//Timothy Walker tpw32
//Hasin Choudhury hmc94
package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import app.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller class for the Tags window
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class TagsController {
	
	/**
	 * ListView to display tags
	 */
	@FXML ListView<String> TagListView;
	
	/**
	 * ComboBox to hold Tag Names
	 */
	@FXML ComboBox<String> ComboTagName;
	
	/**
	 * TextArea that holds TagValues
	 */
	@FXML TextArea TagValues;
	
	/**
	 * Button to add a tag
	 */
	@FXML Button AddTagButton;
	
	/**
	 * Button to finalize tagging
	 */
	@FXML Button DoneButton;
	
	/**
	 * Button to edit tags
	 */
	@FXML Button EditTagButton;
	
	/**
	 * Button to delete selected tag
	 */
	@FXML Button DeleteTagButton;
	
	/**
	 * String of data storage directory
	 */
	public static final String storageDirectory = "data";
	
	/**
	 * String of file to save tag info
	 */
	public static final String storageFile = "tags.data";
	
	/**
	 * ArrayList that holds tags
	 */
	private ArrayList<String> tagList;
	
	/**
	 * HashMap of all tags
	 */
	protected HashMap<String, ArrayList<String>> tags;
	
	/**
	 * ArrayList of key's values
	 */
	private ArrayList<String> values;
	
	/**
	 * Observable List to fill ListView
	 */
	protected ObservableList<String> obsList = FXCollections.observableArrayList();
	
	/**
	 * Start method to initialize the window
	 * @param currentTags ArrayList of tags
	 * @throws Exception
	 */
	public void start(HashMap<String, ArrayList<String>> currentTags) throws Exception{
		tags = currentTags;
		
		//combobox options
		ComboTagName.setEditable(true);
		tagList = new ArrayList<String>();
		if(load() != null) {
			tagList = load();
		}else {
			tagList.add("people");
			tagList.add("location");
		}
		ObservableList<String> tagOptions = FXCollections.observableList(tagList);
		ComboTagName.setItems(tagOptions);
		
		//listview
		if(tags != null) {
			for (String i : tags.keySet()) {
				obsList.add(i + " --> " + String.join(", ", tags.get(i)));
			}
			TagListView.setItems(obsList);
			TagListView.getSelectionModel().selectedItemProperty()
			.addListener((obs, oldVal, newVal) -> {
				EditTagButton.setDisable(false);
				DeleteTagButton.setDisable(false);
			});
		}
		
		AddTagButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					String tag = ComboTagName.getValue();
					String value = TagValues.getText();
					if(checkFields(tag,value,true)) return;

					tag=tag.trim();
					value=value.trim();
					
					if(tagList.contains(tag)==false) {
						tagOptions.add(tag);
						save(tagList);
					}
					
					values = new ArrayList<String>(Arrays.asList(value.split(",")));
					for(int i=0; i < values.size(); i++) {
						values.set(i, values.get(i).trim());
					}
						
					
					obsList.add(tag + " --> " + String.join(", ", values));
					tags.put(tag,values);
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});
		
		
		DoneButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
				    Stage stage = (Stage) DoneButton.getScene().getWindow();
				    stage.close();
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});
		
		EditTagButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					if(TagListView.getItems().isEmpty()) {
						EditTagButton.setDisable(true);
						DeleteTagButton.setDisable(true);
						return;
					}
					
					String key = TagListView.getSelectionModel().getSelectedItem().split(" ")[0].trim();
					
					Stage stage = new Stage();
					stage.setResizable(false);
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("edittagsview.fxml"));
					VBox root = (VBox)loader.load();
					EditTagsController editTagsController = loader.getController();
					editTagsController.start(tags, obsList, key, tags.get(key));
					tags = editTagsController.getTags();
					obsList = editTagsController.getObsList();
					Scene scene = new Scene(root);
					stage.setScene(scene);

					stage.initModality(Modality.APPLICATION_MODAL);
					stage.initOwner(EditTagButton.getScene().getWindow());
					stage.showAndWait();
					
					EditTagButton.setDisable(true);
					DeleteTagButton.setDisable(true);
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});
		
		DeleteTagButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					
					if(TagListView.getItems().isEmpty()) {
						EditTagButton.setDisable(true);
						DeleteTagButton.setDisable(true);
						return;
					}
					
					String key = TagListView.getSelectionModel().getSelectedItem().split(" ")[0];
					obsList.remove(TagListView.getSelectionModel().getSelectedItem());
					tags.remove(key);
					if(obsList.isEmpty()) {
						EditTagButton.setDisable(true);
						DeleteTagButton.setDisable(true);
					}
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});

	}
	
	/**
	 * Method to show an Error Alert
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
	 * Method to check if fields are valid
	 * @param tag a tag
	 * @param value key's value
	 * @param checkTagExists Boolean value if tag exists
	 * @return false if valid, true if not
	 */
	public boolean checkFields(String tag, String value, boolean checkTagExists) {
		if(tags == null) {return false;}
		if (tag==null || tag.isEmpty()) { this.showAlert("Empty Tag Name", "Tag's name cannot be empty!"); return true;}
		else if(checkTagExists &&  tags.containsKey(tag)) { this.showAlert("Tag Exists", "Tag has already been added, please choose to edit that one!"); return true;}
		if(value.isEmpty()) { this.showAlert("Empty Tag Value", "Tag's value cannot be empty!"); return true;}
		if(tag.contains("=") || value.contains("=")) {this.showAlert("Incorrect inputs", "Tag and/or value cannot have an '=' sign!"); return true;}
		return false;
	}
	
	/**
	 * Method that saves the tags to tags.data
	 * @param list ArrayList of Strings
	 */
	public static void save(ArrayList<String> list) {
		ObjectOutputStream outStream;
		
		try {
			outStream = new ObjectOutputStream(new FileOutputStream(storageDirectory + File.separator + storageFile));
			outStream.writeObject(list);
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();			
		}
	}
	
	/**
	 * Method that loads saved tags from tags.data
	 * @return tags
	 */
	public static ArrayList<String> load() {
		ObjectInputStream inStream = null;
		ArrayList<String> list = null;
	
		try {
			inStream = new ObjectInputStream(new FileInputStream(storageDirectory + File.separator + storageFile));
			list = (ArrayList<String>)inStream.readObject();
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
		return list;
	}
	
	/**
	 * Method that returns HashMap of tags
	 * @return HashMap of tags
	 */
	public HashMap<String, ArrayList<String>> getTags() {
		return tags;
	}
	
}
