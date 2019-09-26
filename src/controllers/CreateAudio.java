package controllers;

import Services.DirectoryServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CreateAudio extends Controller {


    @FXML private TextField _searchfield;
    @FXML private Button _searchbutton;
    @FXML private ListView<String> _listarea;
    @FXML private TextArea _lyrics;
    @FXML private Button _moveoverbutton;
    @FXML private ChoiceBox<String> _chooseaccent;
    @FXML private TextField _filefield;
    @FXML private Button _createbutton;
    @FXML private Button _previewbutton;
    private String _previousfxmlpath = "/fxml/MainMenu.fxml";
    private String _searchterm;



    public void SearchWikipedia() throws IOException {


        _searchterm = _searchfield.getText();

        final String searchterm =  _searchterm;

        Thread thread = new Thread(new Task<Void>(){


            @Override
            protected Void call() throws Exception {

                String echowiki = "echo $(wikit " + searchterm + ") > ./text.txt";
                ProcessBuilder getwikiarticle = new ProcessBuilder("/bin/bash", "-c", echowiki);
                Process process = getwikiarticle.start();

                try {

                    process.waitFor();

                } catch (Exception e) {

                }

                FillWikiTextFiles(searchterm);

                return null;
            }

            @Override
            public void done(){
                Platform.runLater(()->{
                    PrintWikiArticleFromLinedFile();
                    _searchbutton.setDisable(false);
                });
            }

    });


    	_searchbutton.setDisable(true);
        thread.start();

    }





    public void FillWikiTextFiles(String searchterm) throws IOException {


                String echowiki = "echo $(wikit "+searchterm+") > ./text.txt";
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
    private void initialize() throws IOException, InterruptedException {


        ProcessBuilder voicesbuilder = new ProcessBuilder("sh","-c","./scripts/list_voices.sh");
        Process voiceprocess = voicesbuilder.start();
        BufferedReader stdout = new BufferedReader(new InputStreamReader(voiceprocess.getInputStream()));
        voiceprocess.waitFor();
        String line = stdout.readLine();
        String[] arr = line.split(" ");


        ObservableList<String> accents = FXCollections.observableArrayList(arr);
        _chooseaccent.setItems(accents);
        _chooseaccent.getSelectionModel().selectFirst();

    }




    public void MoveTextOver() {

        String name = _listarea.getSelectionModel().getSelectedItem();
        _lyrics.appendText(name +"\n");

    }




    public void PreviewAudio() throws IOException, InterruptedException {


        String lyrics = _lyrics.getText();
        String[] arr = lyrics.split(" ");

        if (arr.length > 40){
        	Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Too Many Words");
			alert.setHeaderText(null);
			alert.setContentText("There are more that 40 words to be parsed.");
			alert.getButtonTypes().setAll(ButtonType.OK);
			alert.showAndWait();
        } else {
        	_previewbutton.setDisable(true);
        	Thread thread = new Thread(new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					//right now only have it such that kal_diphone is in it
					ProcessBuilder builder = new ProcessBuilder("./scripts/festival_tts.sh",_chooseaccent.getSelectionModel().getSelectedItem(), _lyrics.getText());
	            	Process process = builder.start();
	            	process.waitFor();
	            	return null;
				}
				@Override
				protected void done() {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							_previewbutton.setDisable(false);
						}
					});
				}
        	});
        	thread.start();
        	
            //String previewtext = "echo "+ "\""+lyrics+"\""+" > ./Preview.txt";
            //ProcessBuilder previewtxtfill = new ProcessBuilder("/bin/bash","-c", previewtext);
            //Process p = previewtxtfill.start();

            //String espeak = "cat ./Preview.txt | espeak " +accent.ReturnFlag();
            //ProcessBuilder sing = new ProcessBuilder("/bin/bash","-c", espeak);
            //Process process = sing.start();

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
        String lyrics = _lyrics.getText();
        lyrics = lyrics.replace("\n", " ");
        String[] arr = lyrics.split(" ");


        if (arr.length > 40){
        	Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Too Many Words");
			alert.setHeaderText(null);
			alert.setContentText("There are more that 40 words to be parsed.");
			alert.getButtonTypes().setAll(ButtonType.OK);
			alert.showAndWait();
			return;
        }
        

        if (name == null || name.trim().isEmpty()){
        	Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Filename");
			alert.setHeaderText(null);
			alert.setContentText("The filename is empty.");
			alert.getButtonTypes().setAll(ButtonType.OK);
			alert.showAndWait();
			return;
        } else {

            boolean exists = DirectoryServices.SearchDirectoryForName(_searchterm, name);

            if (exists) {
            	Alert alert = new Alert(AlertType.WARNING);
    			alert.setTitle("File Already Exists");
    			alert.setHeaderText(null);
    			alert.setContentText("The file "+name+" already exists");
    			alert.getButtonTypes().setAll(ButtonType.OK);
    			alert.showAndWait();
    			return;
            } else if (arr.length <= 40) {
            	
            	_createbutton.setDisable(true);
            	Thread thread = new Thread(new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						//right now only have it such that kal_diphone is in it
						ProcessBuilder builder = new ProcessBuilder("./scripts/festival_make_chunk.sh", name, _searchterm, _chooseaccent.getSelectionModel().getSelectedItem(), _lyrics.getText());
		            	Process process = builder.start();
		            	process.waitFor();
		            	return null;
					}
					@Override
					protected void done() {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								_lyrics.clear();
								_createbutton.setDisable(false);
							}
						});
					}
            	});
            	thread.start();
            	
                //ProcessBuilder audioBuilder = new ProcessBuilder("/bin/bash","-c", "espeak "+accent.ReturnFlag()+" "+ "\""+lyrics+"\""+" -w ./Audio/"+ name +".wav");
                //Process p1 = audioBuilder.start();
                //p1.waitFor();
                //_lyrics.clear();

            }
        }

    }

    public void updateWordCount(){

    }


}
