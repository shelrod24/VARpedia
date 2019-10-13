package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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

    private ArrayList<String> _listOfCreations;
    private String _previousfxmlpath =  "/fxml/ChooseQuiz.fxml";
    private File _fileUrl;
    private Media _video;
    private MediaPlayer _player;
    private int numberCorrect = 0;
    private double _rate = 1.0;



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

    }



    public void checkAnswer(ActionEvent event) throws InterruptedException, IOException {

        String file = _listOfCreations.get(0);
        String[] s = file.split("_");
        System.out.println(s[1]);
        _player.stop();

        if (_textField.getText().equals(s[1])){
            System.out.println("Correct");
            numberCorrect++;
        }

        nextQuestion(event);

    }

    private void nextQuestion(ActionEvent event) throws IOException {

        _listOfCreations.remove(0);
        System.out.println(_listOfCreations.size());

        if(_listOfCreations.size()== 0) {

            //Got to end screen
            SwitchBackScene(event);

        } else  {

            playMedia();

        }


    }


    public void PausePlay(){

        if (_player.getStatus() == MediaPlayer.Status.PLAYING) {

            _player.pause();
            _play.setText("Play");

        } else {

            _player.play();
            _play.setText("Pause");

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

        return null;

    }




}
