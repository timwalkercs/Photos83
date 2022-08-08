//Timothy Walker tpw32
//Hasin Choudhury hmc94
package app;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * Album class, stores and manages Photos in an ArrayList
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class Album implements Serializable {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Date of Album's creation
	 */
	Calendar creationDate;
	
	/**
	 * Date of most recent Photo to be added
	 */
	Calendar lastPhoto;
	
	/**
	 * Date of first Photo to be added
	 */
	Calendar firstPhoto;
	
	/**
	 * Name of the Album
	 */
	String albumName;
	
	/**
	 * Number of photos stored in the Album
	 */
	int photoCount;
	
	/**
	 * The Album itself, ArrayList of Photos
	 */
	public ArrayList<Photo> photos = new ArrayList<Photo>();
	
	/**
	 * Album constructor, takes Album name as input and sets date of creation
	 * @param albumName the name of the Album
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		creationDate = Calendar.getInstance();
		creationDate.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * Method to add a Photo to Album's photo list
	 * @param photo the Photo to add
	 */
	public void addPhoto(Photo photo){
		photos.add(photo);
	}
	
	/**
	 * Getter method for Album's Photos
	 * @return photos
	 */
	public ArrayList<Photo> getPhotos(){
		return photos;
	}

	/**
	 * Setter method that replaces an Album's Photos with another Array List of Photos
	 * @param photos ArrayList of Photos
	 */
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	
	/**
	 * Getter method that returns Album's name
	 * @return albumName
	 */
	public String getAlbumName() {
		return albumName;
	}
	
	/**
	 * Setter method that modifies Album's name
	 * @param albumName String of Album's name
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	
	/**
	 * Getter method that returns Album's photo count
	 * @return photoCount
	 */
	public int getPhotoCount() {
		return photos.size();
	}
	
	/**
	 * Getter method that returns the Album's date range
	 * @return String representing date range
	 */
	public String getDateRange() {
		if(this.photos.size()==0) return null;
		LocalDate oldest=null;
		LocalDate newest=null;
		for(int i=0; i < this.photos.size(); i++) {
			LocalDate date = LocalDateTime.ofInstant(this.photos.get(i).creationDate.toInstant(), ZoneId.systemDefault()).toLocalDate();
			
			if(oldest==null) {
				oldest = date;
				continue;
			}
			if(newest==null) newest=date; //this way if there is 1 photo, only oldest would have a value
			
			if(date.isBefore(oldest)) oldest=date;
			if(date.isAfter(newest)) newest=date;
			
		}
		
		if(newest!=null) return (
				oldest.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) 
				+ " - " 
				+ newest.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
		);
		
		else return oldest.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		
		
	}
	
	/**
	 * Setter method that modifies Album's photo count
	 * @param photoCount number of photos in Album
	 */
	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount;
	}
	
	
	/**
	 * Getter method for Calendar data of oldest Photo
	 * @return firstPhoto (Calendar)
	 */
	public Calendar getFirstPhoto() {
		return firstPhoto;
	}
	
	/**
	 * Setter method that changes Calendar data for firstPhoto
	 * @param firstPhoto the first Photo in the Album
	 */
	public void setFirstPhoto(Calendar firstPhoto) {
		this.firstPhoto = firstPhoto;
	}

	/**
	 * Getter method for Calendar data of newest Photo
	 * @return lastPhoto (Calendar)
	 */
	public Calendar getLastPhoto() {
		return lastPhoto;
	}
	
	/**
	 * Setter method that changes Calendar data for lastPhoto
	 * @param lastPhoto the last Photo in the Album
	 */
	public void setLastPhoto(Calendar lastPhoto) {
		this.lastPhoto = lastPhoto;
	}
	
}
