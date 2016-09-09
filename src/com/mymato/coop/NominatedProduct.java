package com.mymato.coop;

import java.math.BigDecimal;

public class NominatedProduct extends PricelistProduct {
	
	private int pricelistProductId;		// foreign key to pricelist product
	private int cycleNumber;			// the cycle in which the product was nominated
	private int stockId;				// foreign key to the Stock record

	public NominatedProduct() {
		// TODO Auto-generated constructor stub
	}

	public NominatedProduct(int id, String pricelist, String supplier, String brand, String supplierProductCode,
			String productDescription, String unitSize, String quantity, BigDecimal unitTradePrice, boolean valid,
			int cycleNumber, int pricelistProductId, int stockId) {
		
		super(id, pricelist, supplier, brand, supplierProductCode, productDescription, unitSize, quantity,
				unitTradePrice, valid);
		
		this.cycleNumber = cycleNumber;
		this.pricelistProductId = pricelistProductId;
		this.stockId = stockId;
	}

	public int getPricelistProductId() {
		return pricelistProductId;
	}

	public void setPricelistProductId(int pricelistProductId) {
		this.pricelistProductId = pricelistProductId;
	}

	public int getCycleNumber() {
		return cycleNumber;
	}

	public void setCycleNumber(int cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

}
