package org.epnoi.learner.relations.corpus.parallel;


import gate.Document;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.commons.GateUtils;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
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

        ClientConfig config = new ClientConfig();

        Client client = ClientBuilder.newClient(config);

        Document document = null;
        try {

            URI testServiceURI = UriBuilder.fromUri(this.uiaPath).build();
            WebTarget service = client.target(testServiceURI);

            String content = service.path(knowledgeBasePath).queryParam("uri", uri)
                    .queryParam("type", RDFHelper.WIKIPEDIA_PAGE_CLASS).request().accept(javax.ws.rs.core.MediaType.APPLICATION_XML)
                    .get(String.class);

            document = GateUtils.deserializeGATEDocument(content);
        } catch (Exception e) {
            e.printStackTrace();


        }

        return document;
    }

    // --------------------------------------------------------------------------------------------------------------------

}
