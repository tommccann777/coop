package com.mymato.coop;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String username;
	private String password;
	private String passwordhash;
	private String firstname;
	private String lastname;
	private String shortname;
	private String email;
	private boolean emailpublic;
	private boolean receiveemail;
	private String homephone;
	private boolean homephonepublic;
	private String mobilephone;
	private boolean mobilephonepublic;
	private boolean receivetxt;
	private String address1;
	private String address2;
	private String address3;
	private String postcode;
	private boolean addresspublic;
	private String altcontactname;
	private String altcontactphone;
	private String altcontactemail;
	private boolean altcontactpublic;
	
	
	private ArrayList<String> selectedroles = new ArrayList<String>();
	
	
	public User() {
		// no-arg constructor
		selectedroles.add("Buyer");
	}
	
	public User(int id, String username, String passwordhash, String firstname, String lastname, String shortname,
			String email, boolean emailpublic, boolean receiveemail, String homephone, boolean homephonepublic,
			String mobilephone, boolean mobilephonepublic, boolean receivetxt, String address1, String address2,
			String address3, String postcode, boolean addresspublic, String altcontactname, String altcontactphone,
			String altcontactemail, boolean altcontactpublic, ArrayList<String> selectedroles) {
		super();
		this.id = id;
		this.username = username;
		this.passwordhash = passwordhash;
		this.firstname = firstname;
		this.lastname = lastname;
		this.shortname = shortname;
		this.email = email;
		this.emailpublic = emailpublic;
		this.receiveemail = receiveemail;
		this.homephone = homephone;
		this.homephonepublic = homephonepublic;
		this.mobilephone = mobilephone;
		this.mobilephonepublic = mobilephonepublic;
		this.receivetxt = receivetxt;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.postcode = postcode;
		this.addresspublic = addresspublic;
		this.altcontactname = altcontactname;
		this.altcontactphone = altcontactphone;
		this.altcontactemail = altcontactemail;
		this.altcontactpublic = altcontactpublic;
		this.selectedroles = selectedroles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

	public String getPasswordhash() {
		return passwordhash;
	}

	public void setPasswordhash(String passwordhash) {
		this.passwordhash = passwordhash;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	
	public String getShareoutname() {
		
		if (!shortname.equals(""))
			return shortname;
		else
			return username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailpublic() {
		return emailpublic;
	}

	public void setEmailpublic(boolean emailpublic) {
		this.emailpublic = emailpublic;
	}

	public boolean isReceiveemail() {
		return receiveemail;
	}

	public void setReceiveemail(boolean receiveemail) {
		this.receiveemail = receiveemail;
	}

	public String getHomephone() {
		return homephone;
	}

	public void setHomephone(String homephone) {
		this.homephone = homephone;
	}

	public boolean isHomephonepublic() {
		return homephonepublic;
	}

	public void setHomephonepublic(boolean homephonepublic) {
		this.homephonepublic = homephonepublic;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public boolean isMobilephonepublic() {
		return mobilephonepublic;
	}

	public void setMobilephonepublic(boolean mobilephonepublic) {
		this.mobilephonepublic = mobilephonepublic;
	}

	public boolean isReceivetxt() {
		return receivetxt;
	}

	public void setReceivetxt(boolean receivetxt) {
		this.receivetxt = receivetxt;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public boolean isAddresspublic() {
		return addresspublic;
	}

	public void setAddresspublic(boolean addresspublic) {
		this.addresspublic = addresspublic;
	}

	public String getAltcontactname() {
		return altcontactname;
	}

	public void setAltcontactname(String altcontactname) {
		this.altcontactname = altcontactname;
	}

	public String getAltcontactphone() {
		return altcontactphone;
	}

	public void setAltcontactphone(String altcontactphone) {
		this.altcontactphone = altcontactphone;
	}

	public String getAltcontactemail() {
		return altcontactemail;
	}

	public void setAltcontactemail(String altcontactemail) {
		this.altcontactemail = altcontactemail;
	}

	public boolean isAltcontactpublic() {
		return altcontactpublic;
	}

	public void setAltcontactpublic(boolean altcontactpublic) {
		this.altcontactpublic = altcontactpublic;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRolemask() {
		int rolemask = 0;
		
		// parse the selectedRoles array and convert to a bitmap
		for (int i = 0; i < selectedroles.size(); i++) {
			String role = selectedroles.get(i);
			
			if (role.equals("Buyer")) {
				rolemask |= 1;
			} else if (role.equals("Ordering")) {
				rolemask |= 2;
			} else if (role.equals("Accounts")) {
				rolemask |= 4;
			} else if (role.equals("Shareout")) {
				rolemask |= 8;
			} else if (role.equals("Admin")) {
				rolemask |= 16;
			}
		}
		
		return rolemask;
	}

	public ArrayList<String> getSelectedRoles() {
		return selectedroles;
	}

	public void setSelectedRoles(ArrayList<String> selectedRoles) {
		this.selectedroles = selectedRoles;
	}
	
}
