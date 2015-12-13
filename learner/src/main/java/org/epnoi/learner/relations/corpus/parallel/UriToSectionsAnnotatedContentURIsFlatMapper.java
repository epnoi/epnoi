package org.epnoi.learner.relations.corpus.parallel;


import org.epnoi.learner.relations.corpus.RelationalSentencesCorpusCreationParameters;
import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.clients.thrift.UIAServiceClient;
import org.epnoi.model.commons.Parameters;
import org.epnoi.model.rdf.RDFHelper;

import java.util.ArrayList;
import java.util.List;

public class UriToSectionsAnnotatedContentURIsFlatMapper {

    private Parameters parameters;

    public UriToSectionsAnnotatedContentURIsFlatMapper(Parameters parameters) {
        this.parameters = parameters;
    }

    public Iterable<String> call(String uri) throws Exception {
        List<String> sectionsAnnotatedContentURIs = new ArrayList<>();
        try {

            WikipediaPage page = _retrieveWikipediaPage(uri);
            if (page != null) {
                sectionsAnnotatedContentURIs = _obtainSectionsAnnotatedContentURIs(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return sectionsAnnotatedContentURIs;
    }

    // --------------------------------------------------------------------------------------------------------------------

    private List<String> _obtainSectionsAnnotatedContentURIs(WikipediaPage page) {
        List<String> sectionUris = new ArrayList<String>();
        for (String section : page.getSections()) {
            sectionUris.add(_extractURI(page.getUri(), section, AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));
        }
        return sectionUris;
    }

    // --------------------------------------------------------------------------------------------------------------------

    private String _extractURI(String URI, String section, String annotationType) {

        String cleanedSection = section.replaceAll("\\s+$", "").replaceAll("\\s+", "_");

        return URI + "/" + cleanedSection + "/" + annotationType;
    }

    // --------------------------------------------------------------------------------------------------------------------

    private WikipediaPage _retrieveWikipediaPage(String wikipediaPageURI) {
        //	String wikipediaPagePath = "/uia/resources/bytype/wikipediapages/resource";

        //	ClientConfig config = new DefaultClientConfig();

//		Client client = Client.create(config);
//		URI serviceURI = UriBuilder.fromUri((String) parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.UIA_PATH)).build();
//		WebResource service = client.resource(serviceURI);
/*
        WikipediaPage retrievedWikipediaPage = service.path(wikipediaPagePath).queryParam("uri", wikipediaPageURI)
				.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(WikipediaPage.class);
		*/
        Integer thriftPort = (Integer)parameters.getParameterValue(RelationalSentencesCorpusCreationParameters.THRIFT_PORT);
        UIAServiceClient uiaService = new UIAServiceClient();
        org.epnoi.model.WikipediaPage wikipediaPage = null;
        try {
            uiaService.init("localhost", thriftPort);
            //System.out.println("It has been properly initialized!");
            wikipediaPage = (WikipediaPage) uiaService.getResource(wikipediaPageURI, RDFHelper.WIKIPEDIA_PAGE_CLASS);
        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            uiaService.close();
        }

        return wikipediaPage;
    }

    public static void main(String[] args) {
        UIAServiceClient uiaService = new UIAServiceClient();
        org.epnoi.model.WikipediaPage wikipediaPage = null;
        try {
            uiaService.init("localhost", 8585);
            //System.out.println("It has been properly initialized!");
            wikipediaPage = (WikipediaPage) uiaService.getResource("http://en.wikipedia.org/wiki/Ammon", RDFHelper.WIKIPEDIA_PAGE_CLASS);
        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            uiaService.close();
        }

        System.out.println("->> "+wikipediaPage);
    }
}
