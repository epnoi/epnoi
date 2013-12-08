package org.epnoi.uia.rest.services;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.rest.services.response.UIA;

@Path("/UIA")
public class UIAResource extends UIAService {
	

	@Context
	ServletContext context;

	// --------------------------------------------------------------------------------
	// ----------------------------------------------------------

	@GET
	@Produces( { MediaType.APPLICATION_JSON })
	@Path("")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getUIA() {
		System.out.println("GET: UIA");


		
		UIA uia = new UIA();
		Core uiaCore = this.getUIACore();
		String timeStamp = Long.toString(System.currentTimeMillis());
		uia.setTimestamp(timeStamp);
		
		
			System.out.println("--->>>-->> "+uiaCore.getInformationStores());
			for (InformationStore informationStore: uiaCore.getInformationStores()){
				org.epnoi.uia.rest.services.response.InformationStore informationStoreResponse = new org.epnoi.uia.rest.services.response.InformationStore();
				System.out.println("----> "+informationStore);
				informationStoreResponse.setInformationStoreParameters(informationStore.getParameters());
				informationStoreResponse.setStatus(informationStore.test());
				uia.addInformationStores(informationStoreResponse);
				
			}
		
		if (uia != null) {
			return Response.ok(uia, MediaType.APPLICATION_JSON).build();
		}
		return Response.status(404).build();
	}

	
	
}
