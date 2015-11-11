package org.epnoi.uia.core;

import org.epnoi.model.Context;
import org.epnoi.model.WikipediaPage;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;

import java.util.Arrays;
import java.util.HashMap;

public class CoreWikipediaPageTest {

	public static String TEST_USER_URI = "http://www.epnoi.org/users/testUser";

	public static void main(String[] args) {
		System.out.println("Starting the test!" + CoreWikipediaPageTest.class);

		String TEST_URI = "http://externalresourceuri";
		Core core = CoreUtility.getUIACore();

		WikipediaPage wikipediaPage = new WikipediaPage();
		wikipediaPage.setUri("http://externalresourceuri");
		wikipediaPage.setTerm("Proof Term");
		wikipediaPage.setTermDefinition("Proof Term is whatever bla bla bla");
		wikipediaPage.setSections(Arrays.asList("first", "middle section",
				"references"));
		wikipediaPage.setSectionsContent(new HashMap<String, String>());
		wikipediaPage.getSectionsContent().put("first",
				"This is the content of the first section");
		wikipediaPage.getSectionsContent().put("middle section",
				"This is the content of the middle section");
		wikipediaPage.getSectionsContent().put("references",
				"This is the content for the references");

		Context context = new Context();
		context.getParameters().put(Context.INFORMATION_SOURCE_NAME,
				"coreMainTest");

		core.getInformationHandler().put(wikipediaPage, context);

		System.out
				.println("Testing the retrieval! ----------------------------------");

		System.out.println("---> " + core.getInformationHandler().get(TEST_URI));
		/*
		 * Resource readedRO = core.getInformationAccess().get(
		 * "http://testResearchObject", RDFHelper.RESEARCH_OBJECT_CLASS);
		 * System.out.println("Readed RO >" + readedRO);
		 */

		core.getInformationHandler().remove(TEST_URI,
				RDFHelper.WIKIPEDIA_PAGE_CLASS);
		
		System.out
		.println("Testing the removal! ----------------------------------");

		System.out.println("---> " + core.getInformationHandler().get(TEST_URI, RDFHelper.WIKIPEDIA_PAGE_CLASS));

	}

}
