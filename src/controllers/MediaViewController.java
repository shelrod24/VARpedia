package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.io.File;

public class MediaViewController extends Controller{



    @FXML private Pane _anchor;
    @FXML private Slider _slider;
    private File _fileUrl;
    private Media _video;
    private MediaPlayer _player;
    private MediaView _view = new MediaView();
    private double _rate = 1.0;
    @FXML private Button _play;
    private String _previousfxmlpath = "/fxml/ListScene.fxml";



    public void playMedia(String medianame) {

        _fileUrl = new File("./Creations/"+medianame+".mp4");
        _video = new Media(_fileUrl.toURI().toString());
        _player = new MediaPlayer(_video);
        _player.setAutoPlay(true);
        _view.setMediaPlayer(_player);
        _view.setFitWidth(1000);
        _view.setFitHeight(440);
        _anchor.getChildren().addAll(_view);


    }



    public void Pause(){
        if(_player.getStatus() == MediaPlayer.Status.PLAYING ){
            _player.pause();
        }
    }




    public void Play(){
        if(_player.getStatus() == MediaPlayer.Status.PAUSED ){
            _player.play();
        }
    }


    public void FastForward(){
        if ((_rate + 0.25) <= 2) {
            _rate = _rate + 0.25;
            _player.setRate(_rate);
        }
    }

    public void SlowDown(){
        if ((_rate - 0.25) > 0) {
            _rate = _rate - 0.25;
            _player.setRate(_rate);
        }
    }

    public void OnDragOver(){
        if ((_rate - 0.25) > 0) {
            _rate = _rate - 0.25;
            _player.setRate(_rate);
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

    @Override
    public void AuxiliaryFunctionBackwards(FXMLLoader loader) {
        ListScene listcontroller = loader.<ListScene>getController();
        listcontroller.innitialiseList();
    }

}
