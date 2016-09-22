package com.mymato.coop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.primefaces.component.api.UIData;
import org.primefaces.component.treetable.TreeTable;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

// Was originally ViewScoped
@ManagedBean(name="orderFormBean")
@SessionScoped
public class OrderFormBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int MAX_PRODUCTS = 8;
	
	private List<PricelistProduct> pricelistProducts;
	private List<OrderLine> myPersistentOrderLines;
	private TreeNode allocations;
	
	private List<ConsolidatedProductLine> myConsolidatedProductLines;
	private List<ShareoutSlip> shareoutSlips;
	List<User> users;
	
	private TreeNode treeSupplierOrderLines;
	
	private List<PricelistProduct> filteredProductsD;
	private List<PricelistProduct> filteredProductsF;
	private List<PricelistProduct> filteredProductsL;
	private List<PricelistProduct> filteredProductsM;
	
	private ProductDAO productDAO;
	private OrderLineDAO orderLineDAO;
	private SupplierOrderLineDAO supplierOrderLineDAO;
	private String message;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	
	public OrderFormBean() throws Exception {
		pricelistProducts = new ArrayList<>();
		// myOrderLines = new ArrayList<>();
		myPersistentOrderLines = new ArrayList<>();
		myConsolidatedProductLines = new ArrayList<>();
		shareoutSlips = new ArrayList<>();
		
		productDAO = ProductDAO.getInstance();
		orderLineDAO = OrderLineDAO.getInstance();
		supplierOrderLineDAO = SupplierOrderLineDAO.getInstance();
	}
	
	public void addToOrder(PricelistProduct pp) {
		logger.info("In addToOrder");
		
		NominatedProduct np;										// nominated product record
		boolean orderLineInList = false;
		FacesContext context = FacesContext.getCurrentInstance();	// used for messages
		
		
		
		// start by getting the current cycle number and the user id
		Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
		User currentUser = (User) ApplicationMapHelper.getValueFromSessionMap(ApplicationMapHelper.USER_KEY);
		
		// check to see if the maximum number of product nominations will be exceeded
		if (myPersistentOrderLines.size() >= MAX_PRODUCTS) {
			// Give the user an error message
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Maximum " + MAX_PRODUCTS + " products already in cart", ""));
			return;
		}
		
		// First, we make a copy of the pricelist product into the nominatedproduct table
		// We return np, the reference to the nominated product record
		try {
			np = productDAO.copyProductToNominatedProduct(pp, currentCycle.getCycleNumber());
		} catch (Exception exc) {
			logger.severe("cooplog: Nominated Product not copied from Pricelist Product.");
			return;
		}		
		
		// check to see if the nominated product already in order list
		for (int i = 0; i < myPersistentOrderLines.size(); i++) {
			OrderLine myOrderLine = (OrderLine) myPersistentOrderLines.get(i);
			
			if (myOrderLine.getNominatedProductId() == np.getId()) {
				orderLineInList = true;
				break;
			}
		}
		
		if (!orderLineInList) {
			OrderLine orderline = new OrderLine(currentCycle.getCycleNumber(), currentUser.getId(), np);
			
			try {
				OrderLineDAO.getInstance().addOrderLine(orderline);
			} catch (Exception exc) {
				logger.severe("cooplog: Order Line not saved");
				return;
			}
			
			// add message to the growl
			String msg = orderline.getSnapshotProductDescription();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to cart", msg));
			//logger.info("cooplog: " + msg);
		} else {
			// let the user know it's already in the list
			// add message to the growl
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Product already in cart", ""));
		}
	}
	
	public void removeFromOrder(OrderLine o) {
		// remove OrderLine from the database
		
		
		try {
			logger.info("cooplog: Attempting to delete orderline " + o.getId());
			
			orderLineDAO.deleteOrderLine(o.getId());
			
			// if it worked then remove from the order line list
			myPersistentOrderLines.remove(o);
			
			// add message to the growl
			String msg = o.getSnapshotProductDescription();
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Removed from cart", msg));
			logger.info(msg);
		} catch (Exception exc) {
			logger.severe("cooplog: Order Line not deleted");
			return;
		}
		
	}
	
	
	public List<PricelistProduct> getPricelistProducts() {
		//logger.info("Getting products");
		return pricelistProducts;
	}

	public void resetSelected() {
		//logger.info("resetSelected() called");
	}
	
	// get the pricelist products for a pricelist
	public List<PricelistProduct> getPricelistProducts(String aPricelist) {
		List<PricelistProduct> pProducts = new ArrayList<>();
		
		logger.info("Searching " + pricelistProducts.size() + " products in pricelistProducts for pricelist = " + aPricelist);
		
		// Extract the products for the specified pricelist
		for (int i = 0; i < pricelistProducts.size(); i++) {
			PricelistProduct pp = pricelistProducts.get(i);
			
			if (pp.getPricelist().equalsIgnoreCase(aPricelist)) {
				pProducts.add(pp);
			}
		}
		
		logger.info("Returning " + pProducts.size() + " pricelist products for " + aPricelist);
		return pProducts;
	}
	
	public void loadPricelistProducts(ComponentSystemEvent event) {

		//logger.info("cooplog: Attempting to load products");
		
		// if (!FacesContext.getCurrentInstance().isPostback()) {
			
			//logger.info("cooplog: Loading products");
			
			pricelistProducts.clear();

			try {
				
				// get all products from database
				pricelistProducts = productDAO.getPricelistProducts();
				//logger.info("cooplog: Products loaded = " + pricelistProducts.size());
				
			} catch (Exception exc) {
				// send this to server logs
				logger.log(Level.SEVERE, "cooplog: Error loading products", exc);
				
				// add error message for JSF page
				addErrorMessage(exc);
			}
		
	}
	
	
	public void loadVSupplierOrderLines() {
		TreeNode root = null;
		List<VSupplierOrderLine> vSupplierOrderLines;
		
		// What cycle do we want records for?
		Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
		
		// Start by getting the List<vSupplierOrderLine>
		try {
			vSupplierOrderLines = supplierOrderLineDAO.getvSupplierOrderLines(currentCycle.getCycleNumber());
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Could not retrieve list of Supplier Order Lines", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			// Can't do anything else so
			return;
		}
		
		// Build the TreeNode from root - populate with the supplierOrderLines list
		/* How to build a TreeNode:
		 * 
		 * (1) Traverse the record set. For each node level (expandable node), work out when the value changes. Create a 'dummy' TreeNode value for that level 
		 * and record the current parent node.
		 * 
		 * (2) Create a TreeNode record with 'real' values and assign it the appropriate 'current' parent. 
		 * 
		 */
		
		// Create the root node
		root = new DefaultTreeNode(new TnoSupplierOrderLine("", "", null), null);
		
		// Prepare the comparison values used to determine parent nodes
		String currentSupplierName = "";
		//String currentProductCategory = "";
		TreeNode currentSupplierNode = null;
		//TreeNode currentProductNode = null;
		
		for (int i = 0; i < vSupplierOrderLines.size(); i++) {
			// get the vsol object
			VSupplierOrderLine vsol = vSupplierOrderLines.get(i);
			String thisSupplierName = vsol.getSuppliername();
			//String thisProductCategory = vsol.getProductcategory();
			
			// do we get a supplier change?
			if (!currentSupplierName.equals(thisSupplierName)) {
				// Create a supplier node - these are placed under the root
				TreeNode tn = new DefaultTreeNode(new TnoSupplierOrderLine("Category", thisSupplierName, null), root);
				tn.setExpanded(true);
				currentSupplierNode = tn;
				currentSupplierName = thisSupplierName;
				
				// re-start the Product comparisons
				//currentProductCategory = "";
				//currentProductNode = null;
			}
			
			// The following retired code implements a second level category
			// do we have a product category change?
			//if (!currentProductCategory.equals(thisProductCategory)) {
				// Create a product node
				//TreeNode tn = new DefaultTreeNode(new TnoSupplierOrderLine("Category", thisProductCategory, null), currentSupplierNode);
				//tn.setExpanded(true);
				//currentProductNode = tn;
				//currentProductCategory = thisProductCategory;
			//}
			
			//TreeNode tn = new DefaultTreeNode(new TnoSupplierOrderLine("Data", "", vsol), currentProductNode);
			TreeNode tn = new DefaultTreeNode(new TnoSupplierOrderLine("Data", "", vsol), currentSupplierNode);
			tn.setExpanded(true);
		}
		
		// set the result
		treeSupplierOrderLines = root;
	}
	
	public TreeNode getTnSupplierOrderLines() {
		return treeSupplierOrderLines;
	}
	
	// V A L I D A T O R S
	
	// this applies validation to a Stock product to check if total buyer units exceed the stock available
	public void validateReconciliationReceived(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		BigDecimal stock;
		BigDecimal total = new BigDecimal(0.0);
		
		// only applies to Stock products so return if not a stock product
		
		logger.info("Running validator validateReconciliationReceived");
		
		BigDecimal newValue = (BigDecimal) value;
		
		UIData data = (UIData) component.findComponent("shareoutSlipTable");
		ShareoutSlip s = (ShareoutSlip) data.getRowData();
		String uicFieldname = component.getId();
		int fieldNum = Integer.parseInt(uicFieldname.substring("rcvd".length()));
		
		// validation only applies to stock items
		if (s.getSupplierName().equals("Stock")) {
			// Get the (proposed) total of units for this product
			total = s.getReceivedTotal();
			
			// Subtract the current field value
			if (s.getReceivedMember()[fieldNum] != null) {
				total = total.subtract(s.getReceivedMember()[fieldNum]);
			}		
			
			// add in the newValue
			total = total.add(newValue);
			
			// amount of available stock
			stock = s.getStockQuantity();
			
			logger.info("validateReconciliationReceived: total = " + total + ", stock = " + stock);
			
			if (total.compareTo(stock) > 0) {  // total > stock
				String msg = "The total received for " + s.getProductDescription() + " is " + total + ", which is greater then the stock available (" + stock + ")";
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
			}
			
		}

	}
	
	
	// S H A R E O U T   S L I P S
	// For the shareout slip view, 2 methods: loadShareoutSlips and getShareoutSlips
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
	}
	
	
	public List<ShareoutSlip> getShareoutSlips() {
		logger.info("cooplog: Getting Shareout Slips");
		
		return shareoutSlips;
	}
	
	
	// C H A N G E   H A N D L E R S
	
	// Order from Suppliers view - Ordered field change
	public void handleOrderedChange(ValueChangeEvent vcEvent) {
		SupplierOrderLine sol = null;
		
		logger.info("handleOrderedChange called");
		
		TreeTable data = (TreeTable) vcEvent.getComponent().findComponent("supplierOrderLineTable");
		DefaultTreeNode row = (DefaultTreeNode) data.getRowNode();
		TnoSupplierOrderLine tnosol = (TnoSupplierOrderLine) row.getData();
		//String clientId = data.getClientId();
		//logger.info("cooplog: data ClientId=" + clientId);
		logger.info("supplier order id = " + tnosol.getSupplierOrderLineID());
		
		boolean newValue = (boolean) vcEvent.getNewValue();
		logger.info("New value = " + newValue);
		
		// update the objects
		tnosol.setOrdered(newValue);
		
		// get the supplier order line
		try {
			sol = SupplierOrderLineDAO.getInstance().getSupplierOrderLine(tnosol.getSupplierOrderLineID());
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error retrieving Supplier Order Line", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
		
		// change the sol object
		sol.setOrdered(newValue);
		
		// update the sol
		try {
			SupplierOrderLineDAO.getInstance().updateSupplierOrderLine(sol);
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error updating Supplier Order Line", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}		
		
	}
	
	
	// ShareoutReconciliation page, invoicedUnitTradePrice field change
	public void handleInvoicePriceChange(ValueChangeEvent vcEvent) {
		logger.info("In handleInvoicePriceChange");
			
		UIData data = (UIData) vcEvent.getComponent().findComponent("shareoutSlipTable");
		ShareoutSlip s = (ShareoutSlip) data.getRowData();
		BigDecimal newValue = (BigDecimal) vcEvent.getNewValue();
		
		try {
			
			productDAO.updateInvoicePriceNominatedProduct(s.getNominatedProductId(), newValue);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error updating Invoiced Price", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
		
		
	}
		
	
	// ShareoutReconciliation page, received value changed
	public void handleReceivedChange(ValueChangeEvent vcEvent) {
		
		logger.info("cooplog handleReceivedChange: Handling Received change");
		
		// which cycle are we in?
		Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
		
		UIData data = (UIData) vcEvent.getComponent().findComponent("shareoutSlipTable");
		ShareoutSlip s = (ShareoutSlip) data.getRowData();
		String clientId = data.getClientId();
		logger.info("cooplog handleReceivedChange: data ClientId=" + clientId);
		
		FacesContext context = FacesContext.getCurrentInstance();
		UIComponent uic = UIComponent.getCurrentComponent(context);	// User Interface Component

		String uicClientId = uic.getClientId(context);
		logger.info("cooplog handleReceivedChange: uic ClientId=" + uicClientId);
		
		String uicFieldname = uic.getId();
		int fieldNum = Integer.parseInt(uicFieldname.substring("rcvd".length()));

		// note: if fieldNum = 999 then this is for the Auction box //
		
		// the variables we need to perform the update
		int cycleNumber = currentCycle.getCycleNumber();
		int nominatedProductId = s.getNominatedProductId();
		
		BigDecimal newValue = (BigDecimal) vcEvent.getNewValue();
		BigDecimal oldValue = (BigDecimal) vcEvent.getOldValue();
		
		logger.info("cooplog handleReceivedChange: New value is " + newValue);
		// 
		if (fieldNum == 999) {
			// Auction box - add to the stock table
			updateStock(nominatedProductId, cycleNumber, newValue);
			
		} else {
			// user has received, so update the orderline table
			
			int memberId = users.get(fieldNum).getId();
			
			try {
				// get the orderline - if there isn't one then create it
				OrderLine o = orderLineDAO.getOrderLine(cycleNumber, memberId, nominatedProductId);
				
				if (o != null) {
					// update the orderLine with the new value
					o.setReceived(newValue);
					
					logger.info("cooplog handleReceivedChange: about to update order line");
					// update the database
					orderLineDAO.updateOrderLine(o);
					
					// if all worked ok, update the shareoutSlip object
					s.setReceivedMember(newValue, fieldNum);
				} else {
					// create a new orderLine - this is an orderLine for a product the user didn't order
					
					// get the product first
					NominatedProduct np = productDAO.getNominatedProduct(nominatedProductId);
					
					// Create the new OrderLine object and update the value			
					OrderLine newOrderLine = new OrderLine(cycleNumber, memberId, np);
					newOrderLine.setReceived(newValue);
					
					// add it to the database
					orderLineDAO.addOrderLine(newOrderLine);
					
					// if all worked ok, update the shareoutSlip object
					s.setReceivedMember(newValue, fieldNum);
				}
			} catch (Exception exc) {
				// send this to server logs
				logger.log(Level.SEVERE, "cooplog: Error updating or creating Order Line", exc);
				
				// add error message for JSF page
				addErrorMessage(exc);
			}
		}	// end of update orderline for user received
		
		// refresh the receivedTotal
		s.refreshReceivedTotal();
		
		logger.info("The new received auction box is " + s.getReceivedTotal());
	}
	
	// max qty has been changed on form, update the object and database record
	public void handleMaxQtyChange(ValueChangeEvent vcEvent) {
		BigDecimal originalMaxQty;
		
		logger.info("cooplog: Handling Max Qty change");
		
		UIData data = (UIData) vcEvent.getComponent().findComponent("myPersistentOrder");
	    // int rowIndex = data.getRowIndex();
	    OrderLine o = (OrderLine) data.getRowData();
	    
	    // get the original value, in case we need to restore it if the database update does not work
	    originalMaxQty = o.getMaxQty();
	    
	    o.setMaxQty((BigDecimal) vcEvent.getNewValue());
		// logger.info(">>>>>>>>>>> old value= " + vcEvent.getOldValue());
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> new maxQty value= " + vcEvent.getNewValue());
		// logger.info(">>>>>>>>>>> " + vcEvent.getSource().toString());
		// logger.info(">>>>>>>>>>> Row=" + rowIndex);
		// logger.info(">>>>>>>>>>> Product=" + o.getSnapshotDescription());
		
		// update the database with the object
		try {
			orderLineDAO.updateOrderLine(o);
		} catch (Exception exc) {
			
			// database update did not succeed, restore the original value in the OrderLine object
			if (o != null) {
				o.setMaxQty(originalMaxQty);
			}
			
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error updating maximum quantity on Order Line", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
	
	// min qty has been changed on form, update the object and database record
	public void handleMinQtyChange(ValueChangeEvent vcEvent) {
		BigDecimal originalMinQty;
		
		logger.info("cooplog: Handling Min Qty change");
		
		UIData data = (UIData) vcEvent.getComponent().findComponent("myPersistentOrder");
	    // int rowIndex = data.getRowIndex();
	    OrderLine o = (OrderLine) data.getRowData();
	    
	    // get the original value, in case we need to restore it if the database update does not work
	    originalMinQty = o.getMinQty();
	    
	    o.setMinQty((BigDecimal) vcEvent.getNewValue());
		// logger.info(">>>>>>>>>>> old value= " + vcEvent.getOldValue()); 
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> new minQty value= " + vcEvent.getNewValue());
		// logger.info(">>>>>>>>>>> " + vcEvent.getSource().toString());
		// logger.info(">>>>>>>>>>> Row=" + rowIndex);
		// logger.info(">>>>>>>>>>> Product=" + o.getSnapshotDescription());
		
		// update the database with the object
		try {
			orderLineDAO.updateOrderLine(o);
		} catch (Exception exc) {

			// database update did not succeed, restore the original value in the OrderLine object
			if (o != null) {
				o.setMinQty(originalMinQty);
			}
						
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error updating minimum quantity on Order Line", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
	
	
	// Supplier Qty value has been changed on allocation form, update the object and database record
	public void handleSupplierQtyChange(ValueChangeEvent vcEvent) {
		BigDecimal originalSupplierQty;
		int productId;
		int supplierOrderLineId;
		
		logger.info("cooplog: Handling supplier qty change");
		
		TreeTable data = (TreeTable) vcEvent.getComponent().findComponent("allocationTreeTable");
		TreeNode tn = (TreeNode) data.getRowNode();
		TnoOrderAllocation oan = (TnoOrderAllocation) tn.getData();
		originalSupplierQty = oan.getSupplierqty();
		productId = oan.getProductid();
		supplierOrderLineId = oan.getSupplierorderlineid();
		
		// get the current cycle number
		Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
		User currentUser = (User) ApplicationMapHelper.getValueFromSessionMap(ApplicationMapHelper.USER_KEY);
		int cycleNumber = currentCycle.getCycleNumber();
		
		// Check to see if we are updating a real SupplierOrderLine record or inserting one		
		
		if (supplierOrderLineId == 0) {
			// do an insert
			SupplierOrderLine sol = new SupplierOrderLine(0, cycleNumber, oan.getSupplier(), productId, (BigDecimal) vcEvent.getNewValue(),
														  currentUser.getId(), false, null, null);
			try {
				// update the database
				int newRecordId = supplierOrderLineDAO.addSupplierOrderLine(sol);
				logger.info("New supplierOrderLineId=" + newRecordId);
				
				// update the object
				oan.setSupplierqty((BigDecimal) vcEvent.getNewValue());
				oan.setSupplierorderlineid(newRecordId);
				
			} catch (Exception exc) {
				// restore the oan object to the original quantity
				oan.setSupplierqty(originalSupplierQty);
				
				// send this to server logs
				logger.log(Level.SEVERE, "cooplog: Error inserting Supplier Order Line", exc);
				
				// add error message for JSF page
				addErrorMessage(exc);
			}
		} else {
			// do an update
			try {
				SupplierOrderLine sol = supplierOrderLineDAO.getSupplierOrderLine(supplierOrderLineId);
				
				// set the new value
				sol.setQty((BigDecimal) vcEvent.getNewValue());
				
				// update the database
				supplierOrderLineDAO.updateSupplierOrderLine(sol);
			} catch (Exception exc) {
				// restore the oan object to the original quantity
				oan.setSupplierqty(originalSupplierQty);
				
				// send this to server logs
				logger.log(Level.SEVERE, "cooplog: Error inserting Supplier Order Line", exc);
				
				// add error message for JSF page
				addErrorMessage(exc);
			}
		}
	}
	
	// allocation value has been changed on allocation form, update the object and database record
	public void handleAllocationChange(ValueChangeEvent vcEvent) {
		BigDecimal originalAllocation;
		OrderLine o = null;
		
		logger.info("Handling allocation change");
		// get the current cycle number
		Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
		
		TreeTable data = (TreeTable) vcEvent.getComponent().findComponent("allocationTreeTable");
		
		TreeNode tn = (TreeNode) data.getRowNode();
		TnoOrderAllocation oan = (TnoOrderAllocation) tn.getData();
		int orderLineId = oan.getOrderLineid();
	    
		// get the original value, in case we need to restore it if the database update does not work
	    originalAllocation = oan.getAllocation();
	    
	    // update the object
	    oan.setAllocation((BigDecimal) vcEvent.getNewValue());
		
		// update the database with the object
		try {
			// Get the object from the database
			o = orderLineDAO.getOrderLine(orderLineId);
			
			BigDecimal newValue = (BigDecimal) vcEvent.getNewValue();
			
			// Set the new value
			o.setAllocation(newValue);
			// Set the (default) received value
			o.setReceived(newValue);
			
			// Update the database
			orderLineDAO.updateOrderLine(o);
		} catch (Exception exc) {

			// database update did not succeed, restore the original value in the OrderLine object
			if (o != null) {
				o.setAllocation(originalAllocation);
				o.setReceived(originalAllocation);
			}
			
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error updating allocation on Order Line", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
		
		// perform a check to see if the total allocated is greater than the stock available
		// set a stockWarning flag if this is true
		// 1. Get the amount of stock for a stock product
		if (o.getSnapshotSupplier().equals("Stock")) {
			try {
				int np_id = o.getNominatedProductId();
				boolean stockCheck = orderLineDAO.getAllocationStockCheck(currentCycle.getCycleNumber(), np_id);
				// We need a way to signal this to the user
				logger.info("-------------------------- setting StockWarning to " + stockCheck + " for product " + np_id);
			} catch (Exception exc) {
				// send this to server logs. The stock check is not critical so can fail silently 
				logger.log(Level.SEVERE, "cooplog: Error checking total allocation.", exc);
			}
		}
		
		
		
		
	}
	
	
	// min qty has been changed on Consolidated Orders form, update the object and database record
	public void handleMinQtyChangeCons(ValueChangeEvent vcEvent) {
		
		BigDecimal originalMinQty;
		NominatedProduct updatedProduct;	// the nominated product the user updated (possibly null if not his orderLine)
		OrderLine myMatchingOrderLine = null;	// the OrderLine belonging to the user who updated
		OrderLine newOrderLine;	// a new OrderLine if one needs to be created
		
		//UIData data = (UIData) vcEvent.getComponent().findComponent("myConsolidatedProductLines");
		UIData data = (UIData) vcEvent.getComponent().findComponent("myConsolidatedProductLines");
	    int rowIndex = data.getRowIndex();
	    //logger.info("cooplog: - - - cons min handler on row " + rowIndex);
	    ConsolidatedProductLine cpl2 = myConsolidatedProductLines.get(rowIndex);
	    //logger.info("cooplog: - - - cons min handler: cpl2 product " + cpl2.getProductId());
	    
	    ConsolidatedProductLine cpl = (ConsolidatedProductLine) data.getRowData();
	    
	    int nominatedProductId = cpl.getNominatedProductId();
	    //logger.info("cooplog: - - - cons min handler on product " + productId);
	    
	    // save the original min qty in case we have to restore it
	    originalMinQty = cpl.getMinQty();
	    
	    // set the view object first
	    cpl.setMinQty((BigDecimal) vcEvent.getNewValue());
	    logger.info("cooplog handleMinQtyChangeCons: ++++++++ min qty change to " + vcEvent.getNewValue());
		
	    // update the database with the new value
	    // first, work out if it is an insert or an update
		try {
			// get the list of 'my' orderLines and look to see if the product id is there
			List<OrderLine> myOrderLines = getMyPersistentOrderLines();
			
			for (int i = 0; i < myOrderLines.size(); i++) {
				OrderLine o = myOrderLines.get(i);
				if (o.getNominatedProductId() == nominatedProductId) {
					myMatchingOrderLine = o;
					break;
				}
			}
			
			// did we find a match? i.e. have I ordered that product?
			if (myMatchingOrderLine != null) {
				// update the object
				myMatchingOrderLine.setMinQty((BigDecimal) vcEvent.getNewValue());
				
				// update the database
				orderLineDAO.updateOrderLine(myMatchingOrderLine);
			} else {
				// do an insert
				
				// get the user and cycle number
				Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
				User currentUser = (User) ApplicationMapHelper.getValueFromSessionMap(ApplicationMapHelper.USER_KEY);
				
				// get the Product
				updatedProduct = productDAO.getNominatedProduct(nominatedProductId);
				// create the new OrderLine
				newOrderLine = new OrderLine(currentCycle.getCycleNumber(), currentUser.getId(), updatedProduct);
				newOrderLine.setMinQty((BigDecimal) vcEvent.getNewValue());
				// insert it into the database
				orderLineDAO.addOrderLine(newOrderLine);
			}
			
		} catch (Exception exc) {
			// restore the original object values
			if (cpl != null) {
				cpl.setMinQty(originalMinQty);
			}
			
			if (myMatchingOrderLine != null) {
				myMatchingOrderLine.setMinQty(originalMinQty);
			}				
			
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error updating Min Qty on Order Line", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
	
	// max qty has been changed on Consolidated Orders form, update the object and database record
	public void handleMaxQtyChangeCons(ValueChangeEvent vcEvent) {
		
		BigDecimal originalMaxQty;
		NominatedProduct updatedProduct;	// the nominated product the user updated (possibly null if not his orderLine)
		OrderLine myMatchingOrderLine = null;	// the OrderLine belonging to the user who updated
		OrderLine newOrderLine;	// a new OrderLine if one needs to be created
		
		UIData data = (UIData) vcEvent.getComponent().findComponent("myConsolidatedProductLines");
	    int rowIndex = data.getRowIndex();
	    //logger.info("cooplog: + + + cons max handler on row " + rowIndex);
	    ConsolidatedProductLine cpl2 = myConsolidatedProductLines.get(rowIndex);
	    //logger.info("cooplog: + + + cons max handler: cpl2 product " + cpl2.getProductId());
	    
	    ConsolidatedProductLine cpl = (ConsolidatedProductLine) data.getRowData();
	    
	    int nominatedProductId = cpl.getNominatedProductId();
	    //logger.info("cooplog: + + + cons max handler on product " + productId);
	    
	    // save the original max qty in case we have to restore it
	    originalMaxQty = cpl.getMaxQty();
	    
	    // set the view object first
	    cpl.setMaxQty((BigDecimal) vcEvent.getNewValue());
		
	    // update the database with the new value
	    // first, work out if it is an insert or an update
		try {
			// get the list of 'my' orderLines and look to see if the product id is there
			List<OrderLine> myOrderLines = getMyPersistentOrderLines();
			
			for (int i = 0; i < myOrderLines.size(); i++) {
				OrderLine o = myOrderLines.get(i);
				if (o.getNominatedProductId() == nominatedProductId) {
					myMatchingOrderLine = o;
					break;
				}
			}
			
			// did we find a match? i.e. have I ordered that product?
			if (myMatchingOrderLine != null) {
				// update the object
				myMatchingOrderLine.setMaxQty((BigDecimal) vcEvent.getNewValue());
				
				// update the database
				orderLineDAO.updateOrderLine(myMatchingOrderLine);
			} else {
				// do an insert
				
				// get the user and cycle number
				Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
				User currentUser = (User) ApplicationMapHelper.getValueFromSessionMap(ApplicationMapHelper.USER_KEY);
				
				// get the Product
				updatedProduct = productDAO.getNominatedProduct(nominatedProductId);
				// create the new OrderLine
				newOrderLine = new OrderLine(currentCycle.getCycleNumber(), currentUser.getId(), updatedProduct);
				newOrderLine.setMaxQty((BigDecimal) vcEvent.getNewValue());
				// insert it into the database
				orderLineDAO.addOrderLine(newOrderLine);
			}
			
		} catch (Exception exc) {
			// restore the original object values
			if (cpl != null) {
				cpl.setMinQty(originalMaxQty);
			}
			
			if (myMatchingOrderLine != null) {
				myMatchingOrderLine.setMinQty(originalMaxQty);
			}				
			
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error updating Max Qty on Order Line", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}

		
	// end of C H A N G E   H A N D L E R S
	
	
	// add or update a stock record for units added to the auction box
	private void updateStock(int nominatedProductId, int cycleNumber, BigDecimal unitQuantity) {
		Stock stock = null;
		
		logger.info(">>>>>>>>>>>>>>>>>>>> updateStock called");
		
		// first, see if a stock record already exists
		try {
			StockDAO stockDAO = StockDAO.getInstance();
			stock = stockDAO.getStock(nominatedProductId, cycleNumber);
			
			if (stock == null) {
				// create the record
				stock = new Stock(0, cycleNumber, nominatedProductId, unitQuantity);
				stockDAO.addStock(stock);				
			} else {
				// update the record
				stock.setUnitQuantity(unitQuantity);
				stockDAO.updateStock(stock);
			}
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error loading Stock record", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
		
	}
	
	public void excite() {
		logger.info("!!! Excited !!!");
	}
	
	// get orderLines for a user for a cycle
	public List<OrderLine> getMyPersistentOrderLines() {
		
		logger.info("In getMyPersistentOrderLines");
		
		// start by getting the current cycle number and the user id
		Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
		User currentUser = (User) ApplicationMapHelper.getValueFromSessionMap(ApplicationMapHelper.USER_KEY);
		int cycleNumber = currentCycle.getCycleNumber();
		int memberId = currentUser.getId();
			
		myPersistentOrderLines.clear();

		try {
			logger.info("About to call orderLineDAO.getMyOrderLines(" + cycleNumber + ", " + memberId + ")");
			// get all orderLines that don't include Stock
			myPersistentOrderLines = orderLineDAO.getMyOrderLines(cycleNumber, memberId, false);
			logger.info("Returned from orderLineDAO.getMyOrderLines(" + cycleNumber + ", " + memberId + ") with " + myPersistentOrderLines.size() + " order lines");
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error loading Order Lines", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
		
		logger.info("Exiting getMyPersistentOrderLines with " + myPersistentOrderLines.size() + " order lines");
		return myPersistentOrderLines;
		
	}
	
	public List<ConsolidatedProductLine> getMyConsolidatedProductLines() {
		logger.info("cooplog: myConsolidatedProductLines has " + myConsolidatedProductLines.size() + " elements");
		return myConsolidatedProductLines;
	}
	
	// populate (during view pre-load) the list of consolidated product lines
	public void loadMyConsolidatedProductLines() {
		

		logger.info("cooplog: Loading my allocations");
		
		// start by getting the current cycle number and the user id
		Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
		User currentUser = (User) ApplicationMapHelper.getValueFromSessionMap(ApplicationMapHelper.USER_KEY);
		int cycleNumber = currentCycle.getCycleNumber();
		int memberId = currentUser.getId();
			
		myConsolidatedProductLines.clear();

		try {
			
			// get all consolidated ProductLine view from database
			myConsolidatedProductLines = orderLineDAO.getMyConsolidatedProductLines(cycleNumber, memberId);
			logger.info("cooplog: After database access, myConsolidatedProductLines has " + myConsolidatedProductLines.size() + " elements");
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error loading Consolidated Product Lines", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}

	}
	
				
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<PricelistProduct> getFilteredProductsD() {
		return filteredProductsD;
	}

	public void setFilteredProductsD(List<PricelistProduct> filteredProducts) {
		this.filteredProductsD = filteredProducts;
	}
	
	public List<PricelistProduct> getFilteredProductsF() {
		return filteredProductsF;
	}

	public void setFilteredProductsF(List<PricelistProduct> filteredProducts) {
		this.filteredProductsF = filteredProducts;
	}
	
	public List<PricelistProduct> getFilteredProductsL() {
		return filteredProductsL;
	}

	public void setFilteredProductsL(List<PricelistProduct> filteredProducts) {
		this.filteredProductsL = filteredProducts;
	}
	
	public List<PricelistProduct> getFilteredProductsM() {
		return filteredProductsM;
	}

	public void setFilteredProductsM(List<PricelistProduct> filteredProducts) {
		this.filteredProductsM = filteredProducts;
	}
	

	public List<User> getUsers() {
		return users;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TreeNode getAllocations() {
		return allocations;
	}
	
	// Treenode
	public void loadAllocations() {
		
		Cycle currentCycle = (Cycle) ApplicationMapHelper.getValueFromApplicationMap(ApplicationMapHelper.CYCLE_KEY);
		
		try {
			allocations = orderLineDAO.getVAllocationRecords(currentCycle.getCycleNumber());
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "cooplog: Error loading allocation records", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
		
	}
	
	public void setAllocations(TreeNode allocations) {
		this.allocations = allocations;
	}

}
