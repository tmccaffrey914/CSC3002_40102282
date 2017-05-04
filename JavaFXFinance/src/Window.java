import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

/*
 * This class is for creating the UI and displaying the logic and charts that are prepared in the other classes.
 * It would be nicer to have separate classes for each scene however when you do this, there are issues with 
 * multiple windows appearing, and then you have to close them as you open the next window, so it just gets messy.
 */
public class Window extends Application implements EventHandler<ActionEvent> {
	private Stage window;
	private Scene main;

	//Initialise Button Spacing Panes
	private final Pane SPACE = new Pane();
	private final Pane SPACE2 = new Pane();
	private final Pane SPACE3 = new Pane();
	private final Pane SPACE4 = new Pane();
	private final Pane SPACE5 = new Pane();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage mainMenu) throws Exception {
		window = mainMenu;

		BorderPane layout = new BorderPane();

		//This initialises the spacing panes, they will be used later on, and in the footer of the main menu
		setUpBtnSpaces(); 

		//Title Label, styled with CSS
		Label menuTitle = new Label("Finance Reporting");
		menuTitle.setStyle("-fx-text-fill: #525252; -fx-font-size: 32pt; -fx-alignment: center;");

		//Set label position
		menuTitle.setMaxWidth(Double.POSITIVE_INFINITY);
		menuTitle.setMaxHeight(Double.POSITIVE_INFINITY);

		//Add label to the BorderPane Layout
		layout.setTop(menuTitle);

		//Create new user button
		Button addUser = setUpNewUserBtn();

		//Create log in button
		Button logIn = setUpLogInBtn();

		//New layout for the Center of the BorderLayout
		HBox center = new HBox();
		center.setStyle("-fx-alignment:center;");

		//So the buttons aren't right beside one another
		center.setSpacing(100);

		//Add buttons to the Center layout
		center.getChildren().addAll(logIn, addUser);

		//Add the VBox layout with the buttons on it to the center pane of the BorderLayout
		layout.setCenter(center);

		//Set the bottom pane of the BorderLayout to the ready-made main menu footer
		layout.setBottom(mainMenuFooter());
		layout.setStyle("-fx-background-color: #adadad;");

		//Create a scene based on the BorderLayout we just made
		main = new Scene(layout, 400, 200);

		//Set up the window and then add the scene to the window and display it
		window.setTitle("Final Year Project");
		window.setScene(main);
		window.show();
	}

	private Scene mainMenuScene() {
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: #adadad;");

		//Title Label, styled with CSS
		Label menuTitle = new Label("Finance Reporting");
		menuTitle.setStyle("-fx-text-fill: #525252; -fx-font-size: 32pt; -fx-alignment: center;");

		//Set label position
		menuTitle.setMaxWidth(Double.POSITIVE_INFINITY);
		menuTitle.setMaxHeight(Double.POSITIVE_INFINITY);

		//Add label to the top of the BorderPane Layout
		layout.setTop(menuTitle);

		//Open the Option Charts Scenes
		Button goToOptionScene = setUpOptionsCharts();

		//Open the PopUp that will lead to the Stock charts
		Button openMessageBox = setUpMMStockDataBtn();

		//Load the DB with Stock and Contract information
		Button loadDB = setUpLoadData();

		//New layout for the Center of the BorderLayout
		HBox center = new HBox();
		center.setStyle("-fx-alignment:center;");

		//So the buttons aren't right beside one another
		center.setSpacing(100);

		//Add buttons to the Center layout
		center.getChildren().addAll(loadDB, openMessageBox, goToOptionScene);

		//Add the VBox layout with the buttons on it to the center pane of the BorderLayout
		layout.setCenter(center);

		//Set the bottom pane of the BorderLayout to the ready-made main menu footer
		layout.setBottom(mainMenuFooter());

		//Create a scene based on the BorderLayout we just made
		Scene mainMenu = new Scene(layout, 500, 200);

		//Return Main Menu Scene
		return mainMenu;
	}

	private Scene stocksDataScene(String ticker) {
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: #adadad;");

		//Title Label, again styled with CSS
		Label stockTitle = new Label(ticker);
		stockTitle.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 32pt; -fx-alignment: center;");
		stockTitle.setMaxWidth(Double.POSITIVE_INFINITY);
		stockTitle.setMaxHeight(Double.POSITIVE_INFINITY);
		//Add label to the top of the BorderPane Layout
		layout.setTop(stockTitle);

		//##### EFFICIENCY TESTING #############################################################################################
		//Create an instance of the DisplayStocks class, this uses data from the more relational DB
		DisplayStocks chart = new DisplayStocks();

		//For testing DB Efficiency, create a flatfile instance of the DisplayStocks class
		//DisplayFFStocks chart = new DisplayFFStocks();

		/* The displayStocksChart() method here will return a chart that has been populated using a 
		 * query which uses the passed in ticker value to find all changes in price for that financial
		 * instrument. Then the chart is added to the center pane of the BorderPane layout.
		 */
		//For Testing DB Efficiency
		long start = System.nanoTime();
		layout.setCenter(chart.drawChart(ticker));
		long end = System.nanoTime();
		long timeElapsed = (end - start);
		System.out.println("Time taken to present data: " + timeElapsed + "ns");

		//#####################################################################################################################

		//~~~~~ Set up the buttons ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		Button goToMainMenu = setUpReturnToMenuBtn();
		Button checkNewStock = setUpStockDataBtn();
		Button refresh = new Button();

		//This displays mouseover text on the button
		refresh.setTooltip(new Tooltip("Refresh"));

		//Set the image background
		refresh.setId("refresh");
		refresh.getStylesheets().add(this.getClass().getResource("image.css").toExternalForm());
		refresh.setOnAction(e -> {
			layout.setCenter(chart.drawChart(ticker));
		});

		//Mouseover text for refresh button
		refresh.setTooltip(new Tooltip("Refresh"));
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		//Create a ToolBar with the two buttons declared earlier, add spaces between.
		ToolBar options = new ToolBar(SPACE, goToMainMenu, SPACE2, refresh, SPACE3, checkNewStock, SPACE4);

		//Method for styling the ToolBar
		options.setBackground(setToolbarGradientBackground());

		//Add ToolBar to the bottom of the BorderLayout.
		layout.setBottom(options);

		//Return Scene based on the BorderLayout just created. 
		Scene stocks = new Scene(layout, 500, 500);
		return stocks;
	}

	private Scene aaplScene() {
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: #adadad;");

		//Title Label, again styled with CSS and added to the top of the BorderPane layout
		Label tableTitle = new Label("Apple Stocks");
		tableTitle.setStyle("-fx-text-fill: #525252; -fx-font-size: 32pt; -fx-alignment: center;");
		tableTitle.setMaxWidth(Double.POSITIVE_INFINITY);
		tableTitle.setMaxHeight(Double.POSITIVE_INFINITY);
		layout.setTop(tableTitle);

		//Create a table displaying all of the options that expire today for Apple
		OptionsTables aaplContracts = new OptionsTables();
		layout.setCenter(aaplContracts.setUpOptionsTable("AAPL"));

		//Set up buttons, each will take the user to a specific Options Scene
		Button menu = setUpReturnToMenuBtn();
		Button aapl = setUpAppleBtn();		
		Button fb = setUpFacebookBtn();
		Button yhoo = setUpYahooBtn();

		//Create a Toolbar layout, and add the buttons to it, with the spaces in between
		ToolBar options = new ToolBar(SPACE, menu, SPACE2, aapl, SPACE3, fb, SPACE4, yhoo, SPACE5);

		//This sets the colour gradient to the toolbar
		options.setBackground(setToolbarGradientBackground());

		//Add the toolbar to the bottom of the border layout
		layout.setBottom(options);

		Scene charts = new Scene(layout, 925, 700);
		return charts;
	}

	private Scene facebookScene() {
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: #adadad;");

		//Title label
		Label tableTitle = new Label("Facebook Stocks");
		tableTitle.setStyle("-fx-text-fill: #525252; -fx-font-size: 32pt; -fx-alignment: center;");
		tableTitle.setMaxWidth(Double.POSITIVE_INFINITY);
		tableTitle.setMaxHeight(Double.POSITIVE_INFINITY);
		//Add Title to the top of the screen.
		layout.setTop(tableTitle);

		//Create a table displaying all of the options that expire today for Facebook
		OptionsTables fbContracts = new OptionsTables();
		layout.setCenter(fbContracts.setUpOptionsTable("FB"));

		//Set Up the buttons
		Button menu = setUpReturnToMenuBtn();
		Button aapl = setUpAppleBtn();		
		Button fb = setUpFacebookBtn();
		Button yhoo = setUpYahooBtn();

		//Create a Toolbar layout, and add the buttons to it, with the spaces in between
		ToolBar options = new ToolBar(SPACE, menu, SPACE2, aapl, SPACE3, fb, SPACE4, yhoo, SPACE5);	

		//This sets the colour gradient to the toolbar
		options.setBackground(setToolbarGradientBackground());

		//Add the toolbar to the bottom of the border layout
		layout.setBottom(options);

		Scene charts = new Scene(layout, 925, 700);
		return charts;
	}

	private Scene yahooScene() {
		BorderPane layout = new BorderPane();
		layout.setStyle("-fx-background-color: #adadad;");

		//Title Label
		Label tableTitle = new Label("Yahoo Stocks");
		tableTitle.setStyle("-fx-text-fill: #525252; -fx-font-size: 32pt; -fx-alignment: center;");
		tableTitle.setMaxWidth(Double.POSITIVE_INFINITY);
		tableTitle.setMaxHeight(Double.POSITIVE_INFINITY);
		//Add Title to the top of the screen.
		layout.setTop(tableTitle);

		//Create a table displaying all of the options that expire today for Yahoo
		OptionsTables yhooContracts = new OptionsTables();
		layout.setCenter(yhooContracts.setUpOptionsTable("YHOO"));

		//Set up the buttons
		Button menu = setUpReturnToMenuBtn();
		Button aapl = setUpAppleBtn();		
		Button fb = setUpFacebookBtn();
		Button yhoo = setUpYahooBtn(); 

		//Create a Toolbar layout, and add the buttons to it, with the spaces in between
		ToolBar options = new ToolBar(SPACE, menu, SPACE2, aapl, SPACE3, fb, SPACE4, yhoo, SPACE5);

		//This sets the colour gradient to the toolbar
		options.setBackground(setToolbarGradientBackground());

		//Add the toolbar to the bottom of the border layout
		layout.setBottom(options);

		Scene charts = new Scene(layout, 925, 700);
		return charts;
	}

	private Button setUpReturnToMenuBtn() {
		//Set up and return the menu button
		Button menu = new Button();
		menu.setOnAction(E -> {
			window.setScene(mainMenuScene());
		});
		//This displays mouseover text on the button
		menu.setTooltip(new Tooltip("Main Menu"));

		//Set Image Background
		menu.setId("menu");
		menu.getStylesheets().add(this.getClass().getResource("image.css").toExternalForm());

		return menu;
	}

	private Button setUpNewUserBtn() {
		Button addUser = new Button("New User");
		addUser.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #AB4642; -fx-padding: 24 10 24 10;");
		addUser.setOnAction(e -> {
			//Open the add user pop up box, and set msg to equal what is returned
			String msg = PopUpBox.addUser();
			if(!msg.equals("close")) {
				//If msg equals close, this means that the user has cancelled the new user process, so if it
				// doesn't equal close, go to main menu.
				window.setScene(mainMenuScene());
			}
		});
		//These actions set the button to have an on-hover effect
		addUser.setOnMouseEntered(e -> {
			addUser.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #f8938f; -fx-padding: 24 10 24 10;");
		});
		addUser.setOnMouseExited(e -> {
			addUser.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #AB4642; -fx-padding: 24 10 24 10;");
		});
		return addUser;
	}

	private Button setUpLogInBtn() {
		Button logIn = new Button("Log In");
		logIn.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #AB4642; -fx-padding: 24 20 24 20;");
		logIn.setOnAction(e -> {
			//Open the add user pop up box, and set msg to equal what is returned
			String msg = PopUpBox.logIn();
			if(!msg.equals("close")) {
				//If msg equals close, this means that the user has cancelled the new user process, so if it
				// doesn't equal close, go to main menu.
				window.setScene(mainMenuScene());
			}
		});
		//These actions set the button to have an on-hover effect
		logIn.setOnMouseEntered(e -> {
			logIn.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #f8938f; -fx-padding: 24 20 24 20;");
		});
		logIn.setOnMouseExited(e -> {
			logIn.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #AB4642; -fx-padding: 24 20 24 20;");
		});
		return logIn;
	}

	private Button setUpAppleBtn() {
		//Set up and return the aapl button
		Button aapl = new Button();		
		aapl.setOnAction(E -> {
			window.setScene(aaplScene());
		});
		//This displays mouseover text on the button
		aapl.setTooltip(new Tooltip("Apple Contracts"));

		//Set the image background
		aapl.setId("aapl");
		aapl.getStylesheets().add(this.getClass().getResource("image.css").toExternalForm());

		return aapl;
	}

	private Button setUpFacebookBtn() {
		//Set up and return the fb button
		Button fb = new Button();		
		fb.setOnAction(E -> {
			window.setScene(facebookScene());
		});
		//This displays mouseover text on the button
		fb.setTooltip(new Tooltip("FB Contracts"));

		//Set the image background
		fb.setId("fb");
		fb.getStylesheets().add(this.getClass().getResource("image.css").toExternalForm());

		return fb;
	}

	private Button setUpYahooBtn() {
		//Set up and return the yhoo button
		Button yhoo = new Button();
		yhoo.setOnAction(E -> {
			window.setScene(yahooScene());
		});
		//This displays mouseover text on the button
		yhoo.setTooltip(new Tooltip("Yahoo Contracts"));

		//Set the image background
		yhoo.setId("yhoo");
		yhoo.getStylesheets().add(this.getClass().getResource("image.css").toExternalForm());

		return yhoo;
	}

	private Button setUpMMStockDataBtn() {
		//This sets up a button to take the user to the stock data chart.
		Button openMessageBox = new Button();
		openMessageBox.setOnAction(e -> {
			//This opens a pop up which asks for user input, checks if the user has hit the back button
			String ticker = PopUpBox.askForTicker();
			if(ticker == "close") {
				window.setScene(mainMenuScene());
			} else {
				window.setScene(stocksDataScene(ticker));
			}
		});

		//This displays mouseover text on the button
		openMessageBox.setTooltip(new Tooltip("View Stocks"));

		//Set the image background
		openMessageBox.setId("stock");
		openMessageBox.getStylesheets().add(this.getClass().getResource("image.css").toExternalForm());

		return openMessageBox;
	}

	private Button setUpStockDataBtn() {
		//This sets up a button to search for a new stock while on the stock data screen.
		Button openMessageBox = new Button();
		openMessageBox.setOnAction(e -> {
			//This opens a pop up which asks for user input
			String ticker = PopUpBox.askForTicker();
			//This stops the chart from refreshing with the wrong information if the "back" button is hit,
			// and allows correct information to be displayed if "go" is hit
			if(ticker != "close") {
				window.setScene(stocksDataScene(ticker));
			}
		});

		//This displays mouseover text on the button
		openMessageBox.setTooltip(new Tooltip("View Stocks"));

		//Set the image background
		openMessageBox.setId("stock");
		openMessageBox.getStylesheets().add(this.getClass().getResource("image.css").toExternalForm());

		return openMessageBox;
	}

	private Button setUpOptionsCharts() {
		//This button brings the user to the appl scene, allowing them to go through the options contracts for the other companies
		Button goToOptionScene = new Button();
		goToOptionScene.setOnAction(e -> {
			window.setScene(aaplScene());
		});

		//This displays mouseover text on the button
		goToOptionScene.setTooltip(new Tooltip("View Options"));

		//Set the image background
		goToOptionScene.setId("ops");
		goToOptionScene.getStylesheets().add(this.getClass().getResource("image.css").toExternalForm());

		return goToOptionScene;
	}

	private Button setUpLoadData() {
		//This button runs batch scripts that load stock and contract information to the database
		Button loadDataBase = new Button();
		loadDataBase.setOnAction(e -> {
			try {
				//Create DB if it hasn't already been created.
				//Runtime.getRuntime().exec("cmd /c start C:/Users/apple/OneDrive/Documents/Project/BatchScripts/CreateDB/CreateRDB.bat");

				//For Loading share data that has been prepared.
				/*Runtime.getRuntime().exec("cmd /c start C:/Users/apple/OneDrive/Documents/Project/BatchScripts/RLoadBidAsk/"
						+ "LoadThisShareDataToThisRDB.bat C:/Users/apple/OneDrive/Documents/Project/BatchScripts/RLoadBidAsk/"
						+ "DataReadyForLoad.txt");*/

				//For loading Share data that hasn't already been prepared.
				/*Runtime.getRuntime().exec("cmd /c start C:/Users/apple/OneDrive/Documents/Project/BatchScripts/RLoadBidAsk/"
						+ "ReadyThisShareDataForLoad.bat C:/Users/apple/OneDrive/Documents/Project/BatchScripts/RLoadBidAsk/"
						+ "C:\\temporary\\trace_15122015.txt");*/

				//Delay adding the Options Contracts by 10 seconds, because the tickers for the Options must be in the company
				// table before the contracts can be added
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				Runtime.getRuntime().exec("cmd /c start C:/Users/apple/OneDrive/Documents/Project/BatchScripts/RLoadBidAsk/"
						+ "ReadyThisOCDataForLoad.bat C:\\temporary\\AAPL.oc");

				//Delay running the next file of Contracts because each script cant use the same resources simultaneously
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				Runtime.getRuntime().exec("cmd /c start C:/Users/apple/OneDrive/Documents/Project/BatchScripts/RLoadBidAsk/"
						+ "ReadyThisOCDataForLoad.bat C:\\temporary\\FB.oc");

				//Delay running the next file of Contracts because each script cant use the same resources simultaneously
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				//Delay running the next file of Contracts because each script cant use the same resources simultaneously
				Runtime.getRuntime().exec("cmd /c start C:/Users/apple/OneDrive/Documents/Project/BatchScripts/RLoadBidAsk/"
						+ "ReadyThisOCDataForLoad.bat C:\\temporary\\YHOO.oc");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		//This displays mouseover text on the button
		loadDataBase.setTooltip(new Tooltip("Load the DB"));

		//Set the image background
		loadDataBase.setId("load");
		loadDataBase.getStylesheets().add(this.getClass().getResource("image.css").toExternalForm());

		return loadDataBase;
	}

	private void setUpBtnSpaces() {
		//This method is called once as the program starts, it sets the final variables that allow spaces between nodes in the footers
		HBox.setHgrow(
				SPACE,
				Priority.SOMETIMES
				);

		HBox.setHgrow(
				SPACE2,
				Priority.SOMETIMES
				);

		HBox.setHgrow(
				SPACE3,
				Priority.SOMETIMES
				);

		HBox.setHgrow(
				SPACE4,
				Priority.SOMETIMES
				);

		HBox.setHgrow(
				SPACE5,
				Priority.SOMETIMES
				);
	}

	//This method sets up the information for the footer on the main menu
	private HBox mainMenuFooter() {
		//Set up each Label object for the footer the same way:
		Label csc3002 = new Label("  CSC3002");
		csc3002.setStyle("-fx-text-fill: #E8E8E8; -fx-font-size: 12pt; -fx-alignment: center;");

		Label qub = new Label("QUB");
		qub.setStyle("-fx-text-fill: #E8E8E8; -fx-font-size: 12pt; -fx-alignment: center;");

		Label sNo = new Label("40102282  ");
		sNo.setStyle("-fx-text-fill: #E8E8E8; -fx-font-size: 12pt; -fx-alignment: center;");

		//Create a layout to add the labels on to, HBox lays its nodes out in a Horizontal pattern, thats why it was chosen
		HBox bottomLayout = new HBox(csc3002, SPACE, qub, SPACE2, sNo);
		//This method creates the gradient background for the footers of the app
		bottomLayout.setBackground(setToolbarGradientBackground());;

		return bottomLayout;
	}

	private Background setToolbarGradientBackground() {
		//This method sets up the color gradient for the background of the footers of each scene.
		Stop[] stop = new Stop[] { 
				new Stop(0, Color.web("#180a09")),
				new Stop(0.05, Color.web("#491e1c")),				
				new Stop(0.5, Color.web("#AB4642")),
				new Stop(0.95, Color.web("#f5655f")),
				new Stop(1, Color.web("#f8938f")),
		};
		/*
		 * Assigning an array of Stop objects, each set up with different tones of the same color, one that is used on the
		 * QUB website, these tones are assigned a double value representing the percentage of the footer that will be covered
		 * with that color.
		 */ 
		LinearGradient grad = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stop);
		//This sets up a gradient based on the stop array created earlier

		Background b = new Background(new BackgroundFill(grad, CornerRadii.EMPTY, Insets.EMPTY));
		return b;
	}

	@Override
	public void handle(ActionEvent event) {		
	}
}
