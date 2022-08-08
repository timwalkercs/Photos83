//Timothy Walker tpw32
//Hasin Choudhury hmc94
package app;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.IOException;
import java.io.Serializable;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Enumeration;

/**
 * Photo class, maintains all of a Photo's details and data
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class Photo implements Serializable{

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Calander instance of Photo's creation date
	 */
	Calendar creationDate;
	
	/**
	 * transient Image
	 */
	transient Image photo;
	
	/**
	 * Photo's caption
	 */
	String caption = "";
	
	/**
	 * Photo's filepath
	 */
	String filepath = "";
	
	/**
	 * Photo's tags
	 */
	HashMap<String, ArrayList<String>> tags = new HashMap<String, ArrayList<String>>();
	
	/**
	 * Getter method that returns the Tags
	 * @return tags
	 */
	public HashMap<String, ArrayList<String>> getTags(){
		return tags;
	}
	
	/**
	 * Setter method to set tags
	 * @param tags HashMap of tags
	 */
	public void setTags(HashMap<String, ArrayList<String>> tags) {
		this.tags = tags; 
	}
	
	/**
	 * Getter method that returns the Image
	 * @return photo
	 */
	public Image getPhoto() {
		return photo;
	}
	
	/**
	 * Setter method that changes current Image
	 * @param photo a Photo
	 */
	public void setPhoto(Image photo) {
		this.photo = photo;
	}
	
	/**
	 * Getter method that returns caption
	 * @return caption
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * Setter method that modifies the caption
	 * @param caption desired caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Getter method that returns Calendar data of creation date
	 * @return creation date
	 */
	public Calendar getCreationDate() {
		return creationDate;
	}
	
	/**
	 * Setter method that modifies Photo's creation date
	 * @param f a File
	 */
	public void setCreationDate(File f) {
		SimpleDateFormat simp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		simp.format(f.lastModified());
		creationDate = simp.getCalendar();
		creationDate.set(Calendar.MILLISECOND,0);
	}
	
	/**
	 * Setter method that sets filepath
	 * @param file a File
	 */
	public void setFilePath(File file) {
		this.filepath=file.getAbsolutePath();
	}
	
	/**
	 * Getter method that gets filepath of photo
	 * @return filepath String
	 */
	public String getFilePath() {
		return this.filepath;
	}
	
	/**
	 * Reads photo
	 * @param inStream ObjectInputStream
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
		inStream.defaultReadObject();
        photo = SwingFXUtils.toFXImage(ImageIO.read(inStream), null);
    }
	
	/**
	 * Writes photo
	 * @param outStream ObjectOutputStream
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream outStream) throws IOException {
		outStream.defaultWriteObject();
	     if(photo != null){
	       	ImageIO.write(SwingFXUtils.fromFXImage(photo, null), "png", outStream);
	     }
	}
	
}
