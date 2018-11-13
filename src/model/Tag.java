package model;

//class meant to represent a Tag object
public class Tag {
	
	private String tagType;
	private String tagValue;
	
	public Tag(String tagType, String tagValue) {
		this.tagType = tagType;
		this.tagValue = tagValue;
	}
	
	public String getTagType() {
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
