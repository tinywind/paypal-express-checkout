package com.paypal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ReturnServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	   
    
    public ReturnServlet() {
        super();
    
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            		throws ServletException, IOException {
    	HttpSession session = request.getSession(true);
    	//String finalPaymentAmount = (String)session.getAttribute("Payment_Amount");

    	if (isSet(request.getParameter("PayerID")))
    		session.setAttribute("payer_id", request.getParameter("PayerID"));
    	String token = "";
    	if (isSet(request.getParameter("token"))){
    		session.setAttribute("TOKEN", request.getParameter("token"));
    		token = request.getParameter("token");
    	}else{
    		token = (String) session.getAttribute("TOKEN");
    	}

    	// Check to see if the Request object contains a variable named 'token'	
    	PayPal pp = new PayPal();   	
    	Map<String, String> result = new HashMap<String, String>();
    	// If the Request object contains the variable 'token' then it means that the user is coming from PayPal site.	
    	if (isSet(token))
    	{
    		/*
    		* Calls the GetExpressCheckoutDetails API call
    		*/
    		Map<String,String> results = pp.getShippingDetails(token );
    	    String strAck = results.get("ACK").toString();
    		if(strAck !=null && (strAck.equalsIgnoreCase("SUCCESS") || strAck.equalsIgnoreCase("SUCCESSWITHWARNING") ))
    		{
    	    	session.setAttribute("payer_id", results.get("PAYERID"));
    			result.putAll(results);
    			/*
    			* The information that is returned by the GetExpressCheckoutDetails call should be integrated by the partner into his Order Review 
    			* page		
    			*/
    			String email 				= results.get("EMAIL"); // ' Email address of payer.
    			String payerId 			= results.get("PAYERID"); // ' Unique PayPal customer account identification number.
    			String payerStatus		= results.get("PAYERSTATUS"); // ' Status of payer. Character length and limitations: 10 single-byte alphabetic characters.
    			String firstName			= results.get("FIRSTNAME"); // ' Payer's first name.
    			String lastName			= results.get("LASTNAME"); // ' Payer's last name.
    			String shipToName			= results.get("PAYMENTREQUEST_0_SHIPTONAME"); // ' Person's name associated with this address.
    			String shipToStreet		= results.get("PAYMENTREQUEST_0_SHIPTOSTREET"); // ' First street address.
    			String shipToCity			= results.get("PAYMENTREQUEST_0_SHIPTOCITY"); // ' Name of city.
    			String shipToState		= results.get("PAYMENTREQUEST_0_SHIPTOSTATE"); // ' State or province
    			String shipToCntryCode	= results.get("PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE"); // ' Country code. 
    			String shipToZip			= results.get("PAYMENTREQUEST_0_SHIPTOZIP"); // ' U.S. Zip code or other country-specific postal code.
    			String addressStatus 		= results.get("ADDRESSSTATUS"); // ' Status of street address on file with PayPal 
    			String totalAmt   		= results.get("PAYMENTREQUEST_0_AMT"); // ' Total Amount to be paid by buyer
    			String currencyCode       = results.get("CURRENCYCODE"); // 'Currency being used 
    			
    		} 
    		else  
    		{
    			//Display a user friendly Error on the page using any of the following error information returned by PayPal
                String errorCode = results.get("L_ERRORCODE0").toString();
                String errorShortMsg = results.get("L_SHORTMESSAGE0").toString();
                String errorLongMsg = results.get("L_LONGMESSAGE0").toString();
                String errorSeverityCode = results.get("L_SEVERITYCODE0").toString();
                
                String errorString = "SetExpressCheckout API call failed. "+

               		"<br>Detailed Error Message: " + errorLongMsg +
    		        "<br>Short Error Message: " + errorShortMsg +
    		        "<br>Error Code: " + errorCode +
    		        "<br>Error Severity Code: " + errorSeverityCode;
                request.setAttribute("error", errorString);
            	RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            	session.invalidate();
            	if (dispatcher != null){
            		dispatcher.forward(request, response);
            	}
            	return;
            }
    	}   
    	
    	Map<String, String> checkoutDetails = new HashMap<String, String>();
		checkoutDetails.putAll((Map<String, String>) session.getAttribute("checkoutDetails"));
		checkoutDetails.putAll(setRequestParams(request));
		checkoutDetails.put("TOKEN", token);
		checkoutDetails.put("payer_id", (String) session.getAttribute("payer_id"));
		result.put("PAYMENTREQUEST_0_SHIPPINGAMT", checkoutDetails.get("PAYMENTREQUEST_0_SHIPPINGAMT"))	;	
		if(isSet(request.getParameter("shipping_method"))){
    		BigDecimal newShipping = new BigDecimal(checkoutDetails.get("shipping_method")); //need to change this value, just for testing
    		BigDecimal shippingamt = new BigDecimal(checkoutDetails.get("PAYMENTREQUEST_0_SHIPPINGAMT"));
    		BigDecimal paymentAmount = new BigDecimal(checkoutDetails.get("PAYMENTREQUEST_0_AMT"));
    		if(shippingamt.compareTo(new BigDecimal(0)) > 0){ 
    			paymentAmount = paymentAmount.add(newShipping).subtract(shippingamt) ;
    		}
    		checkoutDetails.put("PAYMENTREQUEST_0_AMT",paymentAmount.toString());  //.replace(".00", "")
    		checkoutDetails.put("PAYMENTREQUEST_0_SHIPPINGAMT",newShipping.toString());	
    		checkoutDetails.put("shippingAmt",newShipping.toString());
		}

    	
    	/*
    	* Calls the DoExpressCheckoutPayment API call
    	*/
       	String page="return.jsp";
    	if (isSet(request.getParameter("page")) && request.getParameter("page").equals("return")){  
	    	// FIXME - The method 'request.getServerName()' must be sanitized before being used.
			HashMap results = pp.confirmPayment (checkoutDetails,request.getServerName() );
			request.setAttribute("payment_method","");
	    	String strAck = results.get("ACK").toString().toUpperCase();
	    	if(strAck !=null && (strAck.equalsIgnoreCase("Success") || strAck.equalsIgnoreCase("SuccessWithWarning"))){
		    	result.putAll(results);
		    	result.putAll(checkoutDetails);
		    	request.setAttribute("ack", strAck);
		    	session.invalidate();
	    	}else{
	    		//Display a user friendly Error on the page using any of the following error information returned by PayPal
	            String errorCode = results.get("L_ERRORCODE0").toString();
	            String errorShortMsg = results.get("L_SHORTMESSAGE0").toString();
	            String errorLongMsg = results.get("L_LONGMESSAGE0").toString();
	            String errorSeverityCode = results.get("L_SEVERITYCODE0").toString();	            
	            String errorString = "SetExpressCheckout API call failed. "+
	           		"<br>Detailed Error Message: " + errorLongMsg +
	    	        "<br>Short Error Message: " + errorShortMsg +
	    	        "<br>Error Code: " + errorCode +
	    	        "<br>Error Severity Code: " + errorSeverityCode;
	            request.setAttribute("error", errorString);
	        	RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
	        	session.invalidate();
	        	if (dispatcher != null){
	        		dispatcher.forward(request, response);
	        	}	        	
	        	return;
	    	}
	        page="return.jsp";
    	}else{
    		page="review.jsp";
    	}
    	request.setAttribute("result", result);

    	RequestDispatcher dispatcher = request.getRequestDispatcher(page);
    	
    	if (dispatcher != null){
    		dispatcher.forward(request, response);
    	}
    }
	
   
    
   
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    			doGet(request, response);
    		}
    
	private Map<String,String> setRequestParams(HttpServletRequest request){
		Map<String,String> requestMap = new HashMap<String,String>();
		for (String key : request.getParameterMap().keySet()) {
			
			requestMap.put(key, request.getParameterMap().get(key)[0]);
			}
		
		return requestMap;
		
	}	
	private boolean isSet(Object value){
		return (value !=null && value.toString().length()!=0);
	}
}
