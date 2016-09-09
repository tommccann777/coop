package com.mymato.coop;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class ShareoutSlip {
	
	// size of member array (max number of members supported)
	public static final int MAX_MEMBERS = 20;
	
	private int nominatedProductId;
	private String supplierName;
	private String supplierProductCode;
	private String productDescription;
	
	private BigDecimal[] orderedMember = new BigDecimal[MAX_MEMBERS];
	
	private BigDecimal[] receivedMember = new BigDecimal[MAX_MEMBERS];
	private BigDecimal receivedTotal = new BigDecimal(0.0);

	private BigDecimal receivedAuctionbox = new BigDecimal(0.0);
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public ShareoutSlip() {
		// TODO Auto-generated constructor stub
	}

	public ShareoutSlip(int nominatedProductId, String supplierName, String supplierProductCode, String productDescription,
			BigDecimal orderedMember[], BigDecimal receivedMember[], 
			BigDecimal receivedAuctionbox, BigDecimal receivedTotal) {
		super();
		this.nominatedProductId = nominatedProductId;
		this.supplierName = supplierName;
		this.supplierProductCode = supplierProductCode;
		this.productDescription = productDescription;
		this.orderedMember = orderedMember;
		this.receivedMember = receivedMember;
		this.receivedAuctionbox = receivedAuctionbox;
		this.receivedTotal = receivedTotal;
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

	public BigDecimal[] getOrderedMember() {
		return orderedMember;
	}

	public void setOrderedMember(BigDecimal[] orderedMember) {
		this.orderedMember = orderedMember;
	}
	
	public void setOrderedMember(BigDecimal allocation, int memberIndex) {
		orderedMember[memberIndex] = allocation;
	}
	
	public BigDecimal getOrderedTotal() {
		
		BigDecimal total = new BigDecimal(0);
		
		for (int i = 0; i < MAX_MEMBERS; i++) {
			if (orderedMember[i] != null) {
				total = total.add(orderedMember[i]);
			}			
		}
		
		return total;
	}

	public BigDecimal[] getReceivedMember() {		
		return receivedMember;
	}

	public void setReceivedMember(BigDecimal[] receivedMember) {
		this.receivedMember = receivedMember;
	}

	public void setReceivedMember(BigDecimal received, int memberIndex) {
		receivedMember[memberIndex] = received;
	}
	
	public BigDecimal getReceivedAuctionbox() {
		return receivedAuctionbox;
	}

	public void setReceivedAuctionbox(BigDecimal receivedAuctionbox) {
		this.receivedAuctionbox = receivedAuctionbox;
	}

	public BigDecimal getReceivedTotal() {
		// recalculate the receivedTotal
		BigDecimal total = new BigDecimal(0.0);
		for (int i=0; i < MAX_MEMBERS; i++) {
			if (receivedMember[i] != null) {
				total = total.add(receivedMember[i]);
			}
		}
		
		// add in the auction box
		if (receivedAuctionbox != null) {
			logger.info("Adding " + receivedAuctionbox + " to total " + total);
			total = total.add(receivedAuctionbox);
		} else {
			logger.info("receivedAuctionbox is null");
		}
		
		receivedTotal = total;
		
		logger.info("cooplog: Caclulating Received total=" + total);
		
		return receivedTotal;
	}

	public void setReceivedTotal(BigDecimal receivedTotal) {
		this.receivedTotal = receivedTotal;
	}
}
