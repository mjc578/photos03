package model;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable{
	
	private String username;
	private ArrayList<AlbumInfo> userAlbums;
	private ArrayList<String> tagTypes;
	
	public User(String username) {
		this.username = username;
		userAlbums = new ArrayList<AlbumInfo>();
		tagTypes = new ArrayList<String>();
		tagTypes.add("person");
		tagTypes.add("Location");
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
	
	@Override
	public String toString() {
		return this.getUsername();
	}
	
}
