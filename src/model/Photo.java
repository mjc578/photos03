package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Photo implements Serializable{
	
	private List<Tag> tags;
	private String caption;
	private Calendar date;
	private String url;
	
	//for intantiating a new photo
	public Photo (String caption, Calendar date, String url) {
		this.caption = caption;
		this.date = date;
		this.url = url;
		tags = new ArrayList<Tag>();
	}
	
	public List<Tag> getTags() {
		return tags;
	}
	
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	public String getCaption () {
		return caption;
	}
	
	public void setCaption (String caption) {
		this.caption = caption;
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	public String getURL() {
		return url;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	
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
