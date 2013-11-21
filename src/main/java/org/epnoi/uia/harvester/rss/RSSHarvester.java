package org.epnoi.uia.harvester.rss;

import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.harvester.rss.parse.RSSFeedParser;
import org.epnoi.uia.parameterization.RSSHarvesterParameters;
import org.epnoi.uia.parameterization.manifest.Manifest;
import org.epnoi.uia.parameterization.manifest.ManifestHandler;

import epnoi.model.Feed;
import epnoi.model.Item;

public class RSSHarvester {
	Core core;
	RSSHarvesterParameters parameters;
	Toolkit toolkit;
	Timer timer;
	int numberFeeds;
	HashMap<String, RSSHarvestTask> harvestTasks = new HashMap<String, RSSHarvester.RSSHarvestTask>();

	// ----------------------------------------------------------------------------------------

	public RSSHarvester(Core core, RSSHarvesterParameters parameters) {
		this.core = core;
		this.parameters = parameters;
		this.numberFeeds = 2;

	}

	// ----------------------------------------------------------------------------------------

	public void start() {
		this.toolkit = Toolkit.getDefaultToolkit();
		this.timer = new Timer();
		String[] directories = scanDirectories();
		System.out.println(Arrays.toString(directories));
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
			System.out
					.println("------------------------------------------------------------------- Se acabo lo que se daba");
			timer.cancel();
		} else {
			System.out
					.println("--------------------------------------------------------------------- Another one bites the dust "
							+ numberFeeds);
		}
	}

	// ----------------------------------------------------------------------------------------

	public void _harvestDirectory(File directoryToHarvest, Manifest manifest) {
		try {

			RSSHarvestTask harvestTask = new RSSHarvestTask(manifest,
					directoryToHarvest.getAbsolutePath());

			this.harvestTasks.put(manifest.getURI(), harvestTask);

			timer.schedule(harvestTask, 0, // initial delay
					manifest.getInterval() * 1000); // subsequent rate

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
		RSSHarvestTask task = this.harvestTasks.get(taskURI);
		task.cancel();
	}

	// ----------------------------------------------------------------------------------------

	class RSSHarvestTask extends TimerTask {
		private Manifest manifest;
		private String directoryPath;
		private int numWarningBeeps = 3;

		// ----------------------------------------------------------------------------------------

		public RSSHarvestTask(Manifest manifest, String directoryPath) {
			this.directoryPath = directoryPath;
			this.manifest = manifest;

		}

		// ----------------------------------------------------------------------------------------

		public void run() {
			System.out
					.println("........................................>>>>>>>>>>>................................");
			// _initializeHoarding();
			if (numWarningBeeps > 0) {
				// toolkit.beep();
				System.out
						.println("-------------------------------------------------------------------"
								+ this.manifest.getName() + ":Beep!");
				harvest(this.directoryPath);
				numWarningBeeps--;
			} else {
				// toolkit.beep();
				System.out
						.println("-------------------------------------------------------------------"
								+ this.manifest.getName() + ":Time's up!");
				remove();
				// this.cancel(); // Not necessary because we call System.exit
				// System.exit(0); // Stops the AWT thread (and everything else)

			}
		}

		// ----------------------------------------------------------------------------------------

		public void harvest(String directoryToHarvest) {
			HashMap<String, Item> items = new HashMap<String, Item>();
			try {
				File harvestDirectoy = new File(directoryToHarvest);

				String[] filesToHarvest = scanFilesToHarverst(harvestDirectoy);
				
				//System.out.println("..........> "
				//		+ Arrays.toString(filesToHarvest));
				for (String fileToHarvest : filesToHarvest) {
					System.out.println(">Harvesting :" + fileToHarvest);

					Feed feed = _harvestFile(directoryToHarvest + "/"
							+ fileToHarvest);
					
					if (core != null) {
						core.getInformationAccess().put(feed);
					} else {

						System.out.println(" result >>>>>" + feed);
					}
					/*
					 * for (Item item : _harvestFile(directoryToHarvest + "/" +
					 * fileToHarvest)) { items.put(item.getLink(), item); }
					 */

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			/*
			 * System.out.println("--"); System.out .println(
			 * "Result of the harvest -----------------------------------------------------------> "
			 * + items); System.out.println("--");
			 */
		}

		// ----------------------------------------------------------------------------------------

		private String[] scanFilesToHarverst(File directoryToHarvest) {
			String[] filesToHarvest = directoryToHarvest
					.list(new FilenameFilter() {

						public boolean accept(File current, String name) {
							File file = new File(current, name);
							return (file.isFile()) && (!file.isHidden());
						}

					});
			return filesToHarvest;
		}

		// ----------------------------------------------------------------------------------------

		public Feed _harvestFile(String filePath) {

			RSSFeedParser parser = new RSSFeedParser("file://" + filePath);
			Feed feed = parser.readFeed();
			//System.out.println("Feed : " + feed);
			//System.out.println(feed);
			return feed;

			// return feed.getItems();
		}

	}

	// ----------------------------------------------------------------------------------------

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("//////////////////////////////");
		RSSHarvesterParameters parameters = new RSSHarvesterParameters();
		parameters.setPath("/proofs/rsshoarder");
		parameters.setURI("http://rssHarvesterMainTest");
		RSSHarvester harvester = new RSSHarvester(null, parameters);
		harvester.start();
	}

}
