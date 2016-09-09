package com.mymato.coop;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


@ManagedBean(name="loginBean")
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String suppliedUsername;
	private String suppliedPassword;
	private String validationMessage;
	private String redirectedFrom;
	private UserDAO userDAO;
	private CycleDAO cycleDAO;
	
	User loggedInUser = null;					// the logged in user
	Cycle currentCycle;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public LoginBean() throws Exception {
		userDAO = UserDAO.getInstance();
		cycleDAO = CycleDAO.getInstance();
	}

	public String doLogin() { //ActionEvent event) {
		boolean usernameValid = false;
		boolean passwordValid = false;
		boolean cycleValid = false;
		
		String msg = "Attempting login with Username=" + suppliedUsername + " and Password=" + suppliedPassword;
		
		//LogDAO.getInstance().addMessage("LoginBean.doLogin", msg);
		
		logger.info("cooplog doLogin: " + msg);
		setValidationMessage(msg);
		
		try {
			// get the user matching the supplied username
			loggedInUser = userDAO.getUser(suppliedUsername, 0);
			
			if (loggedInUser != null) {
				logger.info("cooplog doLogin: retrieved the user object ok. User firstname = " + loggedInUser.getFirstname());
				usernameValid = true;
				passwordValid = PasswordStorage.verifyPassword(suppliedPassword, loggedInUser.getPasswordhash());
			} else {
				logger.info("cooplog doLogin: did not retrieve the user object");
			}
			
		} catch (Exception exc) {
			logger.info("cooplog doLogin: In caught exception for user retrieval");
			usernameValid = false;
		}
		
		try {
			// get and store the current cycle
			currentCycle = cycleDAO.getCurrentCycle();
			
			if (currentCycle != null) {
				logger.info("cooplog doLogin: retrieved the cycle object ok. Cycle number = " + currentCycle.getCycleNumber());
				// Save away the cycle in the application map
				ApplicationMapHelper.setValueInApplicationMap(ApplicationMapHelper.CYCLE_KEY, currentCycle);
				cycleValid = true;
			} else {
				logger.info("cooplog doLogin: did not retrieve the cycle object");
			}
			
		} catch (Exception exc) {
			logger.info("cooplog doLogin: In caught exception for cycle retrieval");
			cycleValid = false;
		}
		
		
		if (usernameValid == false || passwordValid == false || cycleValid == false)
		{
			logger.info("cooplog doLogin: Username and Password combination not recognised");
			setValidationMessage("Username and Password combination not recognised");
			return null;
		}
		
		// Set the AUTH_KEY (used to determine if user is logged in or not)
		ApplicationMapHelper.setValueInSessionMap(ApplicationMapHelper.USER_KEY, loggedInUser);
		
		
		// if user came to the Login form directly, then send them to the Welcome Member view
		if (redirectedFrom == null) {
			redirectedFrom = "/restricted/WelcomeMember.xhtml";
		}
		
		logger.info("cooplog doLogin: Attempting to direct to >" + redirectedFrom + "<");
		
		return(redirectedFrom);
	}
	
	
	public String doLogout() {
		logger.info("Logging out");
		ApplicationMapHelper.removeValueFromSessionMap(ApplicationMapHelper.USER_KEY);
		
		// stops any hangover from previous session if one user logs off and another logs in on same computer
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		loggedInUser = null;
		return "/FrontPage.xhtml";
	}
	
	public String getSuppliedUsername() {
		return suppliedUsername;
	}

	public void setSuppliedUsername(String suppliedUsername) {
		this.suppliedUsername = suppliedUsername;
	}

	public String getSuppliedPassword() {
		return suppliedPassword;
	}

	public void setSuppliedPassword(String suppliedPassword) {
		this.suppliedPassword = suppliedPassword;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}
	
	public boolean isLoggedIn() {
	    return ApplicationMapHelper.getValueFromSessionMap(ApplicationMapHelper.USER_KEY) != null;
	}

	public String getRedirectedFrom() {
		return redirectedFrom;
	}

	public void setRedirectedFrom(String redirectedFrom) {
		this.redirectedFrom = redirectedFrom;
	}

	public Cycle getCurrentCycle() {
		return currentCycle;
	}

	public void setCurrentCycle(Cycle currentCycle) {
		this.currentCycle = currentCycle;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	
	
	public boolean hasRole(String rolename) {
		//logger.info("hasRole: called with parameter rolename=" + rolename);
		
		if (loggedInUser == null) {
			//logger.info("hasRole: loggedInUser is null, returning false");
			return false;
		}
	
		if (loggedInUser.getSelectedRoles().contains(rolename))
		{
			//logger.info("hasRole: returning true");
			return true;
		}
		else
		{
			//logger.info("isRole returning false");
			return false;
		}
	}
	
}
