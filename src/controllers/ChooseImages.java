package controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.DirectoryServices;
import services.FlickrAPIService;
import services.NewCreationService;
import javafx.scene.control.Alert.AlertType;

public class ChooseImages extends Controller{
	private final String _backFXMLPath="/fxml/MainMenu.fxml";
	private final String _nextFXMLPath="/fxml/EnterFilename.fxml";
	private final String _previousFXMLPath="/fxml/ChooseMusic.fxml";
	private NewCreationService _creation;
	private Task<Boolean> _getImageTask;
	
	@FXML private ListView<String> _inputImageView;
	@FXML private ListView<String> _outputImageView;

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
	 * sets current creation
	 * @param creation the creation to be set
	 */
	public void setCreation(NewCreationService creation) {
		_creation=creation;
	}

	/**
	 * when add button clicked, adds currently selected image to the outputImageView
	 */
	@FXML
	private void handleAdd() {
		if (_inputImageView.getSelectionModel().getSelectedItem()!=null) {
			String chunk = _inputImageView.getSelectionModel().getSelectedItem();
			_outputImageView.getItems().add(chunk);
			renderImageView(_outputImageView);
		}
	}

	/**
	 * when remove button clicked, removes currently selected image in the outputImageView
	 */
	@FXML
	private void handleRemove() {
		if (_outputImageView.getSelectionModel().getSelectedItem()!=null) {
			int index = _outputImageView.getSelectionModel().getSelectedIndex();
			_outputImageView.getItems().remove(index);
			renderImageView(_outputImageView);
		}
	}

	/**
	 * when move up button clicked, moves currently selected image in outputImageView
	 */
	@FXML
	private void handleMoveUp() {
		if(_outputImageView.getSelectionModel().getSelectedIndex()==-1){
			return;
		}
		int index = _outputImageView.getSelectionModel().getSelectedIndex();
		List<String> chunkList = _outputImageView.getItems();
		//if index is less than or equal to zero, do nothing
		if (index>0) {
			String chunk = chunkList.get(index);
			chunkList.set(index, chunkList.get(index - 1));
			chunkList.set(index - 1, chunk);
			_outputImageView.getSelectionModel().clearAndSelect(index - 1);
			renderImageView(_outputImageView);
		}
	}

	/**
	 * when move down button clicked, moves currently selected image in outputImageView
	 */
	@FXML
	private void handleMoveDown() {
		if(_outputImageView.getSelectionModel().getSelectedIndex()==-1){
			return;
		}
		int index = _outputImageView.getSelectionModel().getSelectedIndex();
		List<String> chunkList = _outputImageView.getItems();
		//if index is more than or equal to index of last element, do nothing
		if (index<chunkList.size()-1) {
			System.out.println(index);
			String chunk = chunkList.get(index);
			chunkList.set(index, chunkList.get(index + 1));
			chunkList.set(index + 1, chunk);
			_outputImageView.getSelectionModel().clearAndSelect(index + 1);
			renderImageView(_outputImageView);
		}
	}

	/**
	 * dowloads the images required to be chosen
	 */
	public void initData() {
		//set task as a field to be cancelled when necessary (eg back or home button clicked)
		_getImageTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				//delete previous images
				ProcessBuilder deleteBuilder = new ProcessBuilder("./scripts/delete_images.sh");
				Process deleteProcess = deleteBuilder.start();
				deleteProcess.waitFor();
				for (int i = 1; i <= 5; i++) {
					//check if cancel is pressed
					if(isCancelled()==true) {
						return true;
					}
					
					//download images
					List<String> imageList = FlickrAPIService.getImages(_creation.getTerm(), 3, i);
					
					//check if no images found, and return false if so
					if(i==1 && DirectoryServices.ListFilesInDir("./temps/image").isEmpty()) {
						cancel();
						return false;
					}

					//populate list once done
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							addImageList(imageList);
						}
					});
				}
				return true;
			}
		};
		//if cancelled, check whether images exist, and if they dont, return an error
		_getImageTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				Boolean imagesExist = _getImageTask.getValue();
				if(imagesExist!=null && imagesExist) {
					createAlert(Alert.AlertType.ERROR, "No Images Found", "No images were found on Flickr\nTry another search term");
				}
			}
		});
		Thread thread = new Thread(_getImageTask);
		thread.start();
	}

	/**
	 * Updates the imageView to show what is currently in the folder
	 */
	public void updateImageList() {
		List<String> imageList=DirectoryServices.ListFilesInDir("./temps/image");
		if (!imageList.isEmpty()) {
			// render image once updated
			_inputImageView.getItems().setAll(imageList);
			renderImageView(_inputImageView);
		}
	}

	/**
	 * Adds all images specified in imageList to the imageList
	 * @param imageList the list of images to be added
	 */
	public void addImageList(List<String> imageList) {
		if (imageList.isEmpty()) {
			createAlert(Alert.AlertType.WARNING, "No Images Found", "No images were found on Flickr");
		}else {
			// render image once updated
			_inputImageView.getItems().addAll(imageList);
			renderImageView(_inputImageView);
		}
	}

	/**
	 * makes it so that the listViews reflect the current creation object
	 */
	public void reflectCreation() {
		_outputImageView.getItems().setAll(_creation.getImageList());
		renderImageView(_outputImageView);
	}

	/**
	 * Called when next button is clicked
	 */
	@FXML
	public void handleNextButton(ActionEvent event) throws IOException {
		if(_outputImageView.getItems().isEmpty()) {
			createAlert(Alert.AlertType.WARNING, "No Images Chosen", "No images were chosen");
		} else if(_outputImageView.getItems().size()>10) {
			createAlert(Alert.AlertType.WARNING, "Too Many Images Chosen", "More than 10 images were chosen");
		} else {
			//swicth scene if there are no errors
			switchForwardScene(event);
		}
	}
	
	/**
	 * Called when switching scene forward
	 */
	@Override
	public void auxiliaryFunction(FXMLLoader loader){
		//set creation to contain the selected images
		_creation.setImageList(_outputImageView.getItems());
		EnterFilename controller = loader.getController();
		//set next scene to contain the current creation
		controller.setCreation(_creation);
	}
	
	/**
	 * Called when switching scene by clicking back button
	 */
	@Override
	public void auxiliaryFunctionPrevious(FXMLLoader loader) {
		//stop loading images when back is pressed
		_getImageTask.cancel();
		//get controller and set creation as currently creation
		ChooseMusic controller = loader.getController();
		controller.setCreation(_creation);
		//make controller reflect the current creation
		//this makes it so that it saves the previous input
		controller.reflectCreation();
	}
	
	/**
	 * Called when switcing scene by clicking home button
	 */
	@Override
	public void auxiliaryFunctionBackwards(FXMLLoader loader) {
		//stop loading images when home is pressed
		if(_getImageTask!=null) {
			_getImageTask.cancel();
		}
	}

	/**
	 * Renders the selected listView to show the images within the list
	 * @param imageView the listView which contains the image to be rendered
	 */
	public void renderImageView(ListView<String> imageView) {
		// sets the cellfactory of the imageview
		imageView.setCellFactory(param -> new ListCell<String>() {
			// create imageview for each cell
			private ImageView imageView = new ImageView();
			@Override
			public void updateItem(String imageName, boolean empty) {
				super.updateItem(imageName, empty);
				if(empty) {
					// if emptu, then set image and text as nothing
					setText(null);
					setGraphic(null);
				} else {
					// getting images
					File file = new File("temps/image/"+imageName);
					Image image = new Image(file.toURI().toString());
					
					// setting image
					imageView.setImage(image);
					setText(imageName);
					setGraphic(imageView);
					imageView.setPreserveRatio(true);
					imageView.setFitHeight(60);
				}
			}
		});
	}
}
