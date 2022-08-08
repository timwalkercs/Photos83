//Timothy Walker tpw32
//Hasin Choudhury hmc94
package view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for the Edit Tags window
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class EditTagsController extends TagsController{
	
	/**
	 * TextField to edit a tags name
	 */
	@FXML TextField EditTagName;
	
	/**
	 * TextField of tags original name
	 */
	@FXML TextField OriginalName;
	
	/**
	 * TextArea to edit tag values
	 */
	@FXML TextArea EditTagValue;
	
	/**
	 * Button to finalize tag edits
	 */
	@FXML Button EditTagDoneButton;
	
	/**
	 * Button to cancel tag edits
	 */
	@FXML Button EditTagCancelButton;
	
	/**
	 * String for new tag name
	 */
	private String newName;
	
	/**
	 * ArrayList of new tag values
	 */
	private ArrayList<String> newValues;
	
	/**
	 * String of old tag name
	 */
	private String oldName;
	
	/**
	 * ArrayList of old tag values
	 */
	private ArrayList<String> oldValues;
	
	/**
	 * Start method to initialize the window
	 * @param tags ArrayList of tags
	 * @param obsList ObservableList of Strings
	 * @param oldName String of old name
	 * @param oldValues ArrayList of old tags
	 */
	public void start(HashMap<String, ArrayList<String>> tags, ObservableList<String> obsList, String oldName, ArrayList<String> oldValues) {
		this.oldName = oldName;
		this.oldValues = oldValues;
		this.tags = tags;
		this.obsList = obsList;
		
		//initial values
		EditTagName.setText(this.oldName);
		EditTagValue.setText(String.join(", ", this.oldValues));
		OriginalName.setText(this.oldName);
		
		EditTagDoneButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					EditTagName.setText(EditTagName.getText().trim());
					EditTagValue.setText(EditTagValue.getText().trim());
					
				    if(checkFields(EditTagName.getText(), EditTagValue.getText(), false)) return;
					
				    
				    
				    int index = obsList.indexOf(oldName + " --> " + String.join(", ", oldValues));
				    obsList.set(index, newName + " --> " + EditTagValue.getText());
				    tags.remove(oldName);
				    tags.put(newName, newValues);
				    
				    Stage stage = (Stage) EditTagDoneButton.getScene().getWindow();
				    stage.close();
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});
		
		
		EditTagCancelButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
				    Stage stage = (Stage) EditTagCancelButton.getScene().getWindow();
				    stage.close();
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});
	}

	/**
	 * Getter method that returns a Hashmap of tags
	 */
	@Override
	public HashMap<String, ArrayList<String>> getTags(){
		return this.tags;
	}
	
	/**
	 * Getter method that returns the ObservableList
	 * @return obsList
	 */
	public ObservableList<String> getObsList(){
		return obsList;
	}
	
	/**
	 * Method that checks the fields and returns whether edits are valid
	 */
	@Override
	public boolean checkFields(String tag, String values, boolean checkIfTagExists) {
		newValues = new ArrayList<String>(Arrays.asList(values.split(",")));
		newName = tag;
		
		for(int i=0; i < newValues.size(); i++) {
			newValues.set(i, newValues.get(i).trim());
		}
		
		if(newName.equals(this.oldName) && equalLists(newValues, oldValues)) { showAlert("Tag Hasn't Changed", "Tag's name and value is the same as the old! Please change something or hit cancel!"); return true;}
		
		return super.checkFields(newName, values, checkIfTagExists);
	}
	
	/**
	 * Method that returns Boolean of whether to ArrayLists are equal
	 * @param one ArrayList of Strings
	 * @param two ArrayList of Strings
	 * @return true if equal, false if not
	 */
	public boolean equalLists(ArrayList<String> one, ArrayList<String> two){     
	    if (one == null && two == null){
	        return true;
	    }

	    if((one == null && two != null) 
	      || one != null && two == null
	      || one.size() != two.size()){
	        return false;
	    }

	    one = new ArrayList<String>(one); 
	    two = new ArrayList<String>(two);   

	    Collections.sort(one);
	    Collections.sort(two);      
	    return one.equals(two);
	}
}
