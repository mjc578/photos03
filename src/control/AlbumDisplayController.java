package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import model.AlbumInfo;
import model.User;

public class AlbumDisplayController implements Serializable {

	@FXML private Button openAlbumButton;
	@FXML private Button newAlbumButton;
	@FXML private Button renameAlbumButton;
	@FXML private Button deleteAlbumButton;
	@FXML private Button searchButton;
	@FXML private Button logOutButton;
	@FXML private TextField searchBar;
	@FXML private ListView<AlbumInfo> albumList;
	
	private AlbumInfo albumInfo;
	private List<AlbumInfo> albums;
	private ObservableList<AlbumInfo> obsList;
	
	private List<User> users;
	private int userIndex;
	
	//gets selected user from loginController
	public void initData(List<User> user, int index) {
		users = user;
		userIndex = index;
		albums = users.get(userIndex).getUserAlbums();
		System.out.println(users);
		System.out.println(userIndex);
	}
	
	//first thing that happens when scene is loaded (must extend Application in class)
	public void initialize() throws ClassNotFoundException, IOException {
				
		if(albums == null) {
			albums = new ArrayList<AlbumInfo>();
			obsList = FXCollections.observableArrayList(albums);
			albumList.setItems(obsList);
			//set stock photos first time scene is loaded
			
			
			/** this is only for stock user this is adding it for every user
			albumInfo = new AlbumInfo("Stock", 5, null, null);
			obsList.add(albumInfo);
			albums.add(albumInfo);
			
			**/
		}
		//executes if this is not the first time loading scene so that Stock album is not created again
		else {
			obsList = FXCollections.observableArrayList(albums);
			albumList.setItems(obsList);
		}

		if (obsList.isEmpty() && obsList != null) {
			disable();
		}
		
		albumList.getSelectionModel().select(0);
	
	}

	//log out button takes you back to login scene
	public void logOutButton(ActionEvent event) throws Exception {
		writeApp(albums);
		Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
		Scene loginScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(loginScene);
		window.setTitle("Photos Login");
		window.show();
	}
	
	//open album button
	//open album button to go to open album scene
	public void openAlbumButton(ActionEvent event) throws Exception{
		writeApp(albums);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/OpenAlbum.fxml"));
		Parent root = loader.load();
		OpenAlbumController controller = loader.getController();
		controller.initData(users, userIndex, albums, albumList.getSelectionModel().getSelectedIndex());
		
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
			else {
				AlbumInfo albumInfo = new AlbumInfo((String)result.get(), 0, null, null);
				//error message if album name already exists
				for(int i=0; i<obsList.size(); i++) {
					if(obsList.get(i).getName().toLowerCase().equals(albumInfo.getName().toLowerCase())) {
						errorMessage();
						return;
					}
				}
				//adds album to list
				obsList.add(albumInfo);	
				albums.add(albumInfo);
				albumList.getSelectionModel().select(albumInfo);
				able();
				System.out.println(albums);
			}
		}
	}
	
	//rename album button
	//text input dialogue pops up for user to enter new name of album
	public void renameAlbumButton(ActionEvent event){
		//TODO: get album name and display it in text box initially
		String currentName = albumList.getSelectionModel().getSelectedItem().getName();
		
		TextInputDialog dialog = new TextInputDialog(currentName);
		dialog.setTitle("Rename Album");
		dialog.setHeaderText("Rename Album");
		dialog.setContentText("Enter new name for album:");
		Optional<String> result = dialog.showAndWait();
		
		//if okay is clicked
		if (result.isPresent()){
			//if input is empty
			if (result.get().equals("")) {
				errorMessage();
			}
			else {
				//checks if it is duplicate name
				for (int i =0; i<obsList.size(); i++) {
					if (obsList.get(i).getName().toLowerCase().equals(result.get().toLowerCase())) {
						errorMessage();
						return;
					}
				}
				albumInfo = albumList.getSelectionModel().getSelectedItem();
				int index = albumList.getSelectionModel().getSelectedIndex();
				albumInfo.setName(result.get());
				obsList.set(index, albumInfo);
				albums.set(index, albumInfo);
				albumList.getSelectionModel().select(index);
				System.out.println(albums);
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
			int index = albumList.getSelectionModel().getSelectedIndex(); 
			obsList.remove(index);
			albums.remove(index);
			if (obsList.isEmpty() && obsList != null) {
				disable();
			}
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
	
	//disable buttons when list is empty
	public void disable() {
		renameAlbumButton.setDisable(true);
		deleteAlbumButton.setDisable(true);
		deleteAlbumButton.setDisable(true);
		openAlbumButton.setDisable(true);
	}
	
	//able buttons when list is not empty anymore
	public void able() {
		renameAlbumButton.setDisable(false);
		deleteAlbumButton.setDisable(false);
		deleteAlbumButton.setDisable(false);
		openAlbumButton.setDisable(false);
	}
	
	public static final String storeDir = "docs";
	public static final String storeFile = "albums.ser"; 
	
	public static void writeApp(List<AlbumInfo> albums) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(albums);
	} 
}
