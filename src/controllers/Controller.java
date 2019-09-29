package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;

public abstract class Controller {



    public final void SwitchBackScene(ActionEvent event) throws IOException {

        String fxmlpath = ReturnFXMLPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
        Parent sceneparent = loader.load();
        Scene scene = new Scene(sceneparent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        AuxiliaryFunctionBackwards(loader);

    }



    public final void SwitchForwardScene(ActionEvent event) throws IOException {

        String fxmlpath = ReturnForwardFXMLPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
        Parent sceneparent = loader.load();
        Scene scene = new Scene(sceneparent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        AuxiliaryFunction(loader);

    }



    public final void SwitchPreviousScene(ActionEvent event) throws IOException {
        String fxmlpath = ReturnPreviousFXMLPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlpath));
        Parent sceneparent = loader.load();
        Scene scene = new Scene(sceneparent);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        AuxiliaryFunctionPrevious(loader);
    }



    public void CreateAlert(Alert.AlertType alerttype, String title, String message){

        Alert alert = new Alert(alerttype);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();

    }



    public abstract String ReturnFXMLPath();




    public abstract String ReturnForwardFXMLPath();




    public String ReturnPreviousFXMLPath() {
    	return null;
    };




    public void AuxiliaryFunction(FXMLLoader loader){}




    public void AuxiliaryFunctionBackwards(FXMLLoader loader) throws IOException {}




    public void AuxiliaryFunctionPrevious(FXMLLoader loader) {}


    public void ProcessRunner(String typeofprocess, String pathtoscriptwithargs) throws IOException {

        ProcessBuilder processBuilder = new ProcessBuilder(typeofprocess, "-c", pathtoscriptwithargs);
        Process process = processBuilder.start();
        try {

            process.waitFor();

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }



}
