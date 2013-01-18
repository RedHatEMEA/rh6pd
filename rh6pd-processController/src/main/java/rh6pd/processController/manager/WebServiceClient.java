package rh6pd.processController.manager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServiceClient {
	private final static transient Logger LOG = LoggerFactory.getLogger(WebServiceClient.class);
	protected final DefaultHttpClient httpClient;
	private static int requestNumber = 0;

	public WebServiceClient() {
		this.httpClient = new DefaultHttpClient();
	}

	protected InputStream getBytesFromRestGetService(String url) throws Exception {
		HttpResponse resp = this.executeGetRequest(url, null);
		return resp.getEntity().getContent();
	}

	protected String getContentFromRestService(ContentType contentType, String url, HttpRequestType requestType) throws Exception {
		String content = getDataFromRestService(url, requestType);

		return content;
	}

	protected String getDataFromRestService(String url, HttpRequestType enumRest) throws Exception {
		return this.getDataFromRestService(url, enumRest, null);
	}

	protected String getDataFromRestService(String url, HttpRequestType httpRequestType, Map<String, Object> parameters) throws Exception {
		requestNumber++;
		String response;

		LOG.debug("HTTP Request  (" + requestNumber + ") " + httpRequestType.toString() + ":" + url);

		switch (httpRequestType) {
		case MULTIPART:
			response = this.requestPostMultipartService(url, parameters);
			break;
		case POST:
			response = this.requestPostService(url, parameters);
			break;
		case GET:
			response = this.requestGetService(url, parameters);
			break;
		default:
			throw new Exception("Invalid type of http request:" + httpRequestType);
		}

		LOG.debug("HTTP Response (" + requestNumber + ") " + httpRequestType.toString() + ":" + response);

		return response;
	}

	private String getResponseContentAsString(HttpResponse response) throws Exception {
		InputStream inputStream = response.getEntity().getContent();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder stringBuilder = new StringBuilder();

		String line;

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}

		inputStream.close();
		inputStreamReader.close();
		bufferedReader.close();

		EntityUtils.consume(response.getEntity());

		return stringBuilder.toString();
	}

	private String requestGetService(String url, Map<String, Object> parameters) throws Exception {
		HttpResponse resp = this.executeGetRequest(url, parameters);
		String responseString = this.getResponseContentAsString(resp);

		EntityUtils.consume(resp.getEntity());

		return responseString;
	}

	private HttpResponse executeGetRequest(String url, Map<String, Object> parameters) throws Exception {
		new ArrayList<NameValuePair>();
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}

		Set<String> keys = parameters.keySet();
		int i = 0;
		String[] a = null;
		if (parameters.size() > 0) {
			url = url + "?";
			a = new String[parameters.size()];
		}
		for (String keyString : keys) {
			String value = parameters.get(keyString).toString();
			a[i++] = keyString + "=" + value;
		}
		if (parameters.size() > 0) {
			String implode = this.implode(a, "&");
			url = url + implode;
		}

		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = this.httpClient.execute(httpGet);

			return response;
		} catch (HttpHostConnectException e) {
			throw new Exception("Could not connect to BRMS server. " + e.getMessage());
		}
	}

	private String implode(String[] ary, String delim) {
		String out = "";
		for (int i = 0; i < ary.length; i++) {
			if (i != 0) {
				out += delim;
			}
			out += ary[i];
		}
		return out;
	}

	private String requestPostMultipartService(String url, Map<String, Object> parameters) throws Exception {
		this.requestNumber++;

		String responseString = "";
		HttpPost httpPost = new HttpPost(url);
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}

		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

		Set<String> keys = parameters.keySet();
		for (String keyString : keys) {
			String value = parameters.get(keyString).toString();
			entity.addPart(keyString, new StringBody(value));
		}
		httpPost.setEntity(entity);
		HttpResponse response = this.httpClient.execute(httpPost);
		responseString = this.getResponseContentAsString(response);

		return responseString;
	}

	private String requestPostService(String url, Map<String, Object> parameters) throws Exception {
		this.requestNumber++;

		String responseString = "";
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}

		Set<String> keys = parameters.keySet();
		for (String keyString : keys) {
			String value = parameters.get(keyString).toString();
			formparams.add(new BasicNameValuePair(keyString, value));
		}

		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		httpPost.setEntity(entity);
		HttpResponse response;

		try {
			response = this.httpClient.execute(httpPost);
		} catch (ConnectException e) {
			throw new Exception("Could not connect to BRMS server due to a Java ConnectException. Is there a BRMS Server running at: " + url, e);
		}

		responseString = this.getResponseContentAsString(response);

		return responseString;
	}
}
