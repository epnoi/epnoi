package org.epnoi.learner.client.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.epnoi.model.Relation;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by rgonzalez on 25/11/15.
 */
public class LearnerClient {
    public static void main(String[] args) {
        ClientConfig clientConfig = new DefaultClientConfig();
        //  clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        clientConfig.getClasses().add(JacksonJsonProvider.class);
        Client client = Client.create(clientConfig);

        URI serviceURI = UriBuilder.fromUri("http://localhost:8082").build();
        WebResource webResource = client.resource(serviceURI);

        _testRelations(webResource);
        _testConfigurations(webResource);
        _testTrainerConfigurations(webResource);

    }

    private static void _testRelations(WebResource webResource) {
        WebResource wr = webResource.path("/learner/domain/relations").queryParam("uri", "estaEsLaUri");

        ClientResponse clientResponse = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        List<Relation> list = clientResponse.getEntity(new GenericType<List<Relation>>() {
        });
        System.out.println("---> " + list);
    }

    private static void _testConfigurations(WebResource webResource) {
        System.out.println("EntrA");
        WebResource wr = webResource.path("/learner/configuration");

        ClientResponse clientResponse = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
     //   System.out.println(clientResponse.getEntity(Object.class));
//       Map map = clientResponse.getEntity((new GenericType<Map<String, String>>(){}));
       /*
        Map<String, Map<String,String>> map = clientResponse.getEntity(new GenericType<Map<String, Map<String,String>>>() {
        });
        */

        Map<String,String> map = clientResponse.getEntity(new GenericType<Map<String,String>>() {
        });
        System.out.println("---> " + map);
    }

    private static void _testTrainerConfigurations(WebResource webResource) {
        WebResource wr = webResource.path("/learner/trainer/configuration");

        ClientResponse clientResponse = wr.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        //System.out.println(clientResponse.getEntity(Object.class));
        Map<String, Map<String,String>> map = clientResponse.getEntity(new GenericType<Map<String, Map<String,String>>>() {
        });
        System.out.println("---> " + map);
    }

}
