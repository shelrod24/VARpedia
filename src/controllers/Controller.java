package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public abstract String ReturnFXMLPath();

    public abstract String ReturnForwardFXMLPath();
    
    public String ReturnPreviousFXMLPath() {
    	return null;
    };

    public void AuxiliaryFunction(FXMLLoader loader){}

    public void AuxiliaryFunctionBackwards(FXMLLoader loader) {}
    
    public void AuxiliaryFunctionPrevious(FXMLLoader loader) {}



}
