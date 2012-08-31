package rh6pd.processController;


import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
  
public class HttpMethodWrapper {
	private Cookie cookie;
	private HttpState state; 

	public void setCookie(Cookie cookie) {
		this.cookie = cookie; 
	} 
	
	private String baseUrl = "http://localhost:8080";
	
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;  
	}
	
	public void httpPost(String url, HashMap<String, String> params) 
	{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(baseUrl + url);
		
		if (cookie != null) {
			client.getState().addCookie(this.cookie);
		}
		
		StringBuffer sb = new StringBuffer(); 
		
		try {
			client.executeMethod(post);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Header contentType = post.getResponseHeader("Content-Type");
		
		if (!contentType.getValue().equals("application/json") )
		{
			System.out.println("Did not get JSON back from server! Actually got: " + contentType.getValue());
			System.out.println(sb.toString());
		} 
		else 
		{
			System.out.println("JSon Result: => " + sb.toString());
		} 
	}
	 
	public String httpGet(String url) throws Exception 
	{
		url = baseUrl + url; 
		System.out.println("HTTP GET: " + url);
		 
		HttpClient httpclient = new HttpClient(); 
		GetMethod get = new GetMethod(url);
		 
		if (cookie != null) {
			httpclient.getState().addCookie(cookie);
			System.out.println("\tGET Request with cookie (should include JSESSIONID): " + cookie);
		} 
  
		StringBuffer sb = new StringBuffer();

		try {
			int result = httpclient.executeMethod(get); 
			
			if (result != 200) {
				throw new Exception("Http response code: " + result); 
			}
			
			this.state = httpclient.getState(); 
			
			sb.append(get.getResponseBodyAsString());

			return sb.toString();
		} catch (Exception e) {
			throw e;  
		} finally {
			get.releaseConnection();
		}
	}

	public HttpState getState() {
		return state; 
	}
}
