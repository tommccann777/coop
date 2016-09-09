package com.mymato.coop;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
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


@ManagedBean(name="userBean")
@SessionScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<User> users;
	private UserDAO userDAO;
	private Logger logger = Logger.getLogger(getClass().getName());

	public UserBean() throws Exception {
		users = new ArrayList<>();		
		userDAO = UserDAO.getInstance();
	}
	
	public void loadUsers(ComponentSystemEvent event) {

		//if (!FacesContext.getCurrentInstance().isPostback()) {
			// this prevents multiple loads in a view scoped bean
			//logger.info("Loading users");
			
		LogDAO.getInstance().addMessage("loadUsers", "Loading List of Users");
		
			users.clear();

			try {
				
				// get all users from database
				users = userDAO.getUsers();
				
			} catch (Exception exc) {
				// send this to server logs
				logger.log(Level.SEVERE, "cooplog: Error loading users", exc);
				
				// add error message for JSF page
				addErrorMessage(exc);
			}

		
	}
	
	public List<User> getUsers() {
		//logger.info("Getting users");
		return users;
	}
	
	public String loadUser(int userId) {
		
		logger.info("cooplog: Loading user " + userId + " (there are " + users.size() + " users in the list)");
		
		try {
			// get the user from the database
			User user = userDAO.getUser(null, userId);
			
			logger.info("cooplog: Loaded user id: " + user.getId());
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> requestMap = externalContext.getRequestMap();
			
			requestMap.put("user", user);			
		} catch(Exception exc) {
			// send this to the server logs
			logger.log(Level.SEVERE, "cooplog: Error loading user id: " + userId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			// stay on current page if a message occurs
			return null;
		}
		
		// If all works, then go to the edit user page
		return "/restricted/AdminUpdateUser";
	}
	

	public String addUser(User user) {
		
		// check for duplicate username first
		for (int i = 0; i < users.size(); i++) {
			if (user.getUsername().compareToIgnoreCase(users.get(i).getUsername()) == 0) {
				addErrorMessage(FacesMessage.SEVERITY_INFO, "There is already a user with username " + user.getUsername() + ". Please choose another username.");
				return null;
			}				
		}
			
		//logger.info("Adding user: " + user.getFirstname() + " " + user.getLastname());
		
		try {
			// update user in the database
			userDAO.addUser(user);
			
		} catch(Exception exc) {
			// send this to the server logs
			logger.log(Level.SEVERE, "cooplog: Error adding user: " + user.getFirstname() + " " + user.getLastname(), exc);
			
			addErrorMessage(exc);
			
			// return to current page
			return null;
		}
		
		// if all works then return to the list
		
		return "AdminUsers?faces-redirect=true";
	}

	
	public String updateUser(User user) {
		
		logger.info("Updating user id: " + user.getId() + " Username: " + user.getFirstname() + " " + user.getLastname());
		
		// check for duplicate username first
		for (int i = 0; i < users.size(); i++) {
			User existingUser = users.get(i);
			
			if (user.getId() != existingUser.getId() && user.getUsername().compareToIgnoreCase(existingUser.getUsername()) == 0) {
				addErrorMessage(FacesMessage.SEVERITY_INFO, "There is already a user with username " + user.getUsername() + ". Please choose another username.");
				return null;
			}				
		}		
		
		try {
			// update user in the database
			userDAO.updateUser(user);
			
		} catch(Exception exc) {
			// send this to the server logs
			logger.log(Level.SEVERE, "cooplog: Error updating user id: " + user.getId(), exc);
			
			addErrorMessage(exc);
			
			// return to current page
			return null;
		}
		
		// if all works then return to the list
		return "AdminUsers?faces-redirect=true";
	}
	
	public String deleteUser(int userId) {
		
		logger.info("Deleting user id: " + userId);
		LogDAO.getInstance().addMessage("deleteUser", "Attempting to delete user " + userId);
		
		try {
			// update user in the database
			userDAO.deleteUser(userId);
			
		} catch(Exception exc) {
			// send this to the server logs
			logger.log(Level.SEVERE, "cooplog: Error deleting user id: " + userId, exc);
			
			addErrorMessage(exc);
			
			// return to current page
			return null;
		}
		
		// if all works then return to the list
		return "AdminUsers?faces-redirect=true";
	}
	
	
	private void addErrorMessage(Exception exc) {
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exc.printStackTrace(pw);
		String st = sw.toString(); // stack trace as a string
		
		FacesMessage message = new FacesMessage("Error: " + st); // exc.getCause() + " - " + exc.getMessage());   //exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	private void addErrorMessage(Severity sev, String errMessage) {
		
		FacesMessage message = new FacesMessage(sev, errMessage, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}


}
