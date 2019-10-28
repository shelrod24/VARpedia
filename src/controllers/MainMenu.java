package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.DirectoryServices;

import java.io.File;
import java.io.IOException;

public class MainMenu extends Application {

    public static void main(String[] args) {
    	launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
    	//create all required directories on startup
        DirectoryServices.createDirectories();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainMenu.fxml"));
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    /**
     * @param event
     * @throws IOException
     * This method switches the scene from the main menu to the scene where all the existing creations are listed.
     */
    public void switchToList(ActionEvent event) throws IOException {

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
     * @param event
     * @throws IOException
     * This method switches the scene from the main menu to the scene where the user can create audio chuncks.
     */
    public void switchToCreateAudio(ActionEvent event) throws IOException {

        Parent createaudioparent = FXMLLoader.load(getClass().getResource("/fxml/CreateAudio.fxml"));
        Scene createaudioscene = new Scene(createaudioparent);

        //Get stage information of the current stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(createaudioscene);

    }

    /**
     * @param event
     * @throws IOException
     * This method switches the scene from the main menu to the scene where the user can create a creation.
     */
    public void switchToCreateCreation(ActionEvent event) throws IOException {

    	Parent createcreationparent = FXMLLoader.load(getClass().getResource("/fxml/ChooseAudio.fxml"));
        Scene createcreationscene = new Scene(createcreationparent);

        //Get stage information of the current stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(createcreationscene);
    }

    /**
     * @param event
     * @throws IOException
     * This method switches the scene from the main menu to the scene where all the existing audio chuncks are listed
     * and where the user will then have the option to play or delete an audio chunk.
     */
    public void switchToDeleteAudio(ActionEvent event) throws IOException {

        Parent deleteaudioparent = FXMLLoader.load(getClass().getResource("/fxml/DeleteAudio.fxml"));
        Scene deleteaudioscene = new Scene(deleteaudioparent);

        //Get stage information of the current stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(deleteaudioscene);

    }

    /**
     * @param event
     * @throws IOException
     * This method switches the scene from the main menu to the scene where the quiz can be configured and then played.
     */
    public void switchToStartQuiz(ActionEvent event) throws IOException {

        Parent quizParent = FXMLLoader.load(getClass().getResource("/fxml/ChooseQuiz.fxml"));
        Scene quiz = new Scene(quizParent);

        //Get stage information of the current stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(quiz);

    }

    /**
     * @param event
     * @throws IOException
     * This method switches the scene from the main menu to the scene where the credits are displayed.
     */
    public void switchToCredits(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/Credits.fxml"));
        Scene scene = new Scene(parent);

        //Get stage information of the current stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);

    }



}
