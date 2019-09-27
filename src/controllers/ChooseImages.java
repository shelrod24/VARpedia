package controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import Services.DirectoryServices;
import Services.FlickrAPIService;
import Services.NewCreationService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;

public class ChooseImages extends Controller{
	private final String _backFXMLPath="/fxml/MainMenu.fxml";
	private final String _nextFXMLPath="/fxml/EnterFilename.fxml";
	private final String _previousFXMLPath="/fxml/ChooseChunk.fxml";
	private NewCreationService _creation;
	@FXML private ListView<String> _inputImageView;
	@FXML private ListView<String> _outputImageView;
	
	
	@FXML
	private void initialize() {
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
	private void handleAdd() {
		if (_inputImageView.getSelectionModel().getSelectedItem()!=null) {
			String chunk = _inputImageView.getSelectionModel().getSelectedItem();
			_outputImageView.getItems().add(chunk);
			renderImageView(_outputImageView);
		}
	}
	
	@FXML
	private void handleRemove() {
		if (_outputImageView.getSelectionModel().getSelectedItem()!=null) {
			int index = _outputImageView.getSelectionModel().getSelectedIndex();
			_outputImageView.getItems().remove(index);
			renderImageView(_outputImageView);
		}
	}
	
	@FXML
	private void handleMoveUp() {
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
	
	@FXML
	private void handleMoveDown() {
		int index = _outputImageView.getSelectionModel().getSelectedIndex();
		List<String> chunkList = _outputImageView.getItems();
		//if index is more than or equal to index of last element, do nothing
		if (index<chunkList.size()-1) {
			String chunk = chunkList.get(index);
			chunkList.set(index, chunkList.get(index + 1));
			chunkList.set(index + 1, chunk);
			_outputImageView.getSelectionModel().clearAndSelect(index + 1);
			renderImageView(_outputImageView);
		}
	}
	
	public void initData() {
		Task<Void>  task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				//delete previous images
				ProcessBuilder deleteBuilder = new ProcessBuilder("./scripts/delete_images.sh");
				Process deleteProcess = deleteBuilder.start();
				deleteProcess.waitFor();
				for (int i = 1; i <= 5; i++) {
					//download images
					//ProcessBuilder downloadBuilder = new ProcessBuilder("./scripts/flickr_downloader.sh", _creation.getTerm());
					//Process downloadProcess = downloadBuilder.start();
					//int exit = downloadProcess.waitFor();
					List<String> imageList = FlickrAPIService.getImages(_creation.getTerm(), 3, i);
					//populate list once done
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if(DirectoryServices.listImages().isEmpty()) {
				    			CreateAlert(Alert.AlertType.WARNING, "No Images Found", "No images were found on Flickr");
							} else {
								addImageList(imageList);
							}
						}
					});					
				}
				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.start();
	}
	
	public void updateImageList() {
		List<String> imageList=DirectoryServices.listImages();
		if (imageList.isEmpty()) {
			CreateAlert(Alert.AlertType.WARNING, "No Images Found", "No images were found on Flickr");
		}else {
			// render image once updated
			_inputImageView.getItems().setAll(imageList);
			renderImageView(_inputImageView);
		}
	}
	
	public void addImageList(List<String> imageList) {
		if (imageList.isEmpty()) {
			CreateAlert(Alert.AlertType.WARNING, "No Images Found", "No images were found on Flickr");
		}else {
			// render image once updated
			_inputImageView.getItems().addAll(imageList);
			renderImageView(_inputImageView);
		}
	}
	
	public void reflectCreation() {
		_outputImageView.getItems().setAll(_creation.getImageList());
		renderImageView(_outputImageView);
	}
	
	@FXML
	public void handleNextButton(ActionEvent event) throws IOException {
		if(_outputImageView.getItems().isEmpty()) {
			CreateAlert(Alert.AlertType.WARNING, "No Images Chosen", "No images were chosen");
		} else if(_outputImageView.getItems().size()>10) {
			CreateAlert(Alert.AlertType.WARNING, "Too Many Images Chosen", "More than 10 images were chosen");
		} else {
			SwitchForwardScene(event);
		}
	}
	
    public void AuxiliaryFunction(FXMLLoader loader){
    	_creation.setImageList(_outputImageView.getItems());
		EnterFilename controller = loader.getController();
		controller.setCreation(_creation);
    }
    
    @Override
	public void AuxiliaryFunctionPrevious(FXMLLoader loader) {
		ChooseChunk controller = loader.getController();
		controller.setCreation(_creation);
		controller.reflectCreation();
	}
    
    public void renderImageView(ListView<String> imageView) {
    	imageView.setCellFactory(param -> new ListCell<String>() {
    		private ImageView imageView = new ImageView();
    		@Override
    		public void updateItem(String imageName, boolean empty) {
    			super.updateItem(imageName, empty);
    			if(empty) {
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
    				imageView.setFitHeight(100);
    			}
    		}
    	});
    }
}
