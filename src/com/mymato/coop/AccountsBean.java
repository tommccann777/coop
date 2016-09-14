package com.mymato.coop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="accountsBean")
@SessionScoped
public class AccountsBean implements Serializable {

	private List<Cycle> cycles = new ArrayList<Cycle>();
	private int selectedCycle;
	
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(getClass().getName());

	public AccountsBean() {
		// TODO Auto-generated constructor stub
	}
	
	// load the list of cycle descriptors
	public void loadCycles() {
		
		try {
			cycles = CycleDAO.getInstance().getCycles();
		} catch (Exception exc) {
			logger.severe("cooplog: Cycle descriptors could not be retrieved.");
			return;
		}
		
	}
	
	public List<OrderLine> getReceived(int cycleNumber) {
		
		List<OrderLine> orderlines = new ArrayList<OrderLine>();
		
		try {
			orderlines = OrderLineDAO.getInstance().getOrderLines(cycleNumber, true);
		} catch (Exception exc) {
			logger.severe("cooplog: OrderLines for cycle " + cycleNumber + " could not be retrieved.");
		}
		
		return orderlines;
		
	}
	
	public List<Cycle> getCycles() {
		return cycles;
	}

	public void setCycles(List<Cycle> cycles) {
		this.cycles = cycles;
	}

	public int getSelectedCycle() {
		logger.info("Getting selectedCycle");
		return selectedCycle;
	}

	public void setSelectedCycle(int selectedCycle) {
		logger.info("Setting selectedCycle");
		this.selectedCycle = selectedCycle;
	}
	
	
	
	

}
