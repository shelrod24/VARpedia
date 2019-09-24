package controllers;

import Services.DirectoryServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateAudio extends Controller {


    @FXML private TextField _searchfield;
    @FXML private Button _searchbutton;
    @FXML private ListView<String> _listarea;
    @FXML private TextArea _lyrics;
    @FXML private Button _moveoverbutton;
    @FXML private ChoiceBox _chooseaccent;
    @FXML private TextField _filefield;
    private String _previousfxmlpath = "/fxml/MainMenu.fxml";

    private ObservableList<AccentType> _accents = FXCollections.observableArrayList(AccentType.EnglishUS, AccentType.EnglishUK, AccentType.Spanish, AccentType.German);




    public void SearchWikipedia() throws IOException {
        String searchterm = _searchfield.getText();
        String echowiki = "echo $(wikit "+searchterm+") > ./text.txt";
        ProcessBuilder getwikiarticle = new ProcessBuilder("/bin/bash","-c",echowiki);
        Process process = getwikiarticle.start();

        try {

            process.waitFor();

        } catch (Exception e) {

        }

        FillWikiTextFiles(searchterm);
        PrintWikiArticleFromLinedFile();

    }



    public void FillWikiTextFiles(String searchterm) throws IOException {

        final String s = searchterm;
        Thread thread = new Thread(new Task<Void>(){

            @Override
            protected Void call() throws Exception {
                String echowiki = "echo $(wikit "+s+") > ./text.txt";
                ProcessBuilder filltextfileprocess = new ProcessBuilder("/bin/bash","-c",echowiki);
                Process process = filltextfileprocess.start();

                try {

                    process.waitFor();

                } catch (Exception e) {

                }

                ProcessBuilder filllinedtextfileprocess = new ProcessBuilder("sh", "-c", "./AddWikiToLinedTextFile.sh");
                Process process2 = filllinedtextfileprocess.start();

                try {

                    process2.waitFor();

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }
                return null;
            }
        });
        thread.start();

    }


    public int PrintWikiArticleFromLinedFile() {

        _listarea.getItems().clear();
        int n = 0;
        BufferedReader reader = null;
        List<String> wikicontent = new ArrayList<String>();

        try {

            reader = new BufferedReader(new FileReader("./Linedtextfile.txt"));
            String line = reader.readLine();

            while (line != null) {

                _listarea.getItems().add(line);
                wikicontent.add(line);
                line = reader.readLine();
            }

            reader.close();

        } catch (IOException e) {

            try {

                reader.close();

            } catch (IOException e1) {

                e.printStackTrace();

            }

        }

        n = wikicontent.size();
        return n;

    }

    @FXML
    private void initialize() {
        _chooseaccent.setItems(_accents);
        _chooseaccent.getSelectionModel().selectFirst();
    }


    public void MoveTextOver() {

        String name = _listarea.getSelectionModel().getSelectedItem();
        _lyrics.appendText(name +"\n");

    }

    public void PreviewAudio() throws IOException, InterruptedException {


        String lyrics = _lyrics.getText();
        String[] arr = lyrics.split(" ");
        AccentType accent = (AccentType)_chooseaccent.getSelectionModel().getSelectedItem();
        System.out.println(accent.ReturnFlag());

        if (arr.length > 1000){

        } else {

            String previewtext = "echo "+ "\""+lyrics+"\""+" > ./Preview.txt";
            ProcessBuilder previewtxtfill = new ProcessBuilder("/bin/bash","-c", previewtext);
            Process p = previewtxtfill.start();

            String espeak = "cat ./Preview.txt | espeak " +accent.ReturnFlag();
            ProcessBuilder sing = new ProcessBuilder("/bin/bash","-c", espeak);
            Process process = sing.start();

        }

    }



    @Override
    public String ReturnFXMLPath() {
        return _previousfxmlpath;
    }

    @Override
    public String ReturnForwardFXMLPath() {
        return null;
    }


    public void NameFile() throws IOException, InterruptedException {

        String name = _filefield.getText();
        System.out.println(_filefield.getText());
        if (name == null){

        } else {

            boolean exists = DirectoryServices.SearchDirectoryForName(name);
            System.out.println(exists);

            if (exists) {

            } else {

                //Creates the audio file
                //Must use the accent....
                ProcessBuilder audioBuilder = new ProcessBuilder("/bin/bash", "-c", "echo `cat ./Linedtextfile.txt` > ./text.txt;"
                        + "espeak -f ./text.txt -w ./Audio/"+name+".wav;");
                Process p1 = audioBuilder.start();
                p1.waitFor();
                _lyrics.clear();

            }
        }

    }


    public void updateWordCount(){

    }


}
