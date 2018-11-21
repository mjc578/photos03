package control;

import javafx.scene.control.TextArea;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.AlbumInfo;
import model.Photo;
import model.Tag;
import model.TagType;
import model.User;

public class EditController implements Serializable {

	@FXML private Button deleteTagButton;
	@FXML private Button addTagButton;
	@FXML private Button addTagTypeButton;
	@FXML private TextArea editCaption;
	@FXML private Label dateLabel;
	@FXML private Label captionLabel;
	@FXML private Label tagsLabel;
	@FXML private ImageView clickedImageView;
	@FXML private Button confirmButton;
	@FXML private Button cancelButton;
	@FXML private Button nextPhotoButton;
	@FXML private Button previousPhotoButton;
	@FXML private ListView<Tag> editTagListView;
	

	/**
	 * String field for caption of photo
	 * Observable List of tags of photo
	 * List of users
	 * int field of index of user that is logged in
	 * int field of index of album that is selected
	 * int field of index of photo that is selected
	 * Photo field of photo that is selected
	 */
	//all the stuff user can edit and may want to save
	//the text of the caption previously there
	private String caption;
	//the edit tag list
	private ObservableList<Tag> obsList;
	private List<User> users;
	private int userIndex;
	private int albumIndex;
	private int photoIndex;
	//will have a temporary photo representing the current photo since the user can cancel all changes at any time
	private Photo p;
	
	/**
	 * Method to get list of user, index of user that is logged in, index of album selected, and index of photo selected
	 * @param user List of users
	 * @param index Index of user that is logged in
	 * @param index2 Index of album that is selected
	 * @param index3 Index of photo that is selected
	 */
	public void initData(List<User> user, int index, int index2, int index3) {
		users = user;
		userIndex = index;
		albumIndex = index2;
		photoIndex = index3;
		
		tagsLabel.setWrapText(true);
		
		p = users.get(userIndex).getUserAlbums().get(albumIndex).getPhotos().get(photoIndex);
		
		obsList = FXCollections.observableArrayList(p.getTags());
		//sets the edit caption text with the caption of the photo that was clicked
		editCaption.setText(p.getCaption());
		editTagListView.setItems(obsList);
		editTagListView.getSelectionModel().select(0);
		
		if (obsList!=null && obsList.isEmpty()) {
			deleteTagButton.setDisable(true);
		}
		
		nextPhotoButton.setDisable(true);
		previousPhotoButton.setDisable(true);
		
		dateLabel.setText(users.get(userIndex).getUserAlbums().get(albumIndex).getPhotos().get(photoIndex).getDate().getTime().toString());
		captionLabel.setText(users.get(userIndex).getUserAlbums().get(albumIndex).getPhotos().get(photoIndex).getCaption());
		tagsLabel.setText(users.get(userIndex).getUserAlbums().get(albumIndex).getPhotos().get(photoIndex).displayTags());
		clickedImageView.setImage(new Image("file:" + users.get(userIndex).getUserAlbums().get(albumIndex).getPhotos().get(photoIndex).getURL()));
		
	} 
	
	/**
	 * Method to delete a tag of a photo
	 * @param event Delete button is pressed
	 */
	//delete button
	public void deleteTagButton(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Tag");
		alert.setContentText("Are you sure you want to delete this?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			//delete from obslist
		    obsList.remove(editTagListView.getSelectionModel().getSelectedItem());
		    
		    if (obsList!=null && obsList.isEmpty()) {
				deleteTagButton.setDisable(true);
			}
		}
	}
	
	/**
	 * Method to add tag to list of tags of a photo
	 * @param event Add tag button is pressed
	 */
	//add tag button
	public void addTagButton(ActionEvent event) {		
		//Choice Dialog to choose out of existing tag types
		ChoiceDialog<TagType> dialog = new ChoiceDialog<>(users.get(userIndex).getTagTypes().get(0), users.get(userIndex).getTagTypes());
		dialog.setTitle("New Tag");
		dialog.setHeaderText("Choose the tag-type for your new tag.");
		dialog.setContentText("Tag-Types:");
		Optional<TagType> result = dialog.showAndWait();
		
		//if okay is clicked
		if (result.isPresent()){
			//if the tag type they chose is not multi valued, check if there already exists one in the list
			//if there is one, they may not add another one, this check is unneccesary for checking if it is multi valued
			if (!result.get().isMultiValued()) {
				for (Tag st : obsList) {
					if (st.getTagType().getType().equals(result.get().getType())) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Single-typed tag already exists");
						alert.setContentText("Please try again.");
						alert.showAndWait();
						return;
					}
				}
			}
			
		    //Text Input Dialog for new tag name after the tag type is selected
		    TextInputDialog dialog2 = new TextInputDialog();
			dialog2.setTitle("New Tag");
			dialog2.setHeaderText("New Tag");
			dialog2.setContentText("Please enter the name of your new tag:");
			Optional<String> result2 = dialog2.showAndWait();
			
			//if okay is clicked
			if (result2.isPresent()){
				//if user did not input anything, invalid input
				if (result2.get().equals("")) {
					errorMessage();
				}
				//adds tag to obsList unless it is a duplicate
				else {
					Tag t = new Tag(result.get(), result2.get());
					
					//duplicate tag
					if(obsList.contains(t)) {
						errorMessageDup();
						return;
					}
					//ifthe tag is a new tag, add it to obs list
					obsList.add(t);
					
					editTagListView.getSelectionModel().select(t);
					deleteTagButton.setDisable(false);
				}
			}
		}
	}
	
	/**
	 * Method to add a tag type
	 * @param event Add tag type button is pressed
	 * @throws IOException
	 */
	//add tag-type button
	public void addTagTypeButton(ActionEvent event) throws IOException {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("New Tag-Type");
		dialog.setHeaderText("New Tag-Type");
		dialog.setContentText("Enter name for new tag-type");
		Optional<String> result = dialog.showAndWait();

		//if okay is clicked
		if (result.isPresent()){
			//if there is no user input, alert for invalid input
			if (result.get().equals("")) {
				errorMessage();
			}
			//the silly user entered something tangible
			else {
				//if the string entered is already a tag type the user has
				for(TagType ttt : users.get(userIndex).getTagTypes()) {
					if(ttt.getType().toLowerCase().equals(result.get().toLowerCase())) {
						errorMessageDup();
						return;
					}
				}

				ChoiceDialog<String> dialog2 = new ChoiceDialog<String>("Yes", "No");
				dialog2.setTitle("New Tag-Type");
				dialog2.setHeaderText("New Tag-Type");
				dialog2.setContentText("Is the Tag-Type multi-valued?");
				Optional<String> result2 = dialog2.showAndWait();
				
				if(result2.isPresent()) {
					TagType tt;
					//tag type is multi valued
					if(result2.equals("Yes")) {
						tt = new TagType(result.get(), true);
					}
					//otherwise, it is not multivalued
					else {
						tt = new TagType(result.get(), false);
					}
					//if the tag type already hecking exists
					if(users.get(userIndex).getTagTypes().contains(tt)) {
						errorMessageDup();
					}
					else {
						//adds tag type
						users.get(userIndex).getTagTypes().add(tt);
					}
				}
			}
		}
	}
	
	/**
	 * Method to confirm all the changes made to caption and tags
	 * @param event Confirm button is pressed
	 * @throws IOException
	 */
	//confirm button to confirm edited caption, tags, and tag-types
	public void confirmButton(ActionEvent event) throws IOException {
		
		caption = editCaption.getText();
		p.setCaption(caption);
		
		//clear the current photo's tag list and replace it with the obsList
		p.getTags().clear();
		for(Tag t : obsList) {
			p.addTag(t);
		}
		
		writeApp(users);
					
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/OpenAlbum.fxml"));
		Parent root = loader.load();
		OpenAlbumController controller = loader.getController();
		controller.initData(users, userIndex, albumIndex);
		
		Scene openAlbumScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(openAlbumScene);
		window.setTitle("Photos");
		window.show();
	}
	
	/**
	 * Method to cancel all the changes made to caption and tags
	 * @param event Cancel button is pressed
	 * @throws Exception
	 */
	//cancel button to cancel edited caption, tags, and tag-types
	//go back to open album scene
	public void cancelButton(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/OpenAlbum.fxml"));
		Parent root = loader.load();
		OpenAlbumController controller = loader.getController();
		controller.initData(users, userIndex, albumIndex);
		
		Scene openAlbumScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(openAlbumScene);
		window.setTitle("Photos");
		window.show();
	}
	
	/**
	 * Method to send an error message in a pop up alert box if user input in invalid
	 */
	//error alert for invalid inputs
	public void errorMessage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Input");
		alert.setContentText("Please try again.");
		alert.showAndWait();
	}
	
	/**
	 * Method to send an error message in a pop up alert box if input is a duplicate
	 */
	//error alert for invalid inputs
	public void errorMessageDup() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Duplicate Input");
		alert.setContentText("Please try again.");
		alert.showAndWait();
	}
	
	/**
	 * String field to store directory
	 * String field to store file
	 */
	public static final String storeDir = "docs";
	public static final String storeFile = "users.ser"; 
	
	/**
	 * Method to serialize and write users to file
	 * @param usersList List of users
	 * @throws IOException
	 */
	public static void writeApp(List<User> usersList) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(usersList);
		oos.close();
	} 
}
