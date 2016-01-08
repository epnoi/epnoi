package org.epnoi.harvester.model;

import lombok.Data;
import org.apache.camel.Exchange;
import org.epnoi.model.Record;

import java.io.Serializable;

/**
 * Created by cbadenes on 07/01/16.
 */
@Data
public class MetaInformation implements Serializable{

    private String pubURI;
    private String pubFormat;
    private String sourceName;
    private String sourceUrl;
    private String sourceUri;
    private String title;
    private String published;
    private String authored;
    private String format;
    private String type;
    private String subject;
    private String language;
    private String rights;
    private String description;
    private String creators;
    private String contributors;

    public MetaInformation(Exchange exchange) {
        // Meta-Information
        this.pubURI           = exchange.getProperty(Record.PUBLICATION_URI, String.class);
        this.pubFormat        = exchange.getProperty(Record.PUBLICATION_METADATA_FORMAT,String.class);
        this.sourceName       = exchange.getProperty(Record.SOURCE_NAME, String.class);
        this.sourceUrl        = exchange.getProperty(Record.SOURCE_URL, String.class);
        this.sourceUri        = exchange.getProperty(Record.SOURCE_URI,String.class);

        // Publication
        this.title            = exchange.getProperty(Record.PUBLICATION_TITLE, String.class);
        this.published        = exchange.getProperty(Record.PUBLICATION_PUBLISHED, String.class);
        this.authored         = exchange.getProperty(Record.PUBLICATION_AUTHORED, String.class);
        this.format           = exchange.getProperty(Record.PUBLICATION_FORMAT, String.class);
        this.type             = exchange.getProperty(Record.PUBLICATION_TYPE, String.class);
        this.subject          = exchange.getProperty(Record.PUBLICATION_SUBJECT, String.class);
        this.language         = exchange.getProperty(Record.PUBLICATION_LANGUAGE, String.class);
        this.rights           = exchange.getProperty(Record.PUBLICATION_RIGHTS, String.class);
        this.description      = exchange.getProperty(Record.PUBLICATION_DESCRIPTION, String.class);
        this.creators         = exchange.getProperty(Record.PUBLICATION_CREATORS, String.class);
        this.contributors     = exchange.getProperty(Record.PUBLICATION_CONTRIBUTORS, String.class);

    }
}
