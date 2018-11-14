package model;

import java.util.*;

public class User {
	
	private String username;
	private List<AlbumInfo> userAlbums;
	
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
}
