package com.mymato.coop;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ConsolidatedProductLine {
	
	private int nominatedProductId;
	private String supplier;
	private String productDescription;
	private String unitSize;
	private String Quantity;
	private BigDecimal unitTradePrice;
	private BigDecimal totalMinQty;
	private BigDecimal totalMaxQty;
	private BigDecimal minQty;
	private BigDecimal maxQty;
	private Timestamp orderDate;

	public ConsolidatedProductLine(int nominatedProductId, String supplier, String productDescription, String unitSize,
			String quantity, BigDecimal unitTradePrice, BigDecimal totalMinQty, BigDecimal totalMaxQty,
			BigDecimal minQty, BigDecimal maxQty, Timestamp orderDate) {
		super();
		this.nominatedProductId = nominatedProductId;
		this.supplier = supplier;
		this.productDescription = productDescription;
		this.unitSize = unitSize;
		this.Quantity = quantity;
		this.unitTradePrice = unitTradePrice;
		this.totalMinQty = totalMinQty;
		this.totalMaxQty = totalMaxQty;
		this.minQty = minQty;
		this.maxQty = maxQty;
		this.orderDate = orderDate;
	}

	public int getNominatedProductId() {
		return nominatedProductId;
	}

	public void setNominatedProductId(int nominatedProductId) {
		this.nominatedProductId = nominatedProductId;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
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
		return Quantity;
	}

	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	public BigDecimal getUnitTradePrice() {
		return unitTradePrice;
	}

	public void setUnitTradePrice(BigDecimal unitTradePrice) {
		this.unitTradePrice = unitTradePrice;
	}

	public BigDecimal getTotalMinQty() {
		return totalMinQty;
	}

	public void setTotalMinQty(BigDecimal totalMinQty) {
		this.totalMinQty = totalMinQty;
	}

	public BigDecimal getTotalMaxQty() {
		return totalMaxQty;
	}

	public void setTotalMaxQty(BigDecimal totalMaxQty) {
		this.totalMaxQty = totalMaxQty;
	}

	public BigDecimal getMinQty() {
		return minQty;
	}

	public void setMinQty(BigDecimal minQty) {
		this.minQty = minQty;
	}

	public BigDecimal getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(BigDecimal maxQty) {
		this.maxQty = maxQty;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

}
