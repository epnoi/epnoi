package org.epnoi.harvester.legacy.wikipedia;

import gate.Document;
import org.epnoi.harvester.legacy.wikipedia.parse.edu.jhu.nlp.wikipedia.WikiPage;
import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Context;
import org.epnoi.model.Selector;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;

import java.util.List;
import java.util.logging.Logger;

// -------------------------------------------------------------------------------------------------------------------


public class WikipediaPageIntroductionTask implements Runnable {
    private static final Logger logger = Logger.getLogger(WikipediaPageIntroductionTask.class.getName());
    private Core core;
    private WikiPage page;
    private Context context;
    private boolean stored;

    // -------------------------------------------------------------------------------------------------------------------

    public WikipediaPageIntroductionTask(Core core, WikiPage page, Context context, boolean stored) {
        this.core = core;
        this.page = page;
        this.context = context;
        this.stored = stored;
    }

    // -------------------------------------------------------------------------------------------------------------------

    @Override
    public void run() {
        try {
            _putWikipediaPage(page, context);
        } catch (java.lang.IndexOutOfBoundsException e) {
            System.out.println(page.getWikiText());
            // System.out.println(page.getText());
            //System.exit(0);
        }
    }

    // -------------------------------------------------------------------------------------------------------------------

    public void _putWikipediaPageAnnotatedContent(WikipediaPage wikipediaPage) {
        List<String> sections = wikipediaPage.getSections();
        if (sections.size() > 1) {
            for (int i = sections.size() - 1; i >= 0; i--) {

                String sectionContent = wikipediaPage.getSectionsContent().get(sections.get(i));

                String annotatedContentURI = _extractURI(wikipediaPage.getUri(), sections.get(i), AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);
                _putWikipediaPageSectionAnnnotatedContent(wikipediaPage, sectionContent, annotatedContentURI);
            }
        }

    }

    // -------------------------------------------------------------------------------------------------------------------

    private String _extractURI(String URI, String section, String annotationType) {

        String cleanedSection = section.replaceAll("\\s+$", "").replaceAll("\\s+", "_");

        return URI + "/" + cleanedSection + "/" + annotationType;
    }

    // -------------------------------------------------------------------------------------------------------------------

    private void _putWikipediaPageSectionAnnnotatedContent(WikipediaPage wikipediaPage, String sectionContent,
                                                           String annotatedContentURI) {
        // First we obtain the linguistic annotation of the content of the
        // section
        Document sectionAnnotatedContent = null;
        try {
            sectionAnnotatedContent = this.core.getNLPHandler().process(sectionContent);
        } catch (EpnoiResourceAccessException e) {

            e.printStackTrace();
        }
        if (sectionAnnotatedContent != null) {
            // Then we introduce it in the UIA
            // We create the selector
            Selector selector = new Selector();
            selector.setProperty(SelectorHelper.URI, wikipediaPage.getUri());
            selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, annotatedContentURI);

            selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIPEDIA_PAGE_CLASS);

            // Then we store it
            core.getInformationHandler().setAnnotatedContent(selector, new org.epnoi.model.Content<Object>(
                    sectionAnnotatedContent, AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));
        }
    }

    // -------------------------------------------------------------------------------------------------------------------


    private void _putWikipediaPage(WikiPage page, Context context) {
        WikipediaPageParser parser = new WikipediaPageParser();

        WikipediaPage wikipediaPage = parser.parse(page);

        // If the wikipedia page is already stored, we delete it

        if (this.stored) {
            this.core.getInformationHandler().remove(wikipediaPage.getUri(), RDFHelper.WIKIPEDIA_PAGE_CLASS);

        }

        long currenttime = System.currentTimeMillis();
        _putWikipediaPageAnnotatedContent(wikipediaPage);
        core.getInformationHandler().put(wikipediaPage, context);
        long time = System.currentTimeMillis() - currenttime;
        //logger.info(wikipediaPage.getURI() + " took " + time + " to be annotated and stored");

    }

    // -------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "WikipediaPageIntroductionTask [page=" + page.getTitle() + ", stored=" + stored + "]";
    }

    // -------------------------------------------------------------------------------------------------------------------

}
