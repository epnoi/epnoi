package org.epnoi.uia.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.epnoi.model.Context;
import org.epnoi.model.Feed;
import org.epnoi.model.InformationSource;
import org.epnoi.model.InformationSourceSubscription;
import org.epnoi.model.Item;
import org.epnoi.model.User;
import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.parameterization.ParametersModel;
import org.epnoi.uia.parameterization.ParametersModelReader;


public class CoreMainUser {
	// ---------------------------------------------------------------------------------
	private static final Logger logger = Logger.getLogger(CoreMainUser.class
			.getName());

	public static Core getUIACore() {

		long time = System.currentTimeMillis();
		Core core = new Core();
		ParametersModel parametersModel = _readParameters();
		core.init(parametersModel);

		long afterTime = System.currentTimeMillis();
		logger.info("It took " + (Long) (afterTime - time) / 1000.0
				+ "to load the UIA core");

		return core;

	}

	// ----------------------------------------------------------------------------------------

	public static ParametersModel _readParameters() {
		ParametersModel parametersModel = null;

		try {

			URL configFileURL = CoreMain.class.getResource("uiaCoreMainUser.xml");

			parametersModel = ParametersModelReader.read(configFileURL
					.getPath());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parametersModel;
	}

	// ----------------------------------------------------------------------------------------

	public static void main(String[] args) {

		Core core = getUIACore();

		User user = (User) core.getInformationAccess().get("http://userSara",
				UserRDFHelper.USER_CLASS);
		System.out.println("The readed user is " + user);

		/*
		 * User unknownUser = (User) core.getInformationAccess().get(
		 * "http://newUser", UserRDFHelper.USER_CLASS);
		 * System.out.println("The readed user is " + unknownUser);
		 * 
		 * /* Search readedSearch = (Search) core.getInformationAccess().get(
		 * "http://searchE", SearchRDFHelper.SEARCH_CLASS);
		 * System.out.println("The readed search is " + readedSearch);
		 * 
		 * User newUser = new User();
		 * 
		 * newUser.setURI("http://newUser"); newUser.setName("Unknown User");
		 * for (int i = 0; i < 10; i++) newUser
		 * .addInformationSourceSubscription
		 * ("http://informationSourceSubscription" + i);
		 * 
		 * for (int i = 0; i < 10; i++)
		 * newUser.addKnowledgeObject("http://knowledgeObject" + i);
		 * 
		 * core.getInformationAccess().put(newUser); User newUserReaded = (User)
		 * core.getInformationAccess().get( newUser.getURI(),
		 * UserRDFHelper.USER_CLASS);
		 * 
		 * System.out.println("The readed newUser is " + newUserReaded);
		 */

		core.getInformationAccess()
				.remove("http://www.epnoi.org/users/testUser",
						UserRDFHelper.USER_CLASS);

		User testUser = new User();

		testUser.setURI("http://www.epnoi.org/users/testUser");
		testUser.setName("testUser");

		for (int i = 0; i < 10; i++)
			testUser.addKnowledgeObject("http://knowledgeObject" + i);

		testUser.addInformationSourceSubscription("http://www.epnoi.org/users/testUser/subscriptions/informationSources/highScalability");
		testUser.addInformationSourceSubscription("http://www.epnoi.org/users/testUser/subscriptions/informationSources/slashdot");

		testUser.addInformationSourceSubscription("http://www.epnoi.org/users/testUser/subscriptions/informationSources/randomInformationSource");
		
		core.getInformationAccess().put(testUser,new Context());
		
		
		
		/*
		 * User newUserReaded2 = (User) core.getInformationAccess().get(
		 * testUser.getURI(), UserRDFHelper.USER_CLASS);
		 * 
		 * System.out.println("The readed newUser is " + newUserReaded2);
		 */
		Context context=new Context();
		core.getInformationAccess().put(testUser,context);
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
		

		InformationSource randomInformationSource = new InformationSource();
		randomInformationSource
				.setURI("http://www.epnoi.org/informationSources/randomInformationSource");
		randomInformationSource.setName("randomInformationSource");
		randomInformationSource
				.setURL("http://www.epnoi.org/informationSources/randomInformationSource");
		randomInformationSource
				.setType(InformationSourceRDFHelper.RSS_INFORMATION_SOURCE_CLASS);
		randomInformationSource
				.setInformationUnitType(FeedRDFHelper.ITEM_CLASS);

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

		InformationSourceSubscription informationSourceSubscriptionRandom = new InformationSourceSubscription();

		informationSourceSubscriptionRandom
				.setURI("http://www.epnoi.org/users/testUser/subscriptions/informationSources/randomInformationSource");
		informationSourceSubscriptionRandom
				.setInformationSource("http://www.epnoi.org/informationSources/randomInformationSource");
		informationSourceSubscriptionRandom.setNumberOfItems(2);
		
		Context emptyContex = new Context();
		core.getInformationAccess().put(slashdotInformationSource, emptyContex);

		core.getInformationAccess().put(highScalabilityInformationSource, emptyContex);

		core.getInformationAccess().put(informationSourceSubscriptionHigh, emptyContex);

		core.getInformationAccess().put(informationSourceSubscription, emptyContex);

		core.getInformationAccess().put(randomInformationSource, emptyContex);

		core.getInformationAccess().put(informationSourceSubscriptionRandom, emptyContex);
		
		
		/*
		 * List<Feed> feeds = _generateFeedsData(); for (Feed feed : feeds) {
		 * core.getInformationAccess().put(feed); }
		 */
/*
		RSSInformationSourceRandomGenerator generator = new RSSInformationSourceRandomGenerator();
		generator.generate(core);
*/
		System.out.println("information source handler");

		for (String subscription : testUser.getInformationSourceSubscriptions()) {
			core.getInformationSourcesHandler().retrieveNotifications(
					subscription);
		}
	}

	private static List<Feed> _generateFeedsData() {
		List<Feed> feeds = new ArrayList<Feed>();
		String feedURI = "http://feedA";
		Feed feedA = new Feed();
		Context context = new Context();

		feedA.setURI(feedURI);
		feedA.setTitle("high");
		feedA.setLink("http://feeds.feedburner.com/HighScalability");
		feedA.setPubDate("Mon, 12 Dec 2013 22:22:16 GMT");
		feedA.setDescription("This is the description of feed A");
		for (int i = 0; i < 10; i++) {
			Item itemA = new Item();

			itemA.setURI("http://uriA" + i);
			itemA.setTitle("titleA" + i);
			itemA.setLink("http://www.cadenaser.com");
			itemA.setDescription("Description \" for item" + i);
			itemA.setPubDate("Mon, 16 Dec 2013 22:22:16 GMT");
			itemA.setAuthor("authorA");

			List<String> kewords = Arrays.asList("mi" + i, "mama" + i,
					"me" + i, "mima" + i);
			context.getElements().put(itemA.getURI(), kewords);
			feedA.addItem(itemA);
		}

		Item itemB = new Item();

		itemB.setURI("http://uriB");
		itemB.setTitle("titleB");
		itemB.setLink("http://www.elpais.es");
		itemB.setDescription("bla bla bla gato blab lba lba");
		itemB.setPubDate("Tue, 17 Dec 2013 23:22:16 GMT");
		itemB.setAuthor("authorB");
		List<String> kewords = Arrays.asList("mi", "mama", "me", "mima",
				"cosarara");

		// ----------------------------------------------------------------
		String feedURIB = "http://feedB";
		Feed feedB = new Feed();
		Context contextB = new Context();

		feedB.setURI(feedURIB);
		feedB.setTitle("slashdot");
		feedB.setLink("http://rss.slashdot.org/Slashdot/slashdot");
		feedB.setPubDate("Fri, 13 Dec 2013 16:57:49 +0000");
		feedB.setDescription("This is the description of feed B");
		for (int i = 0; i < 10; i++) {
			Item itemA = new Item();

			itemA.setURI("http://uriB" + i);
			itemA.setTitle("titleB" + i);
			itemA.setLink("http://www.whatever.com");
			itemA.setDescription("Description for item" + i);
			itemA.setPubDate("Fri, 13 Dec 2013 16:57:49 +0000");
			List<String> kewordsA = Arrays.asList("mi" + i, "mama" + i, "me"
					+ i, "mima" + i);
			context.getElements().put(itemA.getURI(), kewordsA);
			feedB.addItem(itemA);
		}
		contextB.getElements().put(itemB.getURI(), kewords);

		feedA.addItem(itemB);
		feeds.add(feedA);
		feeds.add(feedB);
		return feeds;
	}

}
