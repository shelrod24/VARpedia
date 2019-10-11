package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

import java.io.File;

public class ChooseQuiz extends Controller {

    private ChoiceBox<String> _choiceBox;



    


    @Override
    public String ReturnFXMLPath() {
        return null;
    }

    @Override
    public String ReturnForwardFXMLPath() {
        return null;
    }







    public void populateChoiceBox(){

        String[] files = new File("./questions").list();
        ObservableList<String> listOfFiles = FXCollections.observableArrayList(files);
        _choiceBox.setItems(listOfFiles);
        _choiceBox.getSelectionModel().selectFirst();

        int numberOfFiles = files.length;

    }


}
