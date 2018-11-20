package control;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Photo;
import model.User;

public class SearchController {
	
	@FXML private Button createAlbumButton;
	@FXML private Button previousPhotoButton;
	@FXML private Button nextPhotoButton;
	@FXML private Button backButton;
	@FXML private ListView<Photo> listView;
	@FXML private Label dateTime;
	@FXML private Label caption;
	@FXML private Label tags;
	
	private ObservableList<Photo> obsList;
	
	private List<User> users;
	private int userIndex;
	private String searchQuery;
	
	//gets selected user and search query from the album display controller
	public void initData(List<User> user, int index, String query, String queryType) {
		//initialize the list of users and what user we are logged into
		users = user;
		userIndex = index;
		searchQuery = query;
		
		//process the query based on what type it happens to be
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
	
	//back button to go back to album display scene
	public void backButton(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/AlbumDisplay.fxml"));
		Scene albumDisplayScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(albumDisplayScene);
		window.setTitle("Album Display");
		window.show();
	}
	
	//create new album from search results button
	//text input dialogue pops up for user to enter name of new album created from search results
	public void createAlbumButton(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("New Album");
		dialog.setHeaderText("New Album");
		dialog.setContentText("Enter name for new album:");
		Optional<String> result = dialog.showAndWait();
		
		//if okay is clicked
		if (result.isPresent()){
			//if there is no user input, alert for invalid input
			if (result.get().equals("")) {
				errorMessage();
			}
			//TODO: if album name already exists, send error message
			//TODO: add album to list
			else {
				System.out.println("album name: " + result.get());
			}
		}
	}
	
	//Next Photo Button
	//TODO:implement button
	public void nextPhotoButton(ActionEvent event) {
		System.out.println("heck");
	}
	
	//Previous Photo Button
	//TODO: implement button
	public void previousPhotoButton(ActionEvent event) {
		System.out.println("ahh");
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
