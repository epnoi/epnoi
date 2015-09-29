package org.epnoi.uia.hoarder;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.epnoi.uia.parameterization.RSSFeedParameters;
import org.epnoi.uia.parameterization.RSSHoarderParameters;

public class RSSHoarder {
	private static final Logger logger = Logger.getLogger(RSSHoarder.class
			.getName());
	private RSSHoarderParameters parameters;

	Toolkit toolkit;
	// Timer timer;
	ScheduledThreadPoolExecutor timer;
	int numberActiveFeeds;
	HashMap<String, RSSHoardTask> hoardTasks = new HashMap<String, RSSHoardTask>();
	HashMap<String, ScheduledFuture> hoardTasksFutures = new HashMap<String, ScheduledFuture>();

	// ----------------------------------------------------------------------------------------

	public RSSHoarderParameters getParameters() {
		return parameters;
	}

	// ----------------------------------------------------------------------------------------

	public void setParameters(RSSHoarderParameters parameters) {
		this.parameters = parameters;
	}

	// ----------------------------------------------------------------------------------------

	public RSSHoarder(RSSHoarderParameters parameters) {
		logger.info("Creating an instance of RSSHoarder");
		logger.info("The paramters are :");
		logger.info(parameters.toString());
		this.parameters = parameters;
		this.numberActiveFeeds = parameters.getFeed().size();
	}

	// ----------------------------------------------------------------------------------------

	public synchronized void remove() {
		numberActiveFeeds--;
		if (numberActiveFeeds == 0) {
			logger.info("Stopping the RSSHoarder since there are no active feeds");
			timer.shutdown();
		} else {
			logger.info("One feed has been removed, " + numberActiveFeeds
					+ " remain active");
		}
	}

	// ----------------------------------------------------------------------------------------

	public void start() {
		logger.info("Starting the RSSHoarder with the following parameters");
		logger.info(this.parameters.toString());
		toolkit = Toolkit.getDefaultToolkit();
		timer = new ScheduledThreadPoolExecutor(numberActiveFeeds);

		for (RSSFeedParameters feed : this.parameters.getFeed()) {
			RSSHoardTask hoardTask = new RSSHoardTask(feed, this);
			this.hoardTasks.put(feed.getURI(), hoardTask);
		}
		for (RSSHoardTask hoardTask : this.hoardTasks.values()) {
			ScheduledFuture hoardFuture = timer.scheduleAtFixedRate(
					hoardTask,
					0, // initial
					// delay
					hoardTask.getFeedParameters().getInterval() * 1000,
					TimeUnit.MILLISECONDS); // subsequent
			// rate
			this.hoardTasksFutures.put(hoardTask.getFeedParameters().getURI(),
					hoardFuture);
		}
	}

	// ----------------------------------------------------------------------------------------

	public void cancelTask(String taskURI) {
		ScheduledFuture futureTask = this.hoardTasksFutures.get(taskURI);
		futureTask.cancel(true);
		remove();

	}

	// ----------------------------------------------------------------------------------------

	public static void main(String[] args) {

		System.out.println("RSSHoarder: Entering");
		RSSFeedParameters feedParameters = new RSSFeedParameters();
		feedParameters.setName("slashdot");
		feedParameters.setURL("http://rss.slashdot.org/Slashdot/slashdot");
		feedParameters.setURI("http://www.epnoi.org/feeds/slashdot");
		feedParameters.setInterval(10);

		/*
		 * <feed> <name>highScalability</name>
		 * <URI>http://www.epnoi.org/feeds/highScalability</URI>
		 * <URL>http://feeds.feedburner.com/HighScalability</URL>
		 * <interval>3</interval> </feed>
		 */

		RSSFeedParameters secondfeedParameters = new RSSFeedParameters();
		secondfeedParameters.setName("highScalability");
		secondfeedParameters
				.setURL("http://feeds.feedburner.com/HighScalability");
		secondfeedParameters
				.setURI("http://www.epnoi.org/feeds/highScalability");
		secondfeedParameters.setInterval(40);

		RSSHoarderParameters parameters = new RSSHoarderParameters();
		ArrayList<RSSFeedParameters> feeds = new ArrayList<RSSFeedParameters>();
		feeds.add(feedParameters);
		feeds.add(secondfeedParameters);
		parameters.setFeed(feeds);
		parameters.setURI("hoarder");
		parameters.setPath("/proofs/rsshoarder2");

		RSSHoarder hoarder = new RSSHoarder(parameters);
		hoarder.start();

		System.out.println("RSSHoarder: Exit");
	}
}
