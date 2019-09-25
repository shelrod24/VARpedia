package controllers;

import java.util.ArrayList;
import java.util.List;

import Services.DirectoryServices;
import Services.NewCreationService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ChooseChunk extends Controller{
	private String _previousFXMLPath="/fxml/MainMenu.fxml";
	private String _nextFXMLPath="/fxml/ChooseImages.fxml";
	private NewCreationService _creation;
	@FXML private ListView<String> _folderView;
	@FXML private ListView<String> _inputAudioView;
	@FXML private ListView<String> _outputAudioView;
	
	
	
	@FXML
    private void initialize() {
		_folderView.getItems().setAll(DirectoryServices.listAudioFolders());
    }

	@Override
	public String ReturnFXMLPath() {
		return _previousFXMLPath;
	}

	@Override
	public String ReturnForwardFXMLPath() {
		return _nextFXMLPath;
	}
	
	@FXML
	private void handleFolderView() {
		String folder = _folderView.getSelectionModel().getSelectedItem();
		if(folder==null) {
			return;
		}
		List<String> audioList = DirectoryServices.listAudio(folder);
		_inputAudioView.getItems().setAll(audioList);
		_outputAudioView.getItems().clear();
		// make a new creation with the term as the folder name
		_creation = new NewCreationService(folder);
	}
	
	@FXML
	private void handleAdd() {
		if (_inputAudioView.getSelectionModel().getSelectedItem()!=null) {
			String chunk = _inputAudioView.getSelectionModel().getSelectedItem();
			_outputAudioView.getItems().add(chunk);
		}
	}
	
	@FXML
	private void handleRemove() {
		if (_outputAudioView.getSelectionModel().getSelectedItem()!=null) {
			int index = _outputAudioView.getSelectionModel().getSelectedIndex();
			_outputAudioView.getItems().remove(index);
		}
	}
	
	@FXML
	private void handleMoveUp() {
		int index = _outputAudioView.getSelectionModel().getSelectedIndex();
		List<String> chunkList = _outputAudioView.getItems();
		//if index is less than or equal to zero, do nothing
		if (index>0) {
			String chunk = chunkList.get(index);
			chunkList.set(index, chunkList.get(index - 1));
			chunkList.set(index - 1, chunk);
			_outputAudioView.getSelectionModel().clearAndSelect(index - 1);
		}
	}
	
	@FXML
	private void handleMoveDown() {
		int index = _outputAudioView.getSelectionModel().getSelectedIndex();
		List<String> chunkList = _outputAudioView.getItems();
		//if index is more than or equal to index of last element, do nothing
		if (index<chunkList.size()-1) {
			String chunk = chunkList.get(index);
			chunkList.set(index, chunkList.get(index + 1));
			chunkList.set(index + 1, chunk);
			_outputAudioView.getSelectionModel().clearAndSelect(index + 1);
		}
	}

}
