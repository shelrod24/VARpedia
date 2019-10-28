package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.io.IOException;

public class Score extends Controller {
	
	private final String _backFXMLPath="/fxml/MainMenu.fxml";

	@FXML Label _scorelabel;
    @FXML BarChart<String, Integer> _chart;
    @FXML private CategoryAxis _xAxis;
	@FXML private NumberAxis _yAxis;

    /**
     * @param correct
     * @param maxScore
     * This method is called by the controller of the previous scene, simply displays the amount of questions the user
     * got correct. It gets the information required from the previous scenes controller.
     */
    public void setLabel(int correct, int maxScore){
        String outcome = "You got: "+ correct+ " out of "+ maxScore + " correct";
        _scorelabel.setText(outcome);
    }

    /**
     * This method is called by the controller of the previous scene, creates the barchart and initialises it with the
     * data from the previous scene
     * @param correct the number correctly answered in the quiz
     * @param maxScore the total number of questions in the quiz
     */
    public void setChart(int correct, int maxScore) {
    	//stops animating the x axis to make it expand vertically
    	_xAxis.setAnimated(false);
    	_xAxis.setTickLabelGap(0);
    	
    	_yAxis.setAutoRanging(false);
    	//set upper bound to be max score as a visual comparison
    	_yAxis.setUpperBound(maxScore);
    	//set the units to be whole numbers
    	_yAxis.setTickUnit(1);
    	
    	int incorrect = maxScore-correct;
    	//make a series with the required data
    	XYChart.Series<String, Integer> correctSeries =  new XYChart.Series<String, Integer>();
    	correctSeries.getData().add(new XYChart.Data<String, Integer>("Correct", correct));
    	correctSeries.getData().add(new XYChart.Data<String, Integer>("Incorrect", incorrect));
    	
    	//add series to the BarChart
    	_chart.getData().add(correctSeries);
    	
    	//chnage the color of the bars depending on the position
    	Node n = _chart.lookup(".data0.chart-bar");
        n.setStyle("-fx-bar-fill: #abb7ff");
        n = _chart.lookup(".data1.chart-bar");
        n.setStyle("-fx-bar-fill: #ff9685");
    }



    @Override
    public String returnFXMLPath() {
		return _backFXMLPath;
    }

    @Override
    public String returnForwardFXMLPath() {
        return "/fxml/ChooseQuiz.fxml";
    }


}
