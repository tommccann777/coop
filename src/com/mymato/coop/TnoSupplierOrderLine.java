package com.mymato.coop;

import java.math.BigDecimal;

public class TnoSupplierOrderLine {

	private String rowType;								// type of node row
	private String categoryColumn;						// the contents of column1
	private VSupplierOrderLine vSupplierOrderLine;		// the payload object
	
	public TnoSupplierOrderLine() {
		// TODO Auto-generated constructor stub
	}

	public TnoSupplierOrderLine(String rowType, String categoryColumn, VSupplierOrderLine vSupplierOrderLine) {
		super();
		this.rowType = rowType;
		this.categoryColumn = categoryColumn;
		this.vSupplierOrderLine = vSupplierOrderLine;
	}
	
	// getters for the payload object
	public String getCategoryColumn() {
		// default
		String colValue = categoryColumn;
		
		if (vSupplierOrderLine != null) {
			colValue = vSupplierOrderLine.getSupplierproductcode() + " - " + vSupplierOrderLine.getProductdescription();
		}
		
		return colValue;
	}
	
	public int getSupplierOrderLineID() {
		// default
		int colValue = 0;
		
		if (vSupplierOrderLine != null) {
			colValue = vSupplierOrderLine.getSupplierorderlineid();
		}
		
		return colValue;
	}

	public String getSupplierProductCode() {
		// default
		String colValue = "";
		
		if (vSupplierOrderLine != null) {
			colValue = vSupplierOrderLine.getSupplierproductcode();
		}
		
		return colValue;
	}

	public String getProductDescription() {
		// default
		String colValue = "";
		
		if (vSupplierOrderLine != null) {
			colValue = vSupplierOrderLine.getProductdescription();
		}
		
		return colValue;
	}

	public String getUnitSize() {
		// default
		String colValue = "";
		
		if (vSupplierOrderLine != null) {
			colValue = vSupplierOrderLine.getUnitsize();
		}
		
		return colValue;
	}

	public String getPackQuantity() {
		// default
		String colValue = "";
		
		if (vSupplierOrderLine != null) {
			colValue = vSupplierOrderLine.getPackquantity();
		}
		
		return colValue;
	}

	public BigDecimal getPrice() {
		// default
		BigDecimal colValue = null; //new BigDecimal(0.0);
		
		if (vSupplierOrderLine != null) {
			colValue = vSupplierOrderLine.getPrice();
		}
		
		return colValue;
	}

	public BigDecimal getOrderQuantity() {
		// default
		BigDecimal colValue = null; //new BigDecimal(0.0);
		
		if (vSupplierOrderLine != null) {
			colValue = vSupplierOrderLine.getOrderquantity();
		}
		
		return colValue;
	}

	public boolean isOrdered() {
		// default
		boolean colValue = false;
		
		if (vSupplierOrderLine != null) {
			colValue = vSupplierOrderLine.isOrdered();
		}
		
		return colValue;
	}
	
	public void setOrdered(boolean ordered) {
		if (vSupplierOrderLine != null) {
			vSupplierOrderLine.setOrdered(ordered);
		}
	}
	
	public String getRowType() {
		return rowType;
	}

}
