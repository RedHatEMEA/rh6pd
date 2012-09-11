package rh6pd.processController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.slf4j.Logger;

public class HttpMethodWrapper {
	private static final Logger log = org.slf4j.LoggerFactory
			.getLogger(HttpMethodWrapper.class);

	private HttpState state;
	private final HttpClient client = new HttpClient(); // Use one client here,
														// so that requests go
														// over a single
														// connection.

	private String baseUrl = "http://localhost:8080";

	public String lastContent = "";

	public HttpMethodWrapper() {
		this.client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.BROWSER_COMPATIBILITY);
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public HttpState getState() {
		return this.state;
	}

	public String httpGet(String url) throws Exception {
		url = this.baseUrl + url;
		HttpMethodWrapper.log.debug("HTTP GET: " + url);

		GetMethod get = new GetMethod(url);
		get.addRequestHeader("Connection", "keep-alive");
		get.addRequestHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		if (this.client.getState().getCookies().length > 0) {
			HttpMethodWrapper.log
					.debug("GET Request with cookie (should include JSESSIONID): "
							+ this.client.getState().getCookies()[0]);
		} else {
			HttpMethodWrapper.log.warn("Previous GET request WITHOUT cookie");
		}

		try {
			int responseCode = this.client.executeMethod(get);
			String responseBody;

			this.state = this.client.getState();
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

	public String httpPost(String url) {
		return this.httpPost(url, new NameValuePair[] {});
	}

	public String httpPost(String url, NameValuePair[] params) {
		url = this.baseUrl + url;

		HttpMethodWrapper.log.debug("- HTTP POST: " + url);

		PostMethod post = new PostMethod(url);

		if (params.length > 0) {
			post.setRequestBody(params);
		}

		post.addRequestHeader("Connection", "keep-alive");
		post.addRequestHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		try {
			int responseCode = this.client.executeMethod(post);

			if (responseCode != 200) {
				HttpMethodWrapper.log.debug("Http response code: "
						+ responseCode);
				return this.isToString(post.getResponseBodyAsStream());
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	// FIXME
	private String isToString(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder b = new StringBuilder();
		String line;

		try {
			while ((line = br.readLine()) != null) {
				b.append(line + "\n");
			}
		} catch (IOException e) {
			HttpMethodWrapper.log.error("Exception while reading response IS ",
					e);
		}

		return b.toString();
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
