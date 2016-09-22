package com.mymato.coop;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.opencsv.CSVReader;

 
@ManagedBean(name="pricelistBean")
@SessionScoped
public class PricelistBean {
     
    private UploadedFile file;
    private Logger logger = Logger.getLogger(getClass().getName());
    
    private List<PricelistProduct> uploadedPricelistProducts;
    private Set<String> pricelists;	// set of pricelist names
    private Set<String> suppliers;	// set of supplier names
    private ProductDAO productDAO;

	private List<PricelistProduct> filteredProducts;
 
    
    // no-argument constructor
    public PricelistBean() throws Exception {
    	uploadedPricelistProducts = new ArrayList<PricelistProduct>();
    	pricelists = new HashSet<String>();
    	suppliers = new HashSet<String>();
    	
    	productDAO = ProductDAO.getInstance();
    }
 
    // was: public void upload() {
    public void upload(FileUploadEvent event) {
    	
    	file = event.getFile();
    	
    	Reader reader = null;
    	CSVReader csvreader = null;
    	String[] line;
    	int num_products_imported = 0;
    	int num_products_invalid = 0;
    	
    	logger.info("Upload running");
    	
        if(file != null) {
        	
        	logger.info("File is " + file.getFileName());
        	
        	if (filteredProducts == null) {
        		logger.info("filteredProducts = null");
        	} else {
        		logger.info("filteredProducts = " + filteredProducts.size());
        	}
        	
        	

            // clear the list of uploaded products and supplier names
            uploadedPricelistProducts.clear();
            pricelists.clear();
            suppliers.clear();
            
            // reset the datatable - solves bug when a previous filter causes the table not to update after an upload
            DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("mainform:productsTable");
            dataTable.resetValue();            
            
            // parse the file
            try {
            	// start the counters
            	num_products_imported = 0;
            	num_products_invalid = 0;
            	
            	reader = new InputStreamReader(file.getInputstream());
            	
                csvreader = new CSVReader(reader);
                
                while ((line = csvreader.readNext()) != null) {
                	// check for an empty line
                	logger.info("About to call Arrays.toString(line)");
                	String allValues = Arrays.toString(line);
                	logger.info("After call to Arrays.toString(line)");

                	if (allValues.equals("[, , , , , , , ]")) {
                		// skip this line
                		continue;
                	}
                	
                	logger.info("About to call createPricelistProductFromLine(line)");
                	PricelistProduct product = createPricelistProductFromLine(line);
                	logger.info("After call to createPricelistProductFromLine(line)");
                	
                	// If a product is not created then a data error has occurred
                	if (product != null) {
                		// dump the product to console
                		dumpProductToConsole(product);
                		
                		uploadedPricelistProducts.add(product);
                		pricelists.add(product.getPricelist());
                		suppliers.add(product.getSupplier());
                		
                		// maintain the stats
                		num_products_imported++;
                		if (product.isValid() == false) num_products_invalid++;
                	}                	
                
                }
               
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", num_products_imported + " products were uploaded from " + file.getFileName() + ".");
                FacesContext.getCurrentInstance().addMessage(null, message);


            } catch (Exception exc) {
        		
            	logger.severe("Could not interpret file " + file.getFileName() + ". " + exc.getMessage());
            	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not interpret file " + file.getFileName() + ".");
                FacesContext.getCurrentInstance().addMessage(null, message);
                
            } finally {
                IOUtils.closeQuietly(csvreader);
            }
            
            
        } // end of if (file != null)
        
        // display message if some of the products uploaded were invalid
        if (num_products_invalid > 0) {
        	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", num_products_invalid + " product(s) have an invalid price.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
        
    }
    
    public PricelistProduct createPricelistProductFromLine(String[] line) throws ParseException {
    	
    	logger.info("!checkpoint 1");
    	
    	// check we have the right number of values first
    	if (line.length < 8) {
    		for (int i = 0; i < line.length; i++) {
    			logger.info("line[" + i + "]=" + line[i]);
    		}
    		
    		throw new ParseException("CSV line has insufficient fields", 0);
    	}
    		
    	logger.info("!checkpoint 2");
    	
    	int id = 0;
    	logger.info("!checkpoint 2.1");
    	
    	String pricelist = propercase(line[0]);
    	logger.info("!checkpoint 2.2");
    	String supplier = propercase(line[1]);
    	logger.info("!checkpoint 2.3");
    	String brand = line[2];
    	logger.info("!checkpoint 2.4");
    	String supplierProductCode = line[3];
    	logger.info("!checkpoint 2.5");
    	String productDescription = line[4];
    	logger.info("!checkpoint 2.6");
    	String unitSize = line[5];
    	logger.info("!checkpoint 2.7");
    	String quantity = line[6];
    	
    	logger.info("!checkpoint 3");
    	
    	// parse the price
    	boolean valid = false;
    	BigDecimal unitTradePrice = new BigDecimal(0.0);
    	DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
    	format.setParseBigDecimal(true);
    	
    	logger.info("!checkpoint 4");
    	
    	try {
    		unitTradePrice = (BigDecimal) format.parse(line[7]);
    		valid = true;
    	} catch (ParseException exc) {
    		valid = false;
    	}
    	
    	logger.info("!checkpoint 5");
    	
    	logger.info("About to call new PricelistProduct(...)");
    	PricelistProduct product = new PricelistProduct(id, pricelist, supplier, brand, supplierProductCode,
    			productDescription, unitSize, quantity, unitTradePrice, valid);
    	logger.info("After call to new PricelistProduct(...)");
    	
    	logger.info("!checkpoint 6");
    	return product;    	
    }
    
    public void dumpProductToConsole(PricelistProduct p) {
    	String pdesc = "[id=" + p.getId() + "] "
    			+ "[Pricelist=" + p.getPricelist() + "] "
    			+ "[Supplier=" + p.getSupplier() + "] "
    			+ "[Brand=" + p.getBrand() + "] "
    			+ "[Code=" + p.getSupplierProductCode() + "] "
    			+ "[Description=" + p.getProductDescription() + "] "
    			+ "[UnitSize=" + p.getUnitSize() + "] "
    			+ "[Quantity=" + p.getQuantity() + "] "
    			+ "[Price=" + p.getUnitTradePrice() + "] "
    			+ "[Valid=" + p.isValid() + "]";
    	
    	logger.info(pdesc);
    }
    
    // Import the uploaded products list into the products table
    public void acceptProducts() {
    	logger.info("Importing products");
    	
    	try {
    		logger.info("About to delete existing pricelist products");
    		productDAO.deleteAllPricelistProducts();
    		logger.info("Deleted existing pricelist products");
        	productDAO.addPricelistProducts(uploadedPricelistProducts);
        	logger.info("Added new pricelist products");
        	
        	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Imported " + uploadedPricelistProducts.size() + " products.");
            FacesContext.getCurrentInstance().addMessage(null, message);
    	} catch (Exception exc) {
    		logger.info("An exception occured while importing pricelist products");
    		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Could not import the uploaded products.");
            FacesContext.getCurrentInstance().addMessage(null, message);
    	}
    	
    	logger.info("Finished importing products");
    }
    
    // return true if there are products to import
 	public boolean isAcceptable() {
 		if (uploadedPricelistProducts.size() > 0) {
 			return true;
 		} else {
 			return false;
 		}
 	}
 	
 	// G E T T E R S    A N D    S E T T E R S
 	
 	// not required
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public UploadedFile getFile() {
        return file;
    }    

	public List<PricelistProduct> getUploadedPricelistProducts() {
		return uploadedPricelistProducts;
	}
    
	public Set<String> getPricelists() {
		
		return pricelists;
	}

	public Set<String> getSuppliers() {
		return suppliers;
	}

	public List<PricelistProduct> getFilteredProducts() {
		return filteredProducts;
	}

	public void setFilteredProducts(List<PricelistProduct> filteredProducts) {
		this.filteredProducts = filteredProducts;
	}
	
	// convert string to propercase
	private String propercase(String s) {
		// Deal with empty string
		if (s.isEmpty()) {
			return s;
		}
		
		String trimmedLower = s.trim().toLowerCase();
		char[] chars = trimmedLower.toCharArray();
		chars[0] = Character.toTitleCase(chars[0]);
		
		String result = new String(chars);
		return result;
	}
    
}
