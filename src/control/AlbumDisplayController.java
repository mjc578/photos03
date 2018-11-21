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
import java.util.Calendar;
import java.util.Date;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.AlbumInfo;
import model.Photo;
import model.User;

public class AlbumDisplayController implements Serializable {

	@FXML private Button openAlbumButton;
	@FXML private Button newAlbumButton;
	@FXML private Button renameAlbumButton;
	@FXML private Button deleteAlbumButton;
	@FXML private Button searchButton;
	@FXML private Button logOutButton;
	@FXML private RadioButton tagRadioButton;
	@FXML private RadioButton dateRadioButton;
	@FXML private TextField searchBar;
	@FXML private ListView<AlbumInfo> listView;
	
	//group for the radio buttons
	final ToggleGroup group = new ToggleGroup();
	
	/**
	 * AlbumInfo field for album
	 * Observable list of albums
	 * List of users
	 * int field for index of user that logged in
	 */
	private AlbumInfo albumInfo;
	private ObservableList<AlbumInfo> obsList;
	private List<User> users;
	private int userIndex;
	
	/**
	 * Method to get list of users and index of user that is logged in
	 * @param user List of users
	 * @param index Index of user that is logged in
	 */
	//gets selected user from loginController
	public void initData(List<User> user, int index) {
		//initialize the list of users and what user we are logged into
		users = user;
		userIndex = index;
		//set the obslist on list of albums and set the list view
		obsList = FXCollections.observableArrayList(users.get(userIndex).getUserAlbums());
		listView.setItems(obsList);
		
		if(users.get(userIndex).getUsername().equals("stock") && obsList.size()==0) {
			AlbumInfo stock = new AlbumInfo("stock", 0, null, null);
			obsList.add(stock);
			users.get(userIndex).getUserAlbums().add(stock);
			setStockPhotos();
		}
		
		if (obsList.isEmpty() && obsList != null) {
			disable();
		}
		
		listView.getSelectionModel().select(0);
		searchBar.setPromptText("person=sesh || person=sesh OR/AND location=prague");
		
		//set the radiobuttons
		tagRadioButton.setToggleGroup(group);
		tagRadioButton.setUserData("Tag");
		tagRadioButton.setSelected(true);

		dateRadioButton.setToggleGroup(group);
		dateRadioButton.setUserData("Date");
		
		group.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
			if(newVal.getUserData().equals("Tag")) {
				searchBar.setPromptText("person=sesh || person=sesh OR/AND location=prague");
			}
			else {
				searchBar.setPromptText("Format: MM-DD-YYYY to MM-DD-YYYY");
			}
		});
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		    public void run() {
		        try {
					writeApp(users);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}));
	}

	/**
	 * Method to load login scene
	 * @param event Logout Button is pressed
	 * @throws Exception
	 */
	public void logOutButton(ActionEvent event) throws Exception {
		writeApp(users);
		Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
		Scene loginScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(loginScene);
		window.setTitle("Photos Login");
		window.show();
	}
	
	/**
	 * Method to load open album scene
	 * @param event Open Album Button is pressed
	 * @throws Exception
	 */
	public void openAlbumButton(ActionEvent event) throws Exception{
		writeApp(users);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/OpenAlbum.fxml"));
		Parent root = loader.load();
		OpenAlbumController controller = loader.getController();
		controller.initData(users, userIndex, listView.getSelectionModel().getSelectedIndex());
		
		Scene openAlbumScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(openAlbumScene);
		window.setTitle("Photos");
		window.show();
	}
	
	/**
	 * Method to load search scene
	 * @param event
	 * @throws Exception
	 */
	public void searchButton(ActionEvent event) throws Exception {
		//checks if there is text in search bar
		//alert dialogue if no user input was entered
		writeApp(users);
		if (searchBar.getText().equals("")) {
			errorMessage();
			return;
		}
		if (!tagRadioButton.isSelected() && !dateRadioButton.isSelected()) {
			errorMessage();
		}
		if (tagRadioButton.isSelected()) {
			String args[] = searchBar.getText().split(" ");

			if(args.length == 3) {
				if (!args[1].equals("AND") && !args[1].equals("OR")) {
					errorMessage();
				}	
				else {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/view/Search.fxml"));
					Parent root = loader.load();
					SearchController controller = loader.getController();
					if(tagRadioButton.isSelected()) {
						controller.initData(users, userIndex, searchBar.getText(), "tagQuery");
					}
					else {
						controller.initData(users, userIndex, searchBar.getText(), "dateQuery");
					}
					Scene searchScene = new Scene(root);
					Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
					window.setScene(searchScene);
					window.setTitle("Search");
					window.show();
					
				}
			}
			else if (args.length>1) {
				errorMessage();
			}
			else {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/Search.fxml"));
				Parent root = loader.load();
				SearchController controller = loader.getController();
				if(tagRadioButton.isSelected()) {
					controller.initData(users, userIndex, searchBar.getText(), "tagQuery");
				}
				else {
					controller.initData(users, userIndex, searchBar.getText(), "dateQuery");
				}
				Scene searchScene = new Scene(root);
				Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
				window.setScene(searchScene);
				window.setTitle("Search");
				window.show();
			}
			
		}
	}
	
	/**
	 * Method to create new album
	 * @param event New Album Button is pressed
	 * @throws IOException
	 */
	//text input dialogue pops up for user to enter name of new album
	public void newAlbumButton(ActionEvent event) throws IOException{
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
				AlbumInfo albumInfo = new AlbumInfo((String)result.get(), 0, "*", "*");
				//error message if album name already exists
				for(int i=0; i<obsList.size(); i++) {
					if(obsList.get(i).getName().toLowerCase().equals(albumInfo.getName().toLowerCase())) {
						errorMessage();
						return;
					}
				}
				//adds album to list
				obsList.add(albumInfo);	
				users.get(userIndex).addToAlbums(albumInfo);
				listView.getSelectionModel().select(albumInfo);
				able();
			}
		}
		writeApp(users);
	}
	
	/**
	 * Method to rename album that already exists
	 * @param event Rename Album Button is presses
	 */
	//text input dialogue pops up for user to enter new name of album
	public void renameAlbumButton(ActionEvent event){
		//TODO: get album name and display it in text box initially
		String currentName = listView.getSelectionModel().getSelectedItem().getName();
		
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
				albumInfo = listView.getSelectionModel().getSelectedItem();
				int index = listView.getSelectionModel().getSelectedIndex();
				albumInfo.setName(result.get());
				obsList.set(index, albumInfo);
				users.get(userIndex).getUserAlbums().set(index, albumInfo);
				listView.getSelectionModel().select(index);
			}		
		}
	}
	
	/**
	 * Method to delete selected album
	 * @param event Delete Album Button is presses
	 */
	//delete album button - deletes selected album
	public void deleteAlbumButton(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Album");
		alert.setContentText("Are you sure you want to delete this?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			int index = listView.getSelectionModel().getSelectedIndex(); 
			obsList.remove(index);
			users.get(userIndex).removeFromAlbums(index);
			if (obsList.isEmpty() && obsList != null) {
				disable();
			}
		} 
	}

	/**
	 * Method to send an error message in a pop up alert box if input in invalid
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
	 * Method to disable buttons
	 */
	//disable buttons when list is empty
	public void disable() {
		renameAlbumButton.setDisable(true);
		deleteAlbumButton.setDisable(true);
		deleteAlbumButton.setDisable(true);
		openAlbumButton.setDisable(true);
	}
	
	/*
	 * Method to set buttons to be able to be clicked
	 */
	//able buttons when list is not empty anymore
	public void able() {
		renameAlbumButton.setDisable(false);
		deleteAlbumButton.setDisable(false);
		deleteAlbumButton.setDisable(false);
		openAlbumButton.setDisable(false);
	}
	
	/**
	 * Method to set stock photos in stock user in stock album
	 */
	public void setStockPhotos() {
  		Photo s1 = new Photo("stock1", null, null);
		File file = new File("C:\\Users\\kmist\\eclipse-workspace\\photos03\\src\\stockPhotos\\binary.jpg");
		s1.setURL(file.getAbsolutePath());
		Image imageForFile = new Image("file:" + s1.getURL());
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(file.lastModified()));
		c.set(Calendar.MILLISECOND, 0);
		s1.setDate(c);
		users.get(userIndex).getUserAlbums().get(0).addPhoto(s1);
		
		Photo s2 = new Photo("stock2", null, null);
		File file2 = new File("C:\\Users\\kmist\\eclipse-workspace\\photos03\\src\\stockPhotos\\binary2.jpg");
		s2.setURL(file2.getAbsolutePath());
		Image imageForFile2 = new Image("file:" + s2.getURL());
		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date(file.lastModified()));
		c2.set(Calendar.MILLISECOND, 0);
		s2.setDate(c2);
		users.get(userIndex).getUserAlbums().get(0).addPhoto(s2);
		
		Photo s3 = new Photo("stock3", null, null);
		File file3 = new File("C:\\Users\\kmist\\eclipse-workspace\\photos03\\src\\stockPhotos\\button.jpg");
		s3.setURL(file3.getAbsolutePath());
		Image imageForFile3 = new Image("file:" + s3.getURL());
		Calendar c3 = Calendar.getInstance();
		c3.setTime(new Date(file.lastModified()));
		c3.set(Calendar.MILLISECOND, 0);
		s3.setDate(c3);
		users.get(userIndex).getUserAlbums().get(0).addPhoto(s3);
		
		Photo s4 = new Photo("stock4", null, null);
		File file4 = new File("C:\\Users\\kmist\\eclipse-workspace\\photos03\\src\\stockPhotos\\code.jpg");
		s4.setURL(file4.getAbsolutePath());
		Image imageForFile4 = new Image("file:" + s4.getURL());
		Calendar c4 = Calendar.getInstance();
		c4.setTime(new Date(file.lastModified()));
		c4.set(Calendar.MILLISECOND, 0);
		s4.setDate(c4);
		users.get(userIndex).getUserAlbums().get(0).addPhoto(s4);
		
		Photo s5 = new Photo("stock5", null, null);
		File file5 = new File("C:\\Users\\kmist\\eclipse-workspace\\photos03\\src\\stockPhotos\\robot.jpg");
		s5.setURL(file5.getAbsolutePath());
		Image imageForFile5 = new Image("file:" + s5.getURL());
		Calendar c5 = Calendar.getInstance();
		c5.setTime(new Date(file.lastModified()));
		c5.set(Calendar.MILLISECOND, 0);
		s5.setDate(c5);
		users.get(userIndex).getUserAlbums().get(0).addPhoto(s5);
		
		
		users.get(userIndex).getUserAlbums().get(0).setNumPhotos(users.get(userIndex).getUserAlbums().get(0).getPhotos().size());

		Calendar earliestPic = Calendar.getInstance();
		Calendar latestPic = Calendar.getInstance();
		
		earliestPic.setTime(users.get(userIndex).getUserAlbums().get(0).getPhotos().get(0).getDate().getTime());
		latestPic.setTime(users.get(userIndex).getUserAlbums().get(0).getPhotos().get(0).getDate().getTime());
		for(int i = 0; i < users.get(userIndex).getUserAlbums().get(0).getPhotos().size(); i++) {
			if (users.get(userIndex).getUserAlbums().get(0).getPhotos().get(i).getDate().getTime().compareTo(earliestPic.getTime())<=0) {
				earliestPic.setTime(users.get(userIndex).getUserAlbums().get(0).getPhotos().get(i).getDate().getTime());
				String earliestDate = date(earliestPic);
				users.get(userIndex).getUserAlbums().get(0).setStartDateRange(earliestDate);
			}
			if (users.get(userIndex).getUserAlbums().get(0).getPhotos().get(i).getDate().getTime().compareTo(latestPic.getTime())>=0) {
				latestPic.setTime(users.get(userIndex).getUserAlbums().get(0).getPhotos().get(i).getDate().getTime());
				String latestDate = date(latestPic);
				users.get(userIndex).getUserAlbums().get(0).setEndDateRange(latestDate);
			}
		}
			
  	}
	
	/**
	 * Method to convert Calendar date into a string
	 * @param c Date of photo
	 * @return String format of the date
	 */
	public String date(Calendar c) {
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		return month + "-" + day + "-" + year;
	}
	
	/**
	 * String field to store directory
	 * String field to store file
	 */
	public static final String storeDir = "docs";
	public static final String storeFile = "users.ser"; 
	
	/**
	 * Method to serialize and write users to file
	 * @param users List of users
	 * @throws IOException
	 */
	public static void writeApp(List<User> users) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(users);
	} 
}
