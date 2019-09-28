package controllers;

import Services.AudioService;
import Services.DirectoryServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.List;

public class DeleteAudio extends Controller {


    private String _previousscene = "/fxml/MainMenu.fxml";
    private String _subdirectory;
    private String _audio;
    @FXML private Button _previewbutton;
    @FXML private ListView<String> _audiosubdirs;
    @FXML private ListView<String> _audiofiles;



    public void SelectAudioSubDirectory() {

        _audiofiles.getItems().clear();
        _subdirectory = _audiosubdirs.getSelectionModel().getSelectedItem();

        if (_subdirectory == null) {

        } else {

            List<String> audiofiles = DirectoryServices.ListFilesInDir("./audio/"+_subdirectory);

            for (String s : audiofiles) {

                _audiofiles.getItems().add(s);

            }

        }

    }



     public void DeleteAudiofile() throws IOException, InterruptedException {

         _audio = _audiofiles.getSelectionModel().getSelectedItem();

         if (_audio == null) {

             CreateAlert(Alert.AlertType.WARNING, "No item selected", "ERROR You have not selected an item");
             return;

         }

         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
         alert.setTitle("File Deletion Confirmation");
         alert.setHeaderText(null);
         alert.setContentText("Are you sure you want to delete " + _audio + ".wav?");
         alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
         alert.showAndWait();

         if (alert.getResult() == ButtonType.NO) {

             return;

         } else {


             this.ProcessRunner("/bin/bash", "rm -f " + "\"./audio/" + _subdirectory + "/" + _audio + "\"");

             List<String> audiofiles = DirectoryServices.ListFilesInDir("./audio/"+_subdirectory);
             _audiofiles.getItems().clear();

             for (String s : audiofiles) {

                 _audiofiles.getItems().add(s);

             }

             this.ProcessRunner("sh", "./scripts/checkdir_isempty.sh" + " \"" + _subdirectory + "\"");


             _audiosubdirs.getItems().clear();
             List<String> _audiosubfolders = DirectoryServices.ListFilesInDir("./audio");

             for (String s : _audiosubfolders) {

                 _audiosubdirs.getItems().add(s);

             }

         }

     }



     @FXML
     public void initialize() throws InterruptedException, IOException {


         // Remove all empty directories.
         ProcessBuilder deletedirs = new ProcessBuilder("sh", "-c", "./scripts/delete_all_nonemptydirs.sh");
         Process deletedirprocess = deletedirs.start();
         deletedirprocess.waitFor();

        // Populate ListView
         List<String> _audiosubfolders = DirectoryServices.ListFilesInDir("./audio");

         for(String s: _audiosubfolders) {

             _audiosubdirs.getItems().add(s);

         }

     }

     public void PreviewExistingAudio () throws IOException, InterruptedException {

        _previewbutton.setDisable(true);

        Thread worker = new Thread(new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                _audio = _audiofiles.getSelectionModel().getSelectedItem();
                AudioService.playAudio(_subdirectory, _audio);
                return null;

            }

            @Override
            protected void done() {

                Platform.runLater(()->{

                    _previewbutton.setDisable(false);

                });

            }

        });

        worker.start();

     }


    @Override
    public String ReturnFXMLPath() {
        return _previousscene;
    }

    @Override
    public String ReturnForwardFXMLPath() {
        return null;
    }

}
