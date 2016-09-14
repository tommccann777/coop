package com.mymato.coop;

import java.math.BigDecimal;

public class NominatedProduct extends PricelistProduct {
	
	private int pricelistProductId;				// foreign key to pricelist product
	private int cycleNumber;					// the cycle in which the product was nominated
	private BigDecimal stockQuantity;			// amount of stock held
	private BigDecimal invoicedUnitTradePrice;	// the final price of the product from the supplier invoice

	public NominatedProduct() {
		// TODO Auto-generated constructor stub
	}

	public NominatedProduct(int id, String pricelist, String supplier, String brand, String supplierProductCode,
			String productDescription, String unitSize, String quantity, BigDecimal unitTradePrice, boolean valid,
			int cycleNumber, int pricelistProductId, BigDecimal stockQuantity, BigDecimal invoicedUnitTradePrice) {
		
		super(id, pricelist, supplier, brand, supplierProductCode, productDescription, unitSize, quantity,
				unitTradePrice, valid);
		
		this.cycleNumber = cycleNumber;
		this.pricelistProductId = pricelistProductId;
		this.stockQuantity = stockQuantity;
		this.invoicedUnitTradePrice = invoicedUnitTradePrice;
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

	public BigDecimal getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(BigDecimal stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public BigDecimal getInvoicedUnitTradePrice() {
		return invoicedUnitTradePrice;
	}

	public void setInvoicedUnitTradePrice(BigDecimal invoicedUnitTradePrice) {
		this.invoicedUnitTradePrice = invoicedUnitTradePrice;
	}

}
