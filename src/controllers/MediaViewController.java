package controllers;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import java.io.File;


public class MediaViewController extends Controller{

    @FXML private Pane _anchor;
    @FXML private HBox _hBox;
    @FXML private Slider _slider;
    @FXML private Button _play;
    @FXML private Label _timelabel;
    private File _fileUrl;
    private Media _video;
    private MediaPlayer _player;
    private MediaView _view = new MediaView();
    private String _previousfxmlpath = "/fxml/ListScene.fxml";
    private boolean isrestart;
    private double _rate = 1.0;

    public void playMedia(String medianame) {

        _fileUrl = new File("./creations/" + medianame + ".mp4");
        _video = new Media(_fileUrl.toURI().toString());
        _player = new MediaPlayer(_video);
        _player.setAutoPlay(true);
        _view.setMediaPlayer(_player);
        _view.setFitWidth(1000);
        _view.setFitHeight(416);
        _hBox.getChildren().addAll(_view);


        _player.setOnReady(new Runnable() {

            @Override
            public void run() {

                Initialize();
                _slider.setMin(0);
                _slider.setMax(_player.getTotalDuration().toMillis());

            }

        });

    }



    public void PausePlay(){

        if (isrestart) {

            _player.seek(Duration.ZERO);
            isrestart = false;
            _player.play();
            _play.setText("Pause");

        } else {

            if (_player.getStatus() == MediaPlayer.Status.PLAYING) {

                _player.pause();
                _play.setText("Play");

            } else {

                _player.play();
                _play.setText("Pause");

            }
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

    public void SkipForwardTenSeconds() {

        _player.seek(_player.getCurrentTime().add(Duration.seconds(10)));
    }

    public void SkipBackTenSeconds() {

        _player.seek(_player.getCurrentTime().add(Duration.seconds(-10)));
    }


    @FXML
    public void Initialize(){

        _player.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                String time = "";
                time+=String.format("%02d", ((int)newValue.toMinutes()));;
                time+=":";
                time+=String.format("%02d", ((int)newValue.toSeconds()) % 60);
                _timelabel.setText(time);

            }

        });


        //THIS IS GOING TO CHANGE THE START BUTTON TO RESET.
        _player.setOnEndOfMedia(()-> {

            _slider.adjustValue(_player.getTotalDuration().toMillis());
            _play.setText("Restart");
            isrestart = true;

        });


        _player.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                double time = newValue.toMillis();
                _slider.adjustValue(time);

            }

        });

        isrestart = false;

    }


    @Override
    public String ReturnFXMLPath() {

        return _previousfxmlpath;

    }

    @Override
    public String ReturnForwardFXMLPath() {

        return null;

    }

    @Override
    public void AuxiliaryFunctionBackwards(FXMLLoader loader) {

        _player.stop();
        ListScene listcontroller = loader.<ListScene>getController();
        listcontroller.innitialiseList();

    }

}
