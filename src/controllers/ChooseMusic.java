package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Services.AudioService;
import Services.DirectoryServices;
import Services.NewCreationService;
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
import javafx.scene.control.Alert.AlertType;

public class ChooseMusic extends Controller{
	private final String _backFXMLPath="/fxml/MainMenu.fxml";
	private final String _nextFXMLPath="/fxml/ChooseImages.fxml";
	private final String _previousFXMLPath="/fxml/ChooseChunk.fxml";
	private NewCreationService _creation;
	@FXML private ListView<String> _musicView;
	
	@FXML
    private void initialize() {
		_musicView.getItems().add("None");
		_musicView.getItems().addAll(DirectoryServices.ListFilesInDir("./music"));
		// as default, select None
		_musicView.getSelectionModel().clearAndSelect(0);
    }

	@Override
	public String ReturnFXMLPath() {
		return _backFXMLPath;
	}

	@Override
	public String ReturnForwardFXMLPath() {
		return _nextFXMLPath;
	}

	@Override
	public String ReturnPreviousFXMLPath() {
		return _previousFXMLPath;
	}

	public void setCreation(NewCreationService creation) {
		_creation=creation;
	}

	@FXML
	private void handleInputAudioView(MouseEvent event) {
		if(event.getButton() == MouseButton.PRIMARY && event.getClickCount()==2 && _musicView.getSelectionModel().getSelectedItem() != null) {
	        Thread worker = new Thread(new Task<Void>(){
	            @Override
	            protected Void call() throws Exception {
	                String music = _musicView.getSelectionModel().getSelectedItem();
	                AudioService.playMusic(music);
	                return null;
	            }
	        });
	        worker.start();
		}
	}
	
	@Override
	public void AuxiliaryFunction(FXMLLoader loader) {
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
	public void AuxiliaryFunctionPrevious(FXMLLoader loader) {
		//called when switching scenes
		ChooseChunk controller = loader.getController();
		controller.setCreation(_creation);
		controller.reflectCreation();
	}
	
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
