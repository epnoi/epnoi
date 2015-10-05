package org.epnoi.knowledgebase.wikidata;

import java.util.Set;

public class WikidataHandlerCassandraImpl  implements WikidataHandler {
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
		public String stem(String term) {
			return this.stemmer.stem(term);
		}

		// --------------------------------------------------------------------------------------------------

		@Override
		public Set<String> getRelated(String source, String type) {
			
			return this.wikidataView.getRelated(source, type);
		}

		@Override
		public String toString() {
			return "WikidataHandlerImpl [wikidataView=" + wikidataView + "]";
		}

		// --------------------------------------------------------------------------------------------------

	}
