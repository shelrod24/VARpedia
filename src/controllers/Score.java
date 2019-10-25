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

    public void setLabel(int correct, int maxScore){
        String outcome = "You got: "+ correct+ " out of "+ maxScore;
        _scorelabel.setText(outcome);
    }
    
    public void setChart(int correct, int maxScore) {
    	//stops animating the x axis to make it expand vertically
    	_xAxis.setAnimated(false);
    	_xAxis.setTickLabelGap(0);
    	
    	_yAxis.setLabel("Score");
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
