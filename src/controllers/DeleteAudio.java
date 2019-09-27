package controllers;

import Services.DirectoryServices;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class DeleteAudio extends Controller {


    private String _previousscene = "/fxml/MainMenu.fxml";
    private String _subdirectory;
    @FXML private Button _deletebutton;
    @FXML private ListView<String> _audiosubdirs;
    @FXML private ListView<String> _audiofiles;



    public void SelectAudioSubDirectory() {

        _audiofiles.getItems().clear();

        _subdirectory = _audiosubdirs.getSelectionModel().getSelectedItem();


        if (_subdirectory == null) {


        } else {

            ArrayList<String> audiofiles = DirectoryServices.listAudio(_subdirectory);
            for (String s : audiofiles) {
                _audiofiles.getItems().add(s);
            }

        }
    }


     public void DeleteAudiofile() throws IOException, InterruptedException {

         //Have a pup up for confirmation;


         String audio = _audiofiles.getSelectionModel().getSelectedItem();

         if (audio == null) {

             CreateAlert(Alert.AlertType.WARNING, "No item selected", "ERROR You have not selected an item");


         } else {

             String cmd = "rm -f " + "./audio/" + _subdirectory + "/" + audio;
             ProcessBuilder deleteaudio = new ProcessBuilder("/bin/bash", "-c", cmd);
             Process deleteprocess = deleteaudio.start();
             deleteprocess.waitFor();

             ArrayList<String> audiofiles = DirectoryServices.listAudio(_subdirectory);
             _audiofiles.getItems().clear();

             for (String s : audiofiles) {
                 _audiofiles.getItems().add(s);
             }

             ProcessBuilder deletedir = new ProcessBuilder("sh", "-c", "./scripts/checkdir_isempty.sh" + " \"" + _subdirectory + "\"");
             Process deletedirprocess = deletedir.start();
             deletedirprocess.waitFor();

             _audiosubdirs.getItems().clear();


             ArrayList<String> _audiosubfolders = DirectoryServices.listAudioFolders();
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
         ArrayList<String> _audiosubfolders = DirectoryServices.listAudioFolders();

         for(String s: _audiosubfolders) {
             _audiosubdirs.getItems().add(s);
         }

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
