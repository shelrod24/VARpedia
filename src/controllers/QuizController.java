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
    @FXML private Label _answer;

    private ArrayList<String> _listOfCreations;
    private String _previousfxmlpath =  "/fxml/ChooseQuiz.fxml";
    private File _fileUrl;
    private Media _video;
    private MediaPlayer _player;
    private int _numberCorrect = 0;
    private int _maximumScore;
    private double _rate = 1.0;


    /**
     * @param event
     * Depending on the text of the button when it is pressed, it will direct the control flow to either check the
     * submitted answer, or go to the next question.
     */
    public void junction(ActionEvent event) throws IOException, InterruptedException {
        if (_submit.getText().equals("Next")){
            _outcome.setText("");
            _answer.setVisible(false);
            nextQuestion(event);
        } else {
            checkAnswer();
        }
    }

    /**
     * This method gets the next creation to be played and sets up the media player, the view and the slider. it then plays the
     * video
     */
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
                _slider.valueProperty().addListener(observable -> {
                    if (_slider.isPressed()){
                        _player.seek(new Duration(_slider.getValue()));
                    }
                });

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

    }

    /**
     * This method is called by the controller of the previous scene and recieves a list of creations to play as
     * part of the quiz.
     */
    public void setFileList(ArrayList<String> listOfCreations){
        _listOfCreations = listOfCreations;
        _maximumScore = _listOfCreations.size();
    }

    /**
     * This method checks the submitted answer of the user against the correct answer. It is called when the user pressed
     * submit.
     */
    public void checkAnswer() throws InterruptedException, IOException {
    	//nothing was entered, so do nothing
    	if(_textField.getText()==null || _textField.getText().toLowerCase().trim().equals("")) {
    		return;
    	}

        String file = _listOfCreations.get(0);
        String[] s = file.split("_");

        String[] answer = s[1].split("\\.");

        _answer.setVisible(true);
        _answer.setText(answer[0]);
        _player.stop();

        if (_textField.getText().trim().toLowerCase().equals(answer[0].toLowerCase())){
            _numberCorrect++;
            _outcome.setText("Correct");
            _result.setImage(new Image(getClass().getResourceAsStream("/icons/right.png")));
        } else {
            _outcome.setText("Incorrect");
            _result.setImage(new Image(getClass().getResourceAsStream("/icons/wrong.png")));
        }
        _submit.setText("Next");
    }

    /**
     * The method is called when the user clicks "Next" from the junction() method.
     * This method removes the first index in the list of creations and shifts the other creations down. If there is no creation
     * to play, then it switches to the score scene, else it calls playMedia();
     */
    private void nextQuestion(ActionEvent event) throws IOException {

        _submit.setText("Submit");
        _result.setImage(null);
        _outcome.setText(null);
        _textField.setText(null);
        _listOfCreations.remove(0);

        if(_listOfCreations.size()== 0) {
            //Got to end screen
            switchForwardScene(event);
        } else  {
            _play.setText("Pause");
            Image image = new Image(getClass().getResourceAsStream("/icons/pause.png"));
            _play.setGraphic(new ImageView(image));
            playMedia();
        }
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
        return "/fxml/Score.fxml";
    }

    @Override
    public void auxiliaryFunction(FXMLLoader loader){
        Score scoreController = loader.<Score>getController();
        scoreController.setLabel(_numberCorrect, _maximumScore);
        scoreController.setChart(_numberCorrect, _maximumScore);
    }
    
    @Override
    public void auxiliaryFunctionBackwards(FXMLLoader loader) throws IOException {
    	// stops player when pressing back
    	_player.stop();
    }

}
