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

public class ProductDAO {

	private static ProductDAO instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/coop";
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public static ProductDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new ProductDAO();
		}
		
		return instance;
	}
	
	private ProductDAO() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<PricelistProduct> getPricelistProducts() throws Exception {

		List<PricelistProduct> products = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.pricelistproduct order by ID";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				String pricelist = myRs.getString("Pricelist");
				String supplier = myRs.getString("Supplier");
				String brand = myRs.getString("Brand");
				String supplierProductCode = myRs.getString("SupplierProductCode");
				String productDescription = myRs.getString("ProductDescription");
				String unitSize = myRs.getString("UnitSize");
				String quantity = myRs.getString("Quantity");
				BigDecimal unitTradePrice = myRs.getBigDecimal("UnitTradePrice");
				boolean valid = myRs.getBoolean("Valid");

				// create new product object
				PricelistProduct tempProduct = new PricelistProduct(id, pricelist, supplier, brand, supplierProductCode,
						productDescription, unitSize, quantity, unitTradePrice, valid);

				// add it to the list of products
				products.add(tempProduct);
			}
			
			return products;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}


	public void addPricelistProduct(PricelistProduct theProduct) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into coop.pricelistproduct "
					+ "(Pricelist, Supplier, Brand, SupplierProductCode, ProductDescription, UnitSize, Quantity, UnitTradePrice, Valid)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setString(1, theProduct.getPricelist());
			myStmt.setString(2, theProduct.getSupplier());
			myStmt.setString(3, theProduct.getBrand());
			myStmt.setString(4, theProduct.getSupplierProductCode());
			myStmt.setString(5, theProduct.getProductDescription());
			
			myStmt.setString(6, theProduct.getUnitSize());
			myStmt.setString(7, theProduct.getQuantity());
			myStmt.setBigDecimal(8, theProduct.getUnitTradePrice());
			myStmt.setBoolean(9, theProduct.isValid());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public PricelistProduct getPricelistProduct(int productId) throws Exception {
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.pricelistproduct where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, productId);
			
			myRs = myStmt.executeQuery();

			PricelistProduct theProduct = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				String pricelist = myRs.getString("Pricelist");
				String supplier = myRs.getString("Supplier");
				String brand = myRs.getString("Brand");
				String supplierProductCode = myRs.getString("SupplierProductCode");
				String productDescription = myRs.getString("ProductDescription");
				String unitSize = myRs.getString("UnitSize");
				String quantity = myRs.getString("Quantity");
				BigDecimal unitTradePrice = myRs.getBigDecimal("UnitTradePrice");
				boolean valid = myRs.getBoolean("Valid");

				// create new product object
				theProduct = new PricelistProduct(id, pricelist, supplier, brand, supplierProductCode,
						productDescription, unitSize, quantity, unitTradePrice, valid);
			}
			else {
				throw new Exception("Could not find pricelist product id: " + productId);
			}

			return theProduct;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void updateProduct(PricelistProduct theProduct) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update coop.pricelistproduct "
						+ "(Pricelist, Supplier, Brand, SupplierProductCode, ProductDescription, UnitSize, Quantity, UnitTradePrice, Valid)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
						+ " where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setString(1, theProduct.getPricelist());
			myStmt.setString(2, theProduct.getSupplier());
			myStmt.setString(3, theProduct.getBrand());
			myStmt.setString(4, theProduct.getSupplierProductCode());
			myStmt.setString(5, theProduct.getProductDescription());
			
			myStmt.setString(6, theProduct.getUnitSize());
			myStmt.setString(7, theProduct.getQuantity());
			myStmt.setBigDecimal(8, theProduct.getUnitTradePrice());
			myStmt.setBoolean(9, theProduct.isValid());
			
			myStmt.setInt(10, theProduct.getId());
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void deletePricelistProduct(int productId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from coop.pricelistproduct where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setInt(1, productId);
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}		
	}
	
	// delete all products from the pricelist product table
	public void deleteAllPricelistProducts() throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from coop.pricelistproduct";

			myStmt = myConn.prepareStatement(sql);
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}		
	}
	
	// add a collection of products to the database
	public void addPricelistProducts(List<PricelistProduct> products) throws Exception {
		
		for (int i = 0; i < products.size(); i++) {
			PricelistProduct p = products.get(i);
			
			addPricelistProduct(p);
		}
	}
	
	// copy a product record to the nominated product table
	public NominatedProduct copyProductToNominatedProduct(PricelistProduct p, int cycleNumber) throws Exception {
		// first check to see if the product is already in the nominated product table
		NominatedProduct np;	// nominated product object
		int newRecordId;		// id of the newly created nominated product record
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.nominatedproduct where PricelistProductId=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, p.getId());
			
			myRs = myStmt.executeQuery();

			np = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				logger.info("Found a nominated product with id" + p.getId());
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				String pricelist = myRs.getString("Pricelist");
				String supplier = myRs.getString("Supplier");
				String brand = myRs.getString("Brand");
				String supplierProductCode = myRs.getString("SupplierProductCode");
				String productDescription = myRs.getString("ProductDescription");
				String unitSize = myRs.getString("UnitSize");
				String quantity = myRs.getString("Quantity");
				BigDecimal unitTradePrice = myRs.getBigDecimal("UnitTradePrice");
				boolean valid = myRs.getBoolean("Valid");
				int cycleNum = myRs.getInt("CycleNumber");
				int pricelistProductId = myRs.getInt("PricelistProductID");
				int stockId = myRs.getInt("StockID");

				// create new nominated product object
				np = new NominatedProduct(id, pricelist, supplier, brand, supplierProductCode,
						productDescription, unitSize, quantity, unitTradePrice, valid, cycleNum, pricelistProductId, stockId);
			}
			
		} catch (Exception exc) {
			throw new Exception("Could not copy pricelist product to nominatedproduct table for product: " + p.getId());
		}			
		
		// if it is not then add it
		if (np == null) {
			logger.info("No nominated product found, so inserting one for product id=" + p.getId());
			// insert the new record in the nominated product table
			try {
				myConn = getConnection();

				String sql = "insert into coop.nominatedproduct "
						+ "(Pricelist, Supplier, Brand, SupplierProductCode, ProductDescription, UnitSize, Quantity, UnitTradePrice, Valid, CycleNumber, PricelistProductID, StockID)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				myStmt = myConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				// set parameters
				myStmt.setString(1, p.getPricelist());
				myStmt.setString(2, p.getSupplier());
				myStmt.setString(3, p.getBrand());
				myStmt.setString(4, p.getSupplierProductCode());
				myStmt.setString(5, p.getProductDescription());
				
				myStmt.setString(6, p.getUnitSize());
				myStmt.setString(7, p.getQuantity());
				myStmt.setBigDecimal(8, p.getUnitTradePrice());
				myStmt.setBoolean(9, p.isValid());
				myStmt.setInt(10, cycleNumber);
				myStmt.setInt(11, p.getId());
				myStmt.setInt(12,  0);
				
				logger.info("About to execute " + myStmt.toString());
				myStmt.execute();
				
				// retrieve the generated id
				ResultSet generatedKeysResultSet = myStmt.getGeneratedKeys();
				// first row of this set will contain generated keys
				// in our case it will contain only one row and only one column - generated id
				generatedKeysResultSet.next(); // executing next() method to navigate to first row of generated keys (like with any other result set)
				newRecordId = generatedKeysResultSet.getInt(1); // since our row contains only one column we won't miss with the column index :)
			} catch (Exception exc) {
				logger.info("Exception: Inserting didn't work");
				throw new Exception("Could not create product in nominatedproduct table for pricelist product: " + p.getId());
			}
			
			logger.info("Creating the nominated product object");
			
			// create the nominated product object next
			int id = newRecordId;
			String pricelist = p.getPricelist();
			String supplier = p.getSupplier();
			String brand = p.getBrand();
			String supplierProductCode = p.getSupplierProductCode();
			String productDescription = p.getProductDescription();
			String unitSize = p.getUnitSize();
			String quantity = p.getQuantity();
			BigDecimal unitTradePrice = p.getUnitTradePrice();
			boolean valid = p.isValid();
			int cycleNum = cycleNumber;
			int pricelistProductId = p.getId();
			
			np = new NominatedProduct(id, pricelist, supplier, brand, supplierProductCode,
					productDescription, unitSize, quantity, unitTradePrice, valid, cycleNum, pricelistProductId, 0);
			
			logger.info("Created the nominated product object with id=" + np.getId());
		}
		
		logger.info("Closing the database connection");
		// close the connection
		close (myConn, myStmt);
		
		logger.info("Returning the np");
		// return the nominated product
		return(np);
	}
	
	
	// Nominated Products
	public NominatedProduct getNominatedProduct(int productId) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from coop.nominatedproduct where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, productId);
			
			myRs = myStmt.executeQuery();

			NominatedProduct theProduct = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				// retrieve data from result set row
				int id = myRs.getInt("ID");
				String pricelist = myRs.getString("Pricelist");
				String supplier = myRs.getString("Supplier");
				String brand = myRs.getString("Brand");
				String supplierProductCode = myRs.getString("SupplierProductCode");
				String productDescription = myRs.getString("ProductDescription");
				String unitSize = myRs.getString("UnitSize");
				String quantity = myRs.getString("Quantity");
				BigDecimal unitTradePrice = myRs.getBigDecimal("UnitTradePrice");
				boolean valid = myRs.getBoolean("Valid");
				int cycleNumber = myRs.getInt("CycleNumber");
				int pricelistProductId = myRs.getInt("PricelistProductId");
				int stockId = myRs.getInt("StockID");

				// create new nominated product object
				theProduct = new NominatedProduct(id, pricelist, supplier, brand, supplierProductCode,
						productDescription, unitSize, quantity, unitTradePrice, valid, cycleNumber, pricelistProductId, stockId);
			}
			else {
				throw new Exception("Could not find nominated product id: " + productId);
			}

			return theProduct;
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
