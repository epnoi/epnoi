package org.epnoi.hoarder.legacy;

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
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.epnoi.model.Feed;
import org.epnoi.model.Item;
import org.epnoi.model.parameterization.RSSFeedParameters;
import org.epnoi.model.parameterization.manifest.ManifestHandler;
import org.epnoi.model.parameterization.manifest.RSSManifest;
import org.w3c.dom.Document;

class RSSHoardTask implements Runnable {
	private final Logger logger = Logger.getLogger(RSSHoardTask.class
			.getName());
	RSSHoarder hoarder;
	RSSFeedParameters feedParameters;

	// ----------------------------------------------------------------------------------------

	public RSSHoardTask(RSSFeedParameters parameters, RSSHoarder hoarder) {
		this.feedParameters = parameters;
		this.hoarder = hoarder;
	}

	// ----------------------------------------------------------------------------------------

	public void _initializeHoarding() {
		String manifestPath = hoarder.getParameters().getPath() + "/"
				+ this.feedParameters.getName() + "/manifest.xml";

		if (!new File(manifestPath).exists()) {

			String harvestDirectoryName = hoarder.getParameters().getPath()
					+ "/" + feedParameters.getName() + "/" + "harvests";
			// System.out.println("directory -> " + harvestDirectoryName);
			File harvestDirectory = new File(harvestDirectoryName);
			if (!harvestDirectory.exists()) {
				boolean created = harvestDirectory.mkdirs();
				
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
		hoard();
	}

	// ----------------------------------------------------------------------------------------

	public void hoard() {
		try {
			URL url = null;

			url = new URL(this.feedParameters.getURL());

			InputStream stream = url.openStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			db = dbf.newDocumentBuilder();
			Document doc = null;
			doc = db.parse(stream);

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();

			DOMSource source = new DOMSource(doc);

			Locale locale = Locale.ENGLISH;

			DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",
					locale);

			String baseFileName = "[" + simpleDateFormat.format(new Date())
					+ "]";
			/*
			 * System.out.println(baseFileName);
			 * 
			 * System.out.println("--> crearia si no existe " +
			 * parameters.getPath() + "/" + this.feedParameters.getName() +
			 * "/harvests/" + baseFileName + ".xml");
			 */
			String outputFileName = hoarder.getParameters().getPath() + "/"
					+ this.feedParameters.getName() + "/harvests/"
					+ baseFileName;

			OutputStream outputFileStream = null;

			if (!new File(outputFileName + ".xml").isFile()) {
				File ooutputFile;
				try {
					// "/proofs/rsshoarder/slashdot/harvests/whatever.xml"
					ooutputFile = new File(outputFileName + ".xml");
					// ooutputFile.createNewFile();
					outputFileStream = new FileOutputStream(ooutputFile);
				} catch (Exception e) {
					// logger.severe(e.getMessage());
					e.printStackTrace();
					handleError("There was a problem creating the file "+outputFileName, e.getMessage());
					

				}
			} else {

				int index = 0;

				while (new File(outputFileName + ".xml").isFile()) {
					outputFileName = hoarder.getParameters().getPath() + "/"
							+ this.feedParameters.getName() + "/harvests/"
							+ baseFileName + "_v" + index;
					index++;

				}
				File outputFile = new File(outputFileName + ".xml");
				// ooutputFile.createNewFile();
				outputFileStream = new FileOutputStream(outputFile);
			}

			// System.out.println(outputFileStream);
			// StreamResult result = new StreamResult(System.out);
			StreamResult fileResult = new StreamResult(outputFileStream);
			// transformer.transform(source, result);
			transformer.transform(source, fileResult);
			stream.close();
			outputFileStream.close();
			retrieveFeedContent(outputFileName);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ----------------------------------------------------------------------------------------

	public void retrieveFeedContent(String feedFilePath) {
		logger.info(" ------> " + feedFilePath);
		String harvestDirectoryName = feedFilePath + "Content";
		logger.info("directory -> " + harvestDirectoryName);
		File harvestDirectory = new File(harvestDirectoryName);
		if (!harvestDirectory.exists()) {
			boolean created = harvestDirectory.mkdirs();
			logger.info("Directory created? " + created);
		}
/*
		RSSFeedParser parser = new RSSFeedParser("file://" + feedFilePath
				+ ".xml");
		Feed feed = parser.readFeed();
		for (Item item : feed.getItems()) {
			retrieveURLContent(item.getLink(), harvestDirectoryName + "/"
					+ item.getURI().replaceAll("[^A-Za-z0-9]", "") + ".txt");
		}
*/
	}

	// ----------------------------------------------------------------------------------------

	public void retrieveURLContent(String path, String filePath) {
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;
		PrintWriter pw = null;
		logger.info("Storing the content in " + path + " in the file "
				+ filePath);

		try {
			url = new URL(path);
			is = url.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			pw = new PrintWriter(new FileWriter(filePath));
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				pw.println(line);

			}
		} catch (MalformedURLException mue) {
			
			handleError("There was a problem with the URL", mue.getMessage());
			

		} catch (IOException ioe) {
			handleError("There was a problem creating the output file", ioe.getMessage());
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (pw != null)
					pw.close();
			} catch (IOException ioe) {
				
			}
		}
	}

	// ----------------------------------------------------------------------------------------
	
	private void handleError(String errorMessage, String exceptionMessage) {
		if (exceptionMessage != null) {
			logger.severe(errorMessage);
		} else {
			logger.severe(errorMessage);
			logger.severe("The exception message was: " + errorMessage);
		}
		this.hoarder.cancelTask(this.feedParameters.getURI());
	}
	
	// ----------------------------------------------------------------------------------------

	public RSSFeedParameters getFeedParameters() {
		return feedParameters;
	}
	
	// ----------------------------------------------------------------------------------------

	public void setFeedParameters(RSSFeedParameters feedParameters) {
		this.feedParameters = feedParameters;
	}
}
