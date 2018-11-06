package photos;

public class AlbumInfo{
	
	private String name;
	private int numPhotos;
	private String startDateRange;
	private String endDateRange;
	
	public AlbumInfo(String name, int numPhotos, String startDateRange, String endDateRange) {
		this.name = name;
		this.numPhotos = numPhotos;
		this.startDateRange = startDateRange;
		this.endDateRange = endDateRange;
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
	
	@Override
    public String toString() {
		return this.getName() + " - " + this.getNumPhotos() + " photos - starting " + this.getStartDateRange()+ " to " + this.getEndDateRange();
    }


}
