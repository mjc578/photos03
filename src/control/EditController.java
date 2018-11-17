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
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.AlbumInfo;
import model.Photo;
import model.Tag;
import model.User;

public class EditController implements Serializable {

	@FXML private Button deleteTagButton;
	@FXML private Button addTagButton;
	@FXML private Button addTagTypeButton;
	@FXML private TextArea editCaption;
	@FXML private Button confirmButton;
	@FXML private Button cancelButton;
	@FXML private Button nextPhotoButton;
	@FXML private Button previousPhotoButton;
	@FXML private ListView<Tag> editTagListView;
	
	public static final String storeDir = "docs";
	public static final String storeFile = "myTags.ser"; 
	
	//all the stuff user can edit and may want to save
	
	//the text of the caption previously there
	private String caption;
	//the photo that was clicked on and is being edited
	private Photo photoClicked = new Photo("deepel", "bobby", " ");
	//probably gonna wanna load this and serialize
	private List<String> tagTypes;
	//the edit tag list
	private ObservableList<Tag> obsList;
	
	private List<User> users;
	private int userIndex;
	private List<AlbumInfo> albums;
	private int albumIndex;
	private int photoIndex;
	
	
	public void initData(List<User> user, int index, int index2, int index3) {
		users = user;
		userIndex = index;
		albumIndex = index2;
		photoIndex = index3;
	} 
	
	public void initialize() throws ClassNotFoundException, IOException{
		// TODO Auto-generated method stub
		tagTypes = readApp();
		if(tagTypes == null) {
			tagTypes = new ArrayList<String>();
		}
		photoClicked.addTag(new Tag("Person", "Susan"));
		photoClicked.addTag(new Tag("Person", "Sesh"));
		photoClicked.addTag(new Tag("Location", "Bikini Bottom"));
		obsList = FXCollections.observableArrayList(photoClicked.getTags());
		//sets the edit caption text with the caption of the ohoto that was clicked
		editCaption.setText(photoClicked.getCaption());
		editTagListView.setItems(obsList);
	}
	
	
	//delete button
	public void deleteTagButton(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Tag");
		alert.setContentText("Are you sure you want to delete this?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    obsList.remove(editTagListView.getSelectionModel().getSelectedItem());
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
				//adds tag to obsList unless it is a duplicate
				else {
					Tag t = new Tag(result.get(), result2.get());
					
					//duplicate tag
					if(obsList.contains(t)) {
						errorMessageDup();
						return;
					}
					
					obsList.add(t);
				}
			}
		}	
	}
	
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
			//if tag type already exists, give an error
			else if(tagTypes.contains(result.get())) {
				errorMessageDup();
			}
			else {
				//adds tag type
				tagTypes.add(result.get());
			}
		}

	}
	
	//confirm button to confirm edited caption, tags, and tag-types
	public void confirmButton(ActionEvent event) throws IOException {
		//gets rid  of of the photo of interests tag list and replaces it with the obs list one
		photoClicked.getTags().clear();
		for(Tag t : obsList) {
			photoClicked.addTag(t);
		}
		
		//TEMPORARY: prints out the tag list of photo licked, since we cant see it yet
		for(Tag t : photoClicked.getTags()) {
			System.out.println(t);
		}
		//replace the photo clicked caption with the one that may (or not) have been entered, doesn't really matter
		
		//TODO: this should get the set caption of the picture, not this temp picture's caption...
		//ACTUALLY maybe not cause we are just gonna load that caption from the photo anyway if they cancel/confirm
		caption = editCaption.getText();
		photoClicked.setCaption(caption);
		
		writeApp(tagTypes);
					
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
	
	//error alert for invalid inputs
	public void errorMessage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Input");
		alert.setContentText("Please try again.");
		alert.showAndWait();
	}
	
	//error alert for invalid inputs
	public void errorMessageDup() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Duplicate Input");
		alert.setContentText("Please try again.");
		alert.showAndWait();
	}
	
	public static void writeApp(List<String> tagApp) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(tagApp);
	} 

	public static ArrayList<String> readApp() throws IOException, ClassNotFoundException {
		
		BufferedReader br = new BufferedReader(new FileReader("docs/myTags.ser"));     
		if (br.readLine() == null) {
			br.close();
		    return null;
		}
		ObjectInputStream ois = new ObjectInputStream(
		new FileInputStream(storeDir + File.separator + storeFile));
		ArrayList<String> tagType = (ArrayList<String>) ois.readObject();
		br.close();
		ois.close();
		return tagType;
	}



	
}
