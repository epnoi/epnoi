/*******************************************************************************
 * Copyright (c) 2010 Torsten Zesch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 *     Torsten Zesch - initial API and implementation
 ******************************************************************************/
package org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.tutorial;

import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.NestedList;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.NestedListContainer;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.NestedListElement;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;

/**
 * Displays all nested lists of a page.
 *
 */

public class T6_NestedLists {

	public static void main(String[] args) throws WikiApiException {

		//db connection settings
		DatabaseConfiguration dbConfig = new DatabaseConfiguration();
	    dbConfig.setDatabase("DATABASE");
	    dbConfig.setHost("HOST");
	    dbConfig.setUser("USER");
	    dbConfig.setPassword("PASSWORD");
	    dbConfig.setLanguage(Language.english);

		//initialize a wiki
		Wikipedia wiki = new Wikipedia(dbConfig);

        MediaWikiParserFactory pf = new MediaWikiParserFactory(Language.english);
        MediaWikiParser parser = pf.createParser();

		//get the page 'House_(disambiguation)'
		ParsedPage pp = parser.parse(wiki.getPage("House_(disambiguation)").getText());

		int i = 1;
		// print out all nested lists of the page
		for(NestedList nl : pp.getNestedLists()){
			System.out.println(i + ": \n" + outputNestedList(nl,0));
			i++;
		}
	}

	/**
	 * Returns String with all elements of a NestedList
	 * @param nl NestedList
	 * @param depth Current depth of the Nestedlist
	 * @return
	 */
	public static String outputNestedList(NestedList nl, int depth){
		String result = "";
		if(nl == null)
		 {
			return result; // If null return empty string
		}

		for(int i = 0; i<depth; i++)
		 {
			result += " ";		// insert indentation according to depth
		}

		if(nl.getClass() == NestedListElement.class){ // If it is a NestedListElement,
													  // we reached a leaf, return its contents
			result += nl.getText();
		}else{
			result += "---";	// If it is not a NestedListElement, it is a NestedListContainer
								// print out all its childs, increment depth
			for(NestedList nl2 : ((NestedListContainer)nl).getNestedLists()) {
				result += "\n"+outputNestedList(nl2, depth+1);
			}
		}

		return result;
	}
}
