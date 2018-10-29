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
import javafx.scene.Parent;
import javafx.scene.Node;

public class LoginController {

	@FXML private Button loginButton;
	@FXML private TextField username;
	
	//Login Button 
	public void loginButton(ActionEvent event) throws Exception {
		//checks if username was inputed
		//alert dialogue if no username was entered
		if (username.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid Username");
			alert.setContentText("Please try again.");
			alert.showAndWait();
		}
		//go to album display scene 
		else {
			//TODO: store username and user info somehow
			Parent albumDisplayParent = FXMLLoader.load(getClass().getResource("/view/AlbumDisplay.fxml"));
			Scene albumDisplayScene = new Scene(albumDisplayParent,900,600);
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
			window.setScene(albumDisplayScene);
			window.setTitle("Album Display");
			window.show();
		}
		
		
	}

}
