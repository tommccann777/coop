package com.mymato.coop;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class oldPricelistDAO {
	
	private static oldPricelistDAO instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/coop";
	private Logger logger = Logger.getLogger(getClass().getName());

	public static oldPricelistDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new oldPricelistDAO();
		}
		
		return instance;
	}
	
	public List<Pricelist> getPricelists() throws Exception {

		List<Pricelist> pricelists = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.pricelist as pl "
					+ "join coop.supplier as sup "
					+ "on pl.SupplierID = sup.ID "
					+ "order by pl.ID";

			myStmt = myConn.createStatement();

			logger.info("About to execute: " + sql);
			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				int supplierid = myRs.getInt("SupplierID");
				String suppliernameDenorm = myRs.getString("SupplierName");
				String listname = myRs.getString("ListName");
				boolean active = myRs.getBoolean("Active");
				

				// create new Pricelist object
				Pricelist tempPricelist = new Pricelist(id, supplierid, suppliernameDenorm, listname, active);

				// add it to the list of Pricelists
				pricelists.add(tempPricelist);
			}
			
			return pricelists;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	
	private oldPricelistDAO() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
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
