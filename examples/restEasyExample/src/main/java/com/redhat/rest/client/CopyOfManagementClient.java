//package com.redhat.rest.client;
//
//
////import org.apache.commons.httpclient.*;
////import org.apache.commons.httpclient.methods.GetMethod;
////import org.apache.commons.httpclient.methods.PostMethod;
////import org.jboss.bpm.console.client.model.*;
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import org.apache.http.client.ClientProtocolException;
//import org.jboss.resteasy.client.ClientRequest;
//import org.jboss.resteasy.client.ClientResponse;
//
//import java.io.IOException;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.util.HashMap;
//
//import javax.ws.rs.HttpMethod;
//
////import org.jboss.soa.bpel.console.ModelAdaptor;
//
////import com.google.gson.*;
//
//
//public class CopyOfManagementClient {
//	//private static final String loginUrl = "http://localhost:8080/business-central-server/rs/process/definitions";
//	private static final String loginUrl = "http://localhost:8080/j_security_check";
//	
//    private static final String deployment_url = "http://localhost:8080/business-central-server/rs/engine/deployments";
//    private static final String definitions_url = "http://localhost:8080/business-central-server/rs/process/definitions";
//    private static final String authentication_url = "http://localhost:8080/business-central-server/rs/identity/secure/j_security_check";
//    private static final String history_search_url = "http://localhost:8080/business-central-server/rs/history/definition/";
// 
//    private String username = "admin";
//    private String password = "admin";
//     
//    private boolean isLoggedIn = false;
//
//    public CopyOfManagementClient(String u, String p) {
//    	username = u;
//    	password = p;
//    }
//
//    public void showAllDeployments() throws Exception {
//        System.out.println("-----------------------------");
//        System.out.println("Get all of deployments: ");
//        String result = getDataFromService(deployment_url, "GET");
//
//        System.out.println("-----------------------------");
//        System.out.println("Marshall the Json data into java class.");
//
////        Gson gson = GsonFactory.createInstance();
////        DeploymentRefWrapper wrapper = gson.fromJson(result, DeploymentRefWrapper.class);
////
////        for (DeploymentRef ref : wrapper.getDeployments()) {
////            System.out.println("deployment name is: " + ref.getName());
////        }
//
//    }
//
//    public void showAllDefinitions() throws Exception {
//        System.out.println("-----------------------------");
//        System.out.println("Get all of process definitions: ");
//        String result = getDataFromService(definitions_url, "GET");
// 
//        System.out.println("-----------------------------");
//        System.out.println("Marshall the Json data into java class.");
//        
////        Gson gson = GsonFactory.createInstance();
////        ProcessDefinitionRefWrapper wrapper = gson.fromJson(result, ProcessDefinitionRefWrapper.class);
////
////        for (ProcessDefini    	private final String loginUrl = "http://localhost:8080/business-central-server/rs/form/process/org.jbpm.evaluation.carinsurance.quote/render"tionRef ref : wrapper.getDefinitions()) {
////            System.out.println("process name is: " + ref.getName());
////        }
//    }
//
//    public void getActiveProcessInstance() throws Exception {
//        String processId = "{http://www.jboss.org/bpel/examples}HelloGoodbye-1";
//        //String encodedId = ModelAdaptor.encodeId(processId);
//        //String instances_url = "http://localhost:8080/gwt-console-server/rs/process/definition" + "/" + encodedId + "/instances";
//        System.out.println("-----------------------------");
//        System.out.println("Get active process instances from process definition of : " + processId );
//        //String result = getDataFromService(instances_url, "GET");
//
//        System.out.println("-----------------------------");
//        System.out.println("Marshall the Json data into java class.");
//
////        Gson gson = GsonFactory.createInstance();
////        ProcessInstanceRefWrapper wrapper = gson.fromJson(result, ProcessInstanceRefWrapper.class);
////
////        for (ProcessInstanceRef ref : wrapper.getInstances()) {
////            System.out.println("instance id is: " + ref.getId() + " definition key is: " + ModelAdaptor.decodeId(ref.getDefinitionId()));
////        }
//    }
//
//
//    public void getHistoricProcessInstance() throws Exception {
//        String processId = "{http://www.jboss.org/bpel/examples}HelloGoodbye-1";
//        String encodedId = processId; // FIXME 
//        //String encodedId = ModelAdaptor.encodeId(processId);
//        String status = "COMPLETED";
//
//        Long starttime = new java.util.Date(103, 1, 1).getTime();
//        Long endtime = new java.util.Date().getTime();
//
//        String search_url = history_search_url + encodedId + "/instances?status=" + status
//                            + "&starttime="+starttime + "&endtime=" + endtime;
//
//        System.out.println("-----------------------------");
//        System.out.println("Get historic process instances from process definition of : " + processId );
//
//        String result = getDataFromService(search_url, "GET");
//        System.out.println("-----------------------------");
//        System.out.println("Marshall the Json data into java class.");
//
////        Gson gson = GsonFactory.createInstance();
////        HistoryProcessInstanceRefWrapper wrapper = gson.fromJson(result, HistoryProcessInstanceRefWrapper.class);
////
////        for (HistoryProcessInstanceRef ref: wrapper.getDefinitions()) {
////          System.out.println("historic instance id is: " + ref.getProcessInstanceId() + " definition key is: "
////                                            + URLDecoder.decode(ref.getProcessDefinitionId(), "UTF-8"));
////        }
//
//    }
//    
//    private void doLogin() {
//    	//getDataFromService(loginUrl, "POST");
//    	PostMethod.postMethodHtml(loginUrl);  
//    	
//    }
//
//    private String getDataFromService(String urlpath, String method) throws Exception {
//       if ("GET".equalsIgnoreCase(method)) {
//    	   GetMethod.getMethod(urlpath);
//       } else if ("POST".equalsIgnoreCase(method)) {
//    	   PostMethod.postMethod(urlpath);
//       } 
//
//       /*
//       if (username != null && password != null) {
//
//           try {
//               httpclient.executeMethod(theMethod);
//           } catch (IOException e) {
//               e.printStackTrace();
//           } finally {
//               theMethod.releaseConnection();
//           }
//         PostMethod authMethod = new PostMethod(authentication_url);
//         NameValuePair[] data = {new NameValuePair("j_username", username), new NameValuePair("j_password", password)};
//         authMethod.setRequestBody(data);
//           try {
//               httpclient.executeMethod(authMethod);
//           } catch (IOException e) {
//               e.printStackTrace();
//           } finally {
//               authMethod.releaseConnection();
//           }
//       }
//
//       try {
//         httpclient.executeMethod(theMethod);
//         sb.append(theMethod.getResponseBodyAsString());
//         System.out.println("JSon Result: => " + sb.toString());
//       return sb.toString();
//
//       }catch (Exception e) {
//          throw e;
//       }finally {
//         theMethod.releaseConnection();
//       }
//        */
//       return ""; 
//    }
//
////    public void testInstance()
////    {
////    
////    	HashMap processes=new java.util.HashMap();
////    
////    	HTTPBuilder http = new HTTPBuilder(“http://localhost:8080”);
////    	http.getAuth().basic(username,password); 
////    	http.request( Method.GET, ContentType.JSON) { req ->
////        uri.path = '/gwt-console-server/rs/process/definitions'
////        req.getParams().setParameter("http.connection.timeout",new Integer(5000));
////        req.getParams().setParameter("http.socket.timeout",new Integer(5000));
////        response.success = { resp, json ->
////        System.out.println(json);
////        processes.put(it,json.definitions)
////        }
////    }
//    
//    public static void main(String[] args) throws Exception {
//
//       CopyOfManagementClient client = new CopyOfManagementClient("admin", "admin");
//       
//       client.doLogin();
//       //client.showAllDeployments();
//       //client.showAllDefinitions();
//       //client.getActiveProcessInstance();
//       //client.getHistoricProcessInstance();
//    }
//
//
//}
//
