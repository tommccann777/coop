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

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class OrderLineDAO {

	private static OrderLineDAO instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/coop";
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public static OrderLineDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new OrderLineDAO();
		}
		
		return instance;
	}
	
	public OrderLineDAO() throws Exception {
		dataSource = getDataSource();
	}
	
	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
	
	// get all VShareoutOrderLines for a cycle
	// this brings back the contents of a view (sql query) containing:
	public List<VShareoutOrderLine> getVShareoutOrderLines(int cycleNumber) throws Exception {
		
		List<VShareoutOrderLine> vshareoutorderlines = new ArrayList<>();
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select "
						+ "ol.ID as OrderLineID "
						+ ", prd.Supplier as SupplierName "
						+ ", prd.ID as ProductID "
						+ ", prd.ProductDescription "
						+ ", prd.SupplierProductCode "
						+ ", ol.MemberID "
						+ ", usr.Username "
						+ ", ol.Allocation "
						+ ", ol.Received "
						+ "from coop.orderline as ol "
						+ "left join coop.nominatedproduct as prd "
						+ "on ol.NominatedProductID = prd.ID "
						+ "left join coop.user as usr "
						+ "on ol.MemberID = usr.ID "
						+ "where ol.CycleNumber = ? and ol.Allocation > 0 "
						+ "order by prd.Supplier, prd.SupplierProductCode";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, cycleNumber);
			
			logger.info("cooplog: About to execute sql: " + myStmt.toString());

			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				
				int orderlineid = myRs.getInt("OrderLineID");
				int productid = myRs.getInt("ProductID");
				String suppliername = myRs.getString("SupplierName");
				String productdescription = myRs.getString("ProductDescription");
				String supplierproductcode = myRs.getString("SupplierProductCode");
				int memberid = myRs.getInt("MemberID");
				String username = myRs.getString("Username");
				BigDecimal allocation = myRs.getBigDecimal("Allocation");
				BigDecimal received = myRs.getBigDecimal("Received");

				VShareoutOrderLine tempvShareoutOrderLine = new VShareoutOrderLine(orderlineid, productid, suppliername, 
						supplierproductcode, productdescription, memberid, username, allocation, received);

				// add it to the list of OrderLine
				vshareoutorderlines.add(tempvShareoutOrderLine);
			}		
		}
		finally {
			close (myConn, myStmt, myRs);
		}

		return vshareoutorderlines;
	}
	
	
	
	// get all orderLines for a cycle for a member
	public List<OrderLine> getMyOrderLines(int aCycleNumber, int aMemberID) throws Exception {

		List<OrderLine> orderlines = new ArrayList<>();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.orderline where CycleNumber=? and MemberID=? order by NominatedProductID";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, aCycleNumber);
			myStmt.setInt(2, aMemberID);
			
			//logger.info("About to execute sql: " + myStmt.toString());

			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				
				int id = myRs.getInt("ID");
				int cycleNumber = myRs.getInt("CycleNumber");
				int memberId = myRs.getInt("MemberID");
				int productId = myRs.getInt("NominatedProductID");
				BigDecimal minQty = myRs.getBigDecimal("MinQty");
				BigDecimal maxQty = myRs.getBigDecimal("MaxQty");
				BigDecimal allocation = myRs.getBigDecimal("Allocation");
				BigDecimal received = myRs.getBigDecimal("Received");
				
				String snapshotPricelist = myRs.getString("SnapshotPricelist");
				String snapshotSupplier = myRs.getString("SnapshotSupplier");
				String snapshotBrand = myRs.getString("SnapshotBrand");
				String snapshotSupplierProductCode = myRs.getString("SnapshotSupplierProductCode");
				String snapshotProductDescription = myRs.getString("SnapshotProductDescription");
				String snapshotUnitSize = myRs.getString("SnapshotUnitSize");
				String snapshotQuantity = myRs.getString("SnapshotQuantity");
				BigDecimal snapshotUnitTradePrice = myRs.getBigDecimal("SnapshotUnitTradePrice");
				
				Timestamp created = myRs.getTimestamp("Created");
				Timestamp modified = myRs.getTimestamp("Modified");
				

				OrderLine tempOrderLine = new OrderLine(id, cycleNumber, memberId, productId, minQty, maxQty, allocation, received, 
						snapshotPricelist, snapshotSupplier, snapshotBrand, snapshotSupplierProductCode, snapshotProductDescription,
						snapshotUnitSize, snapshotQuantity, snapshotUnitTradePrice,
						created, modified);

				// add it to the list of OrderLine
				orderlines.add(tempOrderLine);
			}
			
			return orderlines;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
 

	// get consolidated orderLines for a cycle for a member
	public List<ConsolidatedProductLine> getMyConsolidatedProductLines(int aCycleNumber, int aMemberID) throws Exception {

		logger.info("In getMyConsolidatedProductLines");
		List<ConsolidatedProductLine> conprodlines = new ArrayList<>();

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select "
					+ "np.ID as NominatedProductID "
					+ ", np.Supplier "
					+ ", np.ProductDescription "
					+ ", np.UnitSize "
					+ ", np.Quantity "
					+ ", np.UnitTradePrice "
					+ ", coalesce(con.totalminqty, 0) as TotalMinQty "
					+ ", coalesce(con.totalmaxqty, 0) as TotalMaxQty "
					+ ", mine.minqty "
					+ ", mine.maxqty "
					+ "from coop.nominatedproduct as np "
					+ "left join ( "
					+ "select nominatedproductid, sum(minqty) as totalminqty, sum(maxqty) as totalmaxqty "
					+ "from coop.orderline "
					+ "where cyclenumber = ? "
					+ "group by nominatedproductid "
					+ ") as con "
					+ "on np.ID = con.nominatedproductid "
					+ "left join ( "
					+ "select nominatedproductid, myorder.minqty, myorder.maxqty "
					+ "from coop.orderline as myorder "
					+ "where cyclenumber = ? and memberid=? "
					+ ") as mine "
					+ "on np.id = mine.nominatedproductid "
					+ "order by np.stockid desc, np.id";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, aCycleNumber);
			myStmt.setInt(2, aCycleNumber);
			myStmt.setInt(3, aMemberID);
			
			logger.info("cooplog: About to execute sql: " + myStmt.toString());

			myRs = myStmt.executeQuery();

			// process result set
			while (myRs.next()) {
				
				int nominatedProductId = myRs.getInt("NominatedProductID");
				String supplier = myRs.getString("Supplier");
				String productDescription = myRs.getString("ProductDescription");
				String unitSize = myRs.getString("UnitSize");
				String quantity = myRs.getString("Quantity");
				BigDecimal unitTradePrice = myRs.getBigDecimal("UnitTradePrice");
				BigDecimal totalMinQty = myRs.getBigDecimal("TotalMinQty");
				BigDecimal totalMaxQty = myRs.getBigDecimal("TotalMaxQty");
				BigDecimal minQty = myRs.getBigDecimal("MinQty");
				BigDecimal maxQty = myRs.getBigDecimal("MaxQty");			

				ConsolidatedProductLine tempConProductLine = new ConsolidatedProductLine(nominatedProductId, supplier, productDescription,
						unitSize, quantity, unitTradePrice, totalMinQty, totalMaxQty, minQty, maxQty, null);

				// add it to the list of OrderLine
				conprodlines.add(tempConProductLine);
			}
			
			return conprodlines;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	// get OrderLine by cycle, member, product
	public OrderLine getOrderLine(int aCycleNumber, int aMemberId, int aProductId) throws Exception {
		String sql;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();
			
			sql = "select * from coop.orderline where CycleNumber=? and MemberID=? and NominatedProductID=?";
			myStmt = myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, aCycleNumber);
			myStmt.setInt(2,  aMemberId);
			myStmt.setInt(3, aProductId);
			
			myRs = myStmt.executeQuery();

			OrderLine theOrderLine = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				int id = myRs.getInt("ID");
				int cycleNumber = myRs.getInt("CycleNumber");
				int memberId = myRs.getInt("MemberID");
				int productId = myRs.getInt("NominatedProductID");
				BigDecimal minQty = myRs.getBigDecimal("MinQty");
				BigDecimal maxQty = myRs.getBigDecimal("MaxQty");
				BigDecimal allocation = myRs.getBigDecimal("Allocation");
				BigDecimal received = myRs.getBigDecimal("Received");
				
				String snapshotPricelist = myRs.getString("SnapshotPricelist");
				String snapshotSupplier = myRs.getString("SnapshotSupplier");
				String snapshotBrand = myRs.getString("SnapshotBrand");
				String snapshotSupplierProductCode = myRs.getString("SnapshotSupplierProductCode");
				String snapshotProductDescription = myRs.getString("SnapshotProductDescription");
				String snapshotUnitSize = myRs.getString("SnapshotUnitSize");
				String snapshotQuantity = myRs.getString("SnapshotQuantity");
				BigDecimal snapshotUnitTradePrice = myRs.getBigDecimal("SnapshotUnitTradePrice");
				
				Timestamp created = myRs.getTimestamp("Created");
				Timestamp modified = myRs.getTimestamp("Modified");
				

				theOrderLine = new OrderLine(id, cycleNumber, memberId, productId, minQty, maxQty, allocation, received, 
						snapshotPricelist, snapshotSupplier, snapshotBrand, snapshotSupplierProductCode, snapshotProductDescription,
						snapshotUnitSize, snapshotQuantity, snapshotUnitTradePrice,
						created, modified);
			}

			return theOrderLine;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
		
	}
	
	// Database accessor methods - get OrderLine by id
	public OrderLine getOrderLine(int orderLineId) throws Exception {
		
		String sql;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();
			
			sql = "select * from coop.orderline where ID=?";
			myStmt = myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, orderLineId);			
			
			myRs = myStmt.executeQuery();

			OrderLine theOrderLine = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				int id = myRs.getInt("ID");
				int cycleNumber = myRs.getInt("CycleNumber");
				int memberId = myRs.getInt("MemberID");
				int productId = myRs.getInt("NominatedProductID");
				BigDecimal minQty = myRs.getBigDecimal("MinQty");
				BigDecimal maxQty = myRs.getBigDecimal("MaxQty");
				BigDecimal allocation = myRs.getBigDecimal("Allocation");
				BigDecimal received = myRs.getBigDecimal("Received");
				
				String snapshotPricelist = myRs.getString("SnapshotPricelist");
				String snapshotSupplier = myRs.getString("SnapshotSupplier");
				String snapshotBrand = myRs.getString("SnapshotBrand");
				String snapshotSupplierProductCode = myRs.getString("SnapshotSupplierProductCode");
				String snapshotProductDescription = myRs.getString("SnapshotProductDescription");
				String snapshotUnitSize = myRs.getString("SnapshotUnitSize");
				String snapshotQuantity = myRs.getString("SnapshotQuantity");
				BigDecimal snapshotUnitTradePrice = myRs.getBigDecimal("SnapshotUnitTradePrice");
				
				Timestamp created = myRs.getTimestamp("Created");
				Timestamp modified = myRs.getTimestamp("Modified");
				

				theOrderLine = new OrderLine(id, cycleNumber, memberId, productId, minQty, maxQty, allocation, received, 
						snapshotPricelist, snapshotSupplier, snapshotBrand, snapshotSupplierProductCode, snapshotProductDescription,
						snapshotUnitSize, snapshotQuantity, snapshotUnitTradePrice,
						created, modified);
			}
			else {
				throw new Exception("Could not find Order Line. Id=" + orderLineId);
			}

			return theOrderLine;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
		
	}
	
	// add orderLine
	public void addOrderLine(OrderLine theOrderLine) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();
			
			String sql = "insert into coop.orderline "
						+ "(CycleNumber, MemberID, NominatedProductID, MinQty, MaxQty, Allocation, Received, "
						+ "SnapshotPricelist, SnapshotSupplier, SnapshotBrand, SnapshotSupplierProductCode, "
						+ "SnapshotProductDescription, SnapshotUnitSize, SnapshotQuantity, SnapshotUnitTradePrice, "
						+ "Created, Modified)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, theOrderLine.getCycleNumber());
			myStmt.setInt(2, theOrderLine.getMemberId());
			myStmt.setInt(3, theOrderLine.getNominatedProductId());
			myStmt.setBigDecimal(4, theOrderLine.getMinQty());
			myStmt.setBigDecimal(5, theOrderLine.getMaxQty());
			myStmt.setBigDecimal(6, theOrderLine.getAllocation());
			myStmt.setBigDecimal(7, theOrderLine.getReceived());
			
			myStmt.setString(8, theOrderLine.getSnapshotPricelist());
			myStmt.setString(9, theOrderLine.getSnapshotSupplier());
			myStmt.setString(10, theOrderLine.getSnapshotBrand());
			myStmt.setString(11, theOrderLine.getSnapshotSupplierProductCode());
			myStmt.setString(12, theOrderLine.getSnapshotProductDescription());
			myStmt.setString(13, theOrderLine.getSnapshotUnitSize());
			myStmt.setString(14, theOrderLine.getSnapshotQuantity());
			myStmt.setBigDecimal(15, theOrderLine.getSnapshotUnitTradePrice());
			
			java.util.Date today = new java.util.Date();
			myStmt.setTimestamp(16, new java.sql.Timestamp(today.getTime()));
			myStmt.setTimestamp(17, new java.sql.Timestamp(today.getTime()));			
			
			logger.info("cooplog: About to execute:" + myStmt.toString());
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}


	// update orderLine
	public void updateOrderLine(OrderLine theOrderLine) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update coop.orderline "
						+ "SET CycleNumber=?, MemberID=?, NominatedProductID=?, MinQty=?, MaxQty=?, Allocation=?, Received=?, "
						+ "SnapshotPricelist=?, SnapshotSupplier=?, SnapshotBrand=?, SnapshotSupplierProductCode=?, "
						+ "SnapshotProductDescription=?, SnapshotUnitSize=?, SnapshotQuantity=?, SnapshotUnitTradePrice=?, "
						+ "Created=?, Modified=? "
						+ "where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, theOrderLine.getCycleNumber());
			myStmt.setInt(2, theOrderLine.getMemberId());
			myStmt.setInt(3, theOrderLine.getNominatedProductId());
			myStmt.setBigDecimal(4, theOrderLine.getMinQty());
			myStmt.setBigDecimal(5, theOrderLine.getMaxQty());
			myStmt.setBigDecimal(6, theOrderLine.getAllocation());
			myStmt.setBigDecimal(7, theOrderLine.getReceived());
			
			myStmt.setString(8, theOrderLine.getSnapshotPricelist());
			myStmt.setString(9, theOrderLine.getSnapshotSupplier());
			myStmt.setString(10, theOrderLine.getSnapshotBrand());
			myStmt.setString(11, theOrderLine.getSnapshotSupplierProductCode());
			myStmt.setString(12, theOrderLine.getSnapshotProductDescription());
			myStmt.setString(13, theOrderLine.getSnapshotUnitSize());
			myStmt.setString(14, theOrderLine.getSnapshotQuantity());
			myStmt.setBigDecimal(15, theOrderLine.getSnapshotUnitTradePrice());
			
			java.util.Date today = new java.util.Date();
			myStmt.setTimestamp(16, theOrderLine.getCreated());
			myStmt.setTimestamp(17, new java.sql.Timestamp(today.getTime()));
			
			// ID
			myStmt.setInt(18, theOrderLine.getId());
			
			logger.info("cooplog: About to execute:" + myStmt.toString());
			myStmt.execute();
						
		}
		finally {
			close (myConn, myStmt);
		}
		
	}

	
	public void deleteOrderLine(int orderLineId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		logger.info("cooplog: Attempting to delete OrderLine id=" + orderLineId);
		
		try {
			myConn = getConnection();

			String sql = "delete from coop.orderline where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, orderLineId);
			logger.info("cooplog: Attempting to execute sql: " + myStmt.toString());
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}		
	}
	
	
		
	// Build a hierarchy of TreeNodes representing products and orders of those products
	public TreeNode getVAllocationRecords(int cycleNumber) throws Exception {
		
		TreeNode root = new DefaultTreeNode(new TnoOrderAllocation("", 0, 0, "", "", "", "", new BigDecimal(0.0), "", new BigDecimal(0.0), new BigDecimal(0.0), new BigDecimal(0.0), "", 0, new BigDecimal(0.0)), null);
		TreeNode currentProductNode = null;
		TreeNode thisNode = null;	// the current node being processed
		
		// get the records from this query
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			myConn = getConnection();

			String sql = 
			"select * "
			+ "from "
			+ "("
			+ "select 'Product' as RowType "
			+ ", p.ID as NominatedProductID "
			+ ", null as OrderLineID "
			+ ", p.SupplierProductCode As ProductCode "
			+ ", p.ProductDescription As ProductDescription "
			+ ", p.UnitSize As UnitSize "
			+ ", p.Quantity As Quantity "
			+ ", p.UnitTradePrice As Price "
			+ ", null as MemberName "
			+ ", sum(MinQty) as MinQty "
			+ ", sum(MaxQty) as MaxQty "
			+ ", null as Allocation "
			+ ", p.Supplier as Supplier "
			+ ", s.ID as SupplierOrderLineID "
			+ ", s.Qty as SupplierQty "
			+ "from coop.orderline as o "
			+ "join coop.nominatedproduct as p "
			+ "on o.nominatedproductid = p.ID "
			+ "left join coop.supplierorderline as s "
			+ "on o.nominatedproductid = s.ProductID and s.CycleNumber=? "
			+ "where o.cyclenumber=? "
			+ "group by nominatedproductid "
			+ ") as products "
			+ "union "
			+ "( "
			+ "select 'OrderLine' as RowType "
			+ ", o.NominatedProductId as NominatedProductID "
			+ ", o.ID as OrderLineID "
			+ ", null as ProductCode "
			+ ", null as ProductDescription "
			+ ", null as UnitSize "
			+ ", null as Quantity "
			+ ", null as Price "
			+ ", CONCAT(u.FirstName, ' ', u.LastName) as MemberName "
			+ ", MinQty as MinQty "
			+ ", MaxQty as MaxQty "
			+ ", Allocation as Allocation "
			+ ", null as Supplier "
			+ ", null as SupplierOrderLineID "
			+ ", null as SupplierQty "
			+ "from coop.orderline as o "
			+ "join coop.user as u "
			+ "on o.MemberID = u.ID "
			+ "where CycleNumber=? "
			+ ") "
			+ "order by NominatedProductID, RowType Desc ";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, cycleNumber);
			myStmt.setInt(2, cycleNumber);
			myStmt.setInt(3, cycleNumber);
			logger.info("cooplog: Attempting to execute sql: " + myStmt.toString());
			
			myRs = myStmt.executeQuery();
			
			while (myRs.next()) {
				// get the column values
				String rowtype = myRs.getString("RowType");
				int productid = myRs.getInt("NominatedProductID");
				int orderlineid = myRs.getInt("OrderLineID");
				String productcode = myRs.getString("ProductCode");
				String productdescription = myRs.getString("ProductDescription");
				String unitsize = myRs.getString("UnitSize");
				String quantity = myRs.getString("Quantity");
				BigDecimal price = myRs.getBigDecimal("Price");
				String membername = myRs.getString("MemberName");
				BigDecimal minqty = myRs.getBigDecimal("MinQty");
				BigDecimal maxqty = myRs.getBigDecimal("MaxQty");
				BigDecimal allocation = myRs.getBigDecimal("Allocation");
				String supplier = myRs.getString("Supplier");
				int supplierorderlineid = myRs.getInt("SupplierOrderLineID");
				BigDecimal supplierqty = myRs.getBigDecimal("SupplierQty");
				
				TnoOrderAllocation thisoan = new TnoOrderAllocation(rowtype, productid, orderlineid, productcode, productdescription,
																		unitsize, quantity, price, membername, minqty, maxqty, allocation,
																		supplier, supplierorderlineid, supplierqty);
								
				// place the node in the right place in the hierarchy
				if (rowtype.equals("Product")) {
					thisNode = new DefaultTreeNode(thisoan, root);
					currentProductNode = thisNode;
				}
				
				if (rowtype.equals("OrderLine")) {
					thisNode = new DefaultTreeNode(thisoan, currentProductNode);
				}
			}
			
		}
		finally {
			close (myConn, myStmt);
		}
		
		return root;
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
