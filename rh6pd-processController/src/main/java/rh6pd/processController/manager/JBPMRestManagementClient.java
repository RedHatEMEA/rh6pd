package rh6pd.processController.manager;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JBPMRestManagementClient extends WebServiceClient {
	private final Logger log = LoggerFactory.getLogger(JBPMRestManagementClient.class);

	protected static ResourceBundle bundle;

	private String username;
	private String password;

	static {
		bundle = ResourceBundle.getBundle("processResources");
	}

	public JBPMRestManagementClient(String username, String password) {
		this.username = username;
		this.password = password;

		this.authenticate();
	}

	private void authenticate(String username, String password) {
		this.username = username;
		this.password = password;

		authenticated = false;
		authenticate();
	}

	boolean authenticated = false;

	private void authenticate() {
		if (authenticated) {
			log.warn("Already authenticated. Not doing it again.");
			return;
		}

		log.warn("Authenticating as: " + this.username + ":" + this.password);

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		String urlAuth = urlForm("process.j_security_check");
		HttpPost httpPost = new HttpPost(urlAuth);

		try {
			// start a new session
			this.getDataFromRestService(urlForm("user.management.secure.sid"), HttpRequestType.GET);

			HashMap<String, Object> props = new HashMap<String, Object>();
			props.put("j_username", this.username);
			props.put("j_password", this.password);
			this.getDataFromRestService(urlAuth, HttpRequestType.POST, props);

			// kneel to jBPM console madness and present gift
			this.getDataFromRestService(urlForm("user.management.secure.sid"), HttpRequestType.GET);

			this.authenticated = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void logout() throws Exception {
		String url;

		url = urlForm("user.management.invalidate");
		this.getDataFromRestService(url, HttpRequestType.POST);

		url = urlForm("user.management.secure.sid");
		this.getDataFromRestService(url, HttpRequestType.POST);
	}

	protected void relogin(String username, String password) throws Exception {
		logout();
		this.authenticate(username, password);

	}

	protected static String urlForm(String prop) {
		return "http://" + bundle.getString("process.host") + bundle.getString("process.urlContext") + bundle.getString(prop);
	}

	protected static String urlForm(String prop, Object... params) {
		String msg = bundle.getString(prop);
		msg = MessageFormat.format(msg, params);
		return "http://" + bundle.getString("process.host") + bundle.getString("process.urlContext") + msg;
	}
}