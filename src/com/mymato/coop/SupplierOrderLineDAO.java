package com.mymato.coop;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class SupplierOrderLineDAO {

	private static SupplierOrderLineDAO instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/coop";
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public static SupplierOrderLineDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new SupplierOrderLineDAO();
		}
		
		return instance;
	}
	
	public SupplierOrderLineDAO() throws Exception {
		dataSource = getDataSource();
	}
	
	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
	
	// get list of supplier order lines from the supplier order lines view (sql query)
	public List<VSupplierOrderLine> getvSupplierOrderLines(int cycleNumber) throws Exception {
		List<VSupplierOrderLine> sols = new ArrayList<>();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select " 
						+ "prd.Supplier as SupplierName "
						+ ", sol.ID as SupplierOrderLineID "
						+ ", prd.SupplierProductCode "
						+ ", prd.ProductDescription "
						+ ", prd.UnitSize "
						+ ", prd.Quantity as PackQuantity "
						+ ", prd.UnitTradePrice as Price "
						+ ", sol.Qty As OrderQuantity "
						+ ", sol.Ordered "
						+ "from coop.supplierorderline as sol "
						+ "left join coop.nominatedproduct as prd "
						+ "on sol.ProductID = prd.ID "
						+ "where sol.Qty > 0 and sol.CycleNumber = ? "
						+ "order by SupplierName";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, cycleNumber);

			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				String suppliername = myRs.getString("SupplierName");
				int supplierorderlineid = myRs.getInt("SupplierOrderLineID");
				String supplierproductcode = myRs.getString("SupplierProductCode");
				String productdescription = myRs.getString("ProductDescription");
				String unitsize = myRs.getString("UnitSize");
				String packquantity = myRs.getString("PackQuantity");
				BigDecimal price = myRs.getBigDecimal("Price");
				BigDecimal orderquantity = myRs.getBigDecimal("OrderQuantity");
				boolean ordered = myRs.getBoolean("Ordered");
				
				// create new vSupplierOrderLine object
				VSupplierOrderLine tempSol = new VSupplierOrderLine(suppliername, supplierorderlineid, supplierproductcode,
						productdescription, unitsize, packquantity, price, orderquantity, ordered);

				// add it to the list of products
				sols.add(tempSol);
			}
			
			return sols;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	// Database accessor methods - get SupplierOrderLine by id
	public SupplierOrderLine getSupplierOrderLine(int supplierOrderLineId) throws Exception {
		
		String sql;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();
			
			sql = "select * from coop.supplierorderline where ID=?";
			myStmt = myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, supplierOrderLineId);			
			
			myRs = myStmt.executeQuery();

			SupplierOrderLine theSupplierOrderLine = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				int id = myRs.getInt("ID");
				int cycleNumber = myRs.getInt("CycleNumber");
				String supplier = myRs.getString("Supplier");
				int productId = myRs.getInt("ProductID");
				BigDecimal qty = myRs.getBigDecimal("Qty");
				int memberId = myRs.getInt("MemberID");
				boolean ordered = myRs.getBoolean("Ordered");
				Timestamp created = myRs.getTimestamp("Created");
				Timestamp modified = myRs.getTimestamp("Modified");

				theSupplierOrderLine = new SupplierOrderLine(id, cycleNumber, supplier, productId, qty, memberId, ordered, created, modified);
			}
			else {
				throw new Exception("Could not find Supplier Order Line. Id=" + supplierOrderLineId);
			}

			return theSupplierOrderLine;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
		
	}
	
	// add supplierOrderLine - returns id of record created
	public int addSupplierOrderLine(SupplierOrderLine theSupplierOrderLine) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		int newRecordId = 0;					// auto-generated id returned by the Insert

		try {
			myConn = getConnection();
			
			String sql = "insert into coop.supplierorderline "
						+ "(CycleNumber, Supplier, ProductID, Qty, MemberID, Ordered, Created, Modified)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// set parameters
			myStmt.setInt(1, theSupplierOrderLine.getCycleNumber());
			myStmt.setString(2, theSupplierOrderLine.getSupplier());
			myStmt.setInt(3, theSupplierOrderLine.getProductId());
			myStmt.setBigDecimal(4, theSupplierOrderLine.getQty());
			myStmt.setInt(5, theSupplierOrderLine.getMemberId());
			myStmt.setBoolean(6, theSupplierOrderLine.isOrdered());
			
			java.util.Date today = new java.util.Date();
			myStmt.setTimestamp(7, new java.sql.Timestamp(today.getTime()));
			myStmt.setTimestamp(8, new java.sql.Timestamp(today.getTime()));			
			
			logger.info("cooplog: About to execute:" + myStmt.toString());
			myStmt.execute();
			
			ResultSet generatedKeysResultSet = myStmt.getGeneratedKeys();
			// first row of this set will contain generated keys
			// in our case it will contain only one row and only one column - generated id
			generatedKeysResultSet.next(); // executing next() method to navigate to first row of generated keys (like with any other result set)
			newRecordId = generatedKeysResultSet.getInt(1); // since our row contains only one column we won't miss with the column index :)
		}
		finally {
			close (myConn, myStmt);
		}
		
		return newRecordId;
	}


	// update orderLine
	public void updateSupplierOrderLine(SupplierOrderLine theSupplierOrderLine) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update coop.supplierorderline "
						+ "SET CycleNumber=?, Supplier=?, ProductID=?, Qty=?, MemberID=?, Ordered=?, Created=?, Modified=? "
						+ "where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, theSupplierOrderLine.getCycleNumber());
			myStmt.setString(2, theSupplierOrderLine.getSupplier());
			myStmt.setInt(3, theSupplierOrderLine.getProductId());
			myStmt.setBigDecimal(4, theSupplierOrderLine.getQty());
			myStmt.setInt(5, theSupplierOrderLine.getMemberId());
			myStmt.setBoolean(6, theSupplierOrderLine.isOrdered());
			
			java.util.Date today = new java.util.Date();
			myStmt.setTimestamp(7, theSupplierOrderLine.getCreated());
			myStmt.setTimestamp(8, new java.sql.Timestamp(today.getTime()));
			
			// ID
			myStmt.setInt(9, theSupplierOrderLine.getId());
			
			logger.info("cooplog: About to execute:" + myStmt.toString());
			myStmt.execute();
						
		}
		finally {
			close (myConn, myStmt);
		}
		
	}

	
	public void deleteSupplierOrderLine(int supplierOrderLineId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		logger.info("Attempting to delete SupplierOrderLine id=" + supplierOrderLineId);
		
		try {
			myConn = getConnection();

			String sql = "delete from coop.supplierorderline where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, supplierOrderLineId);
			logger.info("cooplog: Attempting to execute sql: " + myStmt.toString());
			
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
