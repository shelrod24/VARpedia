package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainMenu extends Application {




    public static void main(String[] args) {
        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        CreateFileDirectory();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();

    }


    public void SwitchToList(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ListScene.fxml"));
        Parent Listcreationsparent = loader.load();
        Scene Listcreationscene = new Scene(Listcreationsparent);

        //Get stage information of the current stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(Listcreationscene);

        //Getting the controller from the loader
        ListScene listcontroller = loader.<ListScene>getController();

        listcontroller.innitialiseList();

    }

    /**
     * This method will change the scene to the audio creation scene.
     */
    public void SwitchToCreateAudio(ActionEvent event) throws IOException {

        Parent createaudioparent = FXMLLoader.load(getClass().getResource("/fxml/CreateAudio.fxml"));
        Scene createaudioscene = new Scene(createaudioparent);

        //Get stage information of the current stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(createaudioscene);

    }

    public void CreateFileDirectory() {

        File Audio = new File("");
        boolean audioexists = Audio.exists();
        if (audioexists) {

        } else {
            ProcessBuilder createaudio = new ProcessBuilder("/bin/bash","-c","mkdir Audio");
            try {
                createaudio.start();
            } catch (IOException e) {
            }
        }

        File Creations = new File("");
        boolean creationsexists = Creations.exists();
        if (creationsexists) {

        } else {
            ProcessBuilder createcreations = new ProcessBuilder("/bin/bash","-c","mkdir Creations");
            try {
                createcreations.start();
            } catch (IOException e) {
            }
        }

    }



}
