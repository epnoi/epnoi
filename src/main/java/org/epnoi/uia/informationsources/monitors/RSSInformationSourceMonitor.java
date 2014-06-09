package org.epnoi.uia.informationsources.monitors;

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.atmosphere.cpr.Broadcaster;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.parameterization.RSSHarvesterParameters;

public class RSSInformationSourceMonitor {

	private static final Logger logger = Logger
			.getLogger(RSSInformationSourceMonitor.class.getName());

	Core core;
	RSSHarvesterParameters parameters;

	ScheduledThreadPoolExecutor executor;
	int numberFeeds;
	HashMap<String, RSSInformationSourceMonitorTask> monitorTasks = new HashMap<String, RSSInformationSourceMonitorTask>();
	HashMap<String, ScheduledFuture> monitorTasksFutures = new HashMap<String, ScheduledFuture>();

	// ----------------------------------------------------------------------------------------

	public RSSInformationSourceMonitor(Core core,
			RSSHarvesterParameters parameters) {
		this.core = core;
		this.parameters = parameters;
		this.numberFeeds = 100;

	}

	// ----------------------------------------------------------------------------------------

	public void start() {

		executor = new ScheduledThreadPoolExecutor(numberFeeds);

	}

	// ----------------------------------------------------------------------------------------

	public synchronized void remove() {
		numberFeeds--;
		if (numberFeeds == 0) {
			logger.info("Stopping the RSSInformationSourceMonitor since there are no active directories");
			executor.shutdown();
		} else {
			logger.info("One directory has been removed, " + numberFeeds
					+ " remain active");
		}
	}

	// ----------------------------------------------------------------------------------------

	public void add(String informationSourceSubscriptionURI,
			Broadcaster broadcaster) {
		try {

			logger.info("Initializing the RSS harvester task with the following parameters: ");

			RSSInformationSourceMonitorTask monitorTask = new RSSInformationSourceMonitorTask(
					informationSourceSubscriptionURI, broadcaster, this);

			this.monitorTasks
					.put(informationSourceSubscriptionURI, monitorTask);

			ScheduledFuture monitorTaskFuture = executor.scheduleAtFixedRate(
					monitorTask, 0, // initial
									// delay
					10000, TimeUnit.MILLISECONDS); // subsequent
			this.monitorTasksFutures.put(informationSourceSubscriptionURI,
					monitorTaskFuture);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ----------------------------------------------------------------------------------------

	public void cancelTask(String taskURI) {
		ScheduledFuture futureTask = this.monitorTasksFutures.get(taskURI);
		futureTask.cancel(true);
		remove();
	}

	// ----------------------------------------------------------------------------------------

	public RSSHarvesterParameters getParameters() {
		return parameters;
	}

	// ----------------------------------------------------------------------------------------

	public void setParameters(RSSHarvesterParameters parameters) {
		this.parameters = parameters;
	}

	// ----------------------------------------------------------------------------------------

	public Core getCore() {
		return core;
	}

	// ----------------------------------------------------------------------------------------

	public void setCore(Core core) {
		this.core = core;
	}

	// ----------------------------------------------------------------------------------------

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("//////////////////////////////");

		RSSHarvesterParameters parameters = new RSSHarvesterParameters();
		parameters.setPath("/proofs/rsshoarder2");
		parameters.setURI("http://rssHarvesterMainTest");
		RSSInformationSourceMonitor harvester = new RSSInformationSourceMonitor(
				null, parameters);
		harvester.start();

	}

}
