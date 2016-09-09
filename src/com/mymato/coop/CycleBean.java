package com.mymato.coop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

@ManagedBean(name="cycleBean")
@SessionScoped
public class CycleBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Cycle> cycles;
	private CycleDAO cycleDAO;
	private Logger logger = Logger.getLogger(getClass().getName());

	public CycleBean() throws Exception {
		cycles = new ArrayList<>();
		
		cycleDAO = CycleDAO.getInstance();
	}
	
	public String pass(Cycle cycle, Date sd) {
		return("Passed parameters banner message=" + cycle.getBannerMessage() + ", start day=" + sd);
	}
	
	public void loadCycles(ComponentSystemEvent event) {

		//if (!FacesContext.getCurrentInstance().isPostback()) {
			// this prevents multiple loads in a view scoped bean
			//logger.info("Loading cycles");
			
			cycles.clear();

			try {
				
				// get all cycles from database
				cycles = cycleDAO.getCycles();
				
			} catch (Exception exc) {
				// send this to server logs
				logger.log(Level.SEVERE, "cooplog: Error loading cycles", exc);
				
				// add error message for JSF page
				addErrorMessage(exc);
			}

		
	}
	
	public List<Cycle> getCycles() {
		//logger.info("Getting cycles");
		return cycles;
	}
	
	public String loadCycle(int cycleId) {
		
		logger.info("cooplog: Loading cycle " + cycleId + " (there are " + cycles.size() + " cycles in the list)");
		
		try {
			// get the cycle from the database
			Cycle cycle = cycleDAO.getCycle(cycleId);
			
			logger.info("cooplog: Loaded cycle id: " + cycle.getId());
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> requestMap = externalContext.getRequestMap();
			
			requestMap.put("cycle", cycle);			
		} catch(Exception exc) {
			// send this to the server logs
			logger.log(Level.SEVERE, "cooplog: Error loading cycle id: " + cycleId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			// stay on current page if a message occurs
			return null;
		}
		
		// If all works, then go to the edit cycle page
		return "AdminCycle";
	}
	
	
	public String updateCycle(Cycle cycle) {
		
		logger.info("cooplog: Updating cycle id: " + cycle.getId() + " Cycle number: " + cycle.getCycleNumber());
		
		try {
			// update cycle in the database
			cycleDAO.updateCycle(cycle);
			
		} catch(Exception exc) {
			// send this to the server logs
			logger.log(Level.SEVERE, "cooplog: Error updating cycle id: " + cycle.getId(), exc);
			
			addErrorMessage(exc);
			
			// return to current page
			return null;
		}
		
		// if all works then return to the list
		return "AdminCycles?faces-redirect=true";
	}
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
