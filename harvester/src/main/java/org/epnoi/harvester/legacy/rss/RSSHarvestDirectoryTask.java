package org.epnoi.harvester.legacy.rss;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.epnoi.harvester.legacy.rss.parse.RSSFeedParser;
import org.epnoi.model.Context;
import org.epnoi.model.Feed;
import org.epnoi.model.InformationSource;
import org.epnoi.model.Item;
import org.epnoi.model.parameterization.manifest.Manifest;
import org.epnoi.model.rdf.InformationSourceRDFHelper;
import org.xml.sax.ContentHandler;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

class RSSHarvestDirectoryTask implements Runnable {
	private Manifest manifest;
	private String directoryPath;
	private String datePattern = "MM/dd/yyyy";

	RSSHarvester harvester;
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
			.getLogger(RSSHarvestDirectoryTask.class.getName());
	private static final int MAX_TOKEN_LENGTH = 12;

	private static final int MIN_TOKEN_LENGTH = 2;

	// ----------------------------------------------------------------------------------------

	public RSSHarvestDirectoryTask(Manifest manifest, String directoryPath,
			RSSHarvester harvester) {
		this.directoryPath = directoryPath;
		this.manifest = manifest;
		this.harvester = harvester;

	}

	// ----------------------------------------------------------------------------------------

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

		StringTokenizer stringTokenizer = new StringTokenizer(
				handler.toString());

		ArrayList<String> candidateKeywords = new ArrayList<String>();

		while (stringTokenizer.hasMoreTokens()) {

			String token = stringTokenizer.nextToken();

			token = token.replaceAll("[^a-zA-Z 0-9]+", "");

			if (!stopWordsList.contains(token.toLowerCase())) {

				if (token.matches("[\\w]*[a-zA-Z]+[\\w]*")
						&& token.length() > MIN_TOKEN_LENGTH
						&& token.length() < MAX_TOKEN_LENGTH) {

					if (!candidateKeywords.contains(token))
						candidateKeywords.add(token);
				}

			}

		}
		return candidateKeywords;
	}

	// ----------------------------------------------------------------------------------------

	private String _scanContent(String resourceURI) {
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

		return handler.toString();
	}

	// ----------------------------------------------------------------------------------------

	public void run() {
		logger.info("Starting a harversting task " + this.manifest);
		harvest(this.directoryPath);
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
				System.out.println(">Harvesting :"
						+ harvestDirectoy.getAbsolutePath() + "/"
						+ fileToHarvest);

				Feed feed = _harvestFile(directoryToHarvest + "/"
						+ fileToHarvest);

				String contentDirectoryPath = harvestDirectoy.getAbsolutePath()
						+ "/" + fileToHarvest.replace(".xml", "") + "Content";
				Context feedContext = new Context();
				for (Item item : feed.getItems()) {
					String itemContetFileName = "file://"
							+ contentDirectoryPath + "/"
							+ item.getURI().replaceAll("[^A-Za-z0-9]", "")
							+ ".txt";
					String  content = _scanContent(itemContetFileName);

					feedContext.getElements().put(item.getURI(), content);

				}

				if (this.harvester.getCore() != null) {

					InformationSource informationSource = (InformationSource) this.harvester
							.getCore()
							.getInformationHandler()
							.get(this.manifest.getURI(),
									InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS);
					feedContext.getParameters().put(
							Context.INFORMATION_SOURCE_NAME,
							informationSource.getName());
					feedContext.getParameters().put(
							Context.INFORMATION_SOURCE_URI, manifest.getURI());

					this.harvester.getCore().getInformationHandler()
							.put(feed, feedContext);
				} else {

					System.out.println("Result: Feed> " + feed);
					for (Item item : feed.getItems()) {
						System.out.println("		 Item> " + item);
					}

					System.out.println("Result: Context> " + feedContext);
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
		String[] filesToHarvest = directoryToHarvest.list(new FilenameFilter() {

			public boolean accept(File current, String name) {
				File file = new File(current, name);
				return (file.isFile()) && (!file.isHidden());
			}

		});
		return filesToHarvest;
	}

	// ----------------------------------------------------------------------------------------

	protected String convertDateFormat(String dateExpression) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(dateExpression);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		return (dt1.format(date) + "^^xsd:date");

	}

	// ----------------------------------------------------------------------------------------

	public Feed _harvestFile(String filePath) {

		RSSFeedParser parser = new RSSFeedParser("file://" + filePath);
		Feed feed = parser.readFeed();
		// System.out.println("Feed : " + feed);
		// System.out.println(feed);

		if (feed.getPubDate() == "") {

			String date = getDate(filePath);
			System.out.println("date---> " + date);
			feed.setPubDate(date);
		}

		feed.setUri(manifest.getURI());

		return feed;

		// return feed.getItems();
	}

	// ----------------------------------------------------------------------------------------

	private String getDate(String filePath) {
		System.out.println("filePath> " + filePath);
		int bracketOpeningPosition = filePath.indexOf("[");
		int bracketClosingPosition = filePath.indexOf("]");
		String filePathDatePart = filePath.substring(
				bracketOpeningPosition + 1, bracketClosingPosition);
		return filePathDatePart;
	}

	// ----------------------------------------------------------------------------------------

	private void handleError(String errorMessage, String exceptionMessage) {
		if (exceptionMessage != null) {
			logger.severe(errorMessage);
		} else {
			logger.severe(errorMessage);
			logger.severe("The exception message was: " + errorMessage);
		}
		this.harvester.cancelTask(this.manifest.getURI());
	}

}