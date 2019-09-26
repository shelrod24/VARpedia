package controllers;

import Services.DirectoryServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.ArrayList;

public class ListScene extends Controller{

    @FXML private ListView<String> _list;
    private String _previousfxmlpath = "/fxml/MainMenu.fxml";
    private String _returnforwardfxmlpath = "/fxml/MediaViewer.fxml";
    private String _searchterm;



    public void innitialiseList(){

        ArrayList<String> creationfiles = DirectoryServices.listCreations();

        for(String s: creationfiles) {

            s = s.substring(0, s.length()-4);
            _list.getItems().add(s);
        }

    }


    public void PlayCreation(ActionEvent event) throws IOException {

        _searchterm = _list.getSelectionModel().getSelectedItem();
        System.out.println(_searchterm);

        if (_searchterm == null){

        } else {

            SwitchForwardScene(event);

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
        mediaview.playMedia(_searchterm);

    }

}
