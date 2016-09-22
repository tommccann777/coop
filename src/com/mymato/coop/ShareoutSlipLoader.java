package com.mymato.coop;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShareoutSlipLoader {

	public ShareoutSlipLoader() {
		// TODO Auto-generated constructor stub
	}
	
	// Get shareoutslips for a cycle for a set of users
	public static List<ShareoutSlip> loadShareoutSlips(int aCycleNumber, List<User> users) {
		List<ShareoutSlip> shareoutSlips = new ArrayList<>();
		List<VShareoutOrderLine> vshareoutorderlines;
		List<Stock> stocks = new ArrayList<>();
		
		Logger logger = Logger.getLogger("ShareoutSlipLoader");
		
		logger.info("cooplog: Loading Shareout Slips");
		shareoutSlips.clear();
		stocks.clear();
		
		// first, get the array of VShareoutOrderlines
		try {
			vshareoutorderlines = OrderLineDAO.getInstance().getVShareoutOrderLines(aCycleNumber);
			logger.info("cooplog loadShareoutSlips: There were " + vshareoutorderlines.size() + " shareout slips returned");
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog loadShareoutSlips: Could not retrieve list of Shareout Order Lines", exc);
			
			// Can't do anything else so
			return null;
		}
		
		// second, get the list of stock items
		try {
			stocks = StockDAO.getInstance().getStocks();
			logger.info("cooplog loadShareoutSlips: There were " + stocks.size() + " stock records returned");
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog loadShareoutSlips: Could not retrieve list of Stock records", exc);
			
			// Can't do anything else so
			return null;
		}
		
		
		// Track the event of a new product and the record being written to
		String currentSupplierProduct = "";
		ShareoutSlip currentShareoutSlip = null;
		
		// third, construct the ArrayList of ShareoutSlips
		for (int i = 0; i < vshareoutorderlines.size(); i++) {
			// get the vShareoutOrderLine
			VShareoutOrderLine vsool = vshareoutorderlines.get(i);
			
			// get the supplier and product code
			String thisSupplierProduct = vsool.getSupplierName() + "-" + vsool.getNominatedProductId(); //.getSupplierProductCode();
			
			// detect a product change boundary - create a new ShareoutSlip and add it to the list
			if (!currentSupplierProduct.equals(thisSupplierProduct)) {
				// create a new Shareout slip
				currentShareoutSlip = new ShareoutSlip();
				
				// populate the shareoutSlip object
				currentShareoutSlip.setNominatedProductId(vsool.getNominatedProductId());
				//logger.info(">>>>> ProductId=" + currentShareoutSlip.getNominatedProductId());
				
				currentShareoutSlip.setSupplierName(vsool.getSupplierName());
				//logger.info(">>>>> SupplierName=" + currentShareoutSlip.getSupplierName());
				
				currentShareoutSlip.setSupplierProductCode(vsool.getSupplierProductCode());
				//logger.info(">>>>> SupplierProductCode=" + currentShareoutSlip.getSupplierProductCode());
				
				currentShareoutSlip.setProductDescription(vsool.getProductDescription());
				//logger.info(">>>>> ProductDescription=" + currentShareoutSlip.getProductDescription());
				
				currentShareoutSlip.setInvoicedUnitTradePrice(vsool.getInvoicedUnitTradePrice());
				currentShareoutSlip.setStockQuantity(vsool.getStockQuantity());				
				currentShareoutSlip.setRefundAmount(vsool.getRefundAmount());
				
				// add to the list of shareoutSlips
				shareoutSlips.add(currentShareoutSlip);
				
				// update the product tracking variable
				currentSupplierProduct = thisSupplierProduct;
			}
			
			
			// Now populate the allocations
			// For this vsool, work out the user index (from 1 to 20)
			int userIndex = UserIndex(users, vsool.getMemberId());
			logger.info("loadShareoutSlips: userIndex = " + userIndex + " for Member Id " + vsool.getMemberId());
			
			
			// only add this user's allocation and received values if they are within the max permitted number of members - to prevent array overflow
			if (userIndex != -1 && userIndex < ShareoutSlip.MAX_MEMBERS) {
				currentShareoutSlip.setOrderedMember(vsool.getAllocated(), userIndex);
				currentShareoutSlip.setReceivedMember(vsool.getReceived(), userIndex);
			}
			
			// refresh the orderedTotal
			currentShareoutSlip.refreshOrderedTotal();

			// refresh the receivedTotal
			currentShareoutSlip.refreshReceivedTotal();
			
		}
		
		// Add in the stock amounts to the shareout slips
		for (int i = 0; i < shareoutSlips.size(); i++) {
			ShareoutSlip slip = shareoutSlips.get(i);
			
			// scan the stock records to see if we have a matching nominated product id
			for (int j = 0; j < stocks.size(); j++) {
				Stock stock = stocks.get(j);
				
				if (stocks.get(j).getNominatedProductId() == slip.getNominatedProductId()) {
					slip.setReceivedAuctionbox(stock.getUnitQuantity());
				}					
			}
		}
		
		// return the list
		return shareoutSlips;

	}
	
	// get the index of the memberId in the users list
	private static int UserIndex(List<User> users, int memberId) {
		int userIndex = -1;
		
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getId() == memberId) {
				userIndex = i;
				break;
			}				
		}
		
		return userIndex;
	}

}
