package control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
	
	public void backButton(ActionEvent event) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("/view/AlbumDisplay.fxml"));
		Scene albumDisplayScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(albumDisplayScene);
		window.setTitle("Album Display");
		window.show();
	}
	
	public void addButton(ActionEvent event){
		System.out.println("2");
	}
	
	public void deleteButton(ActionEvent event){
		System.out.println("3");
	}
	
	public void copyButton(ActionEvent event){
		System.out.println("4");
	}
	
	public void moveButton(ActionEvent event){
		System.out.println("5");
	}
	
	public void previousPhotoButton(ActionEvent event){
		System.out.println("6");
	}
	
	public void nextPhotoButton(ActionEvent event){
		System.out.println("7");
	}
	
	public void editButton(ActionEvent event) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("/view/Edit.fxml"));
		Scene EditScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(EditScene);
		window.setTitle("Edit");
		window.show();
	}
}
