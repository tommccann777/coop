package com.mymato.coop;

import java.math.BigDecimal;

public class TnoOrderAllocation {
	
	private String rowtype;
	private int productid;
	private int orderLineid;
	private String productcode;
	private String productdescription;
	private String unitsize;
	private String quantity;
	private BigDecimal price;
	private String membername;
	private BigDecimal minqty;
	private BigDecimal maxqty;
	private BigDecimal allocation;
	private String supplier;
	private int supplierorderlineid;
	private BigDecimal supplierqty;

	public TnoOrderAllocation() {
		// TODO Auto-generated constructor stub
	}

	

	public TnoOrderAllocation(String rowtype, int productid, int orderLineid, String productcode,
			String productdescription, String unitsize, String quantity, BigDecimal price, String membername,
			BigDecimal minqty, BigDecimal maxqty, BigDecimal allocation, String supplier, int supplierorderlineid
			, BigDecimal supplierqty) {
		super();
		this.rowtype = rowtype;
		this.productid = productid;
		this.orderLineid = orderLineid;
		this.productcode = productcode;
		this.productdescription = productdescription;
		this.unitsize = unitsize;
		this.quantity = quantity;
		this.price = price;
		this.membername = membername;
		this.minqty = minqty;
		this.maxqty = maxqty;
		this.allocation = allocation;
		this.supplier = supplier;
		this.supplierorderlineid = supplierorderlineid;
		this.supplierqty = supplierqty;
	}


	public String getCodeAndDescription() {
		String result = null;
		
		if (rowtype.equals("Product")) {
			if (supplier.equals("Stock")) {
				// If a Stock item
				result = "Stock - " + productdescription;
			} else {
				// If a supplier item
				if (productcode.equals("")) {
					// Supplier item, productcode blank
					result = productdescription;
				} else {
					// Supplier item with productcode
					result = productcode + " - " + productdescription;
				}
				
			}
			
		} else {
			result = "";
		}
		
		return result;
	}
	
	public String getRowtype() {
		return rowtype;
	}



	public void setRowtype(String rowtype) {
		this.rowtype = rowtype;
	}



	public int getProductid() {
		return productid;
	}



	public void setProductid(int productid) {
		this.productid = productid;
	}



	public int getOrderLineid() {
		return orderLineid;
	}



	public void setOrderLineid(int orderLineid) {
		this.orderLineid = orderLineid;
	}



	public String getProductcode() {
		return productcode;
	}



	public void setProductcode(String productcode) {
		this.productcode = productcode;
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



	public String getQuantity() {
		return quantity;
	}



	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}



	public BigDecimal getPrice() {
		return price;
	}



	public void setPrice(BigDecimal price) {
		this.price = price;
	}



	public String getMembername() {
		return membername;
	}



	public void setMembername(String membername) {
		this.membername = membername;
	}



	public BigDecimal getMinqty() {
		return minqty;
	}



	public void setMinqty(BigDecimal minqty) {
		this.minqty = minqty;
	}



	public BigDecimal getMaxqty() {
		return maxqty;
	}



	public void setMaxqty(BigDecimal maxqty) {
		this.maxqty = maxqty;
	}



	public BigDecimal getAllocation() {
		return allocation;
	}

	public void setAllocation(BigDecimal allocation) {
		this.allocation = allocation;
	}
	
	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public BigDecimal getSupplierqty() {
		return supplierqty;
	}

	public void setSupplierqty(BigDecimal supplierqty) {
		this.supplierqty = supplierqty;
	}

	public int getSupplierorderlineid() {
		return supplierorderlineid;
	}

	public void setSupplierorderlineid(int supplierorderlineid) {
		this.supplierorderlineid = supplierorderlineid;
	}	
	
}
