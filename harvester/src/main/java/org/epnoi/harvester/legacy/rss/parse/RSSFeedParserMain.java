package org.epnoi.harvester.legacy.rss.parse;

import org.epnoi.model.Feed;
import org.epnoi.model.Item;

import java.util.ArrayList;
import java.util.List;





public class RSSFeedParserMain {
	public static void main(String[] args) {	

		List<String> feedChannels = new ArrayList<String>();
		
		feedChannels.add("file://localhost/proofs/rsshoarder/slashdot/harvests/[2013-11-19].xml");
		//feedChannels.add("file://localhost/proofs/rsshoarder/highScalability/harvests/%5B2013-10-28%5D.xml");

		for (String feedChannel : feedChannels) {

			RSSFeedParser parser = new RSSFeedParser(feedChannel);
			Feed feed = parser.readFeed();
			System.out.println("Feed : " + feed);
			System.out.println(feed);
			for (Item message : feed.getItems()) {
				
				System.out
						.println("Showing "
								+ message.getTitle()
								+ "---------------------------------------------------------------");
				System.out.println(message);

				System.out
						.println("---------------------------------------------------------------");

			}
		}
	}
}
