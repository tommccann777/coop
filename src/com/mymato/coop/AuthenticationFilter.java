package com.mymato.coop;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter("/restricted/*")
public class AuthenticationFilter implements Filter {
	
	@SuppressWarnings("unused")
	private FilterConfig config;
	private Logger logger = Logger.getLogger(getClass().getName());

	  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		  
		  logger.info("HttpServletRequest received");
		  
		  // Save the requested destination
		  String requestURI = ((HttpServletRequest) req).getRequestURI();
		  //logger.info("cooplog doFilter: requestURI=" + requestURI);
		  //LogDAO.getInstance().addMessage("AuthenticationFilter", "requestURI=" + requestURI);
		  
		  // Get the context path
		  String contextPath = ((HttpServletRequest) req).getContextPath();
		  //logger.info("cooplog doFilter: contextPath=" + contextPath);
		  //LogDAO.getInstance().addMessage("AuthenticationFilter", "contextPath=" + contextPath);
		  
		  // Extract the context path from the destination
		  String destination = requestURI.substring(contextPath.length());
		  //logger.info("cooplog doFilter: destination=" + destination);
		  //LogDAO.getInstance().addMessage("AuthenticationFilter", "destination=" + destination);
		  
	    if (((HttpServletRequest) req).getSession().getAttribute(ApplicationMapHelper.USER_KEY) == null) {
	    	
	    	//logger.info("cooplog doFilter: Redirecting to " + ((HttpServletRequest) req).getContextPath() + "/login.xhtml?from=" + URLEncoder.encode(destination, "UTF-8"));
	    	
	      ((HttpServletResponse) resp).sendRedirect(((HttpServletRequest) req).getContextPath() + "/login.xhtml?from=" + URLEncoder.encode(destination, "UTF-8"));
	    } else {
	    	//logger.info("cooplog doFilter: chaining");
	    	chain.doFilter(req, resp);
	    }
	  }

	  public void init(FilterConfig config) throws ServletException {
	    this.config = config;
	  }

	  public void destroy() {
	    config = null;
	  }
}