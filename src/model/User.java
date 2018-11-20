package model;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable{
	
	private String username;
	private ArrayList<AlbumInfo> userAlbums;
	private ArrayList<TagType> tagTypes;
	
	public User(String username) {
		this.username = username;
		userAlbums = new ArrayList<AlbumInfo>();
		tagTypes = new ArrayList<TagType>();
		tagTypes.add(new TagType("person", true));
		tagTypes.add(new TagType("location", false));
	}
	
	public String getUsername() {
		return username;
	}
	
	//adds to the user's list of albums
	public void addToAlbums(AlbumInfo album) {
		userAlbums.add(album);
	}
	
	public void removeFromAlbums(int index) {
		userAlbums.remove(index);
	}
	
	public ArrayList<AlbumInfo> getUserAlbums(){
		return userAlbums;
	}
	
	public ArrayList<TagType> getTagTypes(){
		return tagTypes;
	}
	
	public void addTagType(TagType tt) {
		tagTypes.add(tt);
	}
	
	@Override
	public String toString() {
		return this.getUsername();
	}
	
}
