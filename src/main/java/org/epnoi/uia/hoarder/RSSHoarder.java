package org.epnoi.uia.hoarder;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.epnoi.uia.harvester.rss.parse.RSSFeedParser;
import org.epnoi.uia.parameterization.RSSFeedParameters;
import org.epnoi.uia.parameterization.RSSHoarderParameters;
import org.epnoi.uia.parameterization.manifest.ManifestHandler;
import org.epnoi.uia.parameterization.manifest.RSSManifest;
import org.w3c.dom.Document;

import epnoi.model.Feed;
import epnoi.model.Item;

public class RSSHoarder {
	private RSSHoarderParameters parameters;

	Toolkit toolkit;
	Timer timer;
	int numberFeeds;
	HashMap<String, RSSHoardTask> hoardTasks = new HashMap<String, RSSHoarder.RSSHoardTask>();

	// ----------------------------------------------------------------------------------------

	public RSSHoarder(RSSHoarderParameters parameters) {
		this.parameters = parameters;
		this.numberFeeds = parameters.getFeed().size();
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

	public void start() {
		toolkit = Toolkit.getDefaultToolkit();
		timer = new Timer();

		for (RSSFeedParameters feed : this.parameters.getFeed()) {
			RSSHoardTask hoardTask = new RSSHoardTask(feed);
			this.hoardTasks.put(feed.getURI(), hoardTask);

			timer.schedule(hoardTask, 0, // initial delay
					feed.getInterval() * 1000); // subsequent rate
		}
	}

	// ----------------------------------------------------------------------------------------

	public void cancelTask(String taskURI) {
		RSSHoardTask task = this.hoardTasks.get(taskURI);
		task.cancel();
	}

	// ----------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------

	class RSSHoardTask extends TimerTask {
		RSSFeedParameters feedParameters;
		private int numWarningBeeps = 3;

		// ----------------------------------------------------------------------------------------

		public RSSHoardTask(RSSFeedParameters parameters) {
			this.feedParameters = parameters;
		}

		// ----------------------------------------------------------------------------------------

		public void _initializeHoarding() {
			String manifestPath = parameters.getPath() + "/"
					+ this.feedParameters.getName() + "/manifest.xml";

			if (!new File(manifestPath).exists()) {

				String harvestDirectoryName = parameters.getPath() + "/"
						+ feedParameters.getName() + "/" + "harvests";
				System.out.println("directory -> " + harvestDirectoryName);
				File harvestDirectory = new File(harvestDirectoryName);
				if (!harvestDirectory.exists()) {
					boolean created = harvestDirectory.mkdirs();
					System.out.println("Directory created? " + created);
				}

				RSSManifest manifest = new RSSManifest();
				manifest.setName(this.feedParameters.getName());
				manifest.setURL(this.feedParameters.getURL());
				manifest.setURI(this.feedParameters.getURI());
				manifest.setInterval(this.feedParameters.getInterval());

				ManifestHandler.marshallToFile(manifest, manifestPath);
			}
		}

		// ----------------------------------------------------------------------------------------

		public void run() {
			_initializeHoarding();
			if (numWarningBeeps > 0) {
				// toolkit.beep();
				System.out
						.println("-------------------------------------------------------------------"
								+ this.feedParameters.getName() + ":Beep!");
				hoard();
				numWarningBeeps--;
			} else {
				// toolkit.beep();
				System.out
						.println("-------------------------------------------------------------------"
								+ this.feedParameters.getName() + ":Time's up!");
				remove();
				this.cancel(); // Not necessary because we call System.exit
				// System.exit(0); // Stops the AWT thread (and everything else)

			}
		}

		// ----------------------------------------------------------------------------------------

		public void hoard() {
			try {
				System.out.println("--");
				URL url = null;
				url = new URL(this.feedParameters.getURL());

				InputStream stream = url.openStream();

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				db = dbf.newDocumentBuilder();
				Document doc = null;
				doc = db.parse(stream);

				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = tFactory.newTransformer();

				DOMSource source = new DOMSource(doc);

				Locale locale = Locale.ENGLISH;

				DateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd", locale);

				String baseFileName = "[" + simpleDateFormat.format(new Date())
						+ "]";
				System.out.println(baseFileName);

				System.out.println("--> crearia si no existe "
						+ parameters.getPath() + "/"
						+ this.feedParameters.getName() + "/harvests/"
						+ baseFileName + ".xml");

				String outputFileName = parameters.getPath() + "/"
						+ this.feedParameters.getName() + "/harvests/"
						+ baseFileName;

				OutputStream outputFileStream = null;

				if (!new File(outputFileName+ ".xml").isFile()) {
					try {
						// "/proofs/rsshoarder/slashdot/harvests/whatever.xml"
						File ooutputFile = new File(outputFileName + ".xml");
						// ooutputFile.createNewFile();
						outputFileStream = new FileOutputStream(ooutputFile);
					} catch (Exception e) {
						// logger.severe(e.getMessage());
						e.printStackTrace();
					}
				} else {

					int index = 0;

					while (new File(outputFileName+".xml").isFile()) {
						outputFileName = parameters.getPath() + "/"
								+ this.feedParameters.getName() + "/harvests/"
								+ baseFileName + "_v" + index;
						index++;

					}
					File outputFile = new File(outputFileName + ".xml");
					// ooutputFile.createNewFile();
					outputFileStream = new FileOutputStream(outputFile);
				}

				System.out.println(outputFileStream);
				// StreamResult result = new StreamResult(System.out);
				StreamResult fileResult = new StreamResult(outputFileStream);
				// transformer.transform(source, result);
				transformer.transform(source, fileResult);
				stream.close();
				outputFileStream.close();
				retrieveFeedContent(outputFileName);
				System.out.println("--");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// ----------------------------------------------------------------------------------------

		public void retrieveFeedContent(String feedFilePath) {
			System.out.println(" ------> " + feedFilePath);
			String harvestDirectoryName = feedFilePath + "Content";
			System.out.println("directory -> " + harvestDirectoryName);
			File harvestDirectory = new File(harvestDirectoryName);
			if (!harvestDirectory.exists()) {
				boolean created = harvestDirectory.mkdirs();
				System.out.println("Directory created? " + created);
			}

			RSSFeedParser parser = new RSSFeedParser("file://" + feedFilePath
					+ ".xml");
			Feed feed = parser.readFeed();
			for (Item item : feed.getItems()) {
				retrieveURLContent(item.getLink(), harvestDirectoryName + "/"
						+ item.getURI().replaceAll("[^A-Za-z0-9]", "") + ".txt");
			}
		}
	}

	// ----------------------------------------------------------------------------------------

	public void retrieveURLContent(String path, String filePath) {
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		PrintWriter pw = null;

		try {
			url = new URL(path);
			is = url.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			pw = new PrintWriter(new FileWriter(filePath));
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				pw.println(line);

			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (pw != null)
					pw.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
	}

	// ----------------------------------------------------------------------------------------

	public static void main(String[] args) {

		System.out.println("RSSHoarder: Entering");
		RSSFeedParameters feedParameters = new RSSFeedParameters();
		feedParameters.setName("slashdot");
		feedParameters.setURL("http://rss.slashdot.org/Slashdot/slashdot");
		feedParameters.setURI("http://www.epnoi.org/feeds/slashdot");
		feedParameters.setInterval(4);
		RSSHoarderParameters parameters = new RSSHoarderParameters();
		ArrayList<RSSFeedParameters> feeds = new ArrayList<RSSFeedParameters>();
		feeds.add(feedParameters);
		parameters.setFeed(feeds);
		parameters.setURI("hoarder");
		parameters.setPath("/proofs/rsshoarder");

		RSSHoarder hoarder = new RSSHoarder(parameters);
		hoarder.start();

		System.out.println("RSSHoarder: Exit");
	}
}
