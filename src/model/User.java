package model;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable{
	
	/**
	 * String field for username of user
	 * ArrayList of albums that belong to the user
	 * ArrayList of tag types that belong to the user
	 */
	private String username;
	private ArrayList<AlbumInfo> userAlbums;
	private ArrayList<TagType> tagTypes;
	
	/**
	 * Constructor to initialize a user
	 * 
	 * @param username The user's username
	 */
	public User(String username) {
		this.username = username;
		userAlbums = new ArrayList<AlbumInfo>();
		tagTypes = new ArrayList<TagType>();
		tagTypes.add(new TagType("person", true));
		tagTypes.add(new TagType("location", false));
	}
	
	/**
	 * Method to get user's username
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Method to add album to user's list of albums
	 * @param album Album to be added
	 */
	public void addToAlbums(AlbumInfo album) {
		userAlbums.add(album);
	}
	
	/**
	 * Method to remove album from user's list of albums
	 * @param index Index of album to be removed
	 */
	public void removeFromAlbums(int index) {
		userAlbums.remove(index);
	}
	
	/**
	 * Method to get user's albums
	 * @return list of user's albums
	 */
	public ArrayList<AlbumInfo> getUserAlbums(){
		return userAlbums;
	}
	
	/**
	 * Method to get the user's tag types
	 * @return tag types of the user
	 */
	public ArrayList<TagType> getTagTypes(){
		return tagTypes;
	}
	
	/**
	 * Method to add tag type to the user's list of tag types
	 * @param tt Tag type to be added to user's tag type list
	 */
	public void addTagType(TagType tt) {
		tagTypes.add(tt);
	}
	
	/**
	 * toString method to display users's username
	 */
	@Override
	public String toString() {
		return this.getUsername();
	}
	
}
