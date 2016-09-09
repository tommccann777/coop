package com.mymato.coop;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="oldpricelistBean")
@SessionScoped
public class oldPricelistBean {
	
	private List<PricelistProduct> filteredProducts;
	
	private Logger logger = Logger.getLogger(getClass().getName());

	public oldPricelistBean() throws Exception {
		
	}
	
	
	public List<PricelistProduct> getFilteredProducts() {
		return filteredProducts;
	}

	public void setFilteredProducts(List<PricelistProduct> filteredProducts) {
		this.filteredProducts = filteredProducts;
	}
	
		
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
