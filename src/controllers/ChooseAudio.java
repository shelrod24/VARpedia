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
import services.DirectoryServices;
import services.NewCreationService;
import javafx.scene.control.Alert.AlertType;

public class ChooseAudio extends Controller{
	private final String _backFXMLPath="/fxml/MainMenu.fxml";
	private final String _nextFXMLPath="/fxml/ChooseMusic.fxml";
	private NewCreationService _creation;
	private MediaPlayer _player;

	@FXML private ListView<String> _folderView;
	@FXML private ListView<String> _inputAudioView;
	@FXML private ListView<String> _outputAudioView;

	/**
	 * Upon initialization, need to load the folders to select
	 */
	@FXML
	private void initialize() {
		Thread thread = new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				//get audio directories
				List<String> folders = DirectoryServices.ListFilesInDir("./audio");
				//once done, show folders
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						_folderView.getItems().setAll(folders);						
					}});
				return null;
			}

		});
		thread.start();
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
	 * sets current creation
	 * @param creation creation to be set
	 */
	public void setCreation(NewCreationService creation) {
		_creation=creation;
	}

	/**
	 * If folder clicked, display contents of folder
	 */
	@FXML
	private void handleFolderView() {
		//get currently selected folder
		String folder = _folderView.getSelectionModel().getSelectedItem();
		if(folder==null) {
			return;
		}
		//set inputView to show contents of folder 
		updateInputViewList(folder);
		//make a new creation with the term as the folder name
		_creation = new NewCreationService(folder);
	}

	/**
	 * if inputAudioView clicked twice, play audio
	 */
	@FXML
	private void handleInputAudioView(MouseEvent event) {
		if(event.getButton() == MouseButton.PRIMARY && event.getClickCount()==2 && _inputAudioView.getSelectionModel().getSelectedItem() != null) {
			// build filepath
			String folder = _folderView.getSelectionModel().getSelectedItem();
			String audio = _inputAudioView.getSelectionModel().getSelectedItem();
			String filepath ="./audio/"+folder+"/"+audio; 
			//play audio chunk clicked
			playAudio(filepath);
		}
	}

	/**
	 * if outputAudioView clicked twice, play audio
	 */
	@FXML
	private void handleOutputAudioView(MouseEvent event) {
		if(event.getButton() == MouseButton.PRIMARY && event.getClickCount()==2 && _outputAudioView.getSelectionModel().getSelectedItem() != null) {
			// build filepath to audio file
			String folder = _folderView.getSelectionModel().getSelectedItem();
			String audio = _outputAudioView.getSelectionModel().getSelectedItem();
			String filepath ="./audio/"+folder+"/"+audio; 
			//play audio chunk clicked
			playAudio(filepath);
		}
	}
	
	/**
	 * plays audio specified by filepath
	 * @param filepath path to audio file to play
	 */
	private void playAudio(String filepath) {
		// stop currently playing audio
		if(_player!=null) {
			_player.stop();
		}
		// play audio
		File fileUrl = new File(filepath);
		Media audioMedia = new Media(fileUrl.toURI().toString());
		_player = new MediaPlayer(audioMedia);
		_player.autoPlayProperty().set(true);
	}


	/**
	 * Updates the input view list to contain the current files specified folder
	 * @param folder the string dictating the folder clicked
	 */
	public void updateInputViewList(String folder) {
		List<String> audioList = DirectoryServices.ListFilesInDir("./audio/"+folder);
		//only add to view if it ends in .wav
		audioList.removeIf(file -> !file.endsWith(".wav"));
		//set audio to view
		_inputAudioView.getItems().setAll(audioList);
		_outputAudioView.getItems().clear();
	}

	/**
	 * Once add button clicked, adds selected audio to output
	 */
	@FXML
	private void handleAdd() {
		if (_inputAudioView.getSelectionModel().getSelectedItem()!=null) {
			//add chunk selected to output view
			String chunk = _inputAudioView.getSelectionModel().getSelectedItem();
			_outputAudioView.getItems().add(chunk);
		}
	}

	/**
	 * Once remove button clicked, removes selected audio
	 */
	@FXML
	private void handleRemove() {
		if (_outputAudioView.getSelectionModel().getSelectedItem()!=null) {
			//remove item selected in output view
			int index = _outputAudioView.getSelectionModel().getSelectedIndex();
			_outputAudioView.getItems().remove(index);
		}
	}
	
	/**
	 * Once move up button clicked, moves up audio
	 */
	@FXML
	private void handleMoveUp() {
		int index = _outputAudioView.getSelectionModel().getSelectedIndex();
		List<String> chunkList = _outputAudioView.getItems();
		//if index is less than or equal to zero, do nothing
		if (index>0 && index<chunkList.size()) {
			//swap selected item with item above
			String chunk = chunkList.get(index);
			chunkList.set(index, chunkList.get(index - 1));
			chunkList.set(index - 1, chunk);
			_outputAudioView.getSelectionModel().clearAndSelect(index - 1);
		}
	}

	/**
	 * Once move up button clicked, moves up audio
	 */
	@FXML
	private void handleMoveDown() {
		int index = _outputAudioView.getSelectionModel().getSelectedIndex();
		List<String> chunkList = _outputAudioView.getItems();
		//if index is more than or equal to index of last element, do nothing
		if (index<chunkList.size()-1 && index>=0) {
			//swap selected item with item below
			String chunk = chunkList.get(index);
			chunkList.set(index, chunkList.get(index + 1));
			chunkList.set(index + 1, chunk);
			_outputAudioView.getSelectionModel().clearAndSelect(index + 1);
		}
	}

	/**
	 * Called when switching scene forward
	 */
	@Override
	public void auxiliaryFunction(FXMLLoader loader) {
		//stop current audio
		if(_player!=null) {
			_player.stop();
		}
		//set audioList in creations to audio in outputAudioView
		_creation.setAudioList(_outputAudioView.getItems());
		ChooseMusic controller = loader.getController();
		//set next scene to have the current creation
		controller.setCreation(_creation);
	}
	
	/**
	 * Called when pressing home button
	 */
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
		//select the right folder
		int folderIndex = _folderView.getItems().indexOf(_creation.getTerm());
		_folderView.getSelectionModel().clearAndSelect(folderIndex);
		//update the input view with the right folder
		updateInputViewList(_creation.getTerm());
		//set output values to creation current
		_outputAudioView.getItems().setAll(_creation.getAudioList());
	}
	
	/**
	 * Called when the next button is clicked
	 */
	@FXML
	public void handleNextButton(ActionEvent event) throws IOException {
		//checking if output is empty, then throw alert
		if(_outputAudioView.getItems().isEmpty()) {
			createAlert(Alert.AlertType.WARNING, "No Audio Chosen", "No audio was chosen");
		//else switch scene
		}else {
			switchForwardScene(event);
		}
	}

}
