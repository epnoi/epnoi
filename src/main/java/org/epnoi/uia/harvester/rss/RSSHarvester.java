package org.epnoi.uia.harvester.rss;

import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.harvester.rss.parse.RSSFeedParser;
import org.epnoi.uia.parameterization.RSSHarvesterParameters;
import org.epnoi.uia.parameterization.manifest.Manifest;
import org.epnoi.uia.parameterization.manifest.ManifestHandler;
import org.xml.sax.ContentHandler;

import epnoi.model.Context;
import epnoi.model.Feed;
import epnoi.model.Item;
import epnoi.recommeders.WorkflowsGroupBasedRecommender;

public class RSSHarvester {

	private static final Logger logger = Logger
			.getLogger(RSSHarvester.class.getName());
	

	Core core;
	RSSHarvesterParameters parameters;
	Toolkit toolkit;
	ScheduledThreadPoolExecutor timer;
	int numberFeeds;
	HashMap<String, RSSHarvestDirectoryTask> harvestTasks = new HashMap<String, RSSHarvestDirectoryTask>();
	HashMap<String, ScheduledFuture> harvestTasksFutures = new HashMap<String, ScheduledFuture>();

	// ----------------------------------------------------------------------------------------

	public RSSHarvester(Core core, RSSHarvesterParameters parameters) {
		this.core = core;
		this.parameters = parameters;
		this.numberFeeds = 2;

	}

	// ----------------------------------------------------------------------------------------

	public void start() {
		this.toolkit = Toolkit.getDefaultToolkit();
		timer = new ScheduledThreadPoolExecutor(numberFeeds);
		String[] directories = scanDirectories();
		// System.out.println(Arrays.toString(directories));
		for (String directory : directories) {
			File directoryToHarvest = new File(parameters.getPath() + "/"
					+ directory + "/harvests");
			Manifest manifest = ManifestHandler.read(parameters.getPath() + "/"
					+ directory + "/manifest.xml");
			_harvestDirectory(directoryToHarvest, manifest);
		}
	}

	// ----------------------------------------------------------------------------------------

	public synchronized void remove() {
		numberFeeds--;
		if (numberFeeds == 0) {
			logger.info("Stopping the RSSHarvester since there are no active directories");
			timer.shutdown();
		} else {
			logger.info("One directory has been removed, " + numberFeeds
					+ " remain active");
		}
	}

	// ----------------------------------------------------------------------------------------

	public void _harvestDirectory(File directoryToHarvest, Manifest manifest) {
		try {

			logger.info("Initializing the RSS harvester task with the following parameters: "+manifest);
			RSSHarvestDirectoryTask harvestTask = new RSSHarvestDirectoryTask(
					manifest, directoryToHarvest.getAbsolutePath(), this);

			this.harvestTasks.put(manifest.getURI(), harvestTask);

			
			ScheduledFuture hoardFuture = timer.scheduleAtFixedRate(
					harvestTask, 0, // initial
									// delay
					manifest.getInterval() * 1000, TimeUnit.MILLISECONDS); // subsequent
			// rate

			this.harvestTasksFutures.put(manifest.getURI(), hoardFuture);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ----------------------------------------------------------------------------------------

	/*
	 * private String[] scanFilesToHarverst(File directoryToHarvest) { String[]
	 * filesToHarvest = directoryToHarvest.list(new FilenameFilter() {
	 * 
	 * public boolean accept(File current, String name) { return new
	 * File(current, name).isFile(); }
	 * 
	 * }); return filesToHarvest; }
	 */
	private String[] scanDirectories() {
		File file = new File(parameters.getPath());
		String[] directories = file.list(new FilenameFilter() {

			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return directories;
	}

	// ----------------------------------------------------------------------------------------

	public void cancelTask(String taskURI) {
		ScheduledFuture futureTask = this.harvestTasksFutures.get(taskURI);
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
		RSSHarvester harvester = new RSSHarvester(null, parameters);
		harvester.start();
		// harvester.retrieveContent("http://www.cadenaser.com",
		// "/proofs/kk.txt");

	}

}
