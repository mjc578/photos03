package control;

import java.util.Optional;

//import application.AlbumInfo; //TODO: make AlbumInfo class to get name, # of photos, and date range
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AlbumDisplayController {

	@FXML private Button openAlbumButton;
	@FXML private Button newAlbumButton;
	@FXML private Button renameAlbumButton;
	@FXML private Button deleteAlbumButton;
	@FXML private Button searchButton;
	@FXML private Button logOutButton;
	@FXML private TextField searchBar;
	
	//@FXML ListView<AlbumInfo> listView;
	//private ObservableList<AlbumInfo> obsList;
	
	//log out button takes you back to login scene
	public void logOutButton(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
		Scene albumDisplayScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(albumDisplayScene);
		window.setTitle("Photos Login");
		window.show();
	}
	
	//open album button
	//open album button to go to open album scene
	public void openAlbumButton(ActionEvent event) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("/view/OpenAlbum.fxml"));
		Scene openAlbumScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(openAlbumScene);
		window.setTitle("Photos");
		window.show();
	}
	
	//search button
	//go to search scene if there is user input in search bar
	public void searchButton(ActionEvent event) throws Exception {
		//checks if there is text in search bar
		//alert dialogue if no user input was entered
		if (searchBar.getText().equals("")) {
			errorMessage();
		}
		//go to search scene
		else {
			Parent root = FXMLLoader.load(getClass().getResource("/view/Search.fxml"));
			Scene searchScene = new Scene(root);
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
			window.setScene(searchScene);
			window.setTitle("Search");
			window.show();
		}
	}
	
	//new album button
	//text input dialogue pops up for user to enter name of new album
	public void newAlbumButton(ActionEvent event){
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
	
	//rename album button
	//text input dialogue pops up for user to enter new name of album
	public void renameAlbumButton(ActionEvent event){
		//TODO: get album name and display it in text box initially
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Rename Album");
		dialog.setHeaderText("Rename Album");
		dialog.setContentText("Enter new name for album:");
		Optional<String> result = dialog.showAndWait();
		
		//if okay is clicked
		if (result.isPresent()){
			if (result.get().equals("")) {
				errorMessage();
			}
			//TODO: if album name already exists, send error message
			//TODO: rename album in the list
			else{
				System.out.println("album name: " + result.get());
			}
		}
	}
	
	//delete album button - deletes selected album
	public void deleteAlbumButton(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Album");
		alert.setContentText("Are you sure you want to delete this?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    //TODO: delete album of ok is clicked
		} else {
		    //TODO: if cancel button is clicked (I don't think we have to put anything here but not sure rn)
		}
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
