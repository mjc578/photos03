package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
import model.AlbumInfo;
import model.Photo;
import model.Tag;
import model.TagType;
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
	@FXML private Label searchRes;
	
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
		
		searchRes.setDisable(true);
		
		obsList = FXCollections.observableArrayList();
		//process the query based on what type it happens to be
		//searching by tag
		if(queryType.equals("tagQuery")) {
			searchByTag();
		}
		//searching by date
		else {
			searchByDate();
		}
		//if there are actually search results
		if(obsList.size() != 0) {
			listView.setItems(obsList);
			listCellFactory();
		}
		//There are no search results, disable the list view and show text that says no search results, PANIC!
		else {
			disableStuff(true);
			searchRes.setDisable(false);
		}
	}
	
	//if the user selected search by tag
	private void searchByTag() {
		//split the query up if it is a conjuctive/disjunctive whatever
		String args[] = searchQuery.split(" ");
		//only one tag-value and type pair was entered
		if(args.length == 1) {
			singleArgTagSearch(args);
		}
		//otherwise it is a conjunctive disjunctive thingy that has three elements
		else {
			//if its is AND
			if(args[1].equals("AND")) {
				andTagSearch(args);
			}
			//ptherwise OR
			else {
				orTagSearch(args);
			}
		}
	}
	
	//the user only entered a single search item for a tag
	private void singleArgTagSearch(String args[]) {
		Tag t = null;
		//get the tag type and value
		String searchTerms[] = args[0].split("=");
		for(TagType tt : users.get(userIndex).getTagTypes()) {
			if(tt.getType().equals(searchTerms[0])) {
				t = new Tag(tt, searchTerms[1]);
			}
		}
		//the tag type doesn't even exist
		if(t == null) {
			return;
		}
		//for each of the user's albums, look through all the photos and add to the obslist if:
		//it is not there and it has the matching tag
		List<AlbumInfo> albums = users.get(userIndex).getUserAlbums();
		for(int i = 0; i < albums.size(); i++) {
			for(int j = 0; j < albums.get(i).getNumPhotos(); j++) {
				Photo p = albums.get(i).getPhotos().get(j);
				//if the album's current photo has this tag, add to list if it isnt already
				if(p.getTags().contains(t) && !obsList.contains(p)) {
					obsList.add(p);
				}
			}
		}
	}
	
	//and search
	private void andTagSearch(String args[]) {
		Tag t1 = null;
		Tag t2 = null;
		String searchTerms1[] = args[0].split("=");
		String searchTerms2[] = args[2].split("=");
		
		//set the tag to look for for tag t1
		for(TagType tt1 : users.get(userIndex).getTagTypes()) {
			if(tt1.getType().equals(searchTerms1[0])) {
				t1 = new Tag(tt1, searchTerms1[1]);
			}
		}
		//set the tag to look for for tag t2
		for(TagType tt2 : users.get(userIndex).getTagTypes()) {
			if(tt2.getType().equals(searchTerms2[0])) {
				t2 = new Tag(tt2, searchTerms2[1]);
			}
		}
		
		//and only works if both of the tags are not null, so no search results can come up if both are null
		if(t1 == null || t2 == null) {
			return;
		}
		
		//if the tag search by is the same type for both tags, check that it is multi valued, if not, return
		if(!t1.getTagType().isMultiValued() && t1.getTagType().equals(t2.getTagType())) {
			//TODO: show that the user entered a bad input and to redo, maybe do it in previous screen
			return;
		}
		
		//ok now search! a photo must have both tags for this to work, good luck
		List<AlbumInfo> albums = users.get(userIndex).getUserAlbums();
		for(int i = 0; i < albums.size(); i++) {
			for(int j = 0; j < albums.get(i).getNumPhotos(); j++) {
				Photo p = albums.get(i).getPhotos().get(j);
				//if the album's current photo has this tag, add to list if it isnt already
				if((p.getTags().contains(t1) && p.getTags().contains(t2)) && !obsList.contains(p)) {
					obsList.add(p);
				}
			}
		}
	}
	
	//or search
	private void orTagSearch(String args[]) {
		Tag t1 = null;
		Tag t2 = null;
		String searchTerms1[] = args[0].split("=");
		String searchTerms2[] = args[2].split("=");
		
		//set the tag to look for for tag t1
		for(TagType tt1 : users.get(userIndex).getTagTypes()) {
			if(tt1.getType().equals(searchTerms1[0])) {
				t1 = new Tag(tt1, searchTerms1[1]);
			}
		}
		//set the tag to look for for tag t2
		for(TagType tt2 : users.get(userIndex).getTagTypes()) {
			if(tt2.getType().equals(searchTerms2[0])) {
				t2 = new Tag(tt2, searchTerms2[1]);
			}
		}
		
		//or works if both are not null
		if(t1 == null && t2 == null) {
			return;
		}
		
		//ok now search! a photo can have either/both tags for this to work
		List<AlbumInfo> albums = users.get(userIndex).getUserAlbums();
		for(int i = 0; i < albums.size(); i++) {
			for(int j = 0; j < albums.get(i).getNumPhotos(); j++) {
				Photo p = albums.get(i).getPhotos().get(j);
				//if the album's current photo has this tag, add to list if it isnt already
				if((p.getTags().contains(t1) || p.getTags().contains(t2)) && !obsList.contains(p)) {
					obsList.add(p);
				}
			}
		}
	}

	private void searchByDate() {
		//split the query up by spaces, should be of XX/XX/XXXX TO XX/XX/XXXX
		String args[] = searchQuery.split(" ");
		
		//bad input
		if(args.length != 3) {
			return;
		}
		
		String startArgs[] = args[0].split("/");
		String endArgs[] = args[2].split("/");
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		
		start.set(Integer.parseInt(startArgs[2]), Integer.parseInt(startArgs[0]) - 1, Integer.parseInt(startArgs[1]), 0, 0, 0);
		end.set(Integer.parseInt(endArgs[2]), Integer.parseInt(endArgs[0]) - 1, Integer.parseInt(endArgs[1]), 23, 59, 59);
		
				
		//now that the dates are set, go through the list of albums and photos and if the photo lies in the date range
		//add it to the obslist
		List<AlbumInfo> albums = users.get(userIndex).getUserAlbums();
		for(int i = 0; i < albums.size(); i++) {
			for(int j = 0; j < albums.get(i).getNumPhotos(); j++) {
				Photo p = albums.get(i).getPhotos().get(j);
				//check if the current photo is within the start and end date range
				if(p.getDate().compareTo(start) >= 0 && p.getDate().compareTo(end) <= 0) {
					obsList.add(p);
				}
			}
		}
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
		writeApp(users);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/AlbumDisplay.fxml"));
		Parent root = loader.load();
		AlbumDisplayController controller = loader.getController();
		controller.initData(users, userIndex);
		
		Scene openAlbumScene = new Scene(root);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();	
		window.setScene(openAlbumScene);

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
			else {
				//there is input for the album name, now check if it is a duplicate
				for(AlbumInfo ai : users.get(userIndex).getUserAlbums()) {
					if(ai.getName().equals(result.get())) {
						errorMessage();
						return;
					}
				}
				//ok so if there were no duplicates create a new album to populate
				AlbumInfo newlyMadeAlbum = new AlbumInfo(result.get(), 0, null, null);
				//...and then populate it bang bang
				for(Photo p : obsList) {
					newlyMadeAlbum.addPhoto(p);
				}
				//add it to the user's list of albums and you are done
				users.get(userIndex).addToAlbums(newlyMadeAlbum);
			}
		}
	}
	
	//Next Photo Button
	public void nextPhotoButton(ActionEvent event) {
		int index = listView.getSelectionModel().getSelectedIndex() + 1;
		listView.getSelectionModel().select(index);
	}
	
	//Previous Photo Button
	public void previousPhotoButton(ActionEvent event) {
		int index = listView.getSelectionModel().getSelectedIndex() - 1;
		listView.getSelectionModel().select(index);
	}
	
	//error alert for invalid inputs
	public void errorMessage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Input");
		alert.setContentText("Please try again.");
		alert.showAndWait();
	}
	
	public void disableStuff(boolean tf) {
		createAlbumButton.setDisable(tf);
		previousPhotoButton.setDisable(tf);
		nextPhotoButton.setDisable(tf);
		listView.setDisable(tf);
	}
	
	public static final String storeDir = "docs";
	public static final String storeFile = "users.ser"; 
	
	public static void writeApp(List<User> users) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(users);
		oos.close();
	} 
	
}
