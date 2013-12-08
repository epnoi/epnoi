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
	private static final String[] stopWords = { "a", "about", "above", "above",
			"across", "after", "afterwards", "again", "against", "all",
			"almost", "alone", "along", "already", "also", "although",
			"always", "am", "among", "amongst", "amoungst", "amount", "an",
			"and", "another", "any", "anyhow", "anyone", "anything", "anyway",
			"anywhere", "are", "around", "as", "at", "back", "be", "became",
			"because", "become", "becomes", "becoming", "been", "before",
			"beforehand", "behind", "being", "below", "beside", "besides",
			"between", "beyond", "bill", "both", "bottom", "but", "by", "call",
			"can", "cannot", "cant", "co", "con", "could", "couldnt", "cry",
			"de", "describe", "detail", "do", "done", "down", "due", "during",
			"each", "eg", "eight", "either", "eleven", "else", "elsewhere",
			"empty", "enough", "etc", "even", "ever", "every", "everyone",
			"everything", "everywhere", "except", "few", "fifteen", "fify",
			"fill", "find", "fire", "first", "five", "for", "former",
			"formerly", "forty", "found", "four", "from", "front", "full",
			"further", "get", "give", "go", "had", "has", "hasnt", "have",
			"he", "hence", "her", "here", "hereafter", "hereby", "herein",
			"hereupon", "hers", "herself", "him", "himself", "his", "how",
			"however", "hundred", "ie", "if", "in", "inc", "indeed",
			"interest", "into", "is", "it", "its", "itself", "keep", "last",
			"latter", "latterly", "least", "less", "ltd", "made", "many",
			"may", "me", "meanwhile", "might", "mill", "mine", "more",
			"moreover", "most", "mostly", "move", "much", "must", "my",
			"myself", "name", "namely", "neither", "never", "nevertheless",
			"next", "nine", "no", "nobody", "none", "noone", "nor", "not",
			"nothing", "now", "nowhere", "of", "off", "often", "on", "once",
			"one", "only", "onto", "or", "other", "others", "otherwise", "our",
			"ours", "ourselves", "out", "over", "own", "part", "per",
			"perhaps", "please", "put", "rather", "re", "same", "see", "seem",
			"seemed", "seeming", "seems", "serious", "several", "she",
			"should", "show", "side", "since", "sincere", "six", "sixty", "so",
			"some", "somehow", "someone", "something", "sometime", "sometimes",
			"somewhere", "still", "such", "system", "take", "ten", "than",
			"that", "the", "their", "them", "themselves", "then", "thence",
			"there", "thereafter", "thereby", "therefore", "therein",
			"thereupon", "these", "they", "thickv", "thin", "third", "this",
			"those", "though", "three", "through", "throughout", "thru",
			"thus", "to", "together", "too", "top", "toward", "towards",
			"twelve", "twenty", "two", "un", "under", "until", "up", "upon",
			"us", "very", "via", "was", "we", "well", "were", "what",
			"whatever", "when", "whence", "whenever", "where", "whereafter",
			"whereas", "whereby", "wherein", "whereupon", "wherever",
			"whether", "which", "while", "whither", "who", "whoever", "whole",
			"whom", "whose", "why", "will", "with", "within", "without",
			"would", "yet", "you", "your", "yours", "yourself", "yourselves",
			"the" };

	private static final List<String> stopWordsList = Arrays.asList(stopWords);
	private static final Logger logger = Logger
			.getLogger(WorkflowsGroupBasedRecommender.class.getName());
	private static final int MAX_TOKEN_LENGTH = 12;

	private static final int MIN_TOKEN_LENGTH = 2;

	Core core;
	RSSHarvesterParameters parameters;
	Toolkit toolkit;
	Timer timer;
	int numberFeeds;
	HashMap<String, RSSHarvestDirectoryTask> harvestTasks = new HashMap<String, RSSHarvester.RSSHarvestDirectoryTask>();

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
		//System.out.println(Arrays.toString(directories));
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

			RSSHarvestDirectoryTask harvestTask = new RSSHarvestDirectoryTask(
					manifest, directoryToHarvest.getAbsolutePath());

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
		RSSHarvestDirectoryTask task = this.harvestTasks.get(taskURI);
		task.cancel();
	}

	// ----------------------------------------------------------------------------------------

	class RSSHarvestDirectoryTask extends TimerTask {
		private Manifest manifest;
		private String directoryPath;
		private int numWarningBeeps = 3;

		// ----------------------------------------------------------------------------------------

		public RSSHarvestDirectoryTask(Manifest manifest, String directoryPath) {
			this.directoryPath = directoryPath;
			this.manifest = manifest;

		}

		private ArrayList<String> _scanKeywords(String resourceURI) {
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, resourceURI);
			InputStream is = null;
			ContentHandler handler = null;
			try {
				is = new URL(resourceURI).openStream();

				Parser parser = new AutoDetectParser();
				handler = new BodyContentHandler(-1);

				ParseContext context = new ParseContext();
				context.set(Parser.class, parser);

				parser.parse(is, handler, metadata, new ParseContext());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*
			 * String[] tokens = handler.toString().split(delims); for (String
			 * token : tokens) { System.out.println(">>> " + token); }
			 */

			StringTokenizer stringTokenizer = new StringTokenizer(
					handler.toString());

			ArrayList<String> candidateKeywords = new ArrayList<String>();

			while (stringTokenizer.hasMoreTokens()) {

				String token = stringTokenizer.nextToken();
				// token = token.replace(".", "");
				// token = token.replace(",", "");
				token = token.replaceAll("[^a-zA-Z 0-9]+", "");

				if (!stopWordsList.contains(token.toLowerCase())) {

					if (token.matches("[\\w]*[a-zA-Z]+[\\w]*")
							&& token.length() > MIN_TOKEN_LENGTH
							&& token.length() < MAX_TOKEN_LENGTH) {
						// System.out.println("Este si!!");
						// System.out.println("---> " + token);
						if (!candidateKeywords.contains(token))
							candidateKeywords.add(token);
					}

				}

			}
			return candidateKeywords;
		}

		// ----------------------------------------------------------------------------------------

		public void run() {
			
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

				// System.out.println("..........> "
				// + Arrays.toString(filesToHarvest));
				for (String fileToHarvest : filesToHarvest) {
					System.out.println(">Harvesting :" + fileToHarvest);

					Feed feed = _harvestFile(directoryToHarvest + "/"
							+ fileToHarvest);

					
					String contentDirectoryPath = harvestDirectoy.getAbsolutePath()+"/"+fileToHarvest.replace(".xml",
							"") + "Content";
					Context feedContext = new Context();
					for (Item item : feed.getItems()) {
						String itemContetFileName =  "file://"+contentDirectoryPath+"/"+item.getURI().replaceAll("[^A-Za-z0-9]", "") + ".txt";
						ArrayList<String> itemKeywords=_scanKeywords(itemContetFileName);
						feedContext.getElements().put(item.getURI(), itemKeywords);

					}
					
					if (core != null) {
						core.getInformationAccess().put(feed, feedContext);
					} else {

						System.out.println("Result: Feed> " + feed);
						System.out.println("Result: Context> "+feedContext);
					}

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
			// System.out.println("Feed : " + feed);
			// System.out.println(feed);
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
		// harvester.retrieveContent("http://www.cadenaser.com",
		// "/proofs/kk.txt");

	}

}
