package photos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Photo implements Serializable{
	
	private List<Tag> tags;
	private String caption;
	private String date;
	private String url;
	
	public Photo (String caption, String date) {
		this.caption = caption;
		this.date = date;
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
	
	public String getDate() {
		return date;
	}
	
	public String getURL() {
		return url;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
}
