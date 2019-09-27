package controllers;

import Services.DirectoryServices;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class DeleteAudio extends Controller {


    private String _previousscene = "/fxml/MainMenu.fxml";
    private String _subdirectory;
    @FXML private ListView<String> _audiosubdirs;
    @FXML private ListView<String> _audiofiles;



    public void SelectAudioSubDirectory()   {

        _audiofiles.getItems().clear();

        _subdirectory = _audiosubdirs.getSelectionModel().getSelectedItem();

        //Check for null;
        ArrayList<String> audiofiles = DirectoryServices.listAudio(_subdirectory);
        for(String s: audiofiles) {
            _audiofiles.getItems().add(s);
        }

     }


     public void DeleteAudiofile() throws IOException, InterruptedException {

        //Have a pup up for confirmation;

         String audio = _audiofiles.getSelectionModel().getSelectedItem();

         String cmd = "rm -f "+ "./audio/"+ _subdirectory+"/" + audio;
         ProcessBuilder deleteaudio = new ProcessBuilder("/bin/bash", "-c", cmd);
         Process deleteprocess = deleteaudio.start();
         deleteprocess.waitFor();

         ArrayList<String> audiofiles = DirectoryServices.listAudio(_subdirectory);
         _audiofiles.getItems().clear();

         for(String s: audiofiles) {
             _audiofiles.getItems().add(s);
         }

         ProcessBuilder deletedir = new ProcessBuilder("sh", "-c", "./scripts/checkdir_isempty.sh"+" "+_subdirectory);
         Process deletedirprocess = deletedir.start();

         _audiosubdirs.getItems().clear();


         //NOT UPDATING PROPERLY;
         System.out.println("SDGHALGH");
         ArrayList<String> _audiosubfolders = DirectoryServices.listAudioFolders();
         for(String s: _audiosubfolders) {
             _audiosubdirs.getItems().add(s);
         }

     }



     @FXML
     public void initialize(){

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
