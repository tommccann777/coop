package com.mymato.coop;

import java.util.Date;
import java.text.SimpleDateFormat;
//import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Cycle {

	private int id;
	private int cycleNumber;
	private Date startDay;
	private Date orderFirstDay;
	private Date orderLastDay;
	private Date shareoutDay;
	private Date closeDay;
	private String bannerMessage;
	private String shareoutLocation;
	
	//private Logger logger = Logger.getLogger(getClass().getName());
	
	public Cycle() {
		// no-arg constructor
	}

	public Cycle(int id, int cycleNumber, Date startDay, Date orderFirstDay, Date orderLastDay, Date shareoutDay,
			Date closeDay, String bannerMessage, String shareoutLocation) {
		super();
		this.id = id;
		this.cycleNumber = cycleNumber;
		this.startDay = startDay;
		this.orderFirstDay = orderFirstDay;
		this.orderLastDay = orderLastDay;
		this.shareoutDay = shareoutDay;
		this.closeDay = closeDay;
		this.bannerMessage = bannerMessage;
		this.shareoutLocation = shareoutLocation;
	}
	

	public boolean isEditable() {
		//SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    //String strDate = sdfDate.format(startDay);
	    
		//logger.info("running isEditable against " + strDate);
		
		return isActive();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCycleNumber() {
		return cycleNumber;
	}

	public void setCycleNumber(int cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	public boolean isActive() {
		Date exclCloseDate = new Date();
		exclCloseDate.setTime(closeDay.getTime() + 1 * 24 * 60 * 60 * 1000);
		
		if (startDay.compareTo(new java.util.Date()) <= 0 && exclCloseDate.compareTo(new java.util.Date()) >= 0)
			return true;
		else
			return false;
	}

	public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		this.startDay = startDay;
	}
	
	public String getStrStartDay() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	    String strDate = sdfDate.format(startDay);
	    
	    return strDate;
	}

	public Date getOrderFirstDay() {
		return orderFirstDay;
	}

	public void setOrderFirstDay(Date orderFirstDay) {
		this.orderFirstDay = orderFirstDay;
	}

	public Date getOrderLastDay() {
		return orderLastDay;
	}

	public void setOrderLastDay(Date orderLastDay) {
		this.orderLastDay = orderLastDay;
	}

	public Date getShareoutDay() {
		return shareoutDay;
	}

	public void setShareoutDay(Date shareoutDay) {
		this.shareoutDay = shareoutDay;
	}

	public Date getCloseDay() {
		return closeDay;
	}

	public void setCloseDay(Date closeDay) {
		this.closeDay = closeDay;
	}

	public String getBannerMessage() {
		return bannerMessage;
	}

	public void setBannerMessage(String bannerMessage) {
		this.bannerMessage = bannerMessage;
	}
	
	public String getShareoutLocation() {
		return shareoutLocation;
	}

	public void setShareoutLocation(String shareoutLocation) {
		this.shareoutLocation = shareoutLocation;
	}

}
