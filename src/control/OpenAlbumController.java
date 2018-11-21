package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Photo;
import model.User;
import javafx.scene.control.ListCell;

public class OpenAlbumController {
	
	@FXML private Button addButton;
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private Button copyButton;
	@FXML private Button moveButton;
	@FXML private Button backButton;
	@FXML private Button previousPhotoButton;
	@FXML private Button nextPhotoButton;
	@FXML private Label dateTime;
	@FXML private Label caption;
	@FXML private Label albumName;
	@FXML private Label tagsLabel;
	@FXML private ImageView clickedImageView;
	@FXML private ListView<Photo> listView;
	
	private ObservableList<Photo> obsList;
    private List<User> users;
    private int albumIndex;
    private int userIndex;
    FileChooser fileChooser;
    
    public static final String storeDir = "docs";
	public static final String storeFile = "users.ser"; 
    
  //gets selected album and user from albumDisplayController
  	public void initData(List<User> user, int index, int index2) {
  		//sets the users list, the index of user we need, and the index of album we need
  		users = user;
  		userIndex = index;
  		albumIndex = index2;
  		
  		albumName.setText( users.get(userIndex).getUserAlbums().get(albumIndex).getName());
  		//initialize the file chooser
  		fileChooser = new FileChooser();
  		
  		//set the obslist on the users list of photos in the album user clicked on and set the list view
  		obsList = FXCollections.observableArrayList(users.get(userIndex).getUserAlbums().get(albumIndex).getPhotos());
  		listView.setItems(obsList);
  		listView.getSelectionModel().select(0);
  		
  		
  		if (users.get(userIndex).getUsername().equals("stock") && users.get(userIndex).getUserAlbums().get(albumIndex).getName().equals("stock") && obsList.size()==0) {
  			setStockPhotos();
  			listView.getSelectionModel().select(0);
  			showPhotoDetails();
  			
  			
  		}

  		else if (obsList != null && !obsList.isEmpty()) {
			showPhotoDetails();
		}
  		else if (obsList.isEmpty() && obsList != null) {
        	disable(true);
        }
  		//listener for showing the photo details if they are clicked on
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        	if(newVal != null) {
        		showPhotoDetails();
        	}
        });
        
        listCellFactory();
        
  	}
  	
	public void listCellFactory() {
		listView.setCellFactory(param -> new ListCell<Photo>() {
            @Override
            public void updateItem(Photo name, boolean empty) {
                super.updateItem(name, empty);
                ImageView imageView = new ImageView();
                imageView.setFitWidth(75);
                imageView.setFitHeight(75);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                	for (int i=0; i < obsList.size(); i++) {
                		if(name.getURL().equals(obsList.get(i).getURL())) {
                			imageView.setImage(new Image("file:" + obsList.get(i).getURL()));
	                    }
                	}
                    setText(name.getCaption());
                    setGraphic(imageView);
                }
            }
        });

	}
	
	//back button takes you back to Album Display scene
	public void backButton(ActionEvent event) throws Exception{
		writeApp(users);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/AlbumDisplay.fxml"));
		Parent root = loader.load();
		AlbumDisplayController controller = loader.getController();
		controller.initData(users, userIndex);
		
		Scene albumDisplayScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(albumDisplayScene);
		window.setTitle("Album Display");
		window.show();
	}
	
	//add photo button
	public void addButton(ActionEvent event) throws MalformedURLException{
		//get the photo from the file chooser
		Photo p = new Photo(null, null, null);
		fileChooser.setTitle("Open Resource File");
		File file = null;
		file = fileChooser.showOpenDialog(null);
		if(file == null) {
			return;
		}
		p.setURL(file.getAbsolutePath());
		System.out.println(p.getURL());
		Image imageForFile = new Image("file:" + p.getURL());
		//sets the clicked image view as this image
		clickedImageView.setImage(imageForFile);
		//get a calendar and set the date for the photo
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(file.lastModified()));
		c.set(Calendar.MILLISECOND, 0);
		p.setDate(c);
		
		//add caption after photo is added
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Caption");
		dialog.setHeaderText("Caption");
		dialog.setContentText("Enter caption for photo:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			p.setCaption(result.get());
		}

		//add the photo to the obslist and to the user's metadata
		obsList.add(p);
		users.get(userIndex).getUserAlbums().get(albumIndex).addPhoto(p);
		//set the user's album's number of photos
		users.get(userIndex).getUserAlbums().get(albumIndex).setNumPhotos(obsList.size());
		//update the obslist
		listCellFactory();
		
		//select the image
		listView.getSelectionModel().select(p);
		disable(false);
		
		//get start/end date of album
		setAlbumDates();	
	}
	
	public String date(Calendar c) {
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		return month + "-" + day + "-" + year;
	}
	
	//delete button - deletes selected photo
	public void deleteButton(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Photo");
		alert.setContentText("Are you sure you want to delete this?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		   delete();
		}
		
	}
	
	public void delete() {
		int index = listView.getSelectionModel().getSelectedIndex();
		obsList.remove(index);
		   users.get(userIndex).getUserAlbums().get(albumIndex).removePhoto(index); 
		   users.get(userIndex).getUserAlbums().get(albumIndex).setNumPhotos(obsList.size());
		   
		   if (obsList.isEmpty() && obsList != null) {
				disable(true);
				dateTime.setText(null);
				caption.setText(null);
				clickedImageView.setImage(null);
				tagsLabel.setText(null);
			}
		   
		   listCellFactory();
		   setAlbumDates();
	}
	
	//copy button
	public void copyButton(ActionEvent event){
		//list of choices (albums)
		List<String> choices = new ArrayList<>();
		//load album names into array
		for (int i=0; i<users.get(userIndex).getUserAlbums().size(); i++) {
			choices.add(i, users.get(userIndex).getUserAlbums().get(i).getName());
		}

		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle("Copy");
		dialog.setHeaderText("Copy Photo");
		dialog.setContentText("Choose which album you want to copy this photo to:");
		
		int choiceIndex = -1;
		Optional<String> result = dialog.showAndWait();
		for (int i=0; i<choices.size(); i++) {
			if (result.get().equals(choices.get(i))) {
				choiceIndex = i;
			}
		}
		//if okay is clicked
		if (result.isPresent()){
			//check if selected album is current album, else give an error.
			if (users.get(userIndex).getUserAlbums().get(albumIndex).getName().equals(result.get())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid Input");
				alert.setContentText("This photo is already in this album.");
				alert.showAndWait();
			}
			//copy photo to selected album
			else{
				users.get(userIndex).getUserAlbums().get(choiceIndex).addPhoto(listView.getSelectionModel().getSelectedItem());
				users.get(userIndex).getUserAlbums().get(choiceIndex).setNumPhotos(users.get(userIndex).getUserAlbums().get(choiceIndex).getNumPhotos()+1);
			
				setAlbumDates2(choiceIndex);			
			}
		    
		}
		else {
		}
	}
	
	//move button
	public void moveButton(ActionEvent event){
		List<String> choices = new ArrayList<>();
		//load album names into array
		for (int i=0; i<users.get(userIndex).getUserAlbums().size(); i++) {
			choices.add(i, users.get(userIndex).getUserAlbums().get(i).getName());
		}

		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
		dialog.setTitle("Copy");
		dialog.setHeaderText("Copy Photo");
		dialog.setContentText("Choose which album you want to copy this photo to:");
		
		int choiceIndex = -1;
		Optional<String> result = dialog.showAndWait();
		for (int i=0; i<choices.size(); i++) {
			if (result.get().equals(choices.get(i))) {
				choiceIndex = i;
			}
		}
		//if okay is clicked
		if (result.isPresent()){
			//check if selected album is current album, else give an error.
			if (users.get(userIndex).getUserAlbums().get(albumIndex).getName().equals(result.get())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid Input");
				alert.setContentText("This photo is already in this album.");
				alert.showAndWait();
			}
			//add photo to selected album and delete from current album
			else{
				users.get(userIndex).getUserAlbums().get(choiceIndex).addPhoto(listView.getSelectionModel().getSelectedItem());
				users.get(userIndex).getUserAlbums().get(choiceIndex).setNumPhotos(users.get(userIndex).getUserAlbums().get(choiceIndex).getNumPhotos()+1);
				delete();
			
				setAlbumDates2(choiceIndex);
			}   
		}
	}
	
	// "<" button
	public void previousPhotoButton(ActionEvent event){
		int index = listView.getSelectionModel().getSelectedIndex() - 1;
		listView.getSelectionModel().select(index);
	}
	
	// ">" button
	public void nextPhotoButton(ActionEvent event){
		int index = listView.getSelectionModel().getSelectedIndex() + 1;
		listView.getSelectionModel().select(index);
	}
	
	// edit button - takes you to edit scene
	// edit scene allows you to edit caption, add tags, add tag-types, and delete tags
	public void editButton(ActionEvent event) throws Exception{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Edit.fxml"));
		Parent root = loader.load();
		EditController controller = loader.getController();
		int photoIndex = listView.getSelectionModel().getSelectedIndex();
		
		controller.initData(users, userIndex, albumIndex, photoIndex);
		
		Scene EditScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(EditScene);
		window.setTitle("Edit");
		window.show();
	}
	
	public void showPhotoDetails(){
		dateTime.setText(listView.getSelectionModel().getSelectedItem().getDate().getTime().toString());
		caption.setText(listView.getSelectionModel().getSelectedItem().getCaption());
		clickedImageView.setImage(new Image("file:" + listView.getSelectionModel().getSelectedItem().getURL()));
		tagsLabel.setText(listView.getSelectionModel().getSelectedItem().displayTags());
		
		if (listView.getSelectionModel().getSelectedIndex() == 0) {
			previousPhotoButton.setDisable(true);
		}
		else { 
			previousPhotoButton.setDisable(false);
		}
		if (listView.getSelectionModel().getSelectedIndex() == obsList.size() - 1) {
			nextPhotoButton.setDisable(true);
		}
		else { 
			nextPhotoButton.setDisable(false);
		}
	}
	
	//sets album date range when adding/deleting photos
	public void setAlbumDates() {
		if(obsList.size() == 0) {
			users.get(userIndex).getUserAlbums().get(albumIndex).setStartDateRange("*");
			users.get(userIndex).getUserAlbums().get(albumIndex).setEndDateRange("*");
			return;
		}
		Calendar earliestPic = Calendar.getInstance();
		Calendar latestPic = Calendar.getInstance();
		
		earliestPic.setTime(obsList.get(0).getDate().getTime());
		latestPic.setTime(obsList.get(0).getDate().getTime());
		for(int i = 0; i < obsList.size(); i++) {
			if (obsList.get(i).getDate().getTime().compareTo(earliestPic.getTime())<=0) {
				earliestPic.setTime(obsList.get(i).getDate().getTime());
				String earliestDate = date(earliestPic);
				users.get(userIndex).getUserAlbums().get(albumIndex).setStartDateRange(earliestDate);
			}
			if (obsList.get(i).getDate().getTime().compareTo(latestPic.getTime())>=0) {
				latestPic.setTime(obsList.get(i).getDate().getTime());
				String latestDate = date(latestPic);
				users.get(userIndex).getUserAlbums().get(albumIndex).setEndDateRange(latestDate);
			}
		}
	}
	
	//sets album date range when copying/moving photos
	public void setAlbumDates2(int choiceIndex) {
		Calendar earliestPic = Calendar.getInstance();
		Calendar latestPic = Calendar.getInstance();
		earliestPic.setTime(users.get(userIndex).getUserAlbums().get(choiceIndex).getPhotos().get(0).getDate().getTime());
		latestPic.setTime(users.get(userIndex).getUserAlbums().get(choiceIndex).getPhotos().get(0).getDate().getTime());
		
		for (int i = 0; i<users.get(userIndex).getUserAlbums().get(choiceIndex).getNumPhotos(); i++) {
			if (users.get(userIndex).getUserAlbums().get(choiceIndex).getPhotos().get(i).getDate().getTime().compareTo(earliestPic.getTime())<=0) {
				earliestPic.setTime(users.get(userIndex).getUserAlbums().get(choiceIndex).getPhotos().get(i).getDate().getTime());
				String earliestDate = date(earliestPic);
				users.get(userIndex).getUserAlbums().get(choiceIndex).setStartDateRange(earliestDate);
			}
			if (users.get(userIndex).getUserAlbums().get(choiceIndex).getPhotos().get(i).getDate().getTime().compareTo(latestPic.getTime())>=0) {
				latestPic.setTime(users.get(userIndex).getUserAlbums().get(choiceIndex).getPhotos().get(i).getDate().getTime());
				String latestDate = date(latestPic);
				users.get(userIndex).getUserAlbums().get(choiceIndex).setEndDateRange(latestDate);
			}
		}	
	}
	
	public void disable(boolean tf){
		editButton.setDisable(tf);
		deleteButton.setDisable(tf);
		copyButton.setDisable(tf);
		moveButton.setDisable(tf);	
	}
	
	public void setStockPhotos() {
  		Photo s1 = new Photo("stock1", null, null);
		File file = new File("C:\\Users\\kmist\\eclipse-workspace\\photos03\\src\\stockPhotos\\binary.jpg");
		s1.setURL(file.getAbsolutePath());
		Image imageForFile = new Image("file:" + s1.getURL());
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(file.lastModified()));
		c.set(Calendar.MILLISECOND, 0);
		s1.setDate(c);
		obsList.add(s1);
		users.get(userIndex).getUserAlbums().get(albumIndex).addPhoto(s1);
		
		Photo s2 = new Photo("stock2", null, null);
		File file2 = new File("C:\\Users\\kmist\\eclipse-workspace\\photos03\\src\\stockPhotos\\binary2.jpg");
		s2.setURL(file2.getAbsolutePath());
		Image imageForFile2 = new Image("file:" + s2.getURL());
		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date(file.lastModified()));
		c2.set(Calendar.MILLISECOND, 0);
		s2.setDate(c2);
		obsList.add(s2);
		users.get(userIndex).getUserAlbums().get(albumIndex).addPhoto(s2);
		
		Photo s3 = new Photo("stock3", null, null);
		File file3 = new File("C:\\Users\\kmist\\eclipse-workspace\\photos03\\src\\stockPhotos\\button.jpg");
		s3.setURL(file3.getAbsolutePath());
		Image imageForFile3 = new Image("file:" + s3.getURL());
		Calendar c3 = Calendar.getInstance();
		c3.setTime(new Date(file.lastModified()));
		c3.set(Calendar.MILLISECOND, 0);
		s3.setDate(c3);
		obsList.add(s3);
		users.get(userIndex).getUserAlbums().get(albumIndex).addPhoto(s3);
		
		Photo s4 = new Photo("stock4", null, null);
		File file4 = new File("C:\\Users\\kmist\\eclipse-workspace\\photos03\\src\\stockPhotos\\code.jpg");
		s4.setURL(file4.getAbsolutePath());
		Image imageForFile4 = new Image("file:" + s4.getURL());
		Calendar c4 = Calendar.getInstance();
		c4.setTime(new Date(file.lastModified()));
		c4.set(Calendar.MILLISECOND, 0);
		s4.setDate(c4);
		obsList.add(s4);
		users.get(userIndex).getUserAlbums().get(albumIndex).addPhoto(s4);
		
		Photo s5 = new Photo("stock5", null, null);
		File file5 = new File("C:\\Users\\kmist\\eclipse-workspace\\photos03\\src\\stockPhotos\\robot.jpg");
		s5.setURL(file5.getAbsolutePath());
		Image imageForFile5 = new Image("file:" + s5.getURL());
		Calendar c5 = Calendar.getInstance();
		c5.setTime(new Date(file.lastModified()));
		c5.set(Calendar.MILLISECOND, 0);
		s5.setDate(c5);
		obsList.add(s5);
		users.get(userIndex).getUserAlbums().get(albumIndex).addPhoto(s5);
		
		
		users.get(userIndex).getUserAlbums().get(albumIndex).setNumPhotos(obsList.size());
		listCellFactory();
		setAlbumDates();
			
  	}
	
	public static void writeApp(List<User> tagApp) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(tagApp);
		oos.close();
	} 
}
