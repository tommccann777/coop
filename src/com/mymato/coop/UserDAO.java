package com.mymato.coop;

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

public class UserDAO {

	private static UserDAO instance;
	private DataSource dataSource;
	private String jndiName = "java:comp/env/jdbc/coop";
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public static UserDAO getInstance() throws Exception {
		if (instance == null) {
			instance = new UserDAO();
		}
		
		return instance;
	}
	
	public UserDAO() throws Exception {
		dataSource = getDataSource();
	}
	
	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
	
	
	// get all users
	public List<User> getUsers() throws Exception {

		List<User> users = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			 // next 2 lines for Google Cloud
			 //Class.forName("com.mysql.jdbc.GoogleDriver");
		     //String url = "jdbc:google:mysql://coop-140610:asia-east1:coop?user=root";
			
			
		    // original
			myConn = getConnection();
			
			// Google Cloud version
			//myConn = DriverManager.getConnection(url);

			String sql = "select * from coop.user order by username";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				int id = myRs.getInt("ID");
				String username = myRs.getString("Username");
				String passwordhash = myRs.getString("PasswordHash");
				String firstname = myRs.getString("FirstName");
				String lastname = myRs.getString("LastName");
				String shortname = myRs.getString("ShortName");
				String email = myRs.getString("Email");
				boolean emailpublic = myRs.getBoolean("EmailPublic");
				boolean receiveemail = myRs.getBoolean("ReceiveEmail");
				String homephone = myRs.getString("HomePhone");
				boolean homephonepublic = myRs.getBoolean("HomePhonePublic");
				String mobilephone = myRs.getString("MobilePhone");
				boolean mobilephonepublic = myRs.getBoolean("MobilePhonePublic");
				boolean receivetxt = myRs.getBoolean("ReceiveTxt");
				String address1 = myRs.getString("Address1");
				String address2 = myRs.getString("Address2");
				String address3 = myRs.getString("Address3");
				String postcode = myRs.getString("PostCode");
				boolean addresspublic = myRs.getBoolean("AddressPublic");
				String altcontactname = myRs.getString("AltContactName");
				String altcontactphone = myRs.getString("AltContactPhone");
				String altcontactemail = myRs.getString("AltContactEmail");
				boolean altcontactpublic = myRs.getBoolean("AltContactPublic");
				int rolemask = myRs.getInt("RoleMask");

				User tempUser = new User(id, username, passwordhash, firstname, lastname, shortname, email, emailpublic,
						receiveemail, homephone, homephonepublic, mobilephone, mobilephonepublic,
						receivetxt, address1, address2, address3, postcode, addresspublic,
						altcontactname, altcontactphone, altcontactemail, altcontactpublic, getRoleArrayList(rolemask));

				// add it to the list of User
				users.add(tempUser);
			}
			
			return users;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
 
	
	// Database accessor methods - get user by username or id
	public User getUser(String suppliedUsername, int userId) throws Exception {
		
		String sql;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			// get the user from the db, query depends on the parameter provided
			if (suppliedUsername != null)
			{
				sql = "select * from coop.user where Username=?";
				myStmt = myConn.prepareStatement(sql);
				
				// set parameters
				myStmt.setString(1, suppliedUsername);
			}
			else
			{
				sql = "select * from coop.user where ID=?";
				myStmt = myConn.prepareStatement(sql);
				
				// set parameters
				myStmt.setInt(1, userId);
			}
			
			myRs = myStmt.executeQuery();

			User theUser = null;
			
			// retrieve data from result set row
			if (myRs.next()) {
				int id = myRs.getInt("ID");
				String username = myRs.getString("Username");
				String passwordhash = myRs.getString("PasswordHash");
				String firstname = myRs.getString("FirstName");
				String lastname = myRs.getString("LastName");
				String shortname = myRs.getString("ShortName");
				String email = myRs.getString("Email");
				boolean emailpublic = myRs.getBoolean("EmailPublic");
				boolean receiveemail = myRs.getBoolean("ReceiveEmail");
				String homephone = myRs.getString("HomePhone");
				boolean homephonepublic = myRs.getBoolean("HomePhonePublic");
				String mobilephone = myRs.getString("MobilePhone");
				boolean mobilephonepublic = myRs.getBoolean("MobilePhonePublic");
				boolean receivetxt = myRs.getBoolean("ReceiveTxt");
				String address1 = myRs.getString("Address1");
				String address2 = myRs.getString("Address2");
				String address3 = myRs.getString("Address3");
				String postcode = myRs.getString("PostCode");
				boolean addresspublic = myRs.getBoolean("AddressPublic");
				String altcontactname = myRs.getString("AltContactName");
				String altcontactphone = myRs.getString("AltContactPhone");
				String altcontactemail = myRs.getString("AltContactEmail");
				boolean altcontactpublic = myRs.getBoolean("AltContactPublic");
				int rolemask = myRs.getInt("RoleMask");
				
				// convert the rolemask into a String array
				//logger.info("rolemask for " + username + "=" + rolemask);

				theUser = new User(id, username, passwordhash, firstname, lastname, shortname, email, emailpublic,
						receiveemail, homephone, homephonepublic, mobilephone, mobilephonepublic,
						receivetxt, address1, address2, address3, postcode, addresspublic,
						altcontactname, altcontactphone, altcontactemail, altcontactpublic, getRoleArrayList(rolemask));
			}
			else {
				throw new Exception("Could not find user. Username=" + suppliedUsername + " Id=" + userId);
			}

			return theUser;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
		
	}
	
	// add user
	public void addUser(User theUser) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();
			
			String sql = "insert into coop.user "
						+ "(Username, FirstName, LastName, ShortName, Email, EmailPublic, ReceiveEmail, HomePhone, HomePhonePublic, "
						+ "MobilePhone, MobilePhonePublic, ReceiveTxt, Address1, Address2, Address3, PostCode, "
						+ "AddressPublic, AltContactName, AltContactPhone, AltContactEmail, AltContactPublic, RoleMask, PasswordHash)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setString(1, theUser.getUsername());
			myStmt.setString(2, theUser.getFirstname());
			myStmt.setString(3, theUser.getLastname());
			myStmt.setString(4, theUser.getShortname());
			myStmt.setString(5, theUser.getEmail());
			myStmt.setBoolean(6, theUser.isEmailpublic());
			myStmt.setBoolean(7, theUser.isReceiveemail());
			myStmt.setString(8, theUser.getHomephone());
			myStmt.setBoolean(9, theUser.isHomephonepublic());
			myStmt.setString(10, theUser.getMobilephone());
			myStmt.setBoolean(11, theUser.isMobilephonepublic());
			myStmt.setBoolean(12, theUser.isReceivetxt());
			myStmt.setString(13, theUser.getAddress1());
			myStmt.setString(14, theUser.getAddress2());
			myStmt.setString(15, theUser.getAddress3());
			myStmt.setString(16, theUser.getPostcode());
			myStmt.setBoolean(17, theUser.isAddresspublic());
			myStmt.setString(18, theUser.getAltcontactname());
			myStmt.setString(19, theUser.getAltcontactphone());
			myStmt.setString(20, theUser.getAltcontactemail());
			myStmt.setBoolean(21, theUser.isAltcontactpublic());
			myStmt.setInt(22, theUser.getRolemask());
			
			String passwordHash = PasswordStorage.createHash(theUser.getPassword());
			myStmt.setString(23, passwordHash);
			
			
			logger.info("cooplog: About to execute:" + myStmt.toString());
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}


	// update user
	@SuppressWarnings("resource")
	public void updateUser(User theUser) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update coop.user "
						+ "SET Username=?, FirstName=?, LastName=?, ShortName=?, Email=?, EmailPublic=?, ReceiveEmail=?, HomePhone=?, HomePhonePublic=?, "
						+ "MobilePhone=?, MobilePhonePublic=?, ReceiveTxt=?, Address1=?, Address2=?, Address3=?, PostCode=?, "
						+ "AddressPublic=?, AltContactName=?, AltContactPhone=?, AltContactEmail=?, AltContactPublic=?, RoleMask=?"
						+ " where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set parameters
			myStmt.setString(1, theUser.getUsername());
			myStmt.setString(2, theUser.getFirstname());
			myStmt.setString(3, theUser.getLastname());
			myStmt.setString(4, theUser.getShortname());
			myStmt.setString(5, theUser.getEmail());
			myStmt.setBoolean(6, theUser.isEmailpublic());
			myStmt.setBoolean(7, theUser.isReceiveemail());
			myStmt.setString(8, theUser.getHomephone());
			myStmt.setBoolean(9, theUser.isHomephonepublic());
			myStmt.setString(10, theUser.getMobilephone());
			myStmt.setBoolean(11, theUser.isMobilephonepublic());
			myStmt.setBoolean(12, theUser.isReceivetxt());
			myStmt.setString(13, theUser.getAddress1());
			myStmt.setString(14, theUser.getAddress2());
			myStmt.setString(15, theUser.getAddress3());
			myStmt.setString(16, theUser.getPostcode());
			myStmt.setBoolean(17, theUser.isAddresspublic());
			myStmt.setString(18, theUser.getAltcontactname());
			myStmt.setString(19, theUser.getAltcontactphone());
			myStmt.setString(20, theUser.getAltcontactemail());
			myStmt.setBoolean(21, theUser.isAltcontactpublic());
			myStmt.setInt(22, theUser.getRolemask());
							
			// ID
			myStmt.setInt(23, theUser.getId());
			
			logger.info("cooplog: About to execute:" + myStmt.toString());
			myStmt.execute();
			
			// Separate treatment is required for the password
			// only perform this if a replacement password was supplied in the User object
			if (!theUser.getPassword().isEmpty()) {
				logger.info("cooplog: ----> Updating password to >" + theUser.getPassword() + "<");
				
				sql = "update coop.user "
						+ "SET PasswordHash=?"
						+ " where id=?";

				myStmt = myConn.prepareStatement(sql);
				
				String newHash = PasswordStorage.createHash(theUser.getPassword());
				logger.info("Hash is >" + newHash + "<");
			
				myStmt.setString(1, newHash);
				myStmt.setInt(2, theUser.getId());
				
				myStmt.execute();
			}
			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}

	
	public void deleteUser(int userId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from coop.user where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, userId);
			
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
	
	// convert between rolemask and String array
	public ArrayList<String> getRoleArrayList(int rolemask) {
		ArrayList<String> roles = new ArrayList<String>();
		//logger.info("getRoleArrayList: parsing rolemask=" + rolemask);
		
		if ((rolemask & 1) > 0) {
			//logger.info("Adding to list of roles: Buyer");
			roles.add("Buyer");
		}
		
		if ((rolemask & 2) > 0) {
			//logger.info("Adding to list of roles: Ordering");
			roles.add("Ordering");
		}
		
		if ((rolemask & 4) > 0) {
			//logger.info("Adding to list of roles: Accounts");
			roles.add("Accounts");
		}
		
		if ((rolemask & 8) > 0) {
			//logger.info("Adding to list of roles: Shareout");
			roles.add("Shareout");
		}
		
		if ((rolemask & 16) > 0) {
			//logger.info("Adding to list of roles: Admin");
			roles.add("Admin");
		}
		
		return roles;
	}

}
