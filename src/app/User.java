//Timothy Walker tpw32
//Hasin Choudhury hmc94
package app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * User class, holds User's list of Albums, Albums which contain Photos
 * @author Timothy Walker and Hasin Choudhury
 *
 */
public class User implements Serializable {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Username field
	 */
	String username = "";
	
	/**
	 * List of the User's Albums
	 */
	public ArrayList<Album> albumList = new ArrayList<Album>();
	
	/**
	 * Constructor with username parameter
	 * @param username User's name
	 */
	public User(String username) {
		this.username = username;
	}
	
	/**
	 * Getter method which returns username
	 * @return String username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Setter method to edit username
	 * @param username User's name
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Getter method which returns album list
	 * @return ArrayList of Albums
	 */
	public ArrayList<Album> getAlbums(){
		return albumList;
	}
	
	/**
	 * Setter method to change album list
	 * @param albumList ArrayList of Albums
	 */
	public void setAlbums(ArrayList<Album> albumList){
		this.albumList = albumList;
	}
	
	/**
	 * 
	 * @param filepath Filepath String
	 * @return Photo at the given path
	 */
	public Photo filePathPhotoFinder(String filepath) {
		for(int i=0; i<this.albumList.size(); i++) {
			Album currAlbum = this.albumList.get(i);
			for(int j=0; j < currAlbum.getPhotoCount(); j++) {
				if(currAlbum.getPhotos().get(j).getFilePath().equals(filepath)) return currAlbum.getPhotos().get(j);
			}
		}
		return null;
	}
	
	/**
	 * Search method to return index of an album in albumList
	 * @param album Album's name
	 * @return index of album or negative if not found
	 */
	public int searchAlbum(String album){		
		for(int i = 0; i < albumList.size(); i++){
			if(albumList.get(i).getAlbumName().equals(album)){
				return i;
			}
		}
		return -1;
	}
	
}
