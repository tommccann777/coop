package com.mymato.coop;

import java.math.BigDecimal;

public class Stock {
	
	private int id;
	private int cycleNumber;
	private int nominatedProductId;
	private BigDecimal unitQuantity;

	public Stock() {
		// TODO Auto-generated constructor stub
	}

	public Stock(int id, int cycleNumber, int nominatedProductId, BigDecimal unitQuantity) {
		super();
		this.id = id;
		this.cycleNumber = cycleNumber;
		this.nominatedProductId = nominatedProductId;
		this.unitQuantity = unitQuantity;
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

	public int getNominatedProductId() {
		return nominatedProductId;
	}

	public void setNominatedProductId(int nominatedProductId) {
		this.nominatedProductId = nominatedProductId;
	}

	public BigDecimal getUnitQuantity() {
		return unitQuantity;
	}

	public void setUnitQuantity(BigDecimal unitQuantity) {
		this.unitQuantity = unitQuantity;
	}

}
