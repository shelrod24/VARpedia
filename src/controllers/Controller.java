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



    public final void switchBackScene(ActionEvent event) throws IOException {
        String fxmlpath = returnFXMLPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
        Parent sceneparent = loader.load();
        Scene scene = new Scene(sceneparent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        auxiliaryFunctionBackwards(loader);
    }



    public final void switchForwardScene(ActionEvent event) throws IOException {
        String fxmlpath = returnForwardFXMLPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
        Parent sceneparent = loader.load();
        Scene scene = new Scene(sceneparent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        auxiliaryFunction(loader);
    }



    public final void switchPreviousScene(ActionEvent event) throws IOException {
        String fxmlpath = returnPreviousFXMLPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
        Parent sceneparent = loader.load();
        Scene scene = new Scene(sceneparent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        auxiliaryFunctionPrevious(loader);
    }



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



    public abstract String returnFXMLPath();




    public abstract String returnForwardFXMLPath();




    public String returnPreviousFXMLPath() {
    	return null;
    };




    public void auxiliaryFunction(FXMLLoader loader){}




    public void auxiliaryFunctionBackwards(FXMLLoader loader) throws IOException {}




    public void auxiliaryFunctionPrevious(FXMLLoader loader) {}


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
