package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class Score extends Controller {
	
	private final String _backFXMLPath="/fxml/MainMenu.fxml";

    @FXML Label _scorelabel;
	

    public void setLabel(int score, int maxScore){

        String outcome = "You got: "+ score+ " out of "+ maxScore;
        _scorelabel.setText(outcome);


    }



    @Override
    public String ReturnFXMLPath() {
		return _backFXMLPath;
    }

    @Override
    public String ReturnForwardFXMLPath() {
        return "/fxml/ChooseQuiz.fxml";
    }


}
