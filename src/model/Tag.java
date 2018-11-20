package model;

import java.io.Serializable;

//class meant to represent a Tag object
public class Tag implements Serializable {
	
	private TagType tagType;
	private String tagValue;
	
	public Tag(TagType tagType, String tagValue) {
		this.tagType = tagType;
		this.tagValue = tagValue;
	}
	
	public TagType getTagType() {
		return tagType;
	}
	
	public String getTagValue() {
		return tagValue;
	}
	
	public String toString() {
		return tagType + ": " + tagValue;
	}
	
	public boolean equals(Object o) {
		
		if(o == null || !(o instanceof Tag)) {
			return false;
		}
		
		Tag t = (Tag) o;
		return t.tagType.equals(tagType) && t.tagValue.equals(tagValue);
	}

}
