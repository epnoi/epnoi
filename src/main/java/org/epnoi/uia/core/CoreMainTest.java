package org.epnoi.uia.core;

import gate.Document;
import gate.Factory;
import gate.Utils;
import gate.creole.ResourceInstantiationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.epnoi.model.Context;
import org.epnoi.model.Feed;
import org.epnoi.model.InformationSource;
import org.epnoi.model.InformationSourceSubscription;
import org.epnoi.model.Item;
import org.epnoi.model.Paper;
import org.epnoi.model.User;
import org.epnoi.uia.informationstore.dao.rdf.AnnotationRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.search.SearchContext;
import org.epnoi.uia.search.SearchResult;
import org.epnoi.uia.search.select.SelectExpression;

public class CoreMainTest {
	public static String TEST_USER_URI = "http://www.epnoi.org/users/testUser";

	public static void main(String[] args) {

		Core core = CoreUtility.getUIACore();

		core.getInformationAccess().remove(TEST_USER_URI,
				UserRDFHelper.USER_CLASS);

		User testUser = new User();

		testUser.setURI(TEST_USER_URI);
		testUser.setName("testUser");
		testUser.setDescription("User create for testing purposes");
		testUser.setPassword("1234");

		core.getAnnotationHandler().annotate(TEST_USER_URI,
				"http://whatever/topic");

		core.getAnnotationHandler().annotate(TEST_USER_URI,
				"http://whatever/elOtroTopic");

		System.out.println("Anotaciones up to now > "
				+ core.getAnnotationHandler().getAnnotations());

		for (String annotationURI : core.getAnnotationHandler()
				.getAnnotations()) {
			System.out
					.println("-------------------------------annnnnnnotation :> "
							+ core.getInformationAccess().get(annotationURI,
									AnnotationRDFHelper.ANNOTATION_CLASS));
		}

		/*
		 * 
		 * System.out.println("Annotations for "+TEST_USER_URI+
		 * core.getAnnotationHandler().getAnnotations(TEST_USER_URI));
		 * 
		 * core.getAnnotationHandler().removeAnnotation(TEST_USER_URI,
		 * "http://whatever/topic");
		 * 
		 * core.getAnnotationHandler().removeAnnotation(TEST_USER_URI,
		 * "http://whatever/elOtroTopic");
		 * 
		 * System.out.println("Once again, Annotations for "+TEST_USER_URI+
		 * core.getAnnotationHandler().getAnnotations(TEST_USER_URI));
		 */
		for (int i = 0; i < 10; i++)
			testUser.addKnowledgeObject("http://knowledgeObject" + i);

		testUser.addInformationSourceSubscription("http://www.epnoi.org/users/testUser/subscriptions/informationSources/highScalability");
		testUser.addInformationSourceSubscription("http://www.epnoi.org/users/testUser/subscriptions/informationSources/slashdot");

		testUser.addInformationSourceSubscription("http://www.epnoi.org/users/testUser/subscriptions/informationSources/randomInformationSource");

		core.getInformationAccess().put(testUser, new Context());

		_generateInformationSources(core, testUser);
		_generateFeeds(core);

		_testPapers(core);

		_testGateInitialization();

		System.out
				.println("Annotated as whatever"
						+ core.getAnnotationHandler().getAnnotatedAs(
								"http://whatever"));

		System.out
				.println("//////////////////////////////////////////////////////////////////////");
		System.out.println();
		System.out.println();
		System.out.println("Retrieving the user");

		User retrievedUser = (User) core.getInformationAccess().get(
				TEST_USER_URI, UserRDFHelper.USER_CLASS);
		System.out.println("This is the test user :" + retrievedUser);

		System.out
				.println("//////////////////////////////////////////////////////////////////////");

		System.out.println("Searching for word0");

		SelectExpression selectExpression = new SelectExpression();
		selectExpression.setSolrExpression("description:strangeword");
		SearchContext searchContext = new SearchContext();
		searchContext.getFacets().add("date");
		// searchContext.getFilterQueries().add("date:\"2013-12-06T17:54:21Z\"");
		// searchContext.getFilterQueries().add("date:\"2014-03-04T17:56:05Z\"");

		SearchResult searchResult = core.getSearchHandler().search(
				selectExpression, searchContext);
		System.out.println("#results ---> "
				+ searchResult.getResources().size());
		System.out.println("#facets ---> " + searchResult.getFacets().size());

		System.out.println("The results are " + searchResult.getResources());

	}

	private static void _generateInformationSources(Core core, User user) {

		InformationSource slashdotInformationSource = new InformationSource();
		slashdotInformationSource
				.setURI("http://www.epnoi.org/informationSources/slashdot");
		slashdotInformationSource.setName("slashdot");
		slashdotInformationSource
				.setURL("http://rss.slashdot.org/Slashdot/slashdot");
		slashdotInformationSource
				.setType(InformationSourceRDFHelper.RSS_INFORMATION_SOURCE_CLASS);
		slashdotInformationSource
				.setInformationUnitType(FeedRDFHelper.ITEM_CLASS);

		InformationSource highScalabilityInformationSource = new InformationSource();
		highScalabilityInformationSource
				.setURI("http://www.epnoi.org/informationSources/highScalability");
		highScalabilityInformationSource.setName("highScalability");
		highScalabilityInformationSource
				.setURL("http://feeds.feedburner.com/HighScalability");
		highScalabilityInformationSource
				.setType(InformationSourceRDFHelper.RSS_INFORMATION_SOURCE_CLASS);
		highScalabilityInformationSource
				.setInformationUnitType(FeedRDFHelper.ITEM_CLASS);

		InformationSource testInformationSource = new InformationSource();
		testInformationSource
				.setURI("http://www.epnoi.org/informationSources/testInformationSource");
		testInformationSource.setName("randomInformationSource");
		testInformationSource
				.setURL("http://www.epnoi.org/informationSources/randomInformationSource");
		testInformationSource
				.setType(InformationSourceRDFHelper.RSS_INFORMATION_SOURCE_CLASS);
		testInformationSource.setInformationUnitType(FeedRDFHelper.ITEM_CLASS);

		InformationSourceSubscription informationSourceSubscription = new InformationSourceSubscription();

		informationSourceSubscription
				.setURI("http://www.epnoi.org/users/testUser/subscriptions/informationSources/slashdot");
		informationSourceSubscription
				.setInformationSource("http://www.epnoi.org/informationSources/slashdot");
		informationSourceSubscription.setNumberOfItems(6);

		InformationSourceSubscription informationSourceSubscriptionHigh = new InformationSourceSubscription();

		informationSourceSubscriptionHigh
				.setURI("http://www.epnoi.org/users/testUser/subscriptions/informationSources/highScalability");
		informationSourceSubscriptionHigh
				.setInformationSource("http://www.epnoi.org/informationSources/highScalability");
		informationSourceSubscriptionHigh.setNumberOfItems(4);

		Context emptyContex = new Context();
		core.getInformationAccess().put(slashdotInformationSource, emptyContex);

		core.getInformationAccess().put(highScalabilityInformationSource,
				emptyContex);

		core.getInformationAccess().put(informationSourceSubscriptionHigh,
				emptyContex);

		core.getInformationAccess().put(informationSourceSubscription,
				emptyContex);

		/*
		 * RSSInformationSourceRandomGenerator generator = new
		 * RSSInformationSourceRandomGenerator(); generator.generate(core);
		 */

		/*
		 * System.out.println("information source handler");
		 * 
		 * for (String subscription : user.getInformationSourceSubscriptions())
		 * { core.getInformationSourcesHandler().retrieveNotifications(
		 * subscription); }
		 */

	}

	private static void _testPapers(Core core) {

		// System.out.println("This is the oai:arXiv.org:0705.3658 user >"+core.getInformationAccess().get("oai:arXiv.org:0705.3658",
		// RDFHelper.PAPER_CLASS));

		Paper paper = new Paper();
		paper.setURI("http://testPaper");

		String[] authors = { "A", "B" };
		paper.setAuthors(new ArrayList<String>(Arrays.asList(authors)));

		paper.setTitle("Test paper title");
		paper.setDescription("Description of the paper, strangeword");
		paper.setPubDate("Tue, 13 Dec 2013 22:22:16 GMT");

		core.getInformationAccess().put(paper, new Context());

	}

	private static void _testGateInitialization() {
		System.out
				.println("test gate initialization ---------------------------------------------------------------");
		try {
			Document document = (Document) Factory
					.createResource(
							"gate.corpora.DocumentImpl",
							Utils.featureMap(
									gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
									"My taylor is rich and my mum is in the kitchen",
									gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME,
									"text/plain"));

			System.out.println("Este es el document > " + document.toXml());

		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out
				.println("test gate initialization ---------------------------------------------------------------");
	}

	private static void _generateFeeds(Core core) {
		List<Feed> feeds = new ArrayList<Feed>();
		String feedURI = "http://feedA";
		Feed feedA = new Feed();
		Context context = new Context();

		feedA.setURI(feedURI);
		feedA.setTitle("Feed A title");
		feedA.setLink("http://feedA");
		feedA.setPubDate("Mon, 12 Dec 2013 22:22:16 GMT");
		feedA.setDescription("This is the description of feed A");

		for (int i = 0; i < 9; i++) {
			Item itemA = new Item();

			itemA.setURI("http://uriA" + i);
			itemA.setTitle("Iten titleA" + i);
			itemA.setLink("http://feedA" + i);
			itemA.setDescription("Description \" for item A " + i);
			itemA.setPubDate("Mon, 16 Dec 2013 22:22:0" + i + " GMT");
			itemA.setAuthor("authorA");
			/*
			 * List<String> kewords = Arrays.asList("mi" + i, "mama" + i, "me" +
			 * i, "mima" + i);
			 */
			String additionalContent = "My taylor is rich and my mother is in the kitchen, word"
					+ i;
			context.getElements().put(itemA.getURI(), additionalContent);
			feedA.addItem(itemA);
		}
		context.getParameters()
				.put(Context.INFORMATION_SOURCE_URI,
						"http://www.epnoi.org/informationSources/testInformationSource");
		core.getInformationAccess().put(feedA, context);
	}
}
