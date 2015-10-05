package org.epnoi.model.parameterization;

import java.util.ArrayList;
import java.util.Arrays;

public class RSSHoarderParameters extends HoarderParameters{
	ArrayList<RSSFeedParameters> feeds = new ArrayList<RSSFeedParameters>();

	public ArrayList<RSSFeedParameters> getFeed() {
		return feeds;
	}

	public void setFeed(ArrayList<RSSFeedParameters> feeds) {
		this.feeds = feeds;
	}

	@Override
	public String toString() {
		return "RSSHoarderParameters [feeds=" + Arrays.toString(feeds.toArray()) + "] and "+super.toString();
	}

}
