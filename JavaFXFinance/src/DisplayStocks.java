import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.StackPane;

import javafx.util.Duration;

public class DisplayStocks {
	private Timeline animation;

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
		//So that the chart does not display all of the timestamps for each data point
		x.setTickLabelsVisible(false);
		x.setLabel("Time");
		return x;
	}

	public LineChart<String, Number> drawChart(String ticker) {
		//Set up x and y axis' using the methods above
		CategoryAxis x = setUpXAxis();
		NumberAxis y = setUpYAxis();

		//Declare a new LineChart object and apply CSS script to it, this is where the data will be displayed
		LineChart<String, Number> lines = new LineChart<String, Number>(x,y);
		String css = this.getClass().getResource("/stockchartstyle.css").toExternalForm();
		lines.getStylesheets().add(css);
		
		//Set title of the chart, and state that it is animated.
		lines.setTitle(ticker+" Stock Monitoring, 2017");
		lines.setAnimated(true);

		//This is the collection of data points that will be displayed on the chart
		XYChart.Series<String, Number> series = new XYChart.Series<>();

		//This is the charts "Key", informing the user what the line represents
		series.setName("'" + ticker + "' Value over Time");

		//For formatting the date, and eventually turning it into a String datatype
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//Connect to DB and search for all stocks with "ticker" value
		QueryDB qry = new QueryDB();
		ResultSet rs = qry.getStockBidAsks(ticker);

		//Create a timeline of keyframes to use as an animation for the chart
		animation = new Timeline();

		//This method will animate the graph as well as fetching and plotting the data
		animation.getKeyFrames().add(new KeyFrame(Duration.millis(1000/60), new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent actionEvent) {	
				try {
					while(rs.next()) {
						String time  = dateFormat.format(rs.getTimestamp("time"));
						double value = rs.getDouble("bidPrice");
						
						//Create a StackPane to add to the datapoint
						StackPane sp = new StackPane();
						Data<String, Number> toAdd = new Data<>(time, value);
						
						//Set an action on the StackPane to display information on the current ResultSet value
						sp.setOnMouseClicked(e -> {
							PopUpBox.displayNodeInfo(time);
						});

						//Add the StackPane to the data point
						toAdd.setNode(sp);
						
						//Add the data point to the series
						series.getData().add(toAdd);						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}));

		//Add the series of Data Points to the line graph
		lines.getData().add(series);

		//Set the animation to run for the same number of cycles as the query result has rows
		animation.setCycleCount(Animation.INDEFINITE);
		
		//Play the animation
		animation.play();

		return lines;
	}
}
		
