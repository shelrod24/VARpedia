package controllers;

import Services.DirectoryServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

                FillWikiTextFiles(searchterm);

                return null;
            }

            @Override
            public void done() {

                Platform.runLater(()->{

                    PrintWikiArticleFromLinedFile();
                    _searchbutton.setDisable(false);

                });

            }

        });


    	_searchbutton.setDisable(true);
        thread.start();

    }



    public void FillWikiTextFiles(String searchterm) throws IOException, InterruptedException {

        String echowiki = "echo $(wikit "+searchterm+") > ./temps/text.txt";
        ProcessRunner("/bin/bash", echowiki);
        ProcessRunner("sh", "./scripts/AddWikiToLinedTextFile.sh");

    }



    public int PrintWikiArticleFromLinedFile() {

        BufferedReader reader = null;
        List<String> wikicontent = new ArrayList<String>();
        _listarea.getItems().clear();
        int n = 0;

        try {

            reader = new BufferedReader(new FileReader("./temps/Linedtextfile.txt"));
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


        ProcessRunner("sh", "./scripts/list_voices.sh");
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

        if(name == null) {

            CreateAlert(AlertType.WARNING, "No Selected Text", "There are more that 40 words of text");

        } else {

            _lyrics.appendText(name + "\n");

        }

    }



    public void PreviewAudio() throws IOException, InterruptedException {

        String lyrics = _lyrics.getText();
        String[] arr = lyrics.split(" ");

        if (arr.length > 40){

            CreateAlert(AlertType.WARNING, "Too Many Words", "There are more that 40 words of text");

        } else {

        	_previewbutton.setDisable(true);

        	Thread thread = new Thread(new Task<Void>() {

				@Override
				protected Void call() throws Exception {

					ProcessBuilder builder = new ProcessBuilder("./scripts/festival_tts.sh",_chooseaccent.getSelectionModel().getSelectedItem(), _lyrics.getText());
	            	Process process = builder.start();
	            	int exit = process.waitFor();
                    if (exit != 0) {

                        Platform.runLater(()->{

                            CreateAlert(AlertType.ERROR, "Festival Error", "The text could not be handled by the current festival voice.\nTry another voice");

                        });

                        return null;

                    }

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
        }

    }



    public void NameFile() throws IOException, InterruptedException {

        String name = _filefield.getText();
        String lyrics = _lyrics.getText();
        lyrics = lyrics.replace("\n", " ");
        String[] arr = lyrics.split(" ");

        if (arr.length > 40) {

            CreateAlert(AlertType.WARNING, "Too Many Words", "There are more that 40 words of text");
            return;

        } else if (name == null || name.trim().isEmpty()) {

            CreateAlert(AlertType.WARNING, "Invalid Filename", "The filename is empty.");
            return;

        } else if (DirectoryServices.SearchDirectoryForName(_searchterm, name)) {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Overwrite Audio");
            alert.setHeaderText(null);
            alert.setContentText("The audio already exists.\nDo you want to ovewrite " + name + ".wav?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.NO) {

                return;

            }

        } else if(_searchterm == null ) {

            CreateAlert(AlertType.WARNING, "No Search Term", "You have not made a search");
            return;

        }

        _createbutton.setDisable(true);

        Thread thread = new Thread(new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                //right now only have it such that kal_diphone is in it
                ProcessBuilder builder = new ProcessBuilder("./scripts/festival_make_chunk.sh", name, _searchterm, _chooseaccent.getSelectionModel().getSelectedItem(), _lyrics.getText());
                Process process = builder.start();
                int exit = process.waitFor();

                if (exit != 0) {

                    Platform.runLater(() -> {

                        CreateAlert(AlertType.ERROR, "Festival Error", "The text could not be handled by the current festival voice.\nTry another voice");

                    });

                    return null;
                }

                return null;
            }

            @Override
            protected void done() {

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        CreateAlert(Alert.AlertType.INFORMATION, "Audio Made", "The audio file " + name + ".wav was made");
                        _lyrics.clear();
                        _filefield.clear();
                        _createbutton.setDisable(false);
                        try {
                            ProcessRunner("/bin/bash", "rm -f ./temps/*.txt");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                });

            }

        });

        thread.start();

    }


    @Override
    public String ReturnFXMLPath() {

        return _previousfxmlpath;

    }

    @Override
    public String ReturnForwardFXMLPath() {

        return null;

    }

    public void AuxiliaryFunctionBackwards(FXMLLoader loader) throws IOException {

        ProcessRunner("/bin/bash", "rm -f ./temps/*.txt");

    }




}

