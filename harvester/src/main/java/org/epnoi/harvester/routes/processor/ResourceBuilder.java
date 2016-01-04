package org.epnoi.harvester.routes.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.epnoi.model.Record;
import org.epnoi.model.ResearchObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public ResourceBuilder(){
        this.mapper = new ObjectMapper();
    }


    @Override
    public void process(Exchange exchange) throws Exception {

        ResearchObject researchObject = new ResearchObject();


        //TODO Build a valid Research Object from the meta-information

        // URI
        researchObject.setUri(exchange.getProperty(Record.PUBLICATION_URI, String.class));

        // URL
        String refFormat    = exchange.getProperty(Record.PUBLICATION_METADATA_FORMAT,String.class);
        String pubFormat    = exchange.getProperty(Record.PUBLICATION_FORMAT, String.class);
        String path         = exchange.getProperty(Record.PUBLICATION_URL_LOCAL,String.class).replace("."+refFormat, "."+pubFormat);
        String url          = "file://"+path;
//        researchObject.setUrl(url);


//        // Source
//        ResearchSource source = new ResearchSource();
        String sourceName   = exchange.getProperty(Record.SOURCE_NAME, String.class);
//        source.setName(sourceName);
        String sourceUrl    = exchange.getProperty(Record.SOURCE_URL, String.class);
//        source.setUrl(sourceUrl);
        String sourceUri    = exchange.getProperty(Record.SOURCE_URI,String.class)+StringUtils.substringAfter(sourceUrl,"//");
//        source.setUri(sourceUri);
        String sourceProtocol  = exchange.getProperty(Record.SOURCE_PROTOCOL, String.class);
//        source.setProtocol(sourceProtocol);
//        researchObject.setSource(source);
//
//        // Meta Information
//        MetaInformation metaInformation = new MetaInformation();
        String title        = exchange.getProperty(Record.PUBLICATION_TITLE, String.class);
//        metaInformation.setTitle(title);
        String published    = exchange.getProperty(Record.PUBLICATION_PUBLISHED, String.class);
//        metaInformation.setPublished();
        String format       = exchange.getProperty(Record.PUBLICATION_FORMAT, String.class);
//        metaInformation.setFormat(format);
        String language     = exchange.getProperty(Record.PUBLICATION_LANGUAGE, String.class);
//        metaInformation.setLanguage(language);
        String rights       = exchange.getProperty(Record.PUBLICATION_RIGHTS, String.class);
//        metaInformation.setRights(rights);
        String description  = exchange.getProperty(Record.PUBLICATION_DESCRIPTION, String.class);
//        metaInformation.setDescription(description);
//
//
//        // ->   Authors
//        Iterable<String> iterator = Splitter.on(';').trimResults().omitEmptyStrings().split(exchange.getProperty(Record.PUBLICATION_CREATORS, String.class));
//        ArrayList<String> authors = Lists.newArrayList(iterator);
//        List<Creator> creators = new ArrayList<Creator>();
//
//        for(String author: authors){
//
//            String[] tokens = author.split(",");
//
//            Creator creator = new Creator();
//            creator.setName(tokens[1].trim());
//            creator.setSurname(tokens[0].trim());
//            String authorUri = "http://resources.ressist.es/author/" + creator.getSurname().trim() + "-" + creator.getName().trim();
//            creator.setUri(UrlEscapers.urlFragmentEscaper().escape(authorUri));
//
//            creators.add(creator);
//        }
//
//        metaInformation.setCreators(creators);
//
//        // add metainformation to research object
//        researchObject.setMetainformation(metaInformation);
//
//        // BagOfWords: Lucene Stemming from PDF
//        List<LuceneClassifier.Keyword> values = LuceneClassifier.guessFromString(PDFExtractor.from(path));
//
//        List<String> words = new ArrayList<String>();
//
//        for(LuceneClassifier.Keyword keyword: values){
//            if (WordValidator.isValid(keyword.getStem())) words.add(keyword.getStem());
//        }
//        researchObject.setBagOfWords(words);


        // Convert to json
        String json = mapper.writeValueAsString(researchObject);

        // Put in camel flow
        exchange.getIn().setHeader("FileName", StringUtils.replace(StringUtils.substringAfterLast(url, basedir), format, "json"));
        exchange.getIn().setBody(json, String.class);


        LOG.info("ResearchObject as RegularResource: {}", researchObject);

    }
}
