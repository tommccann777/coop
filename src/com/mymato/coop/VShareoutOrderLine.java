package com.mymato.coop;

import java.math.BigDecimal;

public class VShareoutOrderLine {
	
	private int orderLineId;
	private int nominatedProductId;
	private String supplierName;
	private String supplierProductCode;
	private String productDescription;
	private int memberId;
	private String memberName;
	private BigDecimal allocated;
	private BigDecimal received;
	private BigDecimal invoicedUnitTradePrice;
	private BigDecimal stockQuantity;
	private BigDecimal refundAmount;

	public VShareoutOrderLine() {
		// TODO Auto-generated constructor stub
	}

	public VShareoutOrderLine(int orderLineId, int nominatedProductId, String supplierName, String supplierProductCode,
			String productDescription, int memberId, String memberName, BigDecimal allocated, BigDecimal received,
			BigDecimal invoicedUnitTradePrice, BigDecimal stockQuantity, BigDecimal refundAmount) {
		super();
		this.orderLineId = orderLineId;
		this.nominatedProductId = nominatedProductId;
		this.supplierName = supplierName;
		this.supplierProductCode = supplierProductCode;
		this.productDescription = productDescription;
		this.memberId = memberId;
		this.memberName = memberName;
		this.allocated = allocated;
		this.received = received;
		this.invoicedUnitTradePrice = invoicedUnitTradePrice;
		this.stockQuantity = stockQuantity;
		this.refundAmount = refundAmount;		// used in Accounts pages as an input/output
	}

	public int getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(int orderLineId) {
		this.orderLineId = orderLineId;
	}

	public int getNominatedProductId() {
		return nominatedProductId;
	}

	public void setNominatedProductId(int nominatedProductId) {
		this.nominatedProductId = nominatedProductId;
	}
	
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public BigDecimal getAllocated() {
		return allocated;
	}

	public void setAllocated(BigDecimal allocated) {
		this.allocated = allocated;
	}
	
	public BigDecimal getReceived() {
		return received;
	}

	public void setReceived(BigDecimal received) {
		this.received = received;
	}

	public BigDecimal getInvoicedUnitTradePrice() {
		return invoicedUnitTradePrice;
	}

	public void setInvoicedUnitTradePrice(BigDecimal invoicedUnitTradePrice) {
		this.invoicedUnitTradePrice = invoicedUnitTradePrice;
	}

	public BigDecimal getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(BigDecimal stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	
}
