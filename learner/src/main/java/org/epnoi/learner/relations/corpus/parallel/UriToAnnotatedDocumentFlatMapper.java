package org.epnoi.learner.relations.corpus.parallel;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import gate.Document;
import gate.corpora.DocumentImpl;
import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.model.clients.thrift.AnnotatedContentServiceClient;
import org.epnoi.model.clients.thrift.KnowledgeBaseServiceClient;
import org.epnoi.model.commons.Parameters;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.commons.GateUtils;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UriToAnnotatedDocumentFlatMapper {

    private String uiaPath;
    private final String knowledgeBasePath = "/uia/annotatedcontent";
    private Parameters parameters;

    public UriToAnnotatedDocumentFlatMapper(Parameters parameters) {
        this.parameters = parameters;
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
        System.out.println("uri >"+uri);
        Integer thriftPort = (Integer) parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.THRIFT_PORT);
        AnnotatedContentServiceClient uiaService = new AnnotatedContentServiceClient();
        org.epnoi.model.Content<Object> resource = null;
        try {
            uiaService.init("localhost", thriftPort);

            resource = uiaService.getAnnotatedDocument(uri+"/object/gate", RDFHelper.WIKIPEDIA_PAGE_CLASS);
           // System.out.println("resource > "+((DocumentImpl)resource.getContent()).getContent());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            uiaService.close();
        }

        return (Document) resource.getContent();
    }

    // --------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {

        String uri = "http://en.wikipedia.org/wiki/Autism/first/object/gate";
        Long start = System.currentTimeMillis();

        ClientConfig config = new DefaultClientConfig();


        Client client = Client.create(config);
        String uiaPath = "http://localhost:8080/epnoi/rest";
        final String knowledgeBasePath = "/uia/annotatedcontent";
        Document document = null;
        try {

            URI testServiceURI = UriBuilder.fromUri(uiaPath).build();
            WebResource service = client.resource(uiaPath);

            String content = service.path(knowledgeBasePath).queryParam("uri", uri)
                    .queryParam("type", RDFHelper.WIKIPEDIA_PAGE_CLASS).type(javax.ws.rs.core.MediaType.APPLICATION_XML)
                    .get(String.class);


            document = GateUtils.deserializeGATEDocument(content);
        } catch (Exception e) {
            e.printStackTrace();


        }
        //   System.out.println("000> "+resource);
        Long end = System.currentTimeMillis();
        System.out.println("It took " + (start - end) + " the rest service invocation ");

        start = System.currentTimeMillis();
        AnnotatedContentServiceClient uiaService = new AnnotatedContentServiceClient();
        org.epnoi.model.Content<Object> resource = null;
        try {
            uiaService.init("localhost", 8585);

            resource = uiaService.getAnnotatedDocument(uri, RDFHelper.WIKIPEDIA_PAGE_CLASS);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            uiaService.close();
        }

        //  System.out.println("000> "+document);
        end = System.currentTimeMillis();
        System.out.println("It took " + (start - end) + " the thrift service invocation ");


        KnowledgeBaseServiceClient knowledgeBaseServiceClient = new KnowledgeBaseServiceClient();

        try {
            knowledgeBaseServiceClient.init("localhost", 8585);
            System.out.println("It has been properly initialized!");
            System.out.println("Related--------------------------------------");
            List<String> sources = Arrays.asList("cat", "house", "dogs");
            System.out.println("This are the related " + knowledgeBaseServiceClient.getRelated(sources, "hypernymy"));
            // System.out.println("Stem--------------------------------------");
            //  System.out.println("These are  the stemmed "+knowledgeBaseServiceClient.getRelated(sources, RelationHelper.HYPERNYMY));
        } catch (Exception e) {
            e.printStackTrace();

        }
        // knowledgeBaseServiceClient = new KnowledgeBaseServiceClient();
        try {
            //  knowledgeBaseServiceClient.init("localhost", 8585);
            System.out.println("It has been properly initialized!");
            System.out.println("Stem--------------------------------------");
            List<String> sources = Arrays.asList("cat", "houses", "dogs");
            System.out.println("This are the related " + knowledgeBaseServiceClient.stem(sources));


        } catch (Exception e) {
            e.printStackTrace();

        }
        knowledgeBaseServiceClient.close();
    }


}
