package rh6pd.processController;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header; 
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;   
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod; 
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient; 
import org.slf4j.Logger;
  
public class HttpMethodWrapper { 
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(HttpMethodWrapper.class);
	
	private HttpState state; 
	private HttpClient client = new HttpClient(); // Use one client here, so that requests go over a single connection. 
	
	private String baseUrl = "http://localhost:8080";  
	 
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;   
	}
	
	public HttpMethodWrapper() {
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
	} 
	
	public String lastContent = "";
	public int lastResponse; 
	
	public String httpPost(String url) { 
		return httpPost(url, new NameValuePair[] {}); 
	}
	  
	public String httpPost(String url, NameValuePair[] params) {
		url = baseUrl + url;
		
		log.debug("- HTTP POST: " + url); 
		
		PostMethod post = new PostMethod(url);
		 
		if (params.length > 0) { 
			post.setRequestBody(params); 
		} 
		
		post.addRequestHeader("Connection", "keep-alive"); 
		post.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		    
		try {  
			this.lastResponse = client.executeMethod(post);
			
			if (this.lastResponse != 200) {   
				log.debug("Http response code: " + this.lastResponse);  
			}  
			
			return isToString(post.getResponseBodyAsStream());
		} catch (HttpException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		 
		return ""; 
	}
	
	// FIXME 
	private String isToString(InputStream is ) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder b = new StringBuilder();
		String line;
		   
		try {
			while ((line = br.readLine()) != null) {
				b.append(line + "\n"); 
			}
		} catch (IOException e) {
			log.error("Exception while reading response IS ", e);   
		} 
		
		return b.toString();
	}
	 
	public String httpGet(String url) throws Exception 
	{ 
		url = baseUrl + url; 
		log.debug("HTTP GET: " + url); 
		 
		GetMethod get = new GetMethod(url);  
		get.addRequestHeader("Connection", "keep-alive"); 
		get.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		
		if (client.getState().getCookies().length > 0) {      
			log.debug("GET Request with cookie (should include JSESSIONID): " + client.getState().getCookies()[0]);
		} else {
			log.warn("Previous GET request WITHOUT cookie");  
		}
  
		try {
			int responseCode = client.executeMethod(get);
			String responseBody; 
			 
			this.state = client.getState();
			responseBody = get.getResponseBodyAsString();
			 
			if (responseCode != 200) {
				throw new Exception("Http response code: " + responseCode); 
			}
  
			return responseBody;
		} catch (Exception e) {
			throw e;  
		} finally {
			get.releaseConnection();
		}
	}

	public HttpState getState() {
		return state; 
	}

	public String getBaseUrl() {
		return baseUrl; 
	}
}
