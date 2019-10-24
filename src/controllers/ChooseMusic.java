package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import services.AudioService;
import services.DirectoryServices;
import services.NewCreationService;
import javafx.scene.control.Alert.AlertType;

public class ChooseMusic extends Controller{
	private final String _backFXMLPath="/fxml/MainMenu.fxml";
	private final String _nextFXMLPath="/fxml/ChooseImages.fxml";
	private final String _previousFXMLPath="/fxml/ChooseChunk.fxml";
	private NewCreationService _creation;
	private MediaPlayer _player;

	@FXML private ListView<String> _musicView;

	/**
	 * Upon initialization, load music in music directory
	 */
	@FXML
	private void initialize() {
		_musicView.getItems().add("None");
		// not that intensive, so doesnt need to be threaded
		_musicView.getItems().addAll(DirectoryServices.ListFilesInDir("./music"));
		// as default, select None
		_musicView.getSelectionModel().clearAndSelect(0);
	}

	/**
	 * returns path to take when clicking home button
	 * is called by superclass as a template method
	 */
	@Override
	public String returnFXMLPath() {
		return _backFXMLPath;
	}

	/**
	 * returns path to take when clicking next button
	 * is called by superclass as a template method
	 */
	@Override
	public String returnForwardFXMLPath() {
		return _nextFXMLPath;
	}

	/**
	 * returns path to take when clicking back button
	 * is called by superclass as a template method
	 */
	@Override
	public String returnPreviousFXMLPath() {
		return _previousFXMLPath;
	}

	public void setCreation(NewCreationService creation) {
		_creation=creation;
	}

	/**
	 * Plays audio when double clicked
	 */
	@FXML
	private void handleMusicView(MouseEvent event) {
		if(event.getButton() == MouseButton.PRIMARY && event.getClickCount()==2 && _musicView.getSelectionModel().getSelectedItem() != null) {
			// stop current track
			if(_player != null) {
				_player.stop();
			}

			// get currently selected music
			String music = _musicView.getSelectionModel().getSelectedItem();
			
			// if None is selected, skip playing track
			if(music != "None") {
				File fileUrl = new File("./music/" + music);
				Media musicMedia = new Media(fileUrl.toURI().toString());
				_player = new MediaPlayer(musicMedia);
				_player.autoPlayProperty().set(true);
			}
		}
	}

	@Override
	public void auxiliaryFunction(FXMLLoader loader) {
		//stop current audio
		if(_player!=null) {
			_player.stop();
		}
		
		String music = _musicView.getSelectionModel().getSelectedItem();
		if(music.equals("None")) {
			music="";
		}
		_creation.setMusic(music);
		ChooseImages controller = loader.getController();
		controller.setCreation(_creation);
		controller.initData();
	}

	@Override
	public void auxiliaryFunctionPrevious(FXMLLoader loader) {
		// stop current audio
		if(_player!=null) {
			_player.stop();
		}
		
		//called when switching scenes
		ChooseChunk controller = loader.getController();
		controller.setCreation(_creation);
		controller.reflectCreation();
	}
	
	@Override
	public void auxiliaryFunctionBackwards(FXMLLoader loader) {
		//stop current audio
		if(_player!=null) {
			_player.stop();
		}
	}

	/**
	 * makes it so that the listViews reflect the current creation object
	 */
	public void reflectCreation() {
		// get previously selected music
		String music =_creation.getMusic();
		// if music was not selected, select None
		if(music.equals("")) {
			music="None";
		}
		int musicIndex = _musicView.getItems().indexOf(music);
		_musicView.getSelectionModel().clearAndSelect(musicIndex);
	}

}
