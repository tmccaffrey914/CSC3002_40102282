import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.StackPane;

public class DisplayOCs {

	//Set up Y Axis information
	private NumberAxis setUpYAxis() {
		NumberAxis y = new NumberAxis();
		
		//This allows the axis to grow and shrink depending on the data that it is plotting
		y.autosize();
		y.setLabel("Value");
		
		//This allows the chart to only display the highest value to the lowest, it doesn't have to show $0.00
		y.setForceZeroInRange(false);
		
		//This adds the dollar symbol before the values on the axis
		y.setTickLabelFormatter(new NumberAxis.DefaultFormatter(y,"$",null));
		return y;
	}

	//Set up X Axis information
	private CategoryAxis setUpXAxis() {
		CategoryAxis x = new CategoryAxis();
		x.setAnimated(true);
		x.setLabel("Time");
		return x;
	}

	//Public so that the other classes can get it.
	public LineChart<String, Number> displayOCValueChart(String ticker, double strike, double interestrate, double volatility){
		//Set Up X and Y Axis, X axis with string variables of SQL Timestamps
		CategoryAxis x = setUpXAxis();
		NumberAxis y = setUpYAxis();

		LineChart<String, Number> lines = new LineChart<String, Number>(x,y);
		
		//Conditional LineChart style depending on the Company
		if(ticker == "AAPL") {
			String css = this.getClass().getResource("/aaplchartstyle.css").toExternalForm();
			lines.getStylesheets().add(css);
		} else if(ticker == "FB") {
			String css = this.getClass().getResource("/fbchartstyle.css").toExternalForm();
			lines.getStylesheets().add(css);
		} else if(ticker == "YHOO") {
			String css = this.getClass().getResource("/yhoochartstyle.css").toExternalForm();
			lines.getStylesheets().add(css);
		} else {
			String css = this.getClass().getResource("/stockchartstyle.css").toExternalForm();
			lines.getStylesheets().add(css);
		} 

		//Use setUpData() method to create a series object based off the ResultSet
		XYChart.Series<String,Number> series = setUpData(ticker, strike, interestrate, volatility);

		//Do not display a chart title
		lines.setTitle(null);
		
		//Add series to lines
		lines.getData().add(series);

		return lines;
	}

	private Series<String, Number> setUpData(String ticker, double strike, double interestrate, double volatility) {
		//Connect to DB and search for all stocks from "ticker"
		QueryDB qry = new QueryDB();
		ResultSet rs = qry.getStockBidAsks(ticker);

		//Declare a series to add data from the DB to
		XYChart.Series<String, Number> series = new XYChart.Series<>();

		//This sets the legend for the chart
		series.setName("'" + ticker + "' Contract Value");

		//Used to turn timestamp object from the DB to a string that can be added to the categoryaxis
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

		try {
			while(rs.next()) {
				//Get data from the DB
				String time  = dateFormat.format(rs.getTimestamp("time"));
				float stockValue = rs.getFloat("askPrice");

				//Declare an object that has methods to calculate contract value
				GenerateCallOptionValue n = new GenerateCallOptionValue();
				//Calculate the value of the call option
				double valueOC = n.simulatedEuropeanCallOptionPrice(stockValue, 
						strike, interestrate, volatility, 1, 5000);

				//Declare a stackpane that displays a pop up when clicked
				StackPane sp = new StackPane();
				Data<String, Number> toAdd = new Data<>(time, valueOC);
				sp.setOnMouseClicked(e -> {
					//This pop up will display more detailed information on the clicked data-point
					PopUpBox.displayOCNodeInfo(valueOC, time, stockValue, strike, interestrate, volatility);
				});
				
				//Add the stack pane to the datapoint
				toAdd.setNode(sp);
				
				//Add the datapoint to the series
				series.getData().add(toAdd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				qry.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return series;
	}
}