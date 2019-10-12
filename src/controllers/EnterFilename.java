package controllers;

import java.io.IOException;

import Services.DirectoryServices;
import Services.NewCreationService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class EnterFilename extends Controller{
	private final String _backFXMLPath="/fxml/MainMenu.fxml";
	private final String _nextFXMLPath="/fxml/MainMenu.fxml";
	private final String _previousFXMLPath="/fxml/ChooseImages.fxml";
	private NewCreationService _creation;
	@FXML private Button _backButton;
	@FXML private Button _mainButton;
	@FXML private TextField _filenameField;
	@FXML private ProgressBar _progressBar;
	@FXML private Label _progressLabel;
	
	public void setCreation(NewCreationService creation) {
		_creation=creation;
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
	
	@FXML
	public void handleMainButton(ActionEvent event) throws IOException {
		String option = _mainButton.getText();
		String filename = _filenameField.getText().trim();
		if(option.equals("Create")) {
			//check if file exists
			if(filename==null || filename.trim().isEmpty() || !filename.matches("[a-zA-Z0-9]*")){
				CreateAlert(Alert.AlertType.WARNING, "Invalid Filename", "The filename is either empty or invalid\nPlease only use either letters or numbers");
				return;
			}
			if (DirectoryServices.creationExists(filename)) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Overwrite Creation");
				alert.setHeaderText(null);
				alert.setContentText("The creation already exists.\nDo you want to ovewrite " + filename + "?");
				alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
				DialogPane pane = alert.getDialogPane();
		        pane.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
		        pane.setId("background");
				alert.showAndWait();
				if(alert.getResult()==ButtonType.NO) {
					//exit method if dont want to overwrite
					return;
				}
			}
			buildCreation(filename);
		} else {
			SwitchForwardScene(event);
		}
	}
	
	private void buildCreation(String filename) {
		//disable buttons
		_mainButton.setDisable(true);
		_backButton.setDisable(true);
		Task<Void> task = new Task<Void>(){
			@Override
			protected Void call() throws Exception {
				int methods=7;
				updateProgress(0, methods);
				updateMessage("Deleting Temps");
				_creation.deleteFinals();
				
				updateProgress(1, methods);
				updateMessage("Combining Chunks");
				_creation.combineChunks();
				
				updateProgress(2, methods);
				updateMessage("Mixing Audio");
				_creation.mixAudio();
				
				updateProgress(3, methods);
				updateMessage("Formatting Images");
				_creation.formatImages();
				
				updateProgress(4, methods);
				updateMessage("Making Video");
				_creation.makeVideos();
				
				updateMessage("Making Creation");
				updateProgress(5, methods);
				_creation.makeCreation(filename);
				
				updateMessage("Making Question");
				updateProgress(6, methods);
				_creation.makeQuestion(filename);
				
				updateMessage("Done");
				updateProgress(7, methods);
				return null;
			}
		};
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						CreateAlert(Alert.AlertType.INFORMATION, "Creation Made", "The creation " + filename + " was made");
						_backButton.setDisable(false);
						_mainButton.setDisable(false);
						_mainButton.setText("Finish");
					}
				});
			}
		});
		Thread thread = new Thread(task);
		thread.start();
		_progressBar.progressProperty().bind(task.progressProperty());
		_progressLabel.textProperty().bind(task.messageProperty());
	}
	
	@Override
	public void AuxiliaryFunctionPrevious(FXMLLoader loader) {
		ChooseImages controller = loader.getController();
		controller.setCreation(_creation);
		controller.updateImageList();
		controller.reflectCreation();
	}
	
}
