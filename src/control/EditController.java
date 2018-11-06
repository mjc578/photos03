package control;

import java.awt.TextArea;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import photos.Tag;

public class EditController implements Serializable {

	@FXML private Button deleteTagButton;
	@FXML private Button addTagButton;
	@FXML private Button addTagTypeButton;
	@FXML private TextArea editCaption;
	@FXML private Button confirmButton;
	@FXML private Button cancelButton;
	@FXML private Button nextPhotoButton;
	@FXML private Button previousPhotoButton;
	@FXML private ListView<String> tagListView;
	
	private List<String> tagTypes = new ArrayList<String>();
	private List<Tag> tags = new ArrayList<Tag>();
	
	//first thing that happens when scene is loaded
	public void initialize() {
		tagTypes.add("Person");
		tagTypes.add("Location");
	}
	
	//delete button
	public void deleteTagButton(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Tag");
		alert.setContentText("Are you sure you want to delete this?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    //TODO: delete tag if ok is clicked
		} else {
		    //TODO: if cancel button is clicked (I don't think we have to put anything here but not sure rn)
		}
	}
	
	//add tag button
	public void addTagButton(ActionEvent event) {		
		//Choice Dialog to choose out of existing tag types
		ChoiceDialog<String> dialog = new ChoiceDialog<>(tagTypes.get(0), tagTypes);
		dialog.setTitle("New Tag");
		dialog.setHeaderText("Choose the tag-type for your new tag.");
		dialog.setContentText("Tag-Types:");
		Optional<String> result = dialog.showAndWait();
		
		//if okay is clicked
		if (result.isPresent()){
		    
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
				//TODO: if tag already exists, send error message
				//TODO: add tag to tag list
				else {
					Tag t = new Tag(result.get(), result2.get());
					
				}
			}
		}	
	}
	
	//add tag-type button
	public void addTagTypeButton(ActionEvent event) {
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
			//TODO: if tag already exists, send error message
			//TODO: add tag-type
			else {
				//adds tag type
				tagTypes.add(result.get());
			}
		}

	}
	
	//confirm button to confirm edited caption, tags, and tag-types
	//TODO: implement button and go back to open album scene
	public void confirmButton(ActionEvent event) {
		System.out.println("pie");
	}
	
	//cancel button to cancel edited caption, tags, and tag-types
	//go back to open album scene
	public void cancelButton(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/OpenAlbum.fxml"));
		Scene openAlbumScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(openAlbumScene);
		window.setTitle("Photos");
		window.show();
	}
	
	//error alert for invalid inputs
	public void errorMessage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Input");
		alert.setContentText("Please try again.");
		alert.showAndWait();
	}
	
}
