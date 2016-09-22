package com.mymato.coop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="accountsBean")
@SessionScoped
public class AccountsBean implements Serializable {

	private List<Cycle> cycles = new ArrayList<Cycle>();
	private int selectedCycle;
	
	private List<ShareoutSlip> shareoutSlips;
	private List<User> users;
	private BigDecimal[] totalForMember = new BigDecimal[ShareoutSlip.MAX_MEMBERS];
	private BigDecimal totalAllMembers;
	private BigDecimal totalAuctionBox;
	private BigDecimal totalForPricelist;
	
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
		
		// set the initial value of selectedCycle
		if (selectedCycle == 0) {
			selectedCycle = cycles.get(0).getCycleNumber();
		}		
		
	}
	
	
	public List<AccountsMemberReceivedLine> getReceivedLines(int cycleNumber) {
		
		List<AccountsMemberReceivedLine> receivedlines = new ArrayList<AccountsMemberReceivedLine>();
		
		try {
			receivedlines = OrderLineDAO.getInstance().getReceivedLines(cycleNumber);
		} catch (Exception exc) {
			logger.severe("cooplog: Member Received Lines for cycle " + cycleNumber + " could not be retrieved.");
		}
		
		return receivedlines;
		
	}
	
	// load the shareoutslips
	public void loadShareoutSlips() {
		
		// What cycle are we in?
		Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
		
		// Populate the set of users
		try {
			users = UserDAO.getInstance().getUsers();
			logger.info("cooplog loadShareoutSlips: There were " + users.size() + " users returned");

		} catch(Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog loadShareoutSlips: Could not retrieve list of Users", exc);
			
			// Can't do anything else so
			return;
		}
				
		// get the shareout slips - populated for the set of users
		shareoutSlips = ShareoutSlipLoader.loadShareoutSlips(currentCycle.getCycleNumber(), users);
		
		// ACCOUNTS - calculate the accounts summary field for each member
		// iterate through the users (indexed by u)
		BigDecimal totalForAllMembers = new BigDecimal(0.0);
		
		for (int u = 0; u < ShareoutSlip.MAX_MEMBERS; u++) {
			
			BigDecimal totalForAMember = new BigDecimal(0.0);
			
			// total the quantity of product x price of product
			
			// iterate through the shareout slips (indexed by s)
			for (int s = 0; s < shareoutSlips.size(); s++) {
				ShareoutSlip slip = shareoutSlips.get(s);
				
				// Calculate Total for a Member for a Product
				if (slip.getInvoicedUnitTradePrice() != null && slip.getReceivedMember()[u] != null) {
					BigDecimal slipAmount = slip.getInvoicedUnitTradePrice().multiply(slip.getReceivedMember()[u]);
					totalForAMember = totalForAMember.add(slipAmount);
				}
				
			}
			
			totalForMember[u] = totalForAMember;
			totalForAllMembers = totalForAllMembers.add(totalForAMember);
		}
		// End of calculate accounts summary fields for each member
		
		
		// Calculate total for auction box
		BigDecimal totalAuction = new BigDecimal(0.0);
		for (int s = 0; s < shareoutSlips.size(); s++) {
			ShareoutSlip slip = shareoutSlips.get(s);
			
			// Calculate Total for Auction box for a Product
			if (slip.getInvoicedUnitTradePrice() != null && slip.getReceivedAuctionbox() != null) {
				BigDecimal AuctionProductAmount = slip.getInvoicedUnitTradePrice().multiply(slip.getReceivedAuctionbox());
				totalAuction = totalAuction.add(AuctionProductAmount);
				logger.info("------------ Added " + totalAuction + " to Auction Amount (Price=" + slip.getInvoicedUnitTradePrice() + ", Quant=" + slip.getReceivedAuctionbox() + ")");
			}
		}
		// End of calculate total for auction box
		
		// Calculate the total for pricelist
		// (shareoutSlip.receivedTotal + shareoutSlip.receivedAuctionbox) * shareoutSlip.invoicedUnitTradePrice) + shareoutSlip.refundAmount}"
		BigDecimal totalPricelist = new BigDecimal(0.0);
		for (int s = 0; s < shareoutSlips.size(); s++) {
			// establish defaults
			BigDecimal price = new BigDecimal(0.0);
			BigDecimal received = new BigDecimal(0.0);
			BigDecimal auction = new BigDecimal(0.0);
			BigDecimal refund = new BigDecimal(0.0);
			
			ShareoutSlip slip = shareoutSlips.get(s);
			
			// get the values
			// price
			if (slip.getInvoicedUnitTradePrice() != null) {
				price = slip.getInvoicedUnitTradePrice();
			}
			
			// received
			if (slip.getReceivedTotal() != null) {
				received = slip.getReceivedTotal();
			}
			
			// auction
			if (slip.getReceivedAuctionbox() != null) {
				auction = slip.getReceivedAuctionbox();
			}
			
			// refund
			if (slip.getRefundAmount() != null) {
				refund = slip.getRefundAmount();
			}
			
			BigDecimal prods = received.add(auction);
			BigDecimal prods_amount = prods.multiply(price);
			BigDecimal total = prods_amount.add(refund);
			
			totalPricelist = totalPricelist.add(total);			
		}
		// End of calculate total for pricelist
		
		// Make the assignments to properties
		this.totalAllMembers = totalForAllMembers;
		this.totalAuctionBox = totalAuction;
		this.totalForPricelist = totalPricelist;
		
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
	
	public List<User> getUsers() {
		return users;
	}
	
	public List<ShareoutSlip> getShareoutSlips() {
		return shareoutSlips;
	}

	public BigDecimal[] getTotalForMember() {
		return totalForMember;
	}

	public BigDecimal getTotalAllMembers() {
		return totalAllMembers;
	}

	public BigDecimal getTotalAuctionBox() {
		return totalAuctionBox;
	}

	public BigDecimal getTotalForPricelist() {
		return totalForPricelist;
	}
	
}
