package com.mymato.coop;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class SupplierOrderLine {

	private int id;
	private int cycleNumber;
	private String supplier;
	private int productId;
	private BigDecimal qty;
	private int memberId;
	private boolean ordered;
	private Timestamp created;
	private Timestamp modified;
	
	public SupplierOrderLine() {
		// TODO Auto-generated constructor stub
	}

	public SupplierOrderLine(int id, int cycleNumber, String supplier, int productId, BigDecimal qty, int memberId,
			boolean ordered, Timestamp created, Timestamp modified) {
		super();
		this.id = id;
		this.cycleNumber = cycleNumber;
		this.supplier = supplier;
		this.productId = productId;
		this.qty = qty;
		this.memberId = memberId;
		this.ordered = ordered;
		this.created = created;
		this.modified = modified;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCycleNumber() {
		return cycleNumber;
	}

	public void setCycleNumber(int cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public boolean isOrdered() {
		return ordered;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getModified() {
		return modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

}
