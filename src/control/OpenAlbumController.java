package control;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.AlbumInfo;
import model.Photo;
import model.User;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

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
	@FXML private ImageView clickedImageView;
	@FXML private ListView<Photo> listView;
	private ObservableList<Photo> obsList;
	private ArrayList<Photo> arrayList;
	
	FileChooser fileChooser;
    
    private List<User> users;
    private List<AlbumInfo> albums;
    private int albumIndex;
    private int userIndex;
    
  //gets selected album and user from albumDisplayController
  	public void initData(List<User> user, int index, List<AlbumInfo> album, int index2) {
  		users = user;
  		albums = album;
  		userIndex = index;
  		albumIndex = index2;
  		System.out.println(users);
  		System.out.println(albums);
  		System.out.println(userIndex);
  		System.out.println(albumIndex);
  	}
	
	//first thing that happens when scene is loaded
	public void initialize() {
		fileChooser = new FileChooser();
		
		arrayList = new ArrayList<Photo>();
		
		obsList = FXCollections.observableArrayList(arrayList);
		listView.setItems(obsList);
        listView.getSelectionModel().select(0);    
        if (obsList != null && !obsList.isEmpty()) {
			showPhotoDetails();
		}
	        
        if (obsList.isEmpty() && obsList != null) {
        	disable(true);
        }
        
        //listener
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        	if(newVal != null) {
        		showPhotoDetails();
        	}
        });
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
                	for (int i=0; i<arrayList.size(); i++) {
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
	//TODO: implement add button
	public void addButton(ActionEvent event) throws MalformedURLException{
		Photo p = new Photo(null, null, null);
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(null);
		p.setURL(file.getAbsolutePath());
		Image imageForFile = new Image("file:" + p.getURL());
		System.out.println("file:" + p.getURL());
		clickedImageView.setImage(imageForFile);
		
		//add caption after photo is added
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Caption");
		dialog.setHeaderText("Caption");
		dialog.setContentText("Enter caption for photo:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			p.setCaption(result.get());
		}

		obsList.add(p);
		arrayList.add(p);
		listCellFactory();
		
		listView.getSelectionModel().select(p);
		disable(false);
		
	}
	
	//delete button - deletes selected photo
	public void deleteButton(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Photo");
		alert.setContentText("Are you sure you want to delete this?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		   int index = listView.getSelectionModel().getSelectedIndex();
		   obsList.remove(index);
		   arrayList.remove(index); 
		   
		   if (obsList.isEmpty() && obsList != null) {
				disable(true);
			}
		} 
	}
	
	//copy button
	public void copyButton(ActionEvent event){
		//list of choices (albums)
		List<String> choices = new ArrayList<>();
		//TODO: load album names into array
		choices.add("a");
		choices.add("b");
		choices.add("c");

		ChoiceDialog<String> dialog = new ChoiceDialog<>("b", choices);
		dialog.setTitle("Copy");
		dialog.setHeaderText("Copy Photo");
		dialog.setContentText("Choose which album you want to copy this photo to:");

		Optional<String> result = dialog.showAndWait();
		//if okay is clicked
		//TODO: check if selected album is current album, else give an error. Or do not include current album in list of choices.
		if (result.isPresent()){
			//TODO: copy photo to selected album
		    System.out.println("Your choice: " + result.get());
		}
	}
	
	//move button
	public void moveButton(ActionEvent event){
		//list of choices (albums)
		List<String> choices = new ArrayList<>();
		//TODO: load album names into array
		choices.add("a");
		choices.add("b");
		choices.add("c");

		ChoiceDialog<String> dialog = new ChoiceDialog<>("b", choices);
		dialog.setTitle("Move");
		dialog.setHeaderText("Move Photo");
		dialog.setContentText("Choose which album you want to move this photo to:");

		Optional<String> result = dialog.showAndWait();
		//if okay is clicked
		//TODO: check if selected album is current album, else give an error. Or do not include current album in list of choices.
		if (result.isPresent()){
			//TODO: move photo from current album to selected album
		    System.out.println("Your choice: " + result.get());
		}
	}
	
	// "<" button
	//TODO: implement button
	public void previousPhotoButton(ActionEvent event){
		int index = listView.getSelectionModel().getSelectedIndex() - 1;
		listView.getSelectionModel().select(index);
	}
	
	// ">" button
	//TODO: implement button
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
		controller.initData(users, userIndex, albums, albumIndex);
		
		Scene EditScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(EditScene);
		window.setTitle("Edit");
		window.show();
	}
	
	public void showPhotoDetails(){
		dateTime.setText(listView.getSelectionModel().getSelectedItem().getDate());
		caption.setText(listView.getSelectionModel().getSelectedItem().getCaption());
		clickedImageView.setImage(new Image("file:" + listView.getSelectionModel().getSelectedItem().getURL()));
		
		if (listView.getSelectionModel().getSelectedIndex() == 0) {
			previousPhotoButton.setDisable(true);
		}
		else { previousPhotoButton.setDisable(false);}
		if (listView.getSelectionModel().getSelectedIndex() == obsList.size()-1) {
			nextPhotoButton.setDisable(true);
		}
		else { nextPhotoButton.setDisable(false);}
	
	}
	
	public void disable(boolean tf){
		editButton.setDisable(tf);
		deleteButton.setDisable(tf);
		copyButton.setDisable(tf);
		moveButton.setDisable(tf);	
	}
	
}
