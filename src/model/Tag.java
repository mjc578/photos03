package model;

import java.io.Serializable;

//class meant to represent a Tag object
public class Tag implements Serializable {
	
	/**
	 * TagType field for tagType of tag
	 * String field for tag 
	 */
	private TagType tagType;
	private String tagValue;
	
	/**
	 * Constructor to intialize a tag
	 * @param tagType The type of the tag
	 * @param tagValue The value of the tag
	 */
	public Tag(TagType tagType, String tagValue) {
		this.tagType = tagType;
		this.tagValue = tagValue;
	}
	
	/**
	 * Method to get the type of the tag
	 * @return type of the tag
	 */
	public TagType getTagType() {
		return tagType;
	}
	
	/**
	 * Method to get the tag
	 * @return tag
	 */
	public String getTagValue() {
		return tagValue;
	}
	
	/**
	 * toString method to display the type of tag and tag
	 */
	public String toString() {
		return tagType + ": " + tagValue;
	}
	
	/**
	 * equals method to compare a pair of tag type and tag to another pair
	 */
	public boolean equals(Object o) {
		
		if(o == null || !(o instanceof Tag)) {
			return false;
		}
		
		Tag t = (Tag) o;
		return t.tagType.equals(tagType) && t.tagValue.equals(tagValue);
	}

}
