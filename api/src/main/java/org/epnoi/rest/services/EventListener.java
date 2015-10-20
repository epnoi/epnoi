package org.epnoi.rest.services;

import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.websocket.WebSocketEventListener.WebSocketEvent.TYPE;
import org.atmosphere.websocket.WebSocketEventListenerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventListener extends WebSocketEventListenerAdapter {

	private final Logger LOG = LoggerFactory.getLogger(EventListener.class);

	@Override
	public void onMessage(WebSocketEvent event) {
		LOG.info("WebSocket message received from client");
		if (event.type() != TYPE.MESSAGE) return;
		
		LOG.info("New bounds {} for resource {}"+ event.webSocket().resource().hashCode());
		AtmosphereRequest request = event.webSocket().resource().getRequest();
		
	}

}
