package com.mymato.coop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ComponentSystemEvent;

@ManagedBean(name="frontpageBean")
@SessionScoped
public class FrontPage implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<User> users;
	private UserDAO userDAO;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public FrontPage() throws Exception {
		users = new ArrayList<>();		
		userDAO = UserDAO.getInstance();
	}
	
	public void loadUsers(ComponentSystemEvent event) {

		//if (!FacesContext.getCurrentInstance().isPostback()) {
			// this prevents multiple loads in a view scoped bean
			//logger.info("Loading users");
			
			users.clear();

			try {
				
				// get all users from database
				users = userDAO.getUsers();
				
			} catch (Exception exc) {
				// send this to server logs
				logger.log(Level.SEVERE, "cooplog: Error loading users", exc);
			}

		
	}
	
	public List<User> getUsers() {
		//logger.info("Getting users");
		return users;
	}
	
	public String getEnvironment() {
		Properties props = System.getProperties();
		return props.getProperty(getEnvironment());
	}

}
