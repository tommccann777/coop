package com.mymato.coop;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrderLine {
	
	private int id;
	private int nominatedProductId;
	private int memberId;
	private int cycleNumber;
	private String snapshotPricelist;
	private String snapshotSupplier;
	private String snapshotBrand;
	private String snapshotSupplierProductCode;
	private String snapshotProductDescription;
	private String snapshotUnitSize;
	private String snapshotQuantity;
	private BigDecimal snapshotUnitTradePrice;
	private BigDecimal minQty;
	private BigDecimal maxQty;
	private BigDecimal allocation;
	private BigDecimal received;
	private Timestamp created;
	private Timestamp modified;

	public OrderLine() {
		// TODO Auto-generated constructor stub
	}
	
	// constructor for a NominatedProduct object
	public OrderLine(int cycleNumber, int memberId, NominatedProduct nominatedProduct) {
		nominatedProductId = nominatedProduct.getId();
		this.memberId = memberId;
		this.cycleNumber = cycleNumber;
		this.minQty = new BigDecimal(0.0);
		this.maxQty = new BigDecimal(0.0);
		this.allocation = new BigDecimal(0.0);
		this.received = new BigDecimal(0.0);
		
		this.snapshotPricelist = nominatedProduct.getPricelist();
		this.snapshotSupplier = nominatedProduct.getSupplier();
		this.snapshotBrand = nominatedProduct.getBrand();
		this.snapshotSupplierProductCode = nominatedProduct.getSupplierProductCode();
		this.snapshotProductDescription = nominatedProduct.getProductDescription();
		this.snapshotUnitSize = nominatedProduct.getUnitSize();
		this.snapshotQuantity = nominatedProduct.getQuantity();
		this.snapshotUnitTradePrice = nominatedProduct.getUnitTradePrice();		
	}
	
	// constructor for full specification
	public OrderLine(int id, int cycleNumber, int memberId, int nominatedProductId, BigDecimal minQty, BigDecimal maxQty, BigDecimal allocation, BigDecimal received,
			String snapshotPricelist, String snapshotSupplier, String snapshotBrand, String snapshotSupplierProductCode,
			String snapshotProductDescription, String snapshotUnitSize, String snapshotQuantity, BigDecimal snapshotUnitTradePrice,
			Timestamp created, Timestamp modified) {
		this.id = id;
		this.cycleNumber = cycleNumber;
		this.memberId = memberId;
		this.nominatedProductId = nominatedProductId;
		this.minQty = minQty;
		this.maxQty = maxQty;
		this.allocation = allocation;
		this.received = received;
		
		this.snapshotPricelist = snapshotPricelist;
		this.snapshotSupplier = snapshotSupplier;
		this.snapshotBrand = snapshotBrand;
		this.snapshotSupplierProductCode = snapshotSupplierProductCode;
		this.snapshotProductDescription = snapshotProductDescription;
		this.snapshotUnitSize = snapshotUnitSize;
		this.snapshotQuantity = snapshotQuantity;
		this.snapshotUnitTradePrice = snapshotUnitTradePrice;
		
		this.created = created;
		this.modified = modified;
	}

	public int getNominatedProductId() {
		return nominatedProductId;
	}

	public void setNominatedProductId(int nominatedProductId) {
		this.nominatedProductId = nominatedProductId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getCycleNumber() {
		return cycleNumber;
	}

	public void setCycleNumber(int cycleNumber) {
		this.cycleNumber = cycleNumber;
	}
	
	public String getSnapshotPricelist() {
		return snapshotPricelist;
	}

	public void setSnapshotPricelist(String snapshotPricelist) {
		this.snapshotPricelist = snapshotPricelist;
	}

	public String getSnapshotSupplier() {
		return snapshotSupplier;
	}

	public void setSnapshotSupplier(String snapshotSupplier) {
		this.snapshotSupplier = snapshotSupplier;
	}

	public String getSnapshotBrand() {
		return snapshotBrand;
	}

	public void setSnapshotBrand(String snapshotBrand) {
		this.snapshotBrand = snapshotBrand;
	}

	public String getSnapshotSupplierProductCode() {
		return snapshotSupplierProductCode;
	}

	public void setSnapshotSupplierProductCode(String snapshotSupplierProductCode) {
		this.snapshotSupplierProductCode = snapshotSupplierProductCode;
	}

	public String getSnapshotUnitSize() {
		return snapshotUnitSize;
	}

	public void setSnapshotUnitSize(String snapshotUnitSize) {
		this.snapshotUnitSize = snapshotUnitSize;
	}

	public String getSnapshotQuantity() {
		return snapshotQuantity;
	}

	public void setSnapshotQuantity(String snapshotQuantity) {
		this.snapshotQuantity = snapshotQuantity;
	}

	public BigDecimal getSnapshotUnitTradePrice() {
		return snapshotUnitTradePrice;
	}

	public void setSnapshotUnitTradePrice(BigDecimal snapshotUnitTradePrice) {
		this.snapshotUnitTradePrice = snapshotUnitTradePrice;
	}

	public String getSnapshotProductDescription() {
		return snapshotProductDescription;
	}

	public void setSnapshotProductDescription(String snapshotProductDescription) {
		this.snapshotProductDescription = snapshotProductDescription;
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
	
	public BigDecimal getAllocation() {
		return allocation;
	}

	public void setAllocation(BigDecimal allocation) {
		this.allocation = allocation;
	}

	public BigDecimal getReceived() {
		return received;
	}

	public void setReceived(BigDecimal received) {
		this.received = received;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
