//Timothy Walker tpw32
//Hasin Choudhury hmc94
package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import javax.imageio.ImageIO;
import app.Album;
import app.Photo;
import app.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller class for the Album View window
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class AlbumViewController {
	
	/**
	 * Back Button, returns to previous window
	 */
	@FXML Button Back;
	
	/**
	 * Button that initiates a manual slideshow
	 */
	@FXML Button Slideshow;
	
	/**
	 * TilePane to store ImageViews
	 */
	@FXML TilePane PhotoTiles;
	
	/**
	 * Text to display the Album's name
	 */
	@FXML Text AlbumNameDisplay;
	
	/**
	 * Button to add a Photo from FileChooser
	 */
	@FXML Button AddPhoto;
	
	/**
	 * ScrollPane that contains the TilePane
	 */
	@FXML ScrollPane Scroll;
	
	/**
	 * VBox which holds all of the window's contents
	 */
	@FXML VBox VBOX;
		
/**
 * Start method, initializes the window	
 * @param album Album instance
 * @throws Exception
 */
public void start(Album album) throws Exception {	
		AlbumNameDisplay.setText(album.getAlbumName());
		PhotoTiles.setHgap(4);
		PhotoTiles.setVgap(4);
		PhotoTiles.setStyle("-fx-background-color: DBDBDB;");
		
		if(album.getPhotos().size()==0) {
			Slideshow.setDisable(true);
		}
		
		//create list of album's photos and list of imageviews, which will go in TilePane
		ArrayList<Photo> photoList = album.getPhotos();	
		ArrayList<ImageView> imageTiles = new ArrayList<ImageView>();	
		for(int i = 0; i < photoList.size(); i++){
			ImageView newView = new ImageView();
			newView.setFitWidth(150);
			newView.setPreserveRatio(true);
			newView.setImage(photoList.get(i).getPhoto());
			int dex = i;		
			//When Image in TilePane Clicked
			newView.setOnMouseClicked((MouseEvent e) -> {
		        Photo crntPhoto = album.photos.get(dex);
		        ((Node)e.getSource()).getScene().getWindow().hide();
				Stage stage = new Stage();
				stage.setResizable(false);
				FXMLLoader loader = new FXMLLoader();
				try{
					loader.setLocation(getClass().getResource("photoview.fxml"));
					VBox root = (VBox)loader.load();							
					PhotoViewController photoViewController = loader.getController();
					photoViewController.start(album, crntPhoto);						
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show(); 		
				 } catch (IllegalArgumentException e1){
					e1.printStackTrace();
				 } catch (IOException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}		
		    });
			imageTiles.add(newView);
		 }
		
		try{
			PhotoTiles.getChildren().clear();
			 for (int i = 0; i < imageTiles.size(); i++){			 
				 Label captionDisplay = new Label();
				 captionDisplay.setStyle("-fx-font: 15 arial;");
				 captionDisplay.setPrefWidth(150);
				 captionDisplay.setAlignment(Pos.CENTER);
				 captionDisplay.setTextAlignment(TextAlignment.CENTER);
				 captionDisplay.setWrapText(true);
				 captionDisplay.setText(photoList.get(i).getCaption());
				 
				 ToolBar Options = new ToolBar();
				 Options.setStyle("-fx-background-color: DBDBDB;");
				 Button DelPhoto = new Button("X");
				 Button MoveCopy = new Button("Move / Copy");
				 Options.getItems().add(DelPhoto);
				 Options.getItems().add(MoveCopy);
				 int index = i;
				 DelPhoto.setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent e) {
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setHeaderText("DELETE");
							alert.setContentText("Are you sure you want to delete "
									+ "this photo from " + album.getAlbumName() + "?");
							Optional<ButtonType> result = alert.showAndWait();
							if(result.get() == ButtonType.OK){				
								album.getPhotos().remove(index);
								try {
									start(album);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							}
						}
					});
				 
				 MoveCopy.setOnAction(new EventHandler<ActionEvent>() {
						public void handle(ActionEvent e) {
							Stage prevStage = (Stage) AddPhoto.getScene().getWindow();
							prevStage.close();
							try {
								Stage stage = new Stage();
								stage.setResizable(false);
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(getClass().getResource("copyingview.fxml"));
								VBox root = (VBox)loader.load();							
								CopyingController copyingController = loader.getController();
								copyingController.start(album, album.getPhotos().get(index), index);						
								Scene scene = new Scene(root);
								stage.setScene(scene);
								stage.show();
							}
							catch(Exception q){
								q.printStackTrace();
							}
						}
					});
				 
				 
				 
				 //DelPhoto.setShape(new Circle(1.5));
				 PhotoTiles.getChildren().addAll(Options,imageTiles.get(i),captionDisplay);
			 }
		 } catch (IllegalArgumentException e){
			 
		 }
		
		Back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					((Node)e.getSource()).getScene().getWindow().hide();
					Stage stage = new Stage();
					stage.setResizable(false);
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("albumlist.fxml"));
					VBox root = (VBox)loader.load();							
					AlbumListController albumListController = loader.getController();
					albumListController.start();						
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});
		
		AddPhoto.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				
				Stage stage = new Stage();
				stage.setTitle("Select a Photo");
				final FileChooser fileChooser = new FileChooser();
				configFC(fileChooser);
			    File image = fileChooser.showOpenDialog(stage);
			    if (image != null) {
				    addImage(image, album);
				    Slideshow.setDisable(false);
				    stage.close();
			    }
			}
		});
		
		Slideshow.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Stage prevStage = (Stage) AddPhoto.getScene().getWindow();
				prevStage.close();
				if(album.getPhotos().size()==0) {//empty album
					Alert alert = new Alert(AlertType.ERROR);
		        	alert.setTitle("ERROR");
		        	alert.setHeaderText("Empty Album");
		        	alert.setContentText("This album is empty, add photos to view the slideshow.");
		        	alert.showAndWait();
		        	return;
				}
				try {
					((Node)e.getSource()).getScene().getWindow().hide();
					Stage stage = new Stage();
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("slideshowview.fxml"));
					VBox root = (VBox)loader.load();							
					SlideShowController slideshowController = loader.getController();
					slideshowController.start(album);						
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				}
				catch(Exception q){
					q.printStackTrace();
				}
			}
		});
		
		
		VBOX.requestFocus();
	}


	/**
	 * Method to configure the FileChoose
	 * @param fileChooser FileChooser
	 */
	private static void configFC(final FileChooser fileChooser) {      
		fileChooser.setTitle("View Pictures");
		fileChooser.setInitialDirectory(
		    new File(System.getProperty("user.home"))
		);                 
		fileChooser.getExtensionFilters().addAll(
		    new FileChooser.ExtensionFilter("JPG/PNG", "*.jpg", "*.png")
		);
	}
	
	/**
	 * Method to add an image from the FileChooser
	 * @param file an image file
	 * @param album Album to add the image to
	 */
	private void addImage(File file, Album album) {
        try {
        	Photo matching = LoginController.crntUser.filePathPhotoFinder(file.getAbsolutePath());
        	
        	if(matching!=null) {
        		Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("COPY DETECTED");
				alert.setContentText("Since this photo already exists in another album, it will be a copy. Continue?");
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK){
					album.addPhoto(matching);
					start(album);
				}
				return;
        	}
        	
            Photo newPhoto = new Photo();
            newPhoto.setCreationDate(file);
            WritableImage writeable = null;
            BufferedImage buffered = ImageIO.read(file);
            if (buffered != null) {
            	writeable = new WritableImage(buffered.getWidth(), buffered.getHeight());
            	PixelWriter pw = writeable.getPixelWriter();
            	for (int i = 0; i < buffered.getWidth(); i++){
            		for (int j = 0; j < buffered.getHeight(); j++) {
            			pw.setArgb(i, j, buffered.getRGB(i,j));
            		}
            	}
            }
            newPhoto.setPhoto(writeable);
            newPhoto.setCreationDate(file);
            newPhoto.setFilePath(file);
            TextInputDialog popup = new TextInputDialog();
			popup.setTitle("Photo Caption");
			popup.setContentText("Give this photo a caption.");
			Optional<String> result = popup.showAndWait();
			if(result.isEmpty()) {
				newPhoto.setCaption("");
			}else {
				newPhoto.setCaption(result.get());
			}
			
			//adding tags
			try {
				Stage stage = new Stage();
				stage.setResizable(false);
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("addingtagsview.fxml"));
				VBox root = (VBox)loader.load();
				TagsController tagsController = loader.getController();
				tagsController.start(newPhoto.getTags());
				newPhoto.setTags(tagsController.getTags());
				Scene scene = new Scene(root);
				stage.setScene(scene);

				stage.initModality(Modality.APPLICATION_MODAL);
				stage.initOwner(AddPhoto.getScene().getWindow());
				stage.showAndWait();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
            album.addPhoto(newPhoto);
            start(album);
        } catch (IOException ex) {
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("ERROR");
        	alert.setHeaderText("Could not open requested file.");
        	alert.setContentText("Try again...");
        	alert.showAndWait();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
}