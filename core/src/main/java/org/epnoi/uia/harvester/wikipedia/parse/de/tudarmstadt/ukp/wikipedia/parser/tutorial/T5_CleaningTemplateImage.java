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
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.Page;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.mediawiki.FlushTemplates;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;

/**
 * Shows how to clean an article text from "TEMPLATE" and "Image" elements
 * 
 */

public class T5_CleaningTemplateImage {
	
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
		
		//get the page 'Dog'
		Page p = wiki.getPage("Dog");
		
		//get a ParsedPage object
		MediaWikiParserFactory pf = new MediaWikiParserFactory();
		pf.setTemplateParserClass(FlushTemplates.class); // Filtering TEMPLATE-Elements
		
		String IMAGE = "Image"; // Replace it with the image template name in your Wiki language edition,
								// e.g. "Image" in English
		
		// filtering Image-Elements
		pf.getImageIdentifers().add(IMAGE);	
		
		// parse page text
		MediaWikiParser parser = pf.createParser();
		ParsedPage pp = parser.parse(p.getText()); 
		
		System.out.println(pp.getText());	
	}
}
