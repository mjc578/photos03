package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class AlbumInfo implements Serializable{
	
	/**
	 * String field for album name
	 * int field for number of photos in the album
	 * String field for date of the earliest photo in the album
	 * String field for date of the latest photo in the album
	 * ArrayList of photos in the album
	*/
	private String name;
	private int numPhotos=0;
	private String startDateRange;
	private String endDateRange;
	private ArrayList<Photo> photos;
	
	
	/**
	 * Constructor to initialize an album
	 * 
	 * @param name The album's name
	 * @param numPhotos The number of photos in the album
	 * @param startDateRange the date of earliest photo
	 * @param endDateRange the date of latest photo
	 */
	public AlbumInfo(String name, int numPhotos, String startDateRange, String endDateRange) {
		this.name = name;
		this.numPhotos = numPhotos;
		this.startDateRange = startDateRange;
		this.endDateRange = endDateRange;
		photos = new ArrayList<Photo>();
	}
	
	/**
	 * Method to get album's name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method to set album's name
	 * @param name Name of album
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Method to get the number of photos in the album
	 * @return number number of photos
	 */
	public int getNumPhotos() {
		return numPhotos;
	}
	
	/**
	 * Method to set number of photos in the album
	 * @param numPhotos Number of photos in album
	 */
	public void setNumPhotos(int numPhotos) {
		this.numPhotos = numPhotos;
	}
	
	/**
	 * Method to get date of earliest photo in album
	 * @return start date range of album
	 */
	public String getStartDateRange() {
		return startDateRange;
	}
	
	/**
	 * Method to set the start date range of the album
	 * @param startDateRange The date of the earliest photo
	 */
	public void setStartDateRange(String startDateRange) {
		this.startDateRange = startDateRange;
	}
	
	/**
	 * Method to get date of latest photo in album
	 * @return end date range of album
	 */
	public String getEndDateRange() {
		return endDateRange;
	}
	
	/**
	 * Method to set the end date range of the album
	 * @param endDateRange The date of the latest photo
	 */
	public void setEndDateRange(String endDateRange) {
		this.endDateRange = endDateRange;
	}
	
	/**
	 * Method to get list of photos in the album
	 * @return list of photos in the album
	 */
	public ArrayList<Photo> getPhotos(){
		return photos;
	}
	
	/**
	 * Method to add photo to album
	 * @param p Photo to add to album
	 */
	public void addPhoto(Photo p) {
		photos.add(p);
	}
	
	/**
	 * Method to remove photo in album
	 * @param index Index of photo to be removed
	 */
	public void removePhoto(int index) {
		photos.remove(index);
	}
	

	/**
	 * toString method to display album name, number of photos, and date range in the list view in albumDisplayController
	 */
	@Override
    public String toString() {
		return this.getName() + " - " + this.getNumPhotos() + " photos - starting " + this.getStartDateRange() + " to " + this.getEndDateRange();
    }



}
