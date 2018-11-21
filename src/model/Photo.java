package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Photo implements Serializable{
	
	/**
	 * List of tags of the the image
	 * String field for caption of image
	 * Calendar field for date/time of image
	 * String field for path to image from file
	 */
	private List<Tag> tags;
	private String caption;
	private Calendar date;
	private String url;
	
	/**
	 * Constructor to initialize a photo
	 * 
	 * @param caption The caption of the image
	 * @param date The date of the image
	 * @param url The path to the image
	 */
	public Photo (String caption, Calendar date, String url) {
		this.caption = caption;
		this.date = date;
		this.url = url;
		tags = new ArrayList<Tag>();
	}
	
	/**
	 * Method to get tags of the image
	 * @return tags 
	 */
	public List<Tag> getTags() {
		return tags;
	}
	
	/**
	 * Method to add tag to image
	 * @param tag Tag to be added to the image
	 */
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	/**
	 * Method to get caption of image
	 * @return caption
	 */
	public String getCaption () {
		return caption;
	}
	
	/**
	 * Method to set caption of image
	 * @param caption Caption of image
	 */
	public void setCaption (String caption) {
		this.caption = caption;
	}
	
	/**
	 * Method to get date of image
	 * @return date of image
	 */
	public Calendar getDate() {
		return date;
	}
	
	/**
	 * Method to set date of image
	 * @param date Date of image
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	/**
	 * Method to get path to image
	 * @return path to image
	 */
	public String getURL() {
		return url;
	}
	
	/**
	 * Method to set path to image
	 * @param url The path to the image
	 */
	public void setURL(String url) {
		this.url = url;
	}
	
	/**
	 * Method to display all the tags of an image in a label
	 * @return String of all tags of a photo
	 */
	public String displayTags() {
		String tagString = "";
		for(int i = 0; i < tags.size(); i++) {
			tagString += (tags.get(i).toString());
			if(i != tags.size() - 1) {
				tagString += (", ");
			}
		}
		return tagString;
	}
}
