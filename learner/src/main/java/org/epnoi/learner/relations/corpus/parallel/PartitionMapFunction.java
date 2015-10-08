package org.epnoi.learner.relations.corpus.parallel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.epnoi.model.Selector;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.SelectorHelper;

import gate.Document;

public class PartitionMapFunction implements FlatMapFunction<Iterator<String>, Iterator<Document>> {

	Core core;

	@Override
	public Iterable<Iterator<Document>> call(Iterator<String> URIs) throws Exception {
		_initialization();
		while (URIs.hasNext()) {
			String uri = URIs.next();
			WikipediaPage page = (WikipediaPage) core.getInformationHandler().get(uri, RDFHelper.WIKIPEDIA_PAGE_CLASS);
			List<String> sectionsAnnotatedContentURIs = _obtainSectionsAnnotatedContentURIs(page);
			List<Document> sectionsAnnotatedContent = _obtainSectionsAnnotatedContent(sectionsAnnotatedContentURIs);
		}
		return null;
	}

	private List<Document> _obtainSectionsAnnotatedContent(List<String> sectionsAnnotatedContentURIs) {
		
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.TYPE, RDFHelper.WIKIPEDIA_PAGE_CLASS);
		
		
		List<Document> sectionsAnnotatedContent = new ArrayList<Document>();
		String sectionAnnotatedContentURI = _extractURI(URI, section, annotationType)
		selector.setProperty(SelectorHelper.URI, );
				core.getInformationHandler().getAnnotatedContent(selector);
		return sectionsAnnotatedContent;
	}

	private void _initialization() {
		this.core = CoreUtility.getUIACore();

	}

	private List<String> _obtainSectionsAnnotatedContentURIs(WikipediaPage page) {
		// TODO Auto-generated method stub
		return null;
	}

	private String _extractURI(String URI, String section, String annotationType) {

		String cleanedSection = section.replaceAll("\\s+$", "").replaceAll("\\s+", "_");

		return URI + "/" + cleanedSection + "/" + annotationType;
	}

}
