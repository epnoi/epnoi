package org.epnoi.harvester.mining.annotation;

import edu.upf.taln.dri.lib.model.ext.Author;

/**
 * Created by cbadenes on 07/01/16.
 */
public class AnnotatedAuthor {

    private final Author author;

    public AnnotatedAuthor(Author author){
        this.author = author;
    }

    public String getFirstName(){
        return this.author.getFirstName();
    }

    public String getSurName(){
        return this.author.getSurname();
    }

    public String getFullName(){
        return this.getFullName();
    }

    public String getAffiliation(){
        return this.getAffiliation();
    }

    public String getPersonalPageURL(){
        return this.getPersonalPageURL();
    }
}
