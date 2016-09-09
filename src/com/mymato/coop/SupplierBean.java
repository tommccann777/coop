package com.mymato.coop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

@ManagedBean(name="supplierBean")
@SessionScoped
public class SupplierBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Supplier> suppliers;
	private SupplierDAO supplierDAO;
	private Logger logger = Logger.getLogger(getClass().getName());

	public SupplierBean() throws Exception {
		suppliers = new ArrayList<>();
		
		supplierDAO = SupplierDAO.getInstance();
	}
	
	
	public void loadSuppliers(ComponentSystemEvent event) {

		//if (!FacesContext.getCurrentInstance().isPostback()) {
			// this prevents multiple loads in a view scoped bean
			//logger.info("Loading cycles");
			
			suppliers.clear();

			try {
				
				// get all cycles from database
				suppliers = supplierDAO.getSuppliers();
				
			} catch (Exception exc) {
				// send this to server logs
				logger.log(Level.SEVERE, "cooplog: Error loading suppliers", exc);
				
				// add error message for JSF page
				addErrorMessage(exc);
			}

		
	}
	
	public List<Supplier> getSuppliers() {
		//logger.info("Getting suppliers");
		return suppliers;
	}
	
	public String loadSupplier(int supplierId) {
		
		logger.info("cooplog: Loading supplier " + supplierId + " (there are " + suppliers.size() + " suppliers in the list)");
		
		try {
			// get the cycle from the database
			Supplier supplier = supplierDAO.getSupplier(supplierId);
			
			logger.info("cooplog: Loaded supplier id: " + supplier.getId() + " name is " + supplier.getSupplierName());
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> requestMap = externalContext.getRequestMap();
			
			requestMap.put("supplier", supplier);
					
		} catch(Exception exc) {
			// send this to the server logs
			logger.log(Level.SEVERE, "cooplog: Error loading supplier id: " + supplierId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			// stay on current page if a message occurs
			return null;
		}
		
		// If all works, then go to the edit cycle page
		return "AdminUpdateSupplier";
	}
	
	
	public String addSupplier(Supplier supplier) {
		
		// check for duplicate username first
		for (int i = 0; i < suppliers.size(); i++) {
			if (supplier.getSupplierName().compareToIgnoreCase(suppliers.get(i).getSupplierName()) == 0) {
				addErrorMessage(FacesMessage.SEVERITY_INFO, "There is already a supplier with name " + supplier.getSupplierName() + ". Please choose another name.");
				return null;
			}				
		}
			
		//logger.info("Adding supplier: " + user.getSupplierName());
		
		try {
			// update user in the database
			supplierDAO.addSupplier(supplier);
			
		} catch(Exception exc) {
			// send this to the server logs
			logger.log(Level.SEVERE, "cooplog: Error adding supplier: " + supplier.getSupplierName(), exc);
			
			addErrorMessage(exc);
			
			// return to current page
			return null;
		}
		
		// if all works then return to the list
		
		return "AdminSuppliers?faces-redirect=true";
	}

	
	public String updateSupplier(Supplier supplier) {
		
		logger.info("cooplog: Updating supplier id: " + supplier.getId() + " Supplier name: " + supplier.getSupplierName());
		
		// check for duplicate username first
		for (int i = 0; i < suppliers.size(); i++) {
			Supplier existingSupplier = suppliers.get(i);
			
			if (supplier.getId() != existingSupplier.getId() && supplier.getSupplierName().compareToIgnoreCase(existingSupplier.getSupplierName()) == 0) {
				addErrorMessage(FacesMessage.SEVERITY_INFO, "There is already a supplier with name " + supplier.getSupplierName() + ". Please choose another name.");
				return null;
			}				
		}
		
		try {
			// update cycle in the database
			supplierDAO.updateSupplier(supplier);
			
		} catch(Exception exc) {
			// send this to the server logs
			logger.log(Level.SEVERE, "cooplog: Error updating supplier id: " + supplier.getId(), exc);
			
			addErrorMessage(exc);
			
			// return to current page
			return null;
		}
		
		// if all works then return to the list
		return "AdminSuppliers?faces-redirect=true";
	}
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	private void addErrorMessage(Severity sev, String errMessage) {
		
		FacesMessage message = new FacesMessage(sev, errMessage, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	

}

