package com.mymato.coop;

import javax.faces.context.FacesContext;

public class ApplicationMapHelper {
	
	public static final String USER_KEY = "coop.currentUser";
	public static final String CYCLE_KEY = "coop.currentCycle";

	// Application map methods
		public static Object getValueFromApplicationMap(String key) {
			return FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get(key);
		}
		
		public static void setValueInApplicationMap(String key, Object value) {
			FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().put(key, value);
		}
		
		public static void removeValueFromApplicationMap(String key) {
			FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().remove(key);
		}
		
	// Session map methods
		public static Object getValueFromSessionMap(String key) {
			return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(key);
		}
			
		public static void setValueInSessionMap(String key, Object value) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(key, value);
		}
		
		public static void removeValueFromSessionMap(String key) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(key);
		}
}
