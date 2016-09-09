package com.mymato.coop;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class StockDAO {

	private static StockDAO instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/coop";
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public static StockDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new StockDAO();
		}
		
		return instance;
	}
	
	private StockDAO() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Stock> getStocks() throws Exception {

		List<Stock> stocks = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.stock order by ID";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				int cycleNumber = myRs.getInt("CycleNumber");
				int nominatedProductId = myRs.getInt("NominatedProductID");
				BigDecimal unitQuantity = myRs.getBigDecimal("UnitQuantity");

				// create new Stock object
				Stock tempStock = new Stock(id, cycleNumber, nominatedProductId, unitQuantity);

				// add it to the list of Cycles
				stocks.add(tempStock);
			}
			
			return stocks;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	public void addStock(Stock theStock) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into coop.stock "
					+ "(CycleNumber, NominatedProductId, UnitQuantity)"
					+ " values (?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, theStock.getCycleNumber());
			myStmt.setInt(2, theStock.getNominatedProductId());
			myStmt.setBigDecimal(3, theStock.getUnitQuantity());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	// get Stock record by ID
	public Stock getStock(int stockId) throws Exception {
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.stock where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, stockId);
			
			myRs = myStmt.executeQuery();

			Stock theStock = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				int cycleNumber = myRs.getInt("CycleNumber");
				int nominatedProductId = myRs.getInt("NominatedProductID");
				BigDecimal unitQuantity = myRs.getBigDecimal("UnitQuantity");

				// create new Stock object
				theStock = new Stock(id, cycleNumber, nominatedProductId, unitQuantity);

			}
			else {
				throw new Exception("Could not find Stock id: " + stockId);
			}

			return theStock;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	

	// get Stock record by keys
	public Stock getStock(int aNominatedProductId, int aCycleNumber) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		Stock theStock = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.stock where NominatedProductId=? and CycleNumber=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, aNominatedProductId);
			myStmt.setInt(2, aCycleNumber);
			
			myRs = myStmt.executeQuery();
			
			// retrieve data from result set row
			if (myRs.next()) {
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				int cycleNumber = myRs.getInt("CycleNumber");
				int nominatedProductId = myRs.getInt("NominatedProductID");
				BigDecimal unitQuantity = myRs.getBigDecimal("UnitQuantity");

				// create new Stock object
				theStock = new Stock(id, cycleNumber, nominatedProductId, unitQuantity);
			}

		}
		finally {
			close (myConn, myStmt, myRs);
		}
		
		return theStock;
	}

	
	public void updateStock(Stock theStock) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update coop.stock "
						+ "SET CycleNumber=?, NominatedProductID=?, UnitQuantity=? "
						+ "where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, theStock.getCycleNumber());
			myStmt.setInt(2, theStock.getNominatedProductId());
			myStmt.setBigDecimal(3, theStock.getUnitQuantity());
			
			// ID
			myStmt.setInt(4, theStock.getId());
			
			logger.info("cooplog: About to execute:" + myStmt.toString());
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void deleteStock(int stockId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from coop.stock where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, stockId);
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}		
	}
		
	
	private Connection getConnection() throws Exception {

		Connection theConn = dataSource.getConnection();
		
		return theConn;
	}
	
	private void close(Connection theConn, Statement theStmt) {
		close(theConn, theStmt, null);
	}
	
	private void close(Connection theConn, Statement theStmt, ResultSet theRs) {

		try {
			if (theRs != null) {
				theRs.close();
			}

			if (theStmt != null) {
				theStmt.close();
			}

			if (theConn != null) {
				theConn.close();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}	
}
