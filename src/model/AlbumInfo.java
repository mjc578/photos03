package model;

import java.io.Serializable;
import java.util.ArrayList;

public class AlbumInfo implements Serializable{
	
	private String name;
	private int numPhotos;
	private String startDateRange;
	private String endDateRange;
	private ArrayList<Photo> photos;
	
	public AlbumInfo(String name, int numPhotos, String startDateRange, String endDateRange) {
		this.name = name;
		this.numPhotos = numPhotos;
		this.startDateRange = startDateRange;
		this.endDateRange = endDateRange;
		photos = new ArrayList<Photo>();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumPhotos() {
		return numPhotos;
	}
	public void setNumPhotos(int numPhotos) {
		this.numPhotos = numPhotos;
	}
	public String getStartDateRange() {
		return startDateRange;
	}
	public void setStartDateRange(String startDateRange) {
		this.startDateRange = startDateRange;
	}
	public String getEndDateRange() {
		return endDateRange;
	}
	public void setEndDateRange(String endDateRange) {
		this.endDateRange = endDateRange;
	}
	
	public void addPhoto(Photo p) {
		photos.add(p);
	}
	
	public void deletePhoto(int index) {
		photos.remove(index);
	}
	
	@Override
    public String toString() {
		return this.getName() + " - " + this.getNumPhotos() + " photos - starting " + this.getStartDateRange()+ " to " + this.getEndDateRange();
    }


}
