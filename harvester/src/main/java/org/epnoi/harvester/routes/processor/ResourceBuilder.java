package org.epnoi.harvester.routes.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.epnoi.harvester.mining.TextMiner;
import org.epnoi.harvester.mining.annotation.AnnotatedDocument;
import org.epnoi.harvester.model.MetaInformation;
import org.epnoi.model.Record;
import org.epnoi.model.ResearchObject;
import org.epnoi.storage.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.URIGenerator;
import org.epnoi.storage.model.Document;
import org.epnoi.storage.model.Item;
import org.epnoi.storage.model.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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

        try{
            // Domain URI
            String domainUri        = exchange.getProperty(Record.DOMAIN_URI,String.class);

            // Source URI
            String sourceUri        = exchange.getProperty(Record.SOURCE_URI,String.class);

            LOG.info("Processing resource from source: " + sourceUri + " and domain: " + domainUri);

            // Metainformation
            MetaInformation metaInformation = new MetaInformation(exchange);

            // Attached file
            // TODO Handle multiple attached files
            String path             = exchange.getProperty(Record.PUBLICATION_URL_LOCAL,String.class).replace("."+metaInformation.getPubFormat(), "."+metaInformation.getFormat());
            AnnotatedDocument annotatedDocument = textMiner.annotate(path);
            String rawContent       = annotatedDocument.getContent();

            // Update Metainfo if empty
            updateMetainfoFromAnnotatedDoc(metaInformation,annotatedDocument);

            // Document
            Document document = createAndSaveDocument(metaInformation,rawContent,sourceUri,domainUri);

            // Item
            Item item = createAndSaveItem(metaInformation,rawContent,document.getUri());

            // Part:: Abstract
            Part abstractPart = createAndSavePart("abstract", annotatedDocument.getAbstractContent(), item.getUri());

            // Part:: Approach
            Part approachPart = createAndSavePart("approach", annotatedDocument.getApproachContent(), item.getUri());

            // Part:: Background
            Part backgroundPart = createAndSavePart("background", annotatedDocument.getBackgroundContent(), item.getUri());

            // Part:: Challenge
            Part challenge = createAndSavePart("challenge", annotatedDocument.getChallengeContent(), item.getUri());

            // Part:: Outcome
            Part outcome = createAndSavePart("outcome", annotatedDocument.getOutcomeContent(), item.getUri());

            // Part:: FutureWork
            Part futureWork = createAndSavePart("futureWork", annotatedDocument.getFutureWorkContent(), item.getUri());

            // Part:: Summary by Centroid
            Part summCentroid = createAndSavePart("summaryCentroid", annotatedDocument.getSummaryByCentroidContent(25), item.getUri());

            // Part:: Summary by Title Similarity
            Part summTitle = createAndSavePart("summaryTitle", annotatedDocument.getSummaryByTitleSimContent(25), item.getUri());

            // Convert to json
            String json = mapper.writeValueAsString(document);

            // Put in camel flow
            exchange.getIn().setHeader("FileName", StringUtils.replace(StringUtils.substringAfterLast("file://"+path, basedir), metaInformation.getFormat(), "json"));
            exchange.getIn().setBody(json, String.class);
        }catch (RuntimeException e){
            LOG.error("Error creating resources", e);
        }
    }

    private void updateMetainfoFromAnnotatedDoc(MetaInformation metaInformation, AnnotatedDocument annotatedDocument){
        // -> Update Title from parser if empty
        if (Strings.isNullOrEmpty(metaInformation.getTitle())){
            metaInformation.setTitle(annotatedDocument.getTitle());
            LOG.warn("Title load from annotated document: " + metaInformation.getTitle());
        }
        // -> Update Published from parser if empty
        if (Strings.isNullOrEmpty(metaInformation.getPublished())){
            metaInformation.setPublished(annotatedDocument.getYear());
            LOG.warn("Published date load from annotated document: " + metaInformation.getPublished());
        }
        // -> Update Authors from parser if empty
        if (Strings.isNullOrEmpty(metaInformation.getCreators())){
            metaInformation.setCreators(annotatedDocument.getAuthors().stream().map(author -> author.getFullName()).collect(Collectors.joining(";")));
            LOG.warn("Published date load from annotated document: " + metaInformation.getPublished());
        }
    }

    private Document createAndSaveDocument(MetaInformation metaInformation, String rawContent, String sourceUri, String domainUri){
        // Document
        Document document = new Document();
        document.setUri(uriGenerator.newDocument()); // Maybe better using PUBLICATION_URI
        document.setCreationTime(timeGenerator.getNowAsISO());
        document.setPublishedOn(metaInformation.getPublished());
        document.setPublishedBy(metaInformation.getSourceUri());
        document.setAuthoredOn(metaInformation.getAuthored());
        document.setAuthoredBy(metaInformation.getCreators());
        document.setContributedBy(metaInformation.getContributors());
        document.setRetrievedFrom(metaInformation.getSourceUrl());
        document.setRetrievedOn(timeGenerator.getNowAsISO()); //TODO hoarding time
        document.setFormat(metaInformation.getPubFormat());
        document.setLanguage(metaInformation.getLanguage());
        document.setTitle(metaInformation.getTitle());
        document.setSubject(metaInformation.getSubject());
        document.setDescription(metaInformation.getDescription());
        document.setRights(metaInformation.getRights());
        document.setType(metaInformation.getType());
        document.setContent(rawContent);

        String tokens   = textMiner.parse(rawContent).stream().filter(token -> token.isValid()).map(token -> token.getLemma()).collect(Collectors.joining(" "));
        document.setTokens(tokens);

        udm.saveDocument(document);
        // Relate Document To Source
        udm.relateDocumentToSource(document.getUri(), sourceUri, document.getCreationTime());
        // Relate Document to Domain
        udm.relateDocumentToDomain(document.getUri(), domainUri, document.getCreationTime());

        return document;
    }

    private Item createAndSaveItem(MetaInformation metaInformation, String rawContent, String documentUri){
        Item item = new Item();
        item.setUri(uriGenerator.newItem());
        item.setCreationTime(timeGenerator.getNowAsISO());
        item.setAuthoredOn(metaInformation.getAuthored());
        item.setAuthoredBy(metaInformation.getCreators());
        item.setContributedBy(metaInformation.getContributors());
        item.setFormat(metaInformation.getFormat());
        item.setLanguage(metaInformation.getLanguage());
        item.setTitle(metaInformation.getTitle());
        item.setSubject(metaInformation.getSubject());
        item.setDescription(metaInformation.getDescription());
        item.setUrl(metaInformation.getPubURI());
        item.setType(metaInformation.getType());
        item.setContent(rawContent);

        String tokens   = textMiner.parse(rawContent).stream().filter(token -> token.isValid()).map(token -> token.getLemma()).collect(Collectors.joining(" "));
        item.setTokens(tokens);

        udm.saveItem(item);
        // Relate Item to Document
        udm.relateItemToDocument(item.getUri(),documentUri);

        return item;
    }

    private Part createAndSavePart(String sense, String rawContent, String itemUri) {
        Part part = new Part();
        part.setUri(uriGenerator.newPart());
        part.setCreationTime(timeGenerator.getNowAsISO());
        part.setSense(sense);
        part.setContent(rawContent);

        String tokens   = textMiner.parse(rawContent).stream().filter(token -> token.isValid()).map(token -> token.getLemma()).collect(Collectors.joining(" "));
        part.setTokens(tokens);

        udm.savePart(part);
        // Relate Part to Item
        udm.relateItemToPart(itemUri,part.getUri());

        return part;
    }
}
