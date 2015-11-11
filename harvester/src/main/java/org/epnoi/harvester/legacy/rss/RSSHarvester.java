package org.epnoi.harvester.legacy.rss;

import org.epnoi.model.modules.Core;
import org.epnoi.model.parameterization.RSSHarvesterParameters;
import org.epnoi.model.parameterization.manifest.Manifest;
import org.epnoi.model.parameterization.manifest.ManifestHandler;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

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
		this.numberFeeds = 50;

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
