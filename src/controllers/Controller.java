package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import java.io.IOException;

public abstract class Controller {

    /**
     * @param event
     * @throws IOException
     * This method is called when the scene needs to be switched to the home screen scene
     */
    public final void switchBackScene(ActionEvent event) throws IOException {
        String fxmlpath = returnFXMLPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
        Parent sceneparent = loader.load();
        Scene scene = new Scene(sceneparent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        auxiliaryFunctionBackwards(loader);
    }

    /**
     * @param event
     * @throws IOException
     * This method is called when the scene needs to be switched to the one that comes directly after the
     * current scene
     */
    public final void switchForwardScene(ActionEvent event) throws IOException {
        String fxmlpath = returnForwardFXMLPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
        Parent sceneparent = loader.load();
        Scene scene = new Scene(sceneparent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        auxiliaryFunction(loader);
    }

    /**
     * @param event
     * @throws IOException
     * This method is called when the scene needs to be switched to the one that came directly before
     * the current scene.
     */
    public final void switchPreviousScene(ActionEvent event) throws IOException {
        String fxmlpath = returnPreviousFXMLPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
        Parent sceneparent = loader.load();
        Scene scene = new Scene(sceneparent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        auxiliaryFunctionPrevious(loader);
    }

    /**
     * This method is used to create an pop up alert box
     */
    public void createAlert(Alert.AlertType alerttype, String title, String message){
        Alert alert = new Alert(alerttype);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getButtonTypes().setAll(ButtonType.OK);
        DialogPane pane = alert.getDialogPane();
        pane.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
        pane.setId("background");
        alert.showAndWait();
    }

    /**
     * This method returns the relative path to the home screen scene
     */
    public abstract String returnFXMLPath();

    /**
     * This method returns the relative path to the scene that comes after the current scene
     */
    public abstract String returnForwardFXMLPath();

    /**
     * This method returns the relative path to the scene that comes directly before the current scene
     */
    public String returnPreviousFXMLPath() {
    	return null;
    };

    /**
     * This method can be used to get the controller of the scene that comes directly after the current
     * scene. The reason why you may want to do this is if you need to manually set the state of the controller.
     */
    public void auxiliaryFunction(FXMLLoader loader){}

    /**
     * This method can be used to get the controller of the home scene. The reason why you may want to do this
     * is if you need to manually set the state of the controller.
     */
    public void auxiliaryFunctionBackwards(FXMLLoader loader) throws IOException {}

    /**
     * This method can be used to get the controller of the scene that comes directly before the current
     * scene. The reason why you may want to do this is if you need to manually set the state of the controller.
     */
    public void auxiliaryFunctionPrevious(FXMLLoader loader) {}

    /**
     * This takes the type of process to be run and a command in the form of a string and creates and starts a running
     * process.
     */
    public void processRunner(String typeofprocess, String pathtoscriptwithargs) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(typeofprocess, "-c", pathtoscriptwithargs);
        Process process = processBuilder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
