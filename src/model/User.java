package model;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable{
	
	private String username;
	private ArrayList<AlbumInfo> userAlbums;
	
	public User(String username) {
		this.username = username;
		userAlbums = new ArrayList<AlbumInfo>();
	}
	
	public String getUsername() {
		return username;
	}
	
	//adds to the user's list of albums
	public void addToAlbums(AlbumInfo album) {
		userAlbums.add(album);
	}
	
	public ArrayList<AlbumInfo> getUserAlbums(){
		return userAlbums;
	}
	
	@Override
	public String toString() {
		return this.getUsername();
	}
	
}
