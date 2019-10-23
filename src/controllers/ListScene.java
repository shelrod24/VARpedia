package controllers;

import Services.DirectoryServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListScene extends Controller{

    @FXML private ListView<String> _list;
    private String _previousfxmlpath = "/fxml/MainMenu.fxml";
    private String _returnforwardfxmlpath = "/fxml/MediaViewer.fxml";
    private String _creationname;



    public void innitialiseList(){

        List<String> creationfiles = DirectoryServices.ListFilesInDir("./creations");

        for(String s: creationfiles) {

            s = s.substring(0, s.length()-4);
            _list.getItems().add(s);
        }

    }


    public void PlayCreation(ActionEvent event) throws IOException {

        _creationname = _list.getSelectionModel().getSelectedItem();

        if (_creationname == null){

        	return;
        	
        } else {

            switchForwardScene(event);

        }

    }

    public void DeleteCreation() throws IOException, InterruptedException {

        _creationname = _list.getSelectionModel().getSelectedItem();

        if (_creationname == null){

            return;

        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("File Deletion Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the creation " + _creationname + "?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        DialogPane pane = alert.getDialogPane();
        pane.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
        pane.setId("background");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.NO) {

            return;

        } else {
        	
        	//delete both creation and question
        	processRunner("sh", "./scripts/delete_creation_and_question.sh \"" + _creationname + "\"");
        	
            List<String> creationfiles = DirectoryServices.ListFilesInDir("./creations");

            _list.getItems().clear();

            for(String s: creationfiles) {

                s = s.substring(0, s.length()-4);
                _list.getItems().add(s);

            }

        }

    }


    @Override
    public String returnFXMLPath() {
        return _previousfxmlpath;
    }

    @Override
    public String returnForwardFXMLPath() {
        return _returnforwardfxmlpath;
    }

    @Override
    public void auxiliaryFunction(FXMLLoader loader){
        MediaViewController mediaview = loader.<MediaViewController>getController();
        mediaview.playMedia(_creationname);

    }

}
