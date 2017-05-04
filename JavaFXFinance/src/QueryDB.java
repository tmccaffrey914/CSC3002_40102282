import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class QueryDB {
	private Connection con;
	private String rurl = "jdbc:mysql://localhost:3306/rbidaskdb";
	private String ffurl = "jdbc:mysql://localhost:3306/ffbidaskdb";
	private String username = "root";
	private String password = "Lunch914";

	private ResultSet rs;

	//Connect to the relational database
	private void connect() {
		try {
			con = (Connection) DriverManager.getConnection(rurl, username, password);
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect!", e);
		}
	}

	//Connect to the flat file database
	private void ffconnect() {
		try {
			con = (Connection) DriverManager.getConnection(ffurl, username, password);
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect!", e);
		}
	}
	
	//Close the connection
	public void closeConnection() {
		try {
			this.rs.close();
			this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//This is for the pop up on a data point in the stock value chart
	public ResultSet getOneStock(String timestamp) {
		connect();
		try {
			return rs = con.createStatement().executeQuery("SELECT bid.id, bid.size AS bidsize, bid.price AS bidprice,"+
					" marketmatch.timest AS time, marketmatch.ticker AS ticker, ask.id, ask.size AS asksize, ask.price AS"+
					" askprice FROM bid JOIN marketmatch ON bid.id = marketmatch.bid JOIN ask ON"+
					" marketmatch.ask = ask.id WHERE marketmatch.timest = '"+timestamp+"' ORDER BY marketmatch.ask;");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	//This is to return all the changes in stock value for a given ticker
	public ResultSet getStockBidAsks(String ticker) {
		connect();
		try {
			return rs = con.createStatement().executeQuery("SELECT bid.id, bid.size AS bidsize, bid.price AS bidprice,"+
					" marketmatch.ticker AS ticker, marketmatch.timest AS time, ask.id, ask.size AS asksize,"+
					" ask.price AS askprice FROM bid JOIN marketmatch ON bid.id = marketmatch.bid JOIN ask ON"+
					" marketmatch.ask = ask.id WHERE marketmatch.ticker = '"+ticker+"' ORDER BY marketmatch.ask;");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	//This is for the pop up on a data point in the stock value chart
	public ResultSet getOneFFStock(String timestamp, String bidask) {
		ffconnect();
		try {
			return rs = con.createStatement().executeQuery("SELECT id, timest, ticker, size, price, bidask FROM bidaskmatch"
					+ " WHERE timest = '"+ timestamp +"' && bidask = '" + bidask + "';");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	//This is to return all the changes in stock value for a given ticker
	public ResultSet getFFStockBids(String ticker) {
		ffconnect();
		try {
			return rs = con.createStatement().executeQuery("SELECT id, timest, ticker, size, price, bidask FROM bidaskmatch"
					+ " WHERE ticker = '"+ ticker +"' && bidask = 'bid';");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	//This returns all the Options Contracts for a given ticker, it is used on the Options Contracts tables
	public ResultSet getAllOptionsContracts(String ticker) {
		connect();
		try {
			return rs = con.createStatement().executeQuery("SELECT * FROM options WHERE ticker = '" + ticker + "';");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	//All methods below are for the login system
	public boolean addTrader(String name, String phone, String pass, int salt, String ticker) {
		connect();
		try {
			con.createStatement().executeUpdate("INSERT INTO trader (username, phone, pword, salt, tradingin)"
					+ " VALUES ('"+ name + "', '" + phone + "', '" + pass + "', '" + salt + "', '"+ticker+"');");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Return true if the username passed in cannot be found in the database
	public boolean doesNotExist(String username) {
		connect();
		try {
			ResultSet rs = con.createStatement().executeQuery("SELECT * FROM trader WHERE username = '" + username + "';");
			if (!rs.next()){
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	//Return the password belonging to the passed in username.
	public ResultSet getPassword(String username) {
		connect();
		try {
			ResultSet rs = con.createStatement().executeQuery("SELECT * FROM trader WHERE username = '" + username + "';");
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
	}
}
