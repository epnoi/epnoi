package org.epnoi.harvester.routes.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.epnoi.harvester.mining.TextMiner;
import org.epnoi.model.Record;
import org.epnoi.model.ResearchObject;
import org.epnoi.storage.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.URIGenerator;
import org.epnoi.storage.model.Document;
import org.epnoi.storage.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class ResourceBuilder implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceBuilder.class);

    private final ObjectMapper mapper;

    @Value("${epnoi.hoarder.storage.path}")
    protected String basedir;

    @Autowired
    UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    @Autowired
    TextMiner textMiner;

    public ResourceBuilder(){
        this.mapper = new ObjectMapper();
    }


    @Override
    public void process(Exchange exchange) throws Exception {

        // Meta-Information
        String pubURI           = exchange.getProperty(Record.PUBLICATION_URI, String.class);
        String pubFormat        = exchange.getProperty(Record.PUBLICATION_METADATA_FORMAT,String.class);
        String sourceName       = exchange.getProperty(Record.SOURCE_NAME, String.class);
        String sourceUrl        = exchange.getProperty(Record.SOURCE_URL, String.class);

        // Source URI
        String sourceUri        = exchange.getProperty(Record.SOURCE_URI,String.class);
        String sourceProtocol   = exchange.getProperty(Record.SOURCE_PROTOCOL, String.class);
        String title            = exchange.getProperty(Record.PUBLICATION_TITLE, String.class);
        String published        = exchange.getProperty(Record.PUBLICATION_PUBLISHED, String.class);
        String authored         = exchange.getProperty(Record.PUBLICATION_AUTHORED, String.class);
        String format           = exchange.getProperty(Record.PUBLICATION_FORMAT, String.class);
        String type             = exchange.getProperty(Record.PUBLICATION_TYPE, String.class);
        String subject          = exchange.getProperty(Record.PUBLICATION_SUBJECT, String.class);
        String language         = exchange.getProperty(Record.PUBLICATION_LANGUAGE, String.class);
        String rights           = exchange.getProperty(Record.PUBLICATION_RIGHTS, String.class);
        String description      = exchange.getProperty(Record.PUBLICATION_DESCRIPTION, String.class);
        String creators         = exchange.getProperty(Record.PUBLICATION_CREATORS, String.class);
        String contributors     = exchange.getProperty(Record.PUBLICATION_CONTRIBUTORS, String.class);

        // Text Mining
        String path             = exchange.getProperty(Record.PUBLICATION_URL_LOCAL,String.class).replace("."+pubFormat, "."+format);
        textMiner.parse(path);
        // TODO load metainfo from parser if empty

        // Current Time ISO-8601
        String currentTime      = timeGenerator.getNowAsISO();

        // Document
        Document document = new Document();
        document.setUri(uriGenerator.newDocument()); // Maybe better using PUBLICATION_URI
        document.setCreationTime(currentTime);
        document.setPublishedOn(published);
        document.setPublishedBy(sourceUri);
        document.setAuthoredOn(authored);
        document.setAuthoredBy(creators);
        document.setContributedBy(contributors);
        document.setRetrievedFrom(sourceUrl);
        document.setRetrievedOn(currentTime);
        document.setFormat(pubFormat);
        document.setLanguage(language);
        document.setTitle(title);
        document.setSubject(subject);
        document.setDescription(description);
        document.setRights(rights);
        document.setType(type);


        document.setContent("content");
        document.setTokens("tokens");

        LOG.info("Document created: " + document);

        // Save document
        udm.saveDocument(document);

        // Relate Document To Source
        udm.relateDocumentToSource(document.getUri(), sourceUri, document.getCreationTime());

        // Item
        Item item = new Item();
        item.setUri(uriGenerator.newItem());
        item.setCreationTime(currentTime);
        item.setAuthoredOn(authored);
        item.setAuthoredBy(creators);
        item.setContributedBy(contributors);
        item.setFormat(format);
        item.setLanguage(language);
        item.setTitle(title);
        item.setSubject(subject);
        item.setDescription(description);
        item.setUrl(pubURI);
        item.setType(type);

        item.setContent("content");
        item.setTokens("tokens");

        // Save Item
        udm.saveItem(item);

        LOG.info("Item created: " + item);

        // Relate Item to Document
        udm.relateItemToDocument(item.getUri(),document.getUri());

        // TODO Create parts from Text Mining

        // Convert to json
        String json = mapper.writeValueAsString(document);

        // Put in camel flow
        exchange.getIn().setHeader("FileName", StringUtils.replace(StringUtils.substringAfterLast("file://"+path, basedir), format, "json"));
        exchange.getIn().setBody(json, String.class);

    }
}
