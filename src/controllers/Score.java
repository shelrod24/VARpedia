package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class Score extends Controller {

    @FXML
    Label _scorelabel;


    public void setLabel(int score, int maxScore){

        String outcome = "You got: "+ score+ " out of "+ maxScore;
        _scorelabel.setText(outcome);


    }



    @Override
    public String ReturnFXMLPath() {
        return null;
    }

    @Override
    public String ReturnForwardFXMLPath() {
        return "/fxml/ChooseQuiz.fxml";
    }


}
