package com.mymato.coop;

import java.math.BigDecimal;

public class AccountsMemberReceivedLine {
	
	private int cycleNumber;
	private String username;
	private String snapshotPricelist;
	private String snapshotSupplier;
	private String snapshotBrand;
	private String snapshotSupplierProductCode;
	private String snapshotProductDescription;
	private String snapshotUnitSize;
	private String snapshotQuantity;
	private BigDecimal received;
	private BigDecimal invoicedUnitTradePrice;

	public AccountsMemberReceivedLine() {
		// TODO Auto-generated constructor stub
	}

	public AccountsMemberReceivedLine(int cycleNumber, String username, String snapshotPricelist,
			String snapshotSupplier, String snapshotBrand, String snapshotSupplierProductCode,
			String snapshotProductDescription, String snapshotUnitSize, String snapshotQuantity, BigDecimal received,
			BigDecimal invoicedUnitTradePrice) {
		super();
		this.cycleNumber = cycleNumber;
		this.username = username;
		this.snapshotPricelist = snapshotPricelist;
		this.snapshotSupplier = snapshotSupplier;
		this.snapshotBrand = snapshotBrand;
		this.snapshotSupplierProductCode = snapshotSupplierProductCode;
		this.snapshotProductDescription = snapshotProductDescription;
		this.snapshotUnitSize = snapshotUnitSize;
		this.snapshotQuantity = snapshotQuantity;
		this.received = received;
		this.invoicedUnitTradePrice = invoicedUnitTradePrice;
	}

	public int getCycleNumber() {
		return cycleNumber;
	}

	public void setCycleNumber(int cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getSnapshotProductDescription() {
		return snapshotProductDescription;
	}

	public void setSnapshotProductDescription(String snapshotProductDescription) {
		this.snapshotProductDescription = snapshotProductDescription;
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

}
