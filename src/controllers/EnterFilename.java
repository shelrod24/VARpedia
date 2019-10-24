package controllers;

import java.io.IOException;
import java.net.URISyntaxException;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.DirectoryServices;
import services.NewCreationService;

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

	/**
	 * sets current creation
	 * @param creation the creation to be set
	 */
	public void setCreation(NewCreationService creation) {
		_creation=creation;
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

	/**
	 * is called when the main button is clicked
	 * if creation has not been made, then create the creation
	 * otherwise should return to home menu
	 */
	@FXML
	public void handleMainButton(ActionEvent event) throws IOException {
		//get current text of button as welll as filename
		String option = _mainButton.getText();
		String filename = _filenameField.getText().trim();
		if(option.equals("Create")) {
			//check if file exists
			if(filename==null || filename.trim().isEmpty() || !filename.matches("[a-zA-Z0-9]*")){
				createAlert(Alert.AlertType.WARNING, "Invalid Filename", "The filename is either empty or invalid\nPlease only use either letters or numbers");
				return;
			}
			if (DirectoryServices.creationExists(filename)) {
				//prompt overwrite
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Overwrite Creation");
				alert.setHeaderText(null);
				alert.setContentText("The creation already exists.\nDo you want to ovewrite " + filename + "?");
				alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
				//set alert css
				DialogPane pane = alert.getDialogPane();
				pane.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
				pane.setId("background");
				alert.showAndWait();
				if(alert.getResult()==ButtonType.NO) {
					//exit method if dont want to overwrite
					return;
				} else if(alert.getResult()==ButtonType.YES){
					//if overwriting, need to delete question as well
					processRunner("sh", "./scripts/delete_creation_and_question.sh \"" + filename + "\"");
				}
			}
			//build creation
			buildCreation(filename);
		} else {
			//if creation built, then return home
			switchForwardScene(event);
		}
	}
	
	/**
	 * builds the creation as specified by the _creation
	 * @param filename the filename specified by user
	 */
	private void buildCreation(String filename) {
		//make progressbar visible
		_progressBar.setVisible(true);
		//disable buttons
		_mainButton.setDisable(true);
		_backButton.setDisable(true);
		//make creation as well as returning update message and progress
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
		//upon succeed, create an alert, and chnage main button from create to finish
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						createAlert(Alert.AlertType.INFORMATION, "Creation Made", "The creation " + filename + " was made");
						_backButton.setDisable(false);
						_mainButton.setDisable(false);
						_mainButton.setText("Finish");
						Image image = new Image(getClass().getResourceAsStream("/icons/home.png"));
						_mainButton.setGraphic(new ImageView(image));
					}
				});
			}
		});
		Thread thread = new Thread(task);
		thread.start();
		//bind label and progress bar to task process
		_progressBar.progressProperty().bind(task.progressProperty());
		_progressLabel.textProperty().bind(task.messageProperty());
	}

	/**
	 * called when back button is pressed
	 */
	@Override
	public void auxiliaryFunctionPrevious(FXMLLoader loader) {
		ChooseImages controller = loader.getController();
		//set the prevuous scene to contain the current creation
		controller.setCreation(_creation);
		//update image list as well as reflect the current creation
		controller.updateImageList();
		controller.reflectCreation();
	}

}
