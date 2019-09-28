package controllers;

import Services.DirectoryServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
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

        } else {

            //Popup, are you sure you want to delete this creation.

            ProcessBuilder removecreation = new ProcessBuilder("/bin/bash","-c","rm -f ./creations/\"" + _creationname + ".mp4\"");
            Process removecreationprocess = removecreation.start();
            removecreationprocess.waitFor();

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
