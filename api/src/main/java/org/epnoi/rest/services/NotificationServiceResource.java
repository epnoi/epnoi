package org.epnoi.rest.services;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.atmosphere.jersey.SuspendResponse;
import org.epnoi.sources.monitors.RSSInformationSourceMonitor;
import org.epnoi.sources.subscribers.EventBus;



@Path("/notificationsService")
public class NotificationServiceResource extends UIAService {
	private String EVENT_BUS_ATTRIBUTE = "EVENT_BUS";

	@Context
	private BroadcasterFactory broadcasterFactory;

	private EventGenerator generator;
	private Broadcaster broadcaster;

	private RSSInformationSourceMonitor monitor;

	private EventBus eventBus;

	// ------------------------------------------------------------------------------------------------

	private Broadcaster getBroadcaster(String informationSourceSubscriptionURI) {

		Broadcaster broadcaster = broadcasterFactory.lookup(
				DefaultBroadcaster.class, informationSourceSubscriptionURI,
				true);
		/*
		 * ELIMINADO SOLO PARA LAS PRUEBAS SIN MONITOR
		 * monitor.add(informationSourceSubscriptionURI, broadcaster);
		 */
		this.eventBus.subscribe(informationSourceSubscriptionURI, broadcaster);
		return broadcaster;

	}

	// ------------------------------------------------------------------------------------------------

	@PostConstruct
	public void init() {

		System.out.println("Initializing the notifications service!!!");
		this.core = this.getUIACore();
		/*
		 * ELIMINADO PARA HACER PRUEBAS SIN MONITOR this.monitor = new
		 * RSSInformationSourceMonitor(this.getUIACore(), new
		 * RSSHarvesterParameters()); this.monitor.start();
		 * 
		 * generator = new EventGenerator();
		 */
		this.eventBus = this.getEventBus();
	}

	protected EventBus getEventBus() {

		this.eventBus = (EventBus) this.context
				.getAttribute(EVENT_BUS_ATTRIBUTE);
		if (this.eventBus == null) {
			this.eventBus = new EventBus(this.getUIACore());
			this.context.setAttribute(EVENT_BUS_ATTRIBUTE, eventBus);

		}
		return this.eventBus;

	}

	// ------------------------------------------------------------------------------------------------

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("subscribe")
	public SuspendResponse<String> connect(
			@Context AtmosphereResource resource, @QueryParam("URI") String URI) {
		System.out.println("-uri--------> " + URI);
		// generator.start(getBroadcaster(), 5000);

		return new SuspendResponse.SuspendResponseBuilder<String>()
				.broadcaster(getBroadcaster(URI)).outputComments(true).build();
	}

	// ------------------------------------------------------------------------------------------------

}
