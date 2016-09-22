package com.mymato.coop;

import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class PricelistProduct {

	private int id;
	private String pricelist;
	private String supplier;
	private String brand;
	private String supplierProductCode;
	private String productDescription;
	private String unitSize;
	private String quantity;
	private BigDecimal unitTradePrice;
	private boolean valid;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public PricelistProduct() {
		// No-arg constructor
	}


	public PricelistProduct(int id, String pricelist, String supplier, String brand, String supplierProductCode,
			String productDescription, String unitSize, String quantity, BigDecimal unitTradePrice, boolean valid) {
		
		this.id = id;
		this.pricelist = pricelist;
		this.supplier = supplier;
		this.brand = brand;
		this.supplierProductCode = supplierProductCode;		
		this.productDescription = productDescription;
		this.unitSize = unitSize;
		this.quantity = quantity;
		this.unitTradePrice = unitTradePrice;
		this.valid = valid;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPricelist() {
		return pricelist;
	}

	public void setPricelist(String pricelist) {
		this.pricelist = pricelist;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getSupplierProductCode() {
		return supplierProductCode;
	}


	public void setSupplierProductCode(String supplierProductCode) {
		this.supplierProductCode = supplierProductCode;
	}


	public String getProductDescription() {
		return productDescription;
	}


	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}


	public String getUnitSize() {
		return unitSize;
	}


	public void setUnitSize(String unitSize) {
		this.unitSize = unitSize;
	}


	public String getQuantity() {
		return quantity;
	}


	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}


	public BigDecimal getUnitTradePrice() {
		return unitTradePrice;
	}


	public void setUnitTradePrice(BigDecimal unitTradePrice) {
		this.unitTradePrice = unitTradePrice;
	}


	public boolean isValid() {
		return valid;
	}


	public void setValid(boolean valid) {
		this.valid = valid;
	}

}
