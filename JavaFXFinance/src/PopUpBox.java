import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;

import javafx.collections.ObservableList;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopUpBox {
	public static String ticker;
	public static String msg;

	//This method creates a pop up window showing information on a data point inside a chart. This method is for a relational 
	// DB chart specifically.
	public static void displayNodeInfo(String timestamp) {
		Stage displayNode = new Stage();

		//This is to format the String value of a Timestamp
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		displayNode.initModality(Modality.APPLICATION_MODAL);
		displayNode.setTitle("Details:");
		displayNode.setMinWidth(250);

		BorderPane bordered = new BorderPane();

		//Set a row of labels to represent column headers
		TilePane columns = new TilePane(Orientation.HORIZONTAL);
		columns.setPrefColumns(6);
		columns.setStyle("-fx-background-color: #AB4642;-fx-alignment: center;");
		columns.setHgap(90);
		Label[] clabels = new Label[] {
				new Label("bidsize"),
				new Label("bidprice"),
				new Label("time"),
				new Label("ticker"),
				new Label("asksize"),
				new Label("askprice")
		};

		for ( int i = 0; i < 6; i++ ) {
			clabels[i].setTextFill(Color.WHITE);		
			clabels[i].setScaleShape(true);
		}

		ObservableList<Node> clist = columns.getChildren(); 

		clist.addAll(clabels);

		//Add the column headers to the top of the bordered layout
		bordered.setTop(columns);

		TilePane rows = new TilePane(Orientation.HORIZONTAL);
		rows.setHgap(15);
		rows.setStyle("-fx-alignment: top-center;");

		QueryDB qry = new QueryDB();

		try {
			//This method gets one row from the database using the 'timestamp' value of the datapoint that the user
			// just clicked on.
			ResultSet rs = qry.getOneStock(timestamp);

			//This 'if' statement sets the data for the only row in the table
			if(rs.next()){
				int bdsize = rs.getInt("bidsize");
				double bdprice = rs.getDouble("bidprice");
				String time  = dateFormat.format(rs.getTimestamp("time"));
				String ticker = rs.getString("ticker");
				int aksize = rs.getInt("asksize");
				double akprice = rs.getDouble("askprice");

				Label[] rlabels = new Label[] {
						new Label(String.valueOf(bdsize)),
						new Label(String.valueOf(bdprice)),
						new Label(time),
						new Label(ticker),
						new Label(String.valueOf(aksize)),
						new Label(String.valueOf(akprice))
				};

				ObservableList<Node> rlist = rows.getChildren();
				rlist.addAll(rlabels);

				//Add the row to the window
				bordered.setCenter(rows);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			qry.closeConnection();
		}

		bordered.setMinWidth(750);
		Scene scene = new Scene(bordered);
		displayNode.setScene(scene);
		displayNode.setResizable(false);
		displayNode.show();
	}

	//This method creates a pop up window showing information on a data point inside a chart. This method is for a flatfile DB
	// chart specifically.
	public static void displayFFNodeInfo(String timestamp) {
		Stage displayNode = new Stage();

		//This is to format the String value of a Timestamp
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		displayNode.initModality(Modality.APPLICATION_MODAL);
		displayNode.setTitle("Details:");
		displayNode.setMinWidth(250);

		BorderPane bordered = new BorderPane();

		//Set a row of labels to represent column headers
		TilePane columns = new TilePane(Orientation.HORIZONTAL);
		columns.setPrefColumns(6);
		columns.setStyle("-fx-background-color: #AB4642;-fx-alignment: center;");
		columns.setHgap(90);
		Label[] clabels = new Label[] {
				new Label("bidsize"),
				new Label("bidprice"),
				new Label("time"),
				new Label("ticker"),
				new Label("asksize"),
				new Label("askprice")
		};

		for ( int i = 0; i < 6; i++ ) {
			clabels[i].setTextFill(Color.WHITE);		
			clabels[i].setScaleShape(true);
		}

		ObservableList<Node> clist = columns.getChildren(); 

		clist.addAll(clabels);
		//Add the column headers to the top of the bordered layout
		bordered.setTop(columns);

		TilePane rows = new TilePane(Orientation.HORIZONTAL);
		rows.setHgap(15);
		rows.setStyle("-fx-alignment: top-center;");

		QueryDB qry = new QueryDB();

		try {
			ResultSet askrs = qry.getOneFFStock(timestamp, "ask");
			ResultSet bidrs = qry.getOneFFStock(timestamp, "bid");
			ArrayList<Label> rlabels = new ArrayList<Label>();

			if(bidrs.next()) {
				int bdsize = bidrs.getInt("size");
				double bdprice = bidrs.getDouble("price");
				rlabels.add(new Label(String.valueOf(bdsize)));
				rlabels.add(new Label(String.valueOf(bdprice)));
			}

			if(askrs.next()){
				String time  = dateFormat.format(askrs.getTimestamp("timest"));
				String ticker = askrs.getString("ticker");
				int aksize = askrs.getInt("size");
				double akprice = askrs.getDouble("price");

				rlabels.add(new Label(time));
				rlabels.add(new Label(ticker));
				rlabels.add(new Label(String.valueOf(aksize)));
				rlabels.add(new Label(String.valueOf(akprice)));
			}

			ObservableList<Node> rlist = rows.getChildren();
			rlist.addAll(rlabels);
			bordered.setCenter(rows);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			qry.closeConnection();
		}

		bordered.setMinWidth(750);
		Scene scene = new Scene(bordered);
		displayNode.setScene(scene);
		displayNode.setResizable(false);
		displayNode.show();
	}


	//This method asks the user to enter a ticker symbol and then this information is returned
	public static String askForTicker() {
		Stage enterTicker = new Stage();

		//Set up the window
		enterTicker.initModality(Modality.APPLICATION_MODAL);
		enterTicker.setTitle("View Shares");
		enterTicker.setMinWidth(260);

		//Add label to guide the user on what info to enter
		Label msg = new Label();
		msg.setText("Enter Ticker: ");

		//Add the text field where the data will be input
		TextField enterData = new TextField();

		//Add a few prompt ticker symbols as an example
		enterData.setPromptText("AAPL, MSFT, FB...");

		//This sets the size of the textfield
		enterData.setPrefColumnCount(10);

		//Add go and cancel buttons
		Button goToStocksDisplay = new Button("Go");
		goToStocksDisplay.setOnAction(e -> {
			ticker = enterData.getText();

			//Verifying User input
			if(ticker.length() > 4) {
				//Window to pop up and display error message
				Stage window = displayError("Ticker must be less than 5 charaters long");
				window.show();

			} else if(ticker.length() == 0) {
				//Window to pop up and display error message
				Stage window = displayError("Ticker must not be empty");
				window.show();
			} else {
				//So that the ticker is always uppercase when it is displayed on the chart
				ticker = ticker.toUpperCase();
				enterTicker.close();
			}
		});
		Button goToMainMenu = new Button("Back");
		goToMainMenu.setOnAction(e -> {
			ticker = "close";
			enterTicker.close();
		});

		//Create a vertical layout box and add the nodes to it.
		VBox layout = new VBox();
		layout.setSpacing(5);
		layout.getChildren().addAll(msg, enterData, goToStocksDisplay, goToMainMenu);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		enterTicker.setScene(scene);

		//Do not let the user resize the window.
		enterTicker.setResizable(false);

		//So that the program doesn't take us to the charts screen if the user hits the top right close window btn
		enterTicker.setOnCloseRequest(e -> {
			ticker = "close";
		});

		//Display the window and then wait for the user to input the information or cancel.
		enterTicker.showAndWait();

		return ticker;
	}

	//This displays a chart based on the Black-Scholes calculation showing Contact Values over time
	public static void optionsContractsWindow(String ticker, double strike, double interestrate, double volatility) {
		//This is the popup window that the chart will be displayed in
		Stage displayChart = new Stage();

		//Setting how the window will look
		displayChart.initModality(Modality.APPLICATION_MODAL);
		displayChart.setTitle("Contract Value:");
		displayChart.setMinWidth(500);
		displayChart.setMinHeight(500);

		//Title Label styled with CSS
		Label title = new Label("Contract Value Over Time");
		title.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 18pt; -fx-alignment: center;");
		title.setMaxWidth(Double.POSITIVE_INFINITY);
		title.setMaxHeight(Double.POSITIVE_INFINITY);

		//Set up the bordered layout
		BorderPane bordered = new BorderPane();
		bordered.setTop(title);
		bordered.setStyle("-fx-background-color: #adadad;");

		//Create the chart object that the Options Contract Values will be displayed on
		DisplayOCs chart = new DisplayOCs();
		bordered.setCenter(chart.displayOCValueChart(ticker, strike, interestrate, volatility));

		Scene scene = new Scene(bordered);
		displayChart.setScene(scene);
		displayChart.setResizable(true);
		displayChart.show();
	}


	//This method displays information about a data point inside an options contract chart.
	public static void displayOCNodeInfo(double contractValue, String timestamp, double stockValue, double strikePrice, double interestRate, double volatility) {
		Stage displayNode = new Stage();

		displayNode.initModality(Modality.APPLICATION_MODAL);
		displayNode.setTitle("Details:");
		displayNode.setMinWidth(250);

		BorderPane bordered = new BorderPane();

		//Set a row of labels to represent column headers
		TilePane columns = new TilePane(Orientation.HORIZONTAL);
		columns.setPrefColumns(6);
		columns.setStyle("-fx-background-color: #AB4642;-fx-alignment: center;");
		columns.setHgap(50);
		Label[] clabels = new Label[] {
				new Label("StockValue"),
				new Label("StrikePrice"),
				new Label("InterestRate"),
				new Label("Volatility"),
				new Label("Timestamp"),
				new Label("ContractValue")
		};

		//This for loop is to set all of the column headers to use white text
		for ( int i = 0; i < 6; i++ ) {
			clabels[i].setTextFill(Color.WHITE);		
			clabels[i].setScaleShape(true);
		}

		ObservableList<Node> clist = columns.getChildren(); 
		clist.addAll(clabels);

		//Add columns to the top of the layout
		bordered.setTop(columns);

		//A tilepane object to display the row of data from the database
		TilePane rows = new TilePane(Orientation.HORIZONTAL);
		rows.setHgap(70);
		rows.setPrefColumns(6);
		rows.setStyle("-fx-alignment: top-center;");

		Label[] rlabels = new Label[] {
				new Label(String.valueOf(new BigDecimal(Double.toString(stockValue)).setScale(2, RoundingMode.HALF_EVEN))),
				new Label(String.valueOf(new BigDecimal(Double.toString(strikePrice)).setScale(2, RoundingMode.HALF_EVEN))),
				new Label(String.valueOf(interestRate)),
				new Label(String.valueOf(volatility)),
				new Label(timestamp),
				/*
				 * This label uses a BigDecimal object and the static HALF_UP method from the RoundingMode math classes to limit
				 * the Contract Value field to two decimal places, I have chosen the HALF_UP method of rounding because it is
				 * more commonly used in banking.
				 */
				new Label(String.valueOf(new BigDecimal(Double.toString(contractValue)).setScale(2, RoundingMode.HALF_EVEN)))
		};

		ObservableList<Node> rlist = rows.getChildren();
		rlist.addAll(rlabels);
		bordered.setCenter(rows);

		bordered.setMinWidth(750);
		Scene scene = new Scene(bordered);
		displayNode.setScene(scene);
		displayNode.setResizable(false);
		displayNode.show();
	}


	public static String addUser() {
		Stage addUser = new Stage();

		//This sets the message to be returned to "close", so that the window class knows to stay on the start screen
		addUser.setOnCloseRequest(e -> {
			msg = "close";
		});

		//Set up the window
		addUser.initModality(Modality.APPLICATION_MODAL);
		addUser.setTitle("Add Trader");
		addUser.setMinWidth(400);

		//Title
		Label title = new Label("New User");
		title.setStyle("-fx-text-fill: #525252; -fx-font-size: 16pt; -fx-alignment: center;");
		title.setMaxWidth(Double.POSITIVE_INFINITY);
		title.setMaxHeight(Double.POSITIVE_INFINITY);

		//Name
		Label namemsg = new Label();
		namemsg.setText("	Enter Username: ");
		namemsg.setStyle("-fx-text-fill: #FFFFFF;");
		TextField enterName = new TextField();
		enterName.setPromptText("Joe Bloggs...");
		enterName.setPrefColumnCount(20);
		QueryDB qry = new QueryDB();

		//Phone
		Label phonemsg = new Label();
		phonemsg.setText("	Enter Phone: ");
		phonemsg.setStyle("-fx-text-fill: #FFFFFF;");
		TextField enterPhone = new TextField();
		enterPhone.setPromptText("02812345678");
		enterPhone.setPrefColumnCount(20);

		//Password1
		Label pwrd1msg = new Label();
		pwrd1msg.setText("	Enter Password: ");
		pwrd1msg.setStyle("-fx-text-fill: #FFFFFF;");
		TextField enterPwrd1 = new TextField();
		enterPwrd1.setPromptText("P*******123");
		enterPwrd1.setPrefColumnCount(20);

		//Password2
		Label pwrd2msg = new Label();
		pwrd2msg.setText("	Re-enter Password: ");
		pwrd2msg.setStyle("-fx-text-fill: #FFFFFF;");
		TextField enterPwrd2 = new TextField();
		enterPwrd2.setPromptText("P*******123");
		enterPwrd2.setPrefColumnCount(20);

		//Stocks Trading In
		Label ticker = new Label();
		ticker.setText("	Enter Stock Ticker: ");
		ticker.setStyle("-fx-text-fill: #FFFFFF;");
		TextField tradingIn = new TextField();
		tradingIn.setPromptText("AAPL, FB, MSFT...");
		tradingIn.setPrefColumnCount(20);

		//Add go and cancel buttons
		Button enter = new Button("Go");
		enter.setOnAction(e -> {
			//Set Message to go to the main menu
			msg = "Go";
			String name = enterName.getText();
			String phone = enterPhone.getText();
			String pwrd1 = enterPwrd1.getText();
			String pwrd2 = enterPwrd2.getText();
			String trading = tradingIn.getText();

			//Verifying User input
			if(!pwrd1.equals(pwrd2)) {
				//Window to pop up and display error message
				Stage window = displayError("Passwords do not match");
				window.show();
			} else if(name.length() == 0 || phone.length() == 0 || pwrd1.length() == 0 
					|| pwrd2.length() == 0 || trading.length() == 0) {
				//Window to pop up and display error message
				Stage window = displayError("ALL fields must be filled");
				window.show();
			} else if(qry.doesNotExist(name) == false) {
				//Window to pop up and display error message
				Stage window = displayError("Sorry Username Taken");
				window.show();
			} else if(trading.length() > 4) {
				//Window to pop up and display error message
				Stage window = displayError("Ticker must be less than 5 characters long");
				window.show();
			} else if(phone.length() < 11) {
				//Window to pop up and display error message
				Stage window = displayError("Phone Field Must Be 11 Digits Long");
				window.show();
			} else if(!validPhone(phone)) {
				//Window to pop up and display error message
				Stage window = displayError("Phone Field Must only contain numbers");
				window.show();
			} else if(phone.length() > 11) {
				//Window to pop up and display error message
				Stage window = displayError("Phone Field Must Be 11 Digits Long");
				window.show();
			} else if(!validPassword(pwrd1)) {
				//Window to pop up and display error message
				Stage window = displayError("Password must be 8 characters long, \n with upper, lowercase letters and a number");
				window.show();
			} else {
				//So that the ticker is always uppercase when it is displayed on the chart
				trading = trading.toUpperCase();

				//Boolean variable to say whether or not the DB add was successful
				boolean success = false;

				//If add was unsuccessful, try again
				while (!success) {
					//Add new trader to the database with encrypted password and salt
					success = qry.addTrader(name, phone, pwrd1, 1, trading);					
				}
				//Close window
				addUser.close();
			}
		});

		//This sets the message to be returned to "close", so that the window class knows to stay on the start screen
		Button goToMainMenu = new Button("Back");
		goToMainMenu.setOnAction(e -> {
			msg = "close";
			addUser.close();
		});

		//Create a vertical layout box and add the data input nodes to it.
		VBox textFields = new VBox(10);
		textFields.setSpacing(5);
		textFields.getChildren().addAll(namemsg, enterName, phonemsg, enterPhone, pwrd1msg, enterPwrd1,
				pwrd2msg, enterPwrd2, ticker, tradingIn);
		textFields.setAlignment(Pos.CENTER_LEFT);

		//Create a horizontal layout and add the buttons to it
		HBox action = new HBox();
		action.setSpacing(25);
		action.getChildren().addAll(enter, goToMainMenu);
		action.setAlignment(Pos.CENTER);

		//Add both layouts to a border pane layout and create a new scene
		BorderPane layout = new BorderPane();
		layout.setTop(title);
		layout.setCenter(textFields);
		layout.setBottom(action);

		//These two labels are to give a gap on the left and right margins
		Label r = new Label(" ");
		Label l = new Label(" ");
		layout.setRight(r);
		layout.setLeft(l);

		//Set the scene
		layout.setStyle("-fx-background-color: #adadad;");
		Scene scene = new Scene(layout);
		addUser.setScene(scene);
		addUser.setResizable(false);

		//Display the window and then wait for the user to input the information or cancel.
		addUser.showAndWait();

		//Return the message to say whether or not to move to the main menu
		return msg;
	}

	//This method checks if the phone number entered is valid
	private static boolean validPhone(String phone) {
		char current;

		for(int i = 0; i < phone.length(); i++) {
			current = phone.charAt(i);
			//If the current character in the string is not a digit, return false
			if(!Character.isDigit(current)) {
				return false;
			}
		}
		//If every character in the string is a digit, return true
		return true;
	}

	private static boolean validPassword(String pwrd) {
		//These boolean variables say whether or not certain password requirements are met
		boolean isNumber = false;
		boolean isUCase = false;
		boolean isLCase = false;

		//Current character in the string sequence
		char current;

		if(pwrd.length() < 8) {
			//Password is not long enough, return false
			return false;
		}
		for(int i = 0; i < pwrd.length(); i++) {
			current = pwrd.charAt(i);
			if(Character.isLowerCase(current)) {
				//Current character is lower-case, move to next character.
				isLCase = true;
				continue;
			}
			if(Character.isUpperCase(current)) {
				//Current character is Upper-Case, move to next character.
				isUCase = true;
				continue;
			}
			if(Character.isDigit(current)) {
				//Current character is a Number, move to next character
				isNumber = true;
				continue;
			}
		}
		if(isNumber && isUCase && isLCase) {
			//If all requirements are met, return true
			return true;
		}
		return false;
	}

	public static String logIn() {
		Stage logIn = new Stage();

		//This sets the message to be returned to "close", so that the window class knows to stay on the start screen
		logIn.setOnCloseRequest(e -> {
			msg = "close";
		});

		QueryDB qry = new QueryDB();

		//Set up the window
		logIn.initModality(Modality.APPLICATION_MODAL);
		logIn.setTitle("Welcome Back!");
		logIn.setMinWidth(400);

		//Title
		Label title = new Label("Log In");
		title.setStyle("-fx-text-fill: #525252; -fx-font-size: 16pt; -fx-alignment: center;");
		title.setMaxWidth(Double.POSITIVE_INFINITY);
		title.setMaxHeight(Double.POSITIVE_INFINITY);

		//Name
		Label namemsg = new Label();
		namemsg.setText("	Enter Username: ");
		namemsg.setStyle("-fx-text-fill: #FFFFFF;");
		TextField enterName = new TextField();
		enterName.setPromptText("Joe Bloggs...");
		enterName.setPrefColumnCount(20);

		//Password1
		Label pwrdmsg = new Label();
		pwrdmsg.setText("	Enter Password: ");
		pwrdmsg.setStyle("-fx-text-fill: #FFFFFF;");
		TextField enterPwrd = new TextField();
		enterPwrd.setPromptText("P*******123");
		enterPwrd.setPrefColumnCount(20);

		//Add go and cancel buttons
		Button enter = new Button("Go");
		enter.setOnAction(e -> {
			String name = enterName.getText();
			String pwrd = enterPwrd.getText();

			//Verifying User input
			if(name.length() == 0 || pwrd.length() == 0) {
				//Window to pop up and display error message
				Stage window = displayError("ALL fields must be filled");
				window.show();
			} else if(qry.doesNotExist(name) == true) {
				//Window to pop up and display error message
				Stage window = displayError("User Does Not Exist");
				window.show();
			} else {
				//This checks if the username and password have a match in the DB
				ResultSet rs = qry.getPassword(name);
				try {
					if(rs.next()) {
						if(rs.getString("pword").equals(pwrd)){
							//This sets the message returned to "mainmenu", so the user can go to the main menu
							System.out.print("MATCH");
							msg = "mainmenu";
							logIn.close();
						} else {
							msg = "close";
							//Window to pop up and display error message
							Stage window = displayError("Password is incorrect");
							window.show();
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				//Close window
				logIn.close();
			}
		});

		//This sets the message to be returned to "close", so that the window class knows to stay on the start screen
		Button back = new Button("Back");
		back.setOnAction(e -> {
			msg = "close";
			logIn.close();
		});

		//Create a vertical layout box and add the data input nodes to it.
		VBox textFields = new VBox(10);
		textFields.setSpacing(5);
		textFields.getChildren().addAll(namemsg, enterName, pwrdmsg, enterPwrd);
		textFields.setAlignment(Pos.CENTER_LEFT);

		//Create a horizontal layout and add the buttons to it
		HBox action = new HBox();
		action.setSpacing(25);
		action.getChildren().addAll(enter, back);
		action.setAlignment(Pos.CENTER);

		//Add both layouts to a border pane layout and create a new scene
		BorderPane layout = new BorderPane();
		layout.setTop(title);
		layout.setCenter(textFields);
		layout.setBottom(action);

		//These two labels are to give a gap on the left and right margins
		Label r = new Label(" ");
		Label l = new Label(" ");
		layout.setRight(r);
		layout.setLeft(l);

		//Set the scene
		layout.setStyle("-fx-background-color: #adadad;");
		Scene scene = new Scene(layout);
		logIn.setScene(scene);
		logIn.setResizable(false);

		//Display the window and then wait for the user to input the information or cancel.
		logIn.showAndWait();

		//Return the message to say whether or not to move to the main menu
		return msg;
	}

	private static Stage displayError(String errorMsg) {
		//Window to pop up and display error message
		Stage window = new Stage();
		window.setResizable(false);

		//This sets the window to only have a close button, no minimise/maximise 
		window.initStyle(StageStyle.UTILITY);

		//HBox to add the nodes of information to
		HBox h = new HBox();

		//Label to hold an image of a warning sign
		Label warnImg = new Label();
		warnImg.setStyle("-fx-background-image: url('http://icons.iconarchive.com/icons/pixelmixer/basic/32/warning-icon.png');"
				+ "-fx-padding: 24 32 24 32; -fx-background-position: center center; -fx-background-repeat: no-repeat;");

		//Label to have text informing the user why their input was rejected.
		Label info = new Label(errorMsg);

		//Styling the label text
		info.setStyle("-fx-text-fill: #525252; -fx-font-size: 12pt; -fx-alignment: center;");
		info.setMaxWidth(Double.POSITIVE_INFINITY);
		info.setMaxHeight(Double.POSITIVE_INFINITY);

		//Add image and text to the layout
		h.getChildren().addAll(warnImg, info);

		//Create the scene based on the HBox Layout
		Scene invalidSeq = new Scene(h, 400, 200);

		//Add the scene to the window
		window.setScene(invalidSeq);
		return window;
	}
}

