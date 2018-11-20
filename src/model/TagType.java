package model;

import java.io.Serializable;

public class TagType implements Serializable {

	private String type;
	private boolean multiValued;
	
	public TagType(String type, boolean isMulti) {
		this.type = type;
		multiValued = isMulti;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isMultiValued() {
		return multiValued;
	}
	
	public boolean equals(Object o) {
		
		if(o == null || !(o instanceof TagType)) {
			return false;
		}
		
		TagType t = (TagType) o;
		return t.type.equals(type) && t.multiValued == multiValued;
	}
	
	public String toString() {
		return type;
	}
}
