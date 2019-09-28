package controllers;

import Services.DirectoryServices;
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

        DirectoryServices.CreateDirectories();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.setResizable(false);
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
    
    /**
     * 
     */
    public void SwitchToCreateCreation(ActionEvent event) throws IOException {
    	Parent createcreationparent = FXMLLoader.load(getClass().getResource("/fxml/ChooseChunk.fxml"));
        Scene createcreationscene = new Scene(createcreationparent);

        //Get stage information of the current stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(createcreationscene);
    }


    public void SwitchToDeleteAudio(ActionEvent event) throws IOException {

        Parent deleteaudioparent = FXMLLoader.load(getClass().getResource("/fxml/DeleteAudio.fxml"));
        Scene deleteaudioscene = new Scene(deleteaudioparent);

        //Get stage information of the current stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(deleteaudioscene);

    }



}
