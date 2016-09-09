package com.mymato.coop;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Supplier implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String supplierName;
	private String contact;
	private String phone;
	private String email;
	private String website;
	

	public Supplier() {
		// TODO Auto-generated constructor stub
	}


	public Supplier(int id, String supplierName, String contact, String phone, String email, String website) {
		super();
		this.id = id;
		this.supplierName = supplierName;
		this.contact = contact;
		this.phone = phone;
		this.email = email;
		this.website = website;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getSupplierName() {
		return supplierName;
	}


	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}


	public String getContact() {
		return contact;
	}


	public void setContact(String contact) {
		this.contact = contact;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getWebsite() {
		return website;
	}


	public void setWebsite(String website) {
		this.website = website;
	}

}
