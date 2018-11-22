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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.AlbumInfo;
import model.Photo;
import model.User;
import javafx.scene.Parent;
import javafx.scene.Node;

public class LoginController implements Serializable{

	@FXML private Button loginButton;
	@FXML private TextField username;
	
	/**
	 * User field for user
	 * List field for list of users
	 */
	private User user;
	private List<User> users;
	
	/**
	 * Method to initialize the scene
	 * @throws ClassNotFoundException catch exception
	 * @throws IOException catch exceptions
	 */
	//first thing that happens when scene is loaded
	public void initialize() throws ClassNotFoundException, IOException {
		
		users = readApp();
		if (users==null) {
			users = new ArrayList<User>();
			User stock = new User("stock");
			users.add(stock);
			AlbumInfo stockAlbum = new AlbumInfo("stock", 0, null, null);
			users.get(0).getUserAlbums().add(stockAlbum);
			setStockPhotos();
		}
		
		loginButton.setDisable(true);
		
		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (username.getText().trim().isEmpty()) {
		    	loginButton.setDisable(true);
		    }
		    else {
		    	loginButton.setDisable(false);
		    }
		});
	}

	/**
	 * Method to load album display scene or admin scene
	 * @param event event
	 * @throws Exception catch exceptions
	 */
	//Login Button 
	public void loginButton(ActionEvent event) throws Exception {
		//go to album display scene 
		//if username is admin
		if (username.getText().equals("admin")) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/AdminDisplay.fxml"));
			Parent adminParent = loader.load();
			AdminController controller = loader.getController();
			controller.initData(users);
			
			Scene adminScene = new Scene(adminParent,335, 530);
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
			window.setScene(adminScene);
			window.setTitle("Admin");
			window.show();
		}
		//usernames other than admin
		else {
			//if there are no users
			if (users.size()==0) {
				errorMessage();
			}
			//searches user list to see if user exists
			for (int i = 0; i < users.size(); i++) {
				//if user exists, go to album display
				if (username.getText().equals(users.get(i).getUsername())) {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation((getClass().getResource("/view/AlbumDisplay.fxml")));
					Parent albumDisplayParent = loader.load();
					AlbumDisplayController controller = loader.getController();
					controller.initData(users, i);
					
					Scene albumDisplayScene = new Scene(albumDisplayParent,900,600);
					Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
					window.setScene(albumDisplayScene);
					window.setTitle(users.get(i) + " Album Display");
					window.show();
					return;
				}
				//if user does not exist, send error message
				if (i==users.size()-1) {
					errorMessage();
				}
			}
		}
	}
	
	/**
	 * Method to send an error message in a pop up alert box if user does not exit
	 */
	public void errorMessage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("User does not exist.");
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
	 * Method to serialize and read users from file
	 * @return ArrayList of users
	 * @throws IOException catch exceptions
	 * @throws ClassNotFoundException catch exceptions
	 */
	public static ArrayList<User> readApp() throws IOException, ClassNotFoundException {
		
		BufferedReader br = new BufferedReader(new FileReader("docs/users.ser"));     
		if (br.readLine() == null) {
			br.close();
		    return null;
		}
		ObjectInputStream ois = new ObjectInputStream(
		new FileInputStream(storeDir + File.separator + storeFile));
		ArrayList<User> user = (ArrayList<User>) ois.readObject();
		br.close();
		ois.close();
		return user;
	}
	
	/**
	 * Method to set stock photos in stock user in stock album
	 */

	public void setStockPhotos() {
  		Photo s1 = new Photo("stock1", null, null);
		File file = new File(".\\src\\stockPhotos\\binary.jpg");
		s1.setURL(file.getAbsolutePath());
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(file.lastModified()));
		c.set(Calendar.MILLISECOND, 0);
		s1.setDate(c);
		users.get(0).getUserAlbums().get(0).addPhoto(s1);
		
		Photo s2 = new Photo("stock2", null, null);
		File file2 = new File(".\\src\\stockPhotos\\binary2.jpg");
		s2.setURL(file2.getAbsolutePath());
		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date(file.lastModified()));
		c2.set(Calendar.MILLISECOND, 0);
		s2.setDate(c2);
		users.get(0).getUserAlbums().get(0).addPhoto(s2);
		
		Photo s3 = new Photo("stock3", null, null);
		File file3 = new File(".\\src\\stockPhotos\\button.jpg");
		s3.setURL(file3.getAbsolutePath());
		Calendar c3 = Calendar.getInstance();
		c3.setTime(new Date(file.lastModified()));
		c3.set(Calendar.MILLISECOND, 0);
		s3.setDate(c3);
		users.get(0).getUserAlbums().get(0).addPhoto(s3);
		
		Photo s4 = new Photo("stock4", null, null);
		File file4 = new File(".\\src\\stockPhotos\\code.jpg");
		s4.setURL(file4.getAbsolutePath());
		Calendar c4 = Calendar.getInstance();
		c4.setTime(new Date(file.lastModified()));
		c4.set(Calendar.MILLISECOND, 0);
		s4.setDate(c4);
		users.get(0).getUserAlbums().get(0).addPhoto(s4);
		
		Photo s5 = new Photo("stock5", null, null);
		File file5 = new File(".\\src\\stockPhotos\\robot.jpg");
		s5.setURL(file5.getAbsolutePath());
		Calendar c5 = Calendar.getInstance();
		c5.setTime(new Date(file.lastModified()));
		c5.set(Calendar.MILLISECOND, 0);
		s5.setDate(c5);
		users.get(0).getUserAlbums().get(0).addPhoto(s5);
		
		
		users.get(0).getUserAlbums().get(0).setNumPhotos(users.get(0).getUserAlbums().get(0).getPhotos().size());

		Calendar earliestPic = Calendar.getInstance();
		Calendar latestPic = Calendar.getInstance();
		
		earliestPic.setTime(users.get(0).getUserAlbums().get(0).getPhotos().get(0).getDate().getTime());
		latestPic.setTime(users.get(0).getUserAlbums().get(0).getPhotos().get(0).getDate().getTime());
		for(int i = 0; i < users.get(0).getUserAlbums().get(0).getPhotos().size(); i++) {
			if (users.get(0).getUserAlbums().get(0).getPhotos().get(i).getDate().getTime().compareTo(earliestPic.getTime())<=0) {
				earliestPic.setTime(users.get(0).getUserAlbums().get(0).getPhotos().get(i).getDate().getTime());
				String earliestDate = date(earliestPic);
				users.get(0).getUserAlbums().get(0).setStartDateRange(earliestDate);
			}
			if (users.get(0).getUserAlbums().get(0).getPhotos().get(i).getDate().getTime().compareTo(latestPic.getTime())>=0) {
				latestPic.setTime(users.get(0).getUserAlbums().get(0).getPhotos().get(i).getDate().getTime());
				String latestDate = date(latestPic);
				users.get(0).getUserAlbums().get(0).setEndDateRange(latestDate);
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
	

	
	
	
		
	

}
