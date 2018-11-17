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
import model.User;
import javafx.scene.Parent;
import javafx.scene.Node;

public class LoginController implements Serializable{

	@FXML private Button loginButton;
	@FXML private TextField username;
	
	private User user;
	private List<User> users;
	
	//first thing that happens when scene is loaded
	public void initialize() throws ClassNotFoundException, IOException {
		
		users = readApp();
		if (users==null) {
			users = new ArrayList<User>();
			User stock = new User("stock");
			users.add(stock);
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

	//Login Button 
	public void loginButton(ActionEvent event) throws Exception {
		//go to album display scene 
		//TODO: store username and user info somehow
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
	
	public void errorMessage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("User does not exist.");
		alert.setContentText("Please try again.");
		alert.showAndWait();
	}
	
	public static final String storeDir = "docs";
	public static final String storeFile = "users.ser"; 
	
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

	
	
	
		
	

}
