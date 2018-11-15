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
	@FXML private Label dataTime;
	@FXML private Label caption;
	@FXML private Label albumName;
	@FXML private ImageView clickedImageView;
	@FXML private ListView<String> listView;
	private ObservableList<String> obsList;
	
	FileChooser fileChooser;
	
	private final Image stock1  = new Image("stockPhotos/cactus.jpg");
    private final Image stock2  = new Image("stockPhotos/noose book.jpg");
    private final Image stock3  = new Image("stockPhotos/noose hourglass.jpg");
    private final Image stock4 = new Image("stockPhotos/noose part 2.jpg");
    private final Image stock5 = new Image("stockPhotos/utensils.jpg");
    private Image[] listOfImages = {stock1, stock2, stock3, stock4, stock5};
    
    
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
		
        obsList =FXCollections.observableArrayList (
                "stock1", "stock2", "stock3", "stock4", "stock5");
        listView.setItems(obsList);
       
       
        listView.setCellFactory(param -> new ListCell<String>() {
            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                ImageView imageView = new ImageView();
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if(name.equals("stock1"))
                        imageView.setImage(listOfImages[0]);
                    else if(name.equals("stock2"))
                        imageView.setImage(listOfImages[1]);
                    else if(name.equals("stock3"))
                        imageView.setImage(listOfImages[2]);
                    else if(name.equals("stock4"))
                        imageView.setImage(listOfImages[3]);
                    else if(name.equals("stock5"))
                        imageView.setImage(listOfImages[4]);
                    setText(name);
                    setGraphic(imageView);
                }
            }
        });
        
        listView.getSelectionModel().select(0);     
        
        
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
		Photo p = new Photo(" ", " ");
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(null);
		p.setURL(file.getAbsolutePath());
		Image imageForFile = new Image("file:" + p.getURL());
		System.out.println(p.getURL());
		clickedImageView.setImage(imageForFile);
	}
	
	//delete button - deletes selected photo
	public void deleteButton(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Photo");
		alert.setContentText("Are you sure you want to delete this?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    //TODO: delete photo if ok is clicked
		} else {
		    //TODO: if cancel button is clicked (I don't think we have to put anything here but not sure rn)
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
		System.out.println("6");	
	}
	
	// ">" button
	//TODO: implement button
	public void nextPhotoButton(ActionEvent event){
		System.out.println("7");
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
}
