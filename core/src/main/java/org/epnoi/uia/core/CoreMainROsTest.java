package org.epnoi.uia.core;

import org.epnoi.model.Context;
import org.epnoi.model.ResearchObject;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.DublinCoreRDFHelper;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchResult;
import org.epnoi.model.search.SelectExpression;

public class CoreMainROsTest {

	public static String TEST_USER_URI = "http://www.epnoi.org/users/testUser";

	public static void main(String[] args) {

		Core core = CoreUtility.getUIACore();
		/*
		 * User annotatedUser = new User();
		 * annotatedUser.setURI("http://annotatedUser");
		 * core.getInformationAccess().put(annotatedUser, new Context());
		 * core.getAnnotationHandler().label(annotatedUser.getURI(), "math");
		 * 
		 * List<String> userURIs = core.getAnnotationHandler().getLabeledAs(
		 * "math", UserRDFHelper.USER_CLASS);
		 * System.out.println("USER_________________________________" +
		 * userURIs.size());
		 */
		ResearchObject researchObject = new ResearchObject();
		researchObject.setUri("http://testResearchObject");
		researchObject.getAggregatedResources().add("http://resourceA");
		researchObject.getAggregatedResources().add("http://resourceB");
		researchObject.getDcProperties().addPropertyValue(
				DublinCoreRDFHelper.TITLE_PROPERTY,
				"First RO, loquetienesquebuscar");
		researchObject.getDcProperties().addPropertyValue(
				DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
				"Description of the first RO");
		researchObject.getDcProperties().addPropertyValue(
				DublinCoreRDFHelper.DATE_PROPERTY, "2005-02-28T00:00:00Z");

		Context context = new Context();
		context.getParameters().put(Context.INFORMATION_SOURCE_NAME,
				"coreMainTest");

		core.getInformationHandler().put(researchObject, context);

		Resource readedRO = core.getInformationHandler().get(
				"http://testResearchObject", RDFHelper.RESEARCH_OBJECT_CLASS);
		System.out.println("Readed RO >" + readedRO);

		SelectExpression selectExpression = new SelectExpression();

		selectExpression.setSolrExpression("content:loquetienesquebuscar");

		SearchContext searchContext = new SearchContext();
		searchContext.getFacets().add("date");
		// searchContext.getFilterQueries().add("date:\"2013-12-06T17:54:21Z\"");
		// searchContext.getFilterQueries().add("date:\"2014-03-04T17:56:05Z\"");

		/*
		 * REMOVAL
		 * core.getInformationAccess().remove("http://testResearchObject",
		 * RDFHelper.RESEARCH_OBJECT_CLASS);
		 */
		SearchResult searchResult = core.getSearchHandler().search(
				selectExpression, searchContext);
		System.out.println("Result ---> " + searchResult);

		researchObject.getAggregatedResources().add("http://newResource");
		researchObject.getAggregatedResources().remove("http://resourceB");

		core.getInformationHandler().update(researchObject);

		Resource updatedRO = core.getInformationHandler().get(
				"http://testResearchObject", RDFHelper.RESEARCH_OBJECT_CLASS);

		System.out.println("Readed updated RO >" + updatedRO);

		core.getInformationHandler().remove(researchObject);

	}

}
