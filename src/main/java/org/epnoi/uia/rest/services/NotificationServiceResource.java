package org.epnoi.uia.rest.services;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.atmosphere.jersey.SuspendResponse;
import org.epnoi.uia.informationsources.monitors.RSSInformationSourceMonitor;
import org.epnoi.uia.parameterization.RSSHarvesterParameters;

@Path("/notificationsService")
public class NotificationServiceResource extends UIAService {
	private EventListener listener;
	
	@Context
	private BroadcasterFactory broadcasterFactory;

	private EventGenerator generator;
	private Broadcaster broadcaster;
	
	private RSSInformationSourceMonitor monitor;

	private Broadcaster getBroadcaster(String informationSourceSubscriptionURI) {

		
		Broadcaster broadcaster= broadcasterFactory.lookup(DefaultBroadcaster.class, informationSourceSubscriptionURI, true);
		monitor.add(informationSourceSubscriptionURI, broadcaster);
		
		return broadcaster;
		
	}

	@PostConstruct
	public void init() {
		
		System.out.println("Initializing the notifications service!!!");
		 this.core = this.getUIACore();
		//this.broadcasterFactory = BroadcasterFactory.getDefault();
		//this.broadcaster=broadcasterFactory.lookup(DefaultBroadcaster.class,
			//	"proofTopic", true);
	//	BroadcasterConfig config = getBroadcaster().getBroadcasterConfig();
		//config.addFilter(new InformationSourceSubscriptionsBroadcastFilter());
		
		
		//BroadcasterConfig config = getBroadcaster().getBroadcasterConfig();
		//config.addFilter(new InformationSourceSubscriptionsBroadcastFilter());
		this.monitor= new RSSInformationSourceMonitor(this.getUIACore(), new RSSHarvesterParameters());
		this.monitor.start();
	
		generator = new EventGenerator();
		
	}

	@GET
	
	@Produces(MediaType.APPLICATION_JSON)
	@Path("subscribe")
 	
	public SuspendResponse<String> connect(
			@Context AtmosphereResource resource, @QueryParam("URI") String URI) {
		System.out.println("-uri--------> " + URI);
		//generator.start(getBroadcaster(), 5000);
		return new SuspendResponse.SuspendResponseBuilder<String>()
				.broadcaster(getBroadcaster(URI)).outputComments(true).build();
	}
/*
	@GET
	@Path("start")
	public Response start() {
		System.out.println("Starting EventGenerator");
		generator.start(getBroadcaster(), 1000);
		return Response.ok().build();
	}

	@GET
	@Path("stop")
	public Response stop() {
		System.out.println("Stopping EventGenerator");
		generator.stop();
		return Response.ok().build();
	}
*/
}
