package org.epnoi.uia.parameterization;

import java.util.ArrayList;

public class RSSHoarderParameters extends HoarderParameters{
	ArrayList<RSSFeedParameters> feeds = new ArrayList<RSSFeedParameters>();

	public ArrayList<RSSFeedParameters> getFeed() {
		return feeds;
	}

	public void setFeed(ArrayList<RSSFeedParameters> feeds) {
		this.feeds = feeds;
	}

}
