package controllers;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import java.io.File;


public class MediaViewController extends Controller{

    @FXML private HBox _hBox;
    @FXML private Slider _slider;
    @FXML private Button _play;
    @FXML private Label _timelabel;
    private String _previousfxmlpath =  "/fxml/ListScene.fxml";
    private File _fileUrl;
    private Media _video;
    private MediaPlayer _player;
    private MediaView _view = new MediaView();
    private double _rate = 1.0;

    @FXML
    /**
     * This method configures the media player by outlining what should happen when the media ends and
     * outlining what should happen when the current time property of the media player changes, this is
     * called as part of the initialisation process and by playMedia().
     */
    public void initializePlayer(){

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

        _player.setOnEndOfMedia(()-> {
            _player.seek(Duration.ZERO);
            _slider.adjustValue(0);
            _player.pause();
            _play.setText("Play");
            Image image = new Image(getClass().getResourceAsStream("/icons/play.png"));
            _play.setGraphic(new ImageView(image));
        });

        _player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                double time = newValue.toMillis();
                _slider.adjustValue(time);
            }
        });
    }

    /**
     * @param medianame
     * This method is called by the controller of the previous scene and recieves the name of a creation to play,
     * it then finds the MP4 file to play, sets up the player and the view, and also sets up the slider.
     */
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

                initializePlayer();
                _slider.setMin(0);
                _slider.setMax(_player.getTotalDuration().toMillis());
                _slider.valueProperty().addListener(observable -> {
                    if (_slider.isPressed()){
                        _player.seek(new Duration(_slider.getValue()));
                    }
                });
            }
        });
    }

    /**
     * This method just pauses or plays the media depending on the media players current status.
     */
    public void pausePlay(){

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

    /**
     * This method just increases the playback speed of the media player.
     */
    public void fastForward() {
        if ((_rate + 0.25) <= 2) {
            _rate = _rate + 0.25;
            _player.setRate(_rate);
        }
    }

    /**
     * This method just decreases the playback speed of the media player.
     */
    public void slowDown() {
        if ((_rate - 0.25) > 0) {
            _rate = _rate - 0.25;
            _player.setRate(_rate);
        }
    }

    /**
     * This method increases the elapsed time of the media player by 5 seconds.
     */
    public void skipForwardSeconds() {
        if(_player.getStatus() == MediaPlayer.Status.PAUSED){
            //Do nothing
        } else {
            _player.seek(_player.getCurrentTime().add(Duration.seconds(5)));
        }
    }

    /**
     * This method decreases the elapsed time of the media player by 5 seconds.
     */
    public void skipBackSeconds() {
        if(_player.getStatus() == MediaPlayer.Status.PAUSED){
            //Do nothing
        } else {
            _player.seek(_player.getCurrentTime().add(Duration.seconds(-5)));
        }
    }

    @Override
    public String returnFXMLPath() {
        return _previousfxmlpath;
    }

    @Override
    public String returnForwardFXMLPath() {
        return null;
    }

    @Override
    public void auxiliaryFunctionBackwards(FXMLLoader loader) {
        _player.stop();
        ListScene listcontroller = loader.<ListScene>getController();
        listcontroller.innitialiseList();
    }

}
