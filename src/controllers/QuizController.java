package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class QuizController extends Controller {


    @FXML private Button _play;
    @FXML private MediaView _view;
    @FXML private TextField _textField;
    @FXML private Slider _slider;
    @FXML private Button _submit;
    @FXML private Label _outcome;
    @FXML private ImageView _result;

    private ArrayList<String> _listOfCreations;
    private String _previousfxmlpath =  "/fxml/ChooseQuiz.fxml";
    private File _fileUrl;
    private Media _video;
    private MediaPlayer _player;
    private int _numberCorrect = 0;
    private int _maximumScore;
    private double _rate = 1.0;
    

    public void junction(ActionEvent event) throws IOException, InterruptedException {

        if (_submit.getText().equals("Next")){

            _outcome.setText("");
            nextQuestion(event);

        } else {

            checkAnswer();

        }


    }




    public void playMedia() {

            _fileUrl = new File("./questions/" + _listOfCreations.get(0));
            _video = new Media(_fileUrl.toURI().toString());
            _player = new MediaPlayer(_video);
            _player.setAutoPlay(true);
            _view.setMediaPlayer(_player);
            _player.play();

        _player.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                double time = newValue.toMillis();
                _slider.adjustValue(time);

            }

        });

        _player.setOnReady(new Runnable() {

            @Override
            public void run() {

                _slider.setMin(0);
                _slider.setMax(_player.getTotalDuration().toMillis());

            }

        });

    }


    public void setFileList(ArrayList<String> listOfCreations){

        _listOfCreations = listOfCreations;
        _maximumScore = _listOfCreations.size();


    }



    public void checkAnswer() throws InterruptedException, IOException {
    	//nothing was entered, so do nothing
    	if(_textField.getText()==null || _textField.getText().toLowerCase().trim().equals("")) {
    		return;
    	}

        String file = _listOfCreations.get(0);
        String[] s = file.split("_");

        String[] answer = s[1].split("\\.");

        System.out.println(answer[0]);
        _player.stop();

        if (_textField.getText().toLowerCase().equals(answer[0].toLowerCase())){
            _numberCorrect++;
            _outcome.setText("Correct");
            _result.setImage(new Image(getClass().getResourceAsStream("/icons/right.png")));
        } else {
            _outcome.setText("Incorrect");
            _result.setImage(new Image(getClass().getResourceAsStream("/icons/wrong.png")));
        }

        _submit.setText("Next");

    }

    private void nextQuestion(ActionEvent event) throws IOException {

        _submit.setText("Submit");
        _result.setImage(null);
        _outcome.setText(null);
        _textField.setText(null);
        System.out.println("Hello");
        _listOfCreations.remove(0);

        if(_listOfCreations.size()== 0) {

            //Got to end screen
            SwitchForwardScene(event);

        } else  {

            playMedia();

        }


    }


    public void PausePlay(){

        if (_player.getStatus() == MediaPlayer.Status.PLAYING) {

            _player.pause();
            _play.setText("Play");
			Image image = new Image(getClass().getResourceAsStream("/icons/play.png"));
			_play.setGraphic(new ImageView(image));


        } else {

            _player.play();
            _play.setText("Pause");
			Image image = new Image(getClass().getResourceAsStream("/icons/pause.png"));
			_play.setGraphic(new ImageView(image));


        }
    }


    public void FastForward() {

        if ((_rate + 0.25) <= 2) {

            _rate = _rate + 0.25;
            _player.setRate(_rate);

        }

    }


    public void SlowDown() {

        if ((_rate - 0.25) > 0) {
            _rate = _rate - 0.25;
            _player.setRate(_rate);
        }

    }


    public void SkipForwardSeconds() {

        _player.seek(_player.getCurrentTime().add(Duration.seconds(5)));
    }


    public void SkipBackSeconds() {

        _player.seek(_player.getCurrentTime().add(Duration.seconds(-5)));
    }


    @Override
    public String ReturnFXMLPath() {

        return _previousfxmlpath;

    }


    @Override
    public String ReturnForwardFXMLPath() {

        return "/fxml/Score.fxml";

    }

    @Override
    public void AuxiliaryFunction(FXMLLoader loader){

        Score scoreController = loader.<Score>getController();

        scoreController.setLabel(_numberCorrect, _maximumScore);

    }
    
    @Override
    public void AuxiliaryFunctionBackwards(FXMLLoader loader) throws IOException {
    	// stops player when pressing back
    	_player.stop();
    }





}
