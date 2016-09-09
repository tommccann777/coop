package com.mymato.coop;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Pricelist {
	
	private int id;
	private int supplierId;
	private String suppliernameDenorm;
	private String listname;
	private boolean active;

	public Pricelist() {
		// TODO Auto-generated constructor stub
	}

	public Pricelist(int id, int supplierId, String suppliernameDenorm, String listname, boolean active) {
		super();
		this.id = id;
		this.supplierId = supplierId;
		this.suppliernameDenorm = suppliernameDenorm;
		this.listname = listname;
		this.active = active;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getListname() {
		return listname;
	}

	public void setListname(String listname) {
		this.listname = listname;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getSuppliernameDenorm() {
		return suppliernameDenorm;
	}

	public void setSuppliernameDenorm(String suppliernameDenorm) {
		this.suppliernameDenorm = suppliernameDenorm;
	}
}
