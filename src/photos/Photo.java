package photos;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable{
	
	private ArrayList<Tag> tags;
	private String caption;
	private String date;
	
	public Photo (String caption, String date) {
		this.caption = caption;
		this.date = date;
	}
	
	public ArrayList<Tag> getTags() {
		return tags;
	}
	
	public void addTag(Tag tag) {
		tags.add(tag);
	}

}
