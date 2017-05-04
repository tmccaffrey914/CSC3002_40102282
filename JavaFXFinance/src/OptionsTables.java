import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import javafx.collections.ObservableList;

import javafx.geometry.Orientation;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class OptionsTables {
	private BorderPane main;
	
	public BorderPane setUpOptionsTable(String ticker) {
		//The elements will be added to this BorderPane which is later returned
		main = new BorderPane();
		
		String css = this.getClass().getResource("/scrollstyle.css").toExternalForm();
		
		//This creates a Scroll Bar window to allow the user to scroll through the rows of data without resizing the window.
		ScrollPane scroll = new ScrollPane();
		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		//Set the style of the ScrollBar
		scroll.getStylesheets().add(css);
		
		//This is for setting up the Column titles for the table
		TilePane columnHeaders = new TilePane(Orientation.HORIZONTAL);
		columnHeaders.setPrefColumns(7);
		columnHeaders.setStyle("-fx-background-color: #AB4642;-fx-alignment: top-left;");
		columnHeaders.setHgap(75);
		Label[] clabels = new Label[] {
				new Label("	ID"),
				new Label("ExpDate"),
				new Label("Ticker"),
				new Label("StrikePrice"),
				new Label("InterestRate"),
				new Label("Volatility"),
				new Label("Value")
		};

		//This formats the Column headers so that their text is white
		for ( int i = 0; i < 7; i++ ) {
			clabels[i].setTextFill(Color.WHITE);		
			clabels[i].setScaleShape(true);
		}
		
		ObservableList<Node> clist = columnHeaders.getChildren(); 
		clist.addAll(clabels);
				
		//This is so we only get the date from the DB and not the full timestamp.
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		//Query Object to allow us to query the DB
		QueryDB qry = new QueryDB();
		
		//This will be used to add each row from the database to, (like the first array in a 2D array) VBox is used because it
		// lays out its elements in a vertical pattern.
		VBox listOfRows = new VBox();
		
		//This will be used to set each row from the DB to be a different colour, just so the table is easier to read.
		int rowFormatCounter = 0;

		try {
			ResultSet rs = qry.getAllOptionsContracts(ticker);

			while(rs.next()){
				rowFormatCounter++;
				
				//Create a TilePane object to store each row of data from the DB
				TilePane rows = new TilePane(Orientation.HORIZONTAL);
				rows.setPrefColumns(7);
				rows.setHgap(80);
				rows.setVgap(5);
				
				//This if statement sets the background of each row to a different colour
				if(rowFormatCounter % 2 == 0) {
					rows.setStyle("-fx-background-color: #dedede; -fx-alignment: center;");	
				} else {
					rows.setStyle("-fx-background-color: #f7f8f9; -fx-alignment: center;");	
				}
				
				//Get data from ResultSet
				int id = rs.getInt("id");
				String expirationdate  = dateFormat.format(rs.getTimestamp("expirationdate"));
				double strike = rs.getDouble("strikeprice");
				double interestrate = rs.getDouble("interestrate");
				double volatility = rs.getDouble("volatility");

				//Add Data from ResultSet to Labels which can be added to the TilePane layout
				Label[] rlabels = new Label[] {
						new Label(String.valueOf(id)),
						new Label(String.valueOf(expirationdate)),
						new Label(ticker),
						new Label(String.valueOf(new BigDecimal(Double.toString(strike)).setScale(2, RoundingMode.HALF_EVEN))),
						new Label(String.valueOf(interestrate)),
						new Label(String.valueOf(volatility)),
						
				};
				
				//Button to open a chart based on each rows data
				Button openChart = new Button("View");
				openChart.setOnAction(e -> {
					PopUpBox.optionsContractsWindow(ticker, strike, interestrate, volatility);
				});

				//Add everything to the TilePane row
				ObservableList<Node> rlist = rows.getChildren();
				rlist.addAll(rlabels);
				rlist.add(openChart);
				
				//Add the row to the vertical list of rows.
				listOfRows.getChildren().add(rows);
			}
			
			//Add the list of rows to the scroll pane
			scroll.setContent(listOfRows);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			qry.closeConnection();
		}
		
		main.setTop(columnHeaders);
		main.setCenter(scroll);
		return main;
	}

}
