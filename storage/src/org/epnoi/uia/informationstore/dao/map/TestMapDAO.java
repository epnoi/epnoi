package org.epnoi.uia.informationstore.dao.map;

import java.util.regex.Pattern;

import org.epnoi.model.AnnotatedContentHelper;
import org.epnoi.model.Content;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.informationstore.Selector;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.epnoi.uia.parameterization.MapInformationStoreParameters;

import com.rits.cloning.Cloner;

import gate.Document;
import gate.Factory;

public class TestMapDAO extends MapDAO {

	private static final Pattern pattern = Pattern.compile("\\[[^\\]]*\\]");
	private static Cloner cloner = new Cloner();

	@Override
	public Content<Object> getAnnotatedContent(Selector selector) {
		Content<Object> annotatedContent = (Content<Object>) map.get(selector
				.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI));

		// System.out.println(">> "+map.keySet());
		// System.out.println("_---> "+map.get("file:///epnoi/epnoideployment/firstReviewResources/CGCorpus/A01_S01_A_Powell_Optimization_Approach__for_Example-Based_Skinning__CORPUS__v3.xml/text/xml/gate"));

		if (annotatedContent != null) {

			return annotatedContent;

		}
		return null;
	}

	@Override
	public void setAnnotatedContent(Selector selector,
			Content<Object> annotatedContent) {

		System.out.println("puting >"
				+ selector.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI));

		Content<Object> clonedAnnotatedContent = cloner
				.deepClone(annotatedContent);
	//	System.out.println("==========================================================================================================================");
	//	System.out.println("==========================================================================================================================");
		//System.out.println("......:> " + clonedAnnotatedContent.toString());
		map.put(selector.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI),
				clonedAnnotatedContent);
		// System.out.println(">> "+map.keySet());
		database.commit();

	}

	public static void main(String[] args) {

		Core core = CoreUtility.getUIACore();

		TestMapDAO testMapDAO = new TestMapDAO();
		MapInformationStoreParameters parameters = new MapInformationStoreParameters();
		parameters.setPath("/epnoi/epnoideployment/mapDB/epnoi/epnoi");

		testMapDAO.init(parameters);



		String annotatedContentURI = "http://testAnnotated"
				+ AnnotatedContentHelper.CONTENT_TYPE_TEXT_XML_GATE;

		int TEST_CONTENT_LENGTH = 10;

		String content = "This is a simple test. ";
		for (int i = 0; i < TEST_CONTENT_LENGTH; i++) {
			content += content;
		}
		System.out.println("-> " + content);

		Document annotatedContentDocument=null;
		try {
			annotatedContentDocument = core.getNLPHandler()
					.process(content);
		} catch (EpnoiResourceAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Once it has been serialized, we must free the associated GATE
		// resources

		long currentTime = System.currentTimeMillis();
		Selector selector = new Selector();
		selector.setProperty(SelectorHelper.URI, "http://testURI");
		selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
				annotatedContentURI);
		selector.setProperty(SelectorHelper.TYPE,
				RDFHelper.WIKIPEDIA_PAGE_CLASS);

		testMapDAO.setAnnotatedContent(selector,
				new org.epnoi.model.Content<Object>(annotatedContentDocument,
						AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));

		Factory.deleteResource(annotatedContentDocument);
		System.out.println("Writting it took"
				+ Math.abs(System.currentTimeMillis() - currentTime));
		currentTime = System.currentTimeMillis();
		Content<Object> annotatedContentAux = (testMapDAO
				.getAnnotatedContent(selector));

		Document document = (Document) (annotatedContentAux.getContent());
		System.out.println("--->" + document.getAnnotations().size());
		System.out.println("Reading it took"
				+ Math.abs(System.currentTimeMillis() - currentTime));

	}
}
