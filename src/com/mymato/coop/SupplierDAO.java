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

public class SupplierDAO {

	private static SupplierDAO instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/coop";
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public static SupplierDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new SupplierDAO();
		}
		
		return instance;
	}
	
	private SupplierDAO() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Supplier> getSuppliers() throws Exception {

		List<Supplier> suppliers = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.supplier order by ID";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				String supplierName = myRs.getString("SupplierName");
				String contact = myRs.getString("Contact");
				String phone = myRs.getString("Phone");
				String email = myRs.getString("Email");
				String website = myRs.getString("Website");

				// create new Cycle object
				Supplier tempSupplier = new Supplier(id, supplierName, contact, phone, email, website);

				// add it to the list of Cycles
				suppliers.add(tempSupplier);
			}
			
			return suppliers;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	public void addSupplier(Supplier theSupplier) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into coop.supplier "
					+ "(SupplierName, Contact, Phone, Email, Website)"
					+ " values (?, ?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setString(1, theSupplier.getSupplierName());
			myStmt.setString(2, theSupplier.getContact());
			myStmt.setString(3, theSupplier.getPhone());
			myStmt.setString(4, theSupplier.getEmail());
			myStmt.setString(5, theSupplier.getWebsite());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public Supplier getSupplier(int SupplierId) throws Exception {
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.supplier where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, SupplierId);
			
			myRs = myStmt.executeQuery();

			Supplier theSupplier = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				String supplierName = myRs.getString("SupplierName");
				String contact = myRs.getString("Contact");
				String phone = myRs.getString("Phone");
				String email = myRs.getString("Email");
				String website = myRs.getString("Website");
				
				// create new Supplier object
				theSupplier = new Supplier(id, supplierName, contact, phone, email, website);

			}
			else {
				throw new Exception("Could not find Supplier id: " + SupplierId);
			}

			return theSupplier;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void updateSupplier(Supplier theSupplier) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update coop.supplier "
						+ "SET SupplierName=?, Contact=?, Phone=?, Email=?, Website=?"
						+ " where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setString(1, theSupplier.getSupplierName());
			myStmt.setString(2, theSupplier.getContact());
			myStmt.setString(3, theSupplier.getPhone());
			myStmt.setString(4, theSupplier.getEmail());
			myStmt.setString(5, theSupplier.getWebsite());
			
			// ID
			myStmt.setInt(6, theSupplier.getId());
			
			logger.info("cooplog: About to execute:" + myStmt.toString());
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void deleteSupplier(int SupplierId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from coop.supplier where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, SupplierId);
			
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
