package org.epnoi.learner.relations.corpus.parallel;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import gate.Document;
import org.epnoi.model.clients.thrift.AnnotatedContentServiceClient;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.commons.GateUtils;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class UriToAnnotatedDocumentFlatMapper {

   private String uiaPath;
    private final String knowledgeBasePath = "/uia/annotatedcontent";

    public UriToAnnotatedDocumentFlatMapper(String uiaPath) {
        this.uiaPath=uiaPath;
    }

    public Iterable<Document> call(String uri) throws Exception {
        List<Document> sectionsAnnotatedContent = new ArrayList<>();

        Document annotatedContent = _obtainAnnotatedContent(uri);

        if (annotatedContent != null) {
            sectionsAnnotatedContent.add(annotatedContent);
        }
        return sectionsAnnotatedContent;
    }

    // --------------------------------------------------------------------------------------------------------------------

    private Document _obtainAnnotatedContent(String uri) {
/*
        ClientConfig config = new DefaultClientConfig();

        Client client = Client.create(config);

        Document document = null;
        try {

            URI testServiceURI = UriBuilder.fromUri(this.uiaPath).build();
            WebResource service = client.resource(this.uiaPath);

            String content = service.path(knowledgeBasePath).queryParam("uri", uri)
                    .queryParam("type", RDFHelper.WIKIPEDIA_PAGE_CLASS).type(javax.ws.rs.core.MediaType.APPLICATION_XML)
                    .get(String.class);


            document = GateUtils.deserializeGATEDocument(content);
        } catch (Exception e) {
            e.printStackTrace();


        }
        */
        AnnotatedContentServiceClient uiaService = new AnnotatedContentServiceClient();
        org.epnoi.model.Content<Object> resource = null;
        try {
            uiaService.init("localhost", 8585);

            resource = uiaService.getAnnotatedDocument(uri, RDFHelper.WIKIPEDIA_PAGE_CLASS);
        } catch (Exception e) {
            e.printStackTrace();

        }

/*
        System.out.println("(RESOURCE)====> " + resource);
        System.out.println("<--");
*/
        return (Document)resource.getContent();
    }

    // --------------------------------------------------------------------------------------------------------------------

}
