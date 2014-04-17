package org.epnoi.uia.informationsources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.epnoi.uia.informationstore.dao.rdf.FeedRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFOAIOREHelper;
import org.epnoi.uia.parameterization.InformationStoreParameters;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

import epnoi.model.InformationSourceSubscription;

public class RSSInformationSourceQueryBuilder implements
		InformationSourceQueryBuilder {
	public String generateQuery(
			InformationSourceSubscription informationSourceSubscription,
			InformationStoreParameters parameters) {
		VirtuosoInformationStoreParameters rdfParameters = (VirtuosoInformationStoreParameters) parameters;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-mm-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = f.parse("2013-12-16");
			endDate = f.parse("2014-03-17");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		String queryExpression = "PREFIX  xsd:  <http://www.w3.org/2001/XMLSchema#> SELECT ?uri FROM <{GRAPH}> WHERE "
				+ "{<{INFORMATION_SOURCE_URI}> a <{FEED_CLASS}> . "
				+ "?uri <{PUBDATE_PROPERTY}> ?pubDate ."
				+ "<{INFORMATION_SOURCE_URI}> <{AGGREGATES_PROPERTY}> ?uri . "
				// + "FILTER (?pubDate >=\""+
				// f.format(startDate)+"\"^^xsd:dateTime && ?pubDate <=\""+f.format(endDate)+"\"^^xsd:dateTime)"
				+ "} " + " ORDER BY ?pubDate LIMIT {NUMBER_OF_RESULTS}";

		
		
		queryExpression = queryExpression
				.replace("{GRAPH}", rdfParameters.getGraph())
				.replace("{FEED_CLASS}", FeedRDFHelper.FEED_CLASS)
				.replace("{URL_PROPERTY}", RDFHelper.URL_PROPERTY)
				.replace("{AGGREGATES_PROPERTY}",
						RDFOAIOREHelper.AGGREGATES_PROPERTY)
				.replace("{PUBDATE_PROPERTY}", RDFHelper.PUBDATE_PROPERTY)
				.replace("{INFORMATION_SOURCE_URI}",
						informationSourceSubscription.getInformationSource())
				.replace("{NUMBER_OF_RESULTS}",
						informationSourceSubscription.getNumberOfItems().toString());

		return queryExpression;
	}

	String convertDateFormat(String dateExpression) {
		List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
		knownPatterns.add(new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzzz", Locale.ENGLISH));

		knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH));
		knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
				Locale.ENGLISH));
		knownPatterns.add(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
				Locale.ENGLISH));

		for (SimpleDateFormat pattern : knownPatterns) {
			try {
				// Take a try
				Date parsedDate = pattern.parse(dateExpression);
				SimpleDateFormat dt1 = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ssZ");
				return (dt1.format(parsedDate));
			} catch (ParseException pe) {
				// Loop on
			}
		}
		System.err.println("No known Date format found: " + dateExpression);
		return null;

	}

}
