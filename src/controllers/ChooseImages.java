package controllers;

import java.util.List;

import Services.DirectoryServices;
import Services.NewCreationService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
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
		}
	}
	
	@FXML
	private void handleRemove() {
		if (_outputImageView.getSelectionModel().getSelectedItem()!=null) {
			int index = _outputImageView.getSelectionModel().getSelectedIndex();
			_outputImageView.getItems().remove(index);
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
		}
	}
	
	public void initData() {
		Thread thread = new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				//delete previous images
				ProcessBuilder deleteBuilder = new ProcessBuilder("./scripts/delete_images.sh");
				Process deleteProcess = deleteBuilder.start();
				deleteProcess.waitFor();
				//download images
				ProcessBuilder downloadBuilder = new ProcessBuilder("./scripts/flickr_downloader.sh", _creation.getTerm());
				Process downloadProcess = downloadBuilder.start();
				int exit = downloadProcess.waitFor();
				//populate list once done
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if(exit!=0) {
							Alert alert = new Alert(AlertType.WARNING);
			    			alert.setTitle("No Images Found");
			    			alert.setHeaderText(null);
			    			alert.setContentText("No images were found on Flickr");
			    			alert.getButtonTypes().setAll(ButtonType.OK);
			    			alert.showAndWait();
						} else {
							updateImageList();
						}
					}
				});
				return null;
			}
		});
		thread.start();
	}
	
	public void updateImageList() {
		List<String> imageList=DirectoryServices.listImages();
		if (imageList.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No Images Found");
			alert.setHeaderText(null);
			alert.setContentText("No images were found on Flickr");
			alert.getButtonTypes().setAll(ButtonType.OK);
			alert.showAndWait();
		}else {
			_inputImageView.getItems().setAll(imageList);
		}
	}
	
	public void reflectCreation() {
		_outputImageView.getItems().setAll(_creation.getImageList());
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
}
