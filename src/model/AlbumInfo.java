package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class AlbumInfo implements Serializable{
	
	private String name;
	private int numPhotos=0;
	private Date startDateRange;
	private Date endDateRange;
	private ArrayList<Photo> photos;
	
	public AlbumInfo(String name, int numPhotos, Date startDateRange, Date endDateRange) {
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
	public Date getStartDateRange() {
		return startDateRange;
	}
	public void setStartDateRange(Date startDateRange) {
		this.startDateRange = startDateRange;
	}
	public Date getEndDateRange() {
		return endDateRange;
	}
	public void setEndDateRange(Date endDateRange) {
		this.endDateRange = endDateRange;
	}
	
	public ArrayList<Photo> getPhotos(){
		return photos;
	}
	
	public void addPhoto(Photo p) {
		photos.add(p);
	}
	
	public void removePhoto(int index) {
		photos.remove(index);
	}
	

	
	@Override
    public String toString() {
		return this.getName() + " - " + this.getNumPhotos() + " photos - starting " + this.getStartDateRange() + " to " + this.getEndDateRange();
    }



}
