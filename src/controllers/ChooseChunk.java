package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Services.DirectoryServices;
import Services.NewCreationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Alert.AlertType;

public class ChooseChunk extends Controller{
	private final String _backFXMLPath="/fxml/MainMenu.fxml";
	private final String _nextFXMLPath="/fxml/ChooseImages.fxml";
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
		return _backFXMLPath;
	}
	
	public void setCreation(NewCreationService creation) {
		_creation=creation;
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
		updateInputViewList(folder);
		// make a new creation with the term as the folder name
		_creation = new NewCreationService(folder);
	}
	
	public void updateInputViewList(String folder) {
		List<String> audioList = DirectoryServices.listAudio(folder);
		_inputAudioView.getItems().setAll(audioList);
		_outputAudioView.getItems().clear();
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
		if (index>0 && index<chunkList.size()) {
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
		if (index<chunkList.size()-1 && index>=0) {
			String chunk = chunkList.get(index);
			chunkList.set(index, chunkList.get(index + 1));
			chunkList.set(index + 1, chunk);
			_outputAudioView.getSelectionModel().clearAndSelect(index + 1);
		}
	}
	
	@Override
	public void AuxiliaryFunction(FXMLLoader loader) {
		_creation.setAudioList(_outputAudioView.getItems());
		ChooseImages controller = loader.getController();
		controller.setCreation(_creation);
		controller.initData();
	}
	
	public void reflectCreation() {
		//select the right folder
		int folderIndex = _folderView.getItems().indexOf(_creation.getTerm());
		_folderView.getSelectionModel().clearAndSelect(folderIndex);
		//update the input view with the right folder
		updateInputViewList(_creation.getTerm());
		//set output values to creation current
		_outputAudioView.getItems().setAll(_creation.getAudioList());
	}
	
	@FXML
	public void handleNextButton(ActionEvent event) throws IOException {
		if(_outputAudioView.getItems().isEmpty()) {
			CreateAlert(Alert.AlertType.WARNING, "No Audio Chosen", "No audio was chosen");
		}else {
			SwitchForwardScene(event);
		}
	}
	
}
