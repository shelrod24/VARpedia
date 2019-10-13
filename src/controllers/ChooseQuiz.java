package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class ChooseQuiz extends Controller {

    @FXML private ChoiceBox<Integer> _choiceBox;
    String[] files;
    ArrayList<String> quizFiles;



    public void switchToQuiz(ActionEvent event) throws IOException {

        pupulateQuizList();
        SwitchForwardScene(event);

    }


    private void populateChoiceBox(){

        int numberOfFiles = files.length;
        Integer[] array = new Integer[numberOfFiles];

        for(int i = 0; i < numberOfFiles; i++){
            array[i] = i+1;
        }

        ObservableList<Integer> listOfFiles = FXCollections.observableArrayList(array);
        _choiceBox.setItems(listOfFiles);
        _choiceBox.getSelectionModel().selectFirst();

    }


    private void pupulateQuizList(){

        Integer numOfItems = _choiceBox.getSelectionModel().getSelectedItem();
        quizFiles = new ArrayList<String>();
        boolean exists;

        for(int i = 0; i < numOfItems; i++){

            exists = false;
            int random = getRandomIntegerBetweenRange(numOfItems);

            //Checking to see if the randomly selected quiz item has already been added to the list.
            for(int j = 0; j < quizFiles.size(); j++) {

                if(files[random-1].equals(quizFiles.get(j))) {

                    exists = true;

                }

            }

            if (!exists){

                quizFiles.add(files[random-1]);

            } else {

                i--;
            }

            System.out.println(quizFiles.get(i));
        }


    }


    public int getRandomIntegerBetweenRange(int max){

        int x = (int)(Math.random()*max) + 1;
        return x;

    }


    @FXML
    public void initialize(){
        files = new File("./questions").list();
        this.populateChoiceBox();
    }

    @Override
    public String ReturnFXMLPath() {
        return "/fxml/MainMenu.fxml";
    }

    @Override
    public String ReturnForwardFXMLPath() {
        return "/fxml/Quiz.fxml";
    }

    @Override
    public void AuxiliaryFunction(FXMLLoader loader){

        QuizController controller = loader.<QuizController>getController();
        controller.setFileList(quizFiles);
        controller.playMedia();

    }


}
