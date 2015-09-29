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
package org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.tutorial;

import java.util.Set;
import java.util.TreeSet;

import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.Category;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.Page;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.WikiConstants;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.exception.WikiPageNotFoundException;


/**
 * Tutorial 5
 * 
 * Wikipedia categories are used as a kind of semantic tag for pages.
 * They are organized in a thesaurus like structure.
 * 
 * If we get all pages assigned to categories in the sub-tree under the category for "Towns in Germany", 
 *   we can get a quite long list of towns in Germany.  
 * 
 * @author zesch
 *
 */
public class T5_TownList implements WikiConstants {

    public static void main(String[] args) throws WikiApiException {
        
        // configure the database connection parameters
        DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setHost("SERVER_URL");
        dbConfig.setDatabase("DATABASE");
        dbConfig.setUser("USER");
        dbConfig.setPassword("PASSWORD");
        dbConfig.setLanguage(Language.german);

        // Create a new German wikipedia.
        Wikipedia wiki = new Wikipedia(dbConfig);

        // Get the category "Towns in Germany"
        String title = "Towns in Germany";
        Category topCat;
        try {
            topCat = wiki.getCategory(title);
        } catch (WikiPageNotFoundException e) {
            throw new WikiApiException("Category " + title + " does not exist");
        }

        // Add the pages categorized under "Towns in Germany".
        Set<String> towns = new TreeSet<String>();
        for (Page p : topCat.getArticles()) {
            towns.add(p.getTitle().getPlainTitle());
        }
        
        // Get the pages categorized under each subcategory of "Towns in Germany".
        for (Category townCategory : topCat.getDescendants()) {
            for (Page p : townCategory.getArticles()) {
                towns.add(p.getTitle().getPlainTitle());
            }
            System.out.println("Number of towns: " + towns.size());
        }
        
        // Output the pages
        for (String town : towns) {
            System.out.println(town);
        }

    }
}
