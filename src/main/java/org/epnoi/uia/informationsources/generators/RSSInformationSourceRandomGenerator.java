package org.epnoi.uia.informationsources.generators;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.epnoi.model.Context;
import org.epnoi.model.Feed;
import org.epnoi.model.Item;
import org.epnoi.uia.core.Core;



public class RSSInformationSourceRandomGenerator {
	Context emptyContext = new Context();

	// ---------------------------------------------------------------------------------------------------

	public void generate(Core core) {
		Feed feed = _generateFeed();

		core.getInformationHandler().put(feed, emptyContext);
	}

	// ---------------------------------------------------------------------------------------------------

	private Feed _generateFeed() {
		Date currentDate = new Date();
		final String outputFormat = "EEE, dd MMM yyyy HH:mm:ss z";
		final String uriFormat = "dd_MM_yyyy_HH_mm_ss";
		SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat,
				Locale.ENGLISH);
		SimpleDateFormat uriDateFormat = new SimpleDateFormat(uriFormat,
				Locale.ENGLISH);

		System.out
				.println("pubDate -> " + outputDateFormat.format(currentDate));

		String pubDate = outputDateFormat.format(currentDate);

		String feedURI = "http://www.epnoi.org/informationSources/randomInformationSource";
		String feedTitle = "Randomly generated RSS feed";
		String feedLink = "http://www.epnoi.org/informationSources/randomInformationSource";

		Feed feed = new Feed();
		Context context = new Context();

		feed.setURI(feedURI);
		feed.setTitle(feedTitle);
		feed.setLink(feedLink);

		feed.setPubDate(pubDate);
		feed.setDescription("This is the description the random feed");
		for (int i = 0; i < 2; i++) {
			Item item = new Item();
			Date itemCurrentDate = new Date();

			String itemPubDate = outputDateFormat.format(itemCurrentDate);

			item.setPubDate(itemPubDate);
			item.setURI(feedURI + "/item" + i + "/"
					+ uriDateFormat.format(currentDate));
			item.setTitle("Feed" + i + " Title at " + itemPubDate);
			item.setLink(feedURI + "/item" + i);
			item.setDescription("Description for item " + i);

			item.setAuthor("Author of Item" + i);

			List<String> kewords = Arrays.asList("mi" + i, "mama" + i,
					"me" + i, "mima" + i);
			context.getElements().put(item.getURI(), kewords);
			feed.addItem(item);
		}
		return feed;
	}

	// ---------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		RSSInformationSourceRandomGenerator generator = new RSSInformationSourceRandomGenerator();

		System.out.println("Testing the RSSInformationSourceRandomGenerator");
		Feed feed = generator._generateFeed();
		System.out.println("----> " + feed);
	}

}
