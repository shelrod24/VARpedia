package controllers;

import Services.DirectoryServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

            CreateAlert(Alert.AlertType.WARNING, "No item selected", "ERROR You have not selected an item");

        } else {

            SwitchForwardScene(event);

        }

    }

    public void DeleteCreation() throws IOException, InterruptedException {

        _creationname = _list.getSelectionModel().getSelectedItem();

        if (_creationname == null){

            CreateAlert(Alert.AlertType.WARNING, "No item selected", "ERROR You have not selected an item");
            return;

        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("File Deletion Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete " + _creationname + ".wav?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.NO) {

            return;

        } else {


            ProcessRunner("/bin/bash","rm -f ./creations/\"" + _creationname + ".mp4\"");
            List<String> creationfiles = DirectoryServices.ListFilesInDir("./creations");

            _list.getItems().clear();

            for(String s: creationfiles) {

                s = s.substring(0, s.length()-4);
                _list.getItems().add(s);

            }

        }

    }


    @Override
    public String ReturnFXMLPath() {
        return _previousfxmlpath;
    }

    @Override
    public String ReturnForwardFXMLPath() {
        return _returnforwardfxmlpath;
    }

    @Override
    public void AuxiliaryFunction(FXMLLoader loader){
        MediaViewController mediaview = loader.<MediaViewController>getController();
        mediaview.playMedia(_creationname);

    }

}
