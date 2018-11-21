package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.AlbumInfo;
import model.User;

public class AdminController implements Serializable {
	
	@FXML private Button logOutButton;
	@FXML private Button addButton;
	@FXML private Button deleteButton;
	@FXML private ListView<User> usersList;
	private ObservableList<User> obsList;
	private List<User> users;
	
	//gets list of users from loginController
	public void initData(List<User> user) {
		users = user;
		obsList = FXCollections.observableArrayList(users);
		usersList.setItems(obsList);
		
		if (obsList.isEmpty() && obsList != null) {
			deleteButton.setDisable(true);
		}
		System.out.println(users);
		
		usersList.getSelectionModel().select(0);
		
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
	
	//goes back to login screen
	public void logOutButton(ActionEvent event) throws Exception {
		writeApp(users);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/Login.fxml"));
		Parent root = loader.load();
		LoginController controller = loader.getController();
		Scene loginScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(loginScene);
		window.setTitle("Photos Login");
		window.show();
	}
	
	//create new users
	public void addButton(ActionEvent event){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("New User");
		dialog.setHeaderText("New User");
		dialog.setContentText("Enter username for new user:");
		Optional<String> result = dialog.showAndWait();
		
		//if okay is clicked
		if (result.isPresent()){
			//if there is no user input, alert for invalid input
			if (result.get().equals("")) {
				errorMessage();
			}
			else {
				User user = new User((String)result.get());
				//error message if username already exists
				for(int i=0; i<obsList.size(); i++) {
					if(obsList.get(i).getUsername().toLowerCase().equals(user.getUsername().toLowerCase())) {
						errorMessage();
						return;
					}
				}
				//adds user to list
				obsList.add(user);	
				users.add(user);
				usersList.getSelectionModel().select(user);
				deleteButton.setDisable(false);
			}
		}
	}
	
	public void deleteButton(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Album");
		alert.setContentText("Are you sure you want to delete this?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			int index = usersList.getSelectionModel().getSelectedIndex(); 
			obsList.remove(index);
			users.remove(index);
			if (obsList.isEmpty() && obsList != null) {
				deleteButton.setDisable(true);
			}
		} 
	}
	
	public void errorMessage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Input");
		alert.setContentText("Please try again.");
		alert.showAndWait();
	}
	

	
	
	
	
	public static final String storeDir = "docs";
	public static final String storeFile = "users.ser"; 
	
	public static void writeApp(List<User> users) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(users);
	} 
}
