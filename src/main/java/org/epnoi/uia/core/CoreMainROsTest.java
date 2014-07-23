package org.epnoi.uia.core;

import org.epnoi.model.Context;
import org.epnoi.model.ResearchObject;
import org.epnoi.model.Resource;
import org.epnoi.uia.informationstore.dao.rdf.DublinCoreRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.search.SearchContext;
import org.epnoi.uia.search.SearchResult;
import org.epnoi.uia.search.select.SelectExpression;

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
		researchObject.setURI("http://testResearchObject");
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

		core.getInformationAccess().put(researchObject, context);

		Resource readedRO = core.getInformationAccess().get(
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

		core.getInformationAccess().update(researchObject);

		Resource updatedRO = core.getInformationAccess().get(
				"http://testResearchObject", RDFHelper.RESEARCH_OBJECT_CLASS);

		System.out.println("Readed updated RO >" + updatedRO);

		core.getInformationAccess().remove(researchObject);

	}

}
