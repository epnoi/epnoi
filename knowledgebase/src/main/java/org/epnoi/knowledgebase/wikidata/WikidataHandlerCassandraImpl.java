package org.epnoi.knowledgebase.wikidata;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class WikidataHandlerCassandraImpl implements WikidataHandler {
    private static final Logger logger = Logger.getLogger(WikidataHandlerCassandraImpl.class
            .getName());
    private WikidataStemmer stemmer = new WikidataStemmer();
    private CassandraWikidataView wikidataView;

    // --------------------------------------------------------------------------------------------------

    public WikidataHandlerCassandraImpl(CassandraWikidataView wikidataView) {
        this.wikidataView = wikidataView;
    }

		/*
        public WikidataView getWikidataView() {
			return this.wikidataView;
		}
*/
    // --------------------------------------------------------------------------------------------------


    // --------------------------------------------------------------------------------------------------
    @Override
    public synchronized String  stem(String term) {
        return this.stemmer.stem(term);
    }

    // --------------------------------------------------------------------------------------------------

    @Override
    public Set<String> getRelated(String source, String type) {
        Set<String> related = new HashSet<>();
        try {
            related = this.wikidataView.getRelated(source, type);
        } catch (Exception e) {
            logger.info("Getting the related with " + source + " with the relation " + type + "raised an exception :" + e.getMessage());
        }
        return related;
    }

    @Override
    public String toString() {
        return "WikidataHandlerImpl [wikidataView=" + wikidataView + "]";
    }

    // --------------------------------------------------------------------------------------------------

}
