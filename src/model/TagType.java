package model;

import java.io.Serializable;

public class TagType implements Serializable {

	/**
	 * String field for tag type
	 * boolean field for if the tag type can have more than one tag value
	 */
	private String type;
	private boolean multiValued;
	
	/**
	 * Constructor to initialize tag type
	 * 
	 * @param type Tag type
	 * @param isMulti If tag type can have more than one tag value
	 */
	public TagType(String type, boolean isMulti) {
		this.type = type;
		multiValued = isMulti;
	}
	
	/**
	 * Method to get the tag type
	 * @return tag type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Method to see if tag type can have multiple tag values
	 * @return true if tag type can have multiple tag values, otherwise, false
	 */
	public boolean isMultiValued() {
		return multiValued;
	}
	
	/**
	 * equals method to compare pair of tag type and whether it is multivalued to another pair
	 */
	public boolean equals(Object o) {
		
		if(o == null || !(o instanceof TagType)) {
			return false;
		}
		
		TagType t = (TagType) o;
		return t.type.equals(type) && t.multiValued == multiValued;
	}
	
	/**
	 * toString method to display tag type
	 */
	public String toString() {
		return type;
	}
}
