package org.jboss.samples.rs.webservices;

import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/SampleWeb")
public class HelloWorldResource {

	@GET()
	@Produces("text/plain")
	public String sayHello() {
	    return "Hello World!";
	}
}
