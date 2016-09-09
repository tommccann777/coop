package com.mymato.coop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class CycleDAO {

	private static CycleDAO instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/coop";
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public static CycleDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new CycleDAO();
		}
		
		return instance;
	}
	
	private CycleDAO() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Cycle> getCycles() throws Exception {

		List<Cycle> cycles = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.cycle order by CycleNumber desc";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				int cycleNumber = myRs.getInt("CycleNumber");
				Date startDay = myRs.getDate("StartDay");
				Date orderFirstDay = myRs.getDate("OrderFirstDay");
				Date orderLastDay = myRs.getDate("OrderLastDay");
				Date shareoutDay = myRs.getDate("ShareoutDay");
				Date closeDay = myRs.getDate("CloseDay");
				String bannerMessage = myRs.getString("BannerMessage");
				String shareoutLocation = myRs.getString("shareoutLocation");
				

				// create new Cycle object
				Cycle tempCycle = new Cycle(id, cycleNumber, startDay, orderFirstDay, orderLastDay,
						shareoutDay, closeDay, bannerMessage, shareoutLocation);

				// add it to the list of Cycles
				cycles.add(tempCycle);
			}
			
			return cycles;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	public void addCycle(Cycle theCycle) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into coop.cycle "
					+ "(CycleNumber, StartDay, OrderFirstDay, OrderLastDay, ShareoutDay, CloseDay, BannerMessage, ShareoutLocation)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, theCycle.getCycleNumber());
			
			myStmt.setDate(2, (java.sql.Date) theCycle.getStartDay());
			myStmt.setDate(3, (java.sql.Date) theCycle.getOrderFirstDay());
			myStmt.setDate(4, (java.sql.Date) theCycle.getOrderLastDay());
			myStmt.setDate(5, (java.sql.Date) theCycle.getShareoutDay());
			myStmt.setDate(6, (java.sql.Date) theCycle.getCloseDay());
			

			myStmt.setString(7, theCycle.getBannerMessage());
			myStmt.setString(8, theCycle.getShareoutLocation());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public Cycle getCycle(int CycleId) throws Exception {
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.cycle where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, CycleId);
			
			myRs = myStmt.executeQuery();

			Cycle theCycle = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				int cycleNumber = myRs.getInt("CycleNumber");
				Date startDay = myRs.getDate("StartDay");
				Date orderFirstDay = myRs.getDate("OrderFirstDay");
				Date orderLastDay = myRs.getDate("OrderLastDay");
				Date shareoutDay = myRs.getDate("ShareoutDay");
				Date closeDay = myRs.getDate("CloseDay");
				String bannerMessage = myRs.getString("BannerMessage");
				String shareoutLocation = myRs.getString("shareoutLocation");
				

				// create new Cycle object
				theCycle = new Cycle(id, cycleNumber, startDay, orderFirstDay, orderLastDay,
						shareoutDay, closeDay, bannerMessage, shareoutLocation);

			}
			else {
				throw new Exception("Could not find Cycle id: " + CycleId);
			}

			return theCycle;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void updateCycle(Cycle theCycle) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update coop.cycle "
						+ "SET CycleNumber=?, StartDay=?, OrderFirstDay=?, OrderLastDay=?, ShareoutDay=?, CloseDay=?, "
						+ "BannerMessage=?, ShareoutLocation=?"
						+ " where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, theCycle.getCycleNumber());
			
			// StartDay
			if (theCycle.getStartDay() == null)
				myStmt.setNull(2, java.sql.Types.DATE);
			else
				myStmt.setDate(2, new java.sql.Date(theCycle.getStartDay().getTime()));		
						
			// OrderFirstDay
			if (theCycle.getOrderFirstDay() == null)
				myStmt.setNull(3, java.sql.Types.DATE);
			else
				myStmt.setDate(3, new java.sql.Date(theCycle.getOrderFirstDay().getTime()));		
									
			// OrderLastDay
			if (theCycle.getOrderLastDay() == null)
				myStmt.setNull(4, java.sql.Types.DATE);
			else
				myStmt.setDate(4, new java.sql.Date(theCycle.getOrderLastDay().getTime()));		
									
			// ShareoutDay
			if (theCycle.getShareoutDay() == null)
				myStmt.setNull(5, java.sql.Types.DATE);
			else
				myStmt.setDate(5, new java.sql.Date(theCycle.getShareoutDay().getTime()));		
									
			// CloseDay
			if (theCycle.getCloseDay() == null)
				myStmt.setNull(6, java.sql.Types.DATE);
			else
				myStmt.setDate(6, new java.sql.Date(theCycle.getCloseDay().getTime()));		
									
			// BannerMessage
			myStmt.setString(7, theCycle.getBannerMessage());
			
			// ShareoutLocation
			myStmt.setString(8, theCycle.getShareoutLocation());
						
			// ID
			myStmt.setInt(9, theCycle.getId());
			
			logger.info("cooplog: About to execute:" + myStmt.toString());
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void deleteCycle(int CycleId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from coop.cycle where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, CycleId);
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}		
	}
	
	public Cycle getCurrentCycle() throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.cycle order by cyclenumber desc limit 1";

			myStmt = myConn.prepareStatement(sql);
			
			myRs = myStmt.executeQuery();

			Cycle theCycle = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				int cycleNumber = myRs.getInt("CycleNumber");
				Date startDay = myRs.getDate("StartDay");
				Date orderFirstDay = myRs.getDate("OrderFirstDay");
				Date orderLastDay = myRs.getDate("OrderLastDay");
				Date shareoutDay = myRs.getDate("ShareoutDay");
				Date closeDay = myRs.getDate("CloseDay");
				String bannerMessage = myRs.getString("BannerMessage");
				String shareoutLocation = myRs.getString("shareoutLocation");
				

				// create new Cycle object
				theCycle = new Cycle(id, cycleNumber, startDay, orderFirstDay, orderLastDay,
						shareoutDay, closeDay, bannerMessage, shareoutLocation);

			}
			else {
				throw new Exception("There are no cycles in database");
			}

			return theCycle;
		}
		finally {
			close (myConn, myStmt, myRs);
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
