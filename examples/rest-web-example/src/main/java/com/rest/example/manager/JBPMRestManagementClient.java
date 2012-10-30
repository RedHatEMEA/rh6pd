package com.rest.example.manager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class JBPMRestManagementClient {
	protected String KEY_USERNAME = "j_username";
	protected String KEY_PASSWORD = "j_password";
	private DefaultHttpClient httpClient; // keep this out of the method in order to reuse the object for calling other services without losing session
	protected String address;
	protected ResourceBundle bundle;
	
	private int i = 0;
	
    protected String urlForm(String prop){
    	return "http://"+bundle.getString("process.host")+bundle.getString("process.urlContext")+bundle.getString(prop);
    }

    protected String urlForm(String prop, String ... params){
    	String msg = bundle.getString(prop);
    	msg = MessageFormat.format(msg, params);
    	return "http://"+bundle.getString("process.host")+bundle.getString("process.urlContext")+msg;
    }
	
    public JBPMRestManagementClient(){
    	if(this.bundle == null)
    		this.bundle = ResourceBundle.getBundle("processResources");
    	this.init(this.bundle.getString("process.user"), this.bundle.getString("process.password"));
    }
    
    public JBPMRestManagementClient(String username, String password){
		this.init(username, password);
    }
    
    protected void relogin(String username, String password)throws Exception{
    	String url = urlForm("user.management.invalidate");
    	this.requestPostService(url, null);
    	url = urlForm("user.management.secure.sid");
    	requestGetService(url, null);
    	this.init(username, password);
    	
    }
    
    private void init(String username, String password){
    	if(this.bundle == null)
    		this.bundle = ResourceBundle.getBundle("processResources");
    	if(httpClient == null)
    		httpClient = new DefaultHttpClient();
		this.address = bundle.getString("process.host");
		try{
			String url = urlForm("user.management.secure.sid");
			String resp = this.requestPostService(url, null);
    		this.authenticate(username, password);
    		String lol = this.requestPostService(url, null);
    		System.out.println(lol);
		}catch (Exception e) {
			e.printStackTrace();
		}
    }
    
//	public JBPMRestManagementClient(String host,String port){
//    	if(this.bundle == null)
//    		this.bundle = ResourceBundle.getBundle("processResources");
//    	if(httpClient == null)
//    		httpClient = new DefaultHttpClient();
//		this.address = host+":"+port;
//		}
	
	private String authenticate(String username, String password) {

		String responseString = "";
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(KEY_USERNAME, username));
		formparams.add(new BasicNameValuePair(KEY_PASSWORD, password));
		String urlAuth = urlForm("process.j_security_check");
		HttpPost httpPost = new HttpPost(urlAuth);
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");//UrlEncodedFormEntity(formparams, "multipart/form-data");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			while (line != null) {
				stringBuilder.append(line);
				line = bufferedReader.readLine();
			}
			responseString = stringBuilder.toString();

		} catch (Exception e) {
		throw new RuntimeException(e);

		} finally {

		if (inputStreamReader != null) {
			try {
				inputStreamReader.close();

		} catch (Exception e) {

		throw new RuntimeException(e);

		}

		}

		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		}
		return responseString;
		}			
	
	private String requestPostMultipartService(String url, Map<String, Object> parameters) throws Exception{
		i = i +1;
		String responseString = "";
		HttpPost httpPost = new HttpPost(url);
		if (parameters == null) 
			parameters = new HashMap<String, Object>();

		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		Set<String> keys = parameters.keySet();
		for (Iterator<String> keysIterator = keys.iterator(); keysIterator.hasNext();) {
			String keyString = keysIterator.next();
			String value = parameters.get(keyString).toString();
			entity.addPart(keyString, new StringBody(value));
		}	
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);		
		responseString = this.getRequestString(response);
		
		System.out.println("DEBUG POST " + i + " URL : " + url);
		return responseString;
	}
	
	private byte[] getRequestByteArray(HttpResponse response)throws Exception{
		InputStream is = response.getEntity().getContent();
	    int len;
	    int size = 1024;
	    byte[] buf;

	    if (is instanceof ByteArrayInputStream) {
	      size = is.available();
	      buf = new byte[size];
	      len = is.read(buf, 0, size);
	    } else {
	      ByteArrayOutputStream bos = new ByteArrayOutputStream();
	      buf = new byte[size];
	      while ((len = is.read(buf, 0, size)) != -1)
	        bos.write(buf, 0, len);
	      buf = bos.toByteArray();
	    }
		return buf;
	}
	
	
	private String getRequestString(HttpResponse response)throws Exception{
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		String req = "";
		InputStream inputStream = response.getEntity().getContent();
		inputStreamReader = new InputStreamReader(inputStream);
		bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder stringBuilder = new StringBuilder();
		String line = bufferedReader.readLine();
		while (line != null) {
			stringBuilder.append(line);
			line = bufferedReader.readLine();
		}
		req = stringBuilder.toString();
		return req;
	}
	
	private String implode(String[] ary, String delim) {
	    String out = "";
	    for(int i=0; i<ary.length; i++) {
	        if(i!=0) { out += delim; }
	        out += ary[i];
	    }
	    return out;
	}
	

	
	 private  HttpResponse getResponseGET(String url, Map<String, Object> parameters) throws Exception{
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			if (parameters == null) 
				parameters = new HashMap<String, Object>();
			
			Set<String> keys = parameters.keySet();
			int i = 0;
			String[] a = null;
			if(parameters.size() > 0){
				url = url + "?";
				a = new String[parameters.size()];
			}
			for (Iterator<String> keysIterator = keys.iterator(); keysIterator.hasNext();) {
				String keyString = keysIterator.next();
				String value = parameters.get(keyString).toString();
				a[i++] = keyString + "=" + value;
			}
			if(parameters.size() > 0){
				String implode = this.implode(a, "&");
				url = url + implode;
			}
				
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);
			return response;
	 }
	 
	private InputStream getBytesFromRestGetService(String url) throws Exception{
		 HttpResponse resp = this.getResponseGET(url, null);
		 return resp.getEntity().getContent();
	}
	 
	private String requestGetService(String url, Map<String, Object> parameters) throws Exception{
		String responseString = "";
		responseString = this.getRequestString(this.getResponseGET(url, parameters));
		return responseString;		
	}
	
	private String requestPostService(String url, Map<String, Object> parameters) throws Exception{

		i = i + 1;
		
		String responseString = "";
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		if (parameters == null) 
			parameters = new HashMap<String, Object>();
		
		Set<String> keys = parameters.keySet();
		for (Iterator<String> keysIterator = keys.iterator(); keysIterator.hasNext();) {
			String keyString = keysIterator.next();
			String value = parameters.get(keyString).toString();
			formparams.add(new BasicNameValuePair(keyString, value));
		}

		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");		
		httpPost.setEntity(entity);
		HttpResponse response = httpClient.execute(httpPost);
		responseString = this.getRequestString(response);
		
		System.out.println("DEBUG POST " + i + " URL : " + url);

		return responseString;

		}
	
	
	protected String getDataFromRestService(String url, EnumJBPMRestType enumRest)throws Exception{
		return this.getDataFromRestService(url, enumRest, null);
	}
	
	protected InputStream getBytesFromRestService(String url, EnumJBPMRestType enumRest)throws Exception{
		
		InputStream bt = null;
//		String responseString = this.requestGetService(url, null);
//    	if(responseString.contains("j_security_check")){ 
//    		this.authenticate(userName, password);    
//    		bt = this.getBytesFromRestGetService(url);
//    	}else
    		bt = this.getBytesFromRestGetService(url);
		return bt;
	}
	
	
	protected String getDataFromRestService(String url, EnumJBPMRestType enumRest, Map<String, Object> parameters)throws Exception{
		String json = "";	
		
		System.out.println("DEBUG MGR: " + url + " : " + enumRest.getCode());

		if(EnumJBPMRestType.MULTIPART.code.equals(enumRest.getCode())){
			String responseString = this.requestPostMultipartService(url, parameters);
//			if(responseString.contains("j_security_check")){ 
//	    		this.authenticate(userName, password);    
//	    		json = this.requestPostMultipartService(url, parameters);
//	    	}else
	    		json = responseString;	    
		}
		if(EnumJBPMRestType.POST.code.equals(enumRest.getCode())){
			//System.out.println("DEBUG MGR: FIRING POST");
			String responseString = this.requestPostService(url, parameters);
//			if(responseString.contains("j_security_check")){ 
//	    		this.authenticate(userName, password);    
//	    		json = this.requestPostService(url, parameters);
//	    	}else
	    		json = responseString;
		}
		if(EnumJBPMRestType.GET.code.equals(enumRest.getCode())){
			String responseString = this.requestGetService(url, parameters);
//			if(responseString.contains("j_security_check")){ 
//	    		this.authenticate(userName, password);    
//	    		json = this.requestGetService(url, parameters);
//	    	}else
	    		json = responseString;
		}		
//		System.out.println("DEBUG HTTP POST REQUESTS: " + i);
		return json;
	}
	
    public static void main(String[] args) throws Exception {
    	

     }
}