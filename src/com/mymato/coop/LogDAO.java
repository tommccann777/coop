package com.mymato.coop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class LogDAO {
	
	private static LogDAO instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/coop";
	private Logger logger = Logger.getLogger(getClass().getName());

	public static LogDAO getInstance() {
		try {
			if (instance == null) {
				instance = new LogDAO();
			}
		} catch (Exception exc) {
			;
		}
		
		
		return instance;
	}
	
	private LogDAO() throws Exception {		
		dataSource = getDataSource();
	}
	
	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
	
	public void addMessage(String agent, String msg) {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into coop.log "
					+ "(Agent, EventTime, Message)"
					+ " values (?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setString(1, agent);
			
			java.util.Date today = new java.util.Date();
			myStmt.setTimestamp(2, new java.sql.Timestamp(today.getTime()));
			
			myStmt.setString(3, msg);

			myStmt.execute();			
		} catch (Exception exc) {
			logger.info("cooplog: Error writing message record to the log table");
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
