package control;

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
import model.User;
import javafx.scene.Parent;
import javafx.scene.Node;

public class LoginController {

	@FXML private Button loginButton;
	@FXML private TextField username;
	
	//first thing that happens when scene is loaded
	public void initialize() {
		
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
		if (username.getText().equals("admin")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDisplay.fxml"));
			Parent adminParent = loader.load();
			Scene adminScene = new Scene(adminParent,335, 530);
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
			window.setScene(adminScene);
			window.setTitle("Admin");
			window.show();
		}
		
		else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumDisplay.fxml"));
			Parent albumDisplayParent = loader.load();
			Scene albumDisplayScene = new Scene(albumDisplayParent,900,600);
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
			window.setScene(albumDisplayScene);
			window.setTitle("Album Display");
			window.show();
		}
	}
		
		
	

}
