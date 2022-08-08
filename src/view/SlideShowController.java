//Timothy Walker tpw32
//Hasin Choudhury hmc94
package view;

import app.Album;
import app.Photo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller class for the Slideshow window
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class SlideShowController {
	
	/**
	 * Back Button, returns to previous window
	 */
	@FXML Button Back;
	
	/**
	 * Button to move to previous Photo
	 */
	@FXML Button Prev;
	
	/**
	 * Button to move to next Photo
	 */
	@FXML Button Next;
	
	/**
	 * ImageView to display current Photo
	 */
	@FXML ImageView PhotoView;
	
	/**
	 * Text to display Album's name
	 */
	@FXML Text AlbumNameDisplay;
	
	/**
	 * Text to display number of Photo's in current Album
	 */
	@FXML Text PhotoCount;
	
	/**
	 * Int to track current index being displayed
	 */
	int index = 0;
	
	/**
	 * Start Method to initialize the window
	 * @param album Album instance
	 */
	public void start(Album album){
		AlbumNameDisplay.setText("\"" + album.getAlbumName() + "\"");
		PhotoView.setImage(album.getPhotos().get(index).getPhoto());
		PhotoCount.setText(index+1 + " of " + album.getPhotos().size());		
		
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
					albumViewController.start(album);						
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});
		
		Next.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				index ++;
				if(index == album.getPhotos().size()) { //loop around photolist
					index = 0;				
				}			
				PhotoView.setImage(album.getPhotos().get(index).getPhoto());
				PhotoCount.setText(index+1 + " of " + album.getPhotos().size());
			}
		});
		
		Prev.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				index --;
				if(index < 0) { //loop around photolist
					index = album.getPhotos().size()-1;				
				}			
				PhotoView.setImage(album.getPhotos().get(index).getPhoto());
				PhotoCount.setText(index+1 + " of " + album.getPhotos().size());
			}
		});
	}
}
