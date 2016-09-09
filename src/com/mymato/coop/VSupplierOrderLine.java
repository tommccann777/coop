package com.mymato.coop;

import java.math.BigDecimal;

public class VSupplierOrderLine {
	
	private String suppliername;
	private int supplierorderlineid;
	private String supplierproductcode;
	private String productdescription;
	private String unitsize;
	private String packquantity;
	private BigDecimal price;
	private BigDecimal orderquantity;
	private boolean ordered;

	public VSupplierOrderLine() {
		// TODO Auto-generated constructor stub
	}

	public VSupplierOrderLine(String suppliername, int supplierorderlineid,
			String supplierproductcode, String productdescription, String unitsize, String packquantity,
			BigDecimal price, BigDecimal orderquantity, boolean ordered) {
		super();
		this.suppliername = suppliername;
		this.supplierorderlineid = supplierorderlineid;
		this.supplierproductcode = supplierproductcode;
		this.productdescription = productdescription;
		this.unitsize = unitsize;
		this.packquantity = packquantity;
		this.price = price;
		this.orderquantity = orderquantity;
		this.ordered = ordered;
	}

	public String getSuppliername() {
		return suppliername;
	}

	public void setSuppliername(String suppliername) {
		this.suppliername = suppliername;
	}

	public int getSupplierorderlineid() {
		return supplierorderlineid;
	}

	public void setSupplierorderlineid(int supplierorderlineid) {
		this.supplierorderlineid = supplierorderlineid;
	}

	public String getSupplierproductcode() {
		return supplierproductcode;
	}

	public void setSupplierproductcode(String supplierproductcode) {
		this.supplierproductcode = supplierproductcode;
	}

	public String getProductdescription() {
		return productdescription;
	}

	public void setProductdescription(String productdescription) {
		this.productdescription = productdescription;
	}

	public String getUnitsize() {
		return unitsize;
	}

	public void setUnitsize(String unitsize) {
		this.unitsize = unitsize;
	}

	public String getPackquantity() {
		return packquantity;
	}

	public void setPackquantity(String packquantity) {
		this.packquantity = packquantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getOrderquantity() {
		return orderquantity;
	}

	public void setOrderquantity(BigDecimal orderquantity) {
		this.orderquantity = orderquantity;
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}
}
