package org.epnoi.uia.knowledgebase.wikidata;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WikidataCassandraHandler  implements WikidataHandler {
		private WikidataStemmer stemmer = new WikidataStemmer();
		private CassandraWikidataView wikidataView;

		// --------------------------------------------------------------------------------------------------

		public WikidataCassandraHandler(CassandraWikidataView wikidataView) {
			this.wikidataView = wikidataView;
		}

		/*
		public WikidataView getWikidataView() {
			return this.wikidataView;
		}
*/
		// --------------------------------------------------------------------------------------------------

		@Override
		public Set<String> getRelated(String sourceLabel, String type) {

			Set<String> targetLabels = new HashSet<String>();

			//Map<String, Set<String>> consideredRelations = this.wikidataView.getRelations().get(type);

			// Firstly we retrieve the IRIs of the source label
			Set<String> sourceIRIs = this.wikidataView.getIRIsOfLabel(sourceLabel);
			System.out.println("Inital sourceIRIs obtained from the label" +sourceIRIs);
			if (sourceIRIs != null) {

				for (String sourceIRI : sourceIRIs) {
					// System.out.println("sourceIRI " + sourceIRI);
					Set<String> targetIRIs = this.wikidataView.getIRIRelatedIRIs(type, sourceIRI);
					// System.out.println(" ("+sourceIRI+") targetIRIs " +
					// targetIRIs);
					if (targetIRIs != null) {
						for (String targetIRI : targetIRIs) {
							// System.out.println(" trying > "+ targetIRI);
							// // .getLabelsReverseDictionary().get(
							// targetIRI));
							if (targetIRI != null) {
								if (this.wikidataView.getLabelsOfIRI(targetIRI) != null) {

									for (String destinationTarget : this.wikidataView.getLabelsOfIRI(targetIRI)) {
										targetLabels.add(destinationTarget);
									}
								}

							}
						}
					}
				}
			}
			return targetLabels;
		}

		// --------------------------------------------------------------------------------------------------
		@Override
		public String stem(String term) {
			return this.stemmer.stem(term);
		}

		// --------------------------------------------------------------------------------------------------

		@Override
		public String toString() {
			return "WikidataHandlerImpl [wikidataView=" + wikidataView + "]";
		}

		// --------------------------------------------------------------------------------------------------

	}
