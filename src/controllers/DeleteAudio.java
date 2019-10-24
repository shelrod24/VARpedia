package controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import services.DirectoryServices;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DeleteAudio extends Controller {


	private String _previousscene = "/fxml/MainMenu.fxml";
	private String _subdirectory;
	private String _audio;
	private MediaPlayer _player;

	@FXML private Button _previewbutton;
	@FXML private ListView<String> _audiosubdirs;
	@FXML private ListView<String> _audiofiles;



	public void SelectAudioSubDirectory() {

		_audiofiles.getItems().clear();
		_subdirectory = _audiosubdirs.getSelectionModel().getSelectedItem();

		if (_subdirectory == null) {

		} else {

			List<String> audiofiles = DirectoryServices.ListFilesInDir("./audio/"+_subdirectory);

			for (String s : audiofiles) {
				if (s.endsWith(".wav")) {
					_audiofiles.getItems().add(s);
				}
			}

		}

	}



	public void DeleteAudiofile() throws IOException, InterruptedException {

		_audio = _audiofiles.getSelectionModel().getSelectedItem();

		if (_audio == null) {

			return;

		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("File Deletion Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete " + _audio + "?");
		DialogPane pane = alert.getDialogPane();
		pane.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
		pane.setId("background");
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.NO) {

			return;

		} else {

			// deleting original audio
			this.processRunner("/bin/bash", "rm -f " + "\"./audio/" + _subdirectory + "/" + _audio + "\"");
			// deleting redacted audio
			String redactedAudio = _audio.substring(0, _audio.length()-4) + "_redacted.wav";
			this.processRunner("/bin/bash", "rm -f " + "\"./audio/" + _subdirectory + "/redacted/" + redactedAudio + "\"");


			List<String> audiofiles = DirectoryServices.ListFilesInDir("./audio/"+_subdirectory);
			_audiofiles.getItems().clear();

			for (String s : audiofiles) {
				if (s.endsWith(".wav")) {
					_audiofiles.getItems().add(s);
				}
			}

			//check if redacted is empty
			this.processRunner("sh", "./scripts/checkdir_isempty.sh" + " \"" + _subdirectory + "/redacted\"");
			//check if subdirectory is empty
			this.processRunner("sh", "./scripts/checkdir_isempty.sh" + " \"" + _subdirectory + "\"");


			_audiosubdirs.getItems().clear();
			List<String> _audiosubfolders = DirectoryServices.ListFilesInDir("./audio");

			for (String s : _audiosubfolders) {

				_audiosubdirs.getItems().add(s);

			}

		}

	}



	@FXML
	public void initialize() throws InterruptedException, IOException {

		// Remove all empty directories.
		ProcessBuilder deletedirs = new ProcessBuilder("sh", "-c", "./scripts/delete_all_nonemptydirs.sh");
		Process deletedirprocess = deletedirs.start();
		deletedirprocess.waitFor();

		// Populate ListView
		List<String> _audiosubfolders = DirectoryServices.ListFilesInDir("./audio");

		for(String s: _audiosubfolders) {

			_audiosubdirs.getItems().add(s);

		}

	}

	public void PreviewExistingAudio() throws IOException, InterruptedException {
		if(_audiofiles.getSelectionModel().getSelectedItem()==null) {
			return;
		}

		if(_previewbutton.getText().equals("Play")) {
			// Change button to stop
			_previewbutton.setText("Stop");
			Image image = new Image(getClass().getResourceAsStream("/icons/stop.png"));
			_previewbutton.setGraphic(new ImageView(image));

			playAudio();

		} else if(_previewbutton.getText().equals("Stop") && _player!=null) {
			// Stop player and set button to play
			_previewbutton.setText("Play");
			Image image = new Image(getClass().getResourceAsStream("/icons/play.png"));
			_previewbutton.setGraphic(new ImageView(image));
			
			_player.stop();
			
		}
	}

	/**
	 * if _audiofiles clicked twice, stop current audio and play currently selected
	 */
	@FXML
	private void handleAudioView(MouseEvent event) throws IOException, InterruptedException {
		if(event.getButton() == MouseButton.PRIMARY && event.getClickCount()==2 && _audiofiles.getSelectionModel().getSelectedItem() != null) {
			// Change button to stop
			_previewbutton.setText("Stop");
			Image image = new Image(getClass().getResourceAsStream("/icons/stop.png"));
			_previewbutton.setGraphic(new ImageView(image));

			playAudio();		
		}
	}

	/**
	 * Plays the currently selected audio
	 * Will stop currently playing audio before playing
	 */
	private void playAudio() {
		if(_player != null) {
			_player.stop();
		}
		
		// Play audio
		_audio = _audiofiles.getSelectionModel().getSelectedItem();
		File fileUrl = new File("./audio/" + _subdirectory + "/" + _audio);
		Media audio = new Media(fileUrl.toURI().toString());
		_player = new MediaPlayer(audio);
		_player.autoPlayProperty().set(true);

		// At the end, call method again to reset button
		_player.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				try {
					PreviewExistingAudio();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public String returnFXMLPath() {
		return _previousscene;
	}

	@Override
	public String returnForwardFXMLPath() {
		return null;
	}
	
	@Override
	public void auxiliaryFunctionBackwards(FXMLLoader loader) {
		//stop current audio
		if(_player!=null) {
			_player.stop();
		}
	}


}
