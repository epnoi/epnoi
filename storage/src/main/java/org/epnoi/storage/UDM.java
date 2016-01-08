package org.epnoi.storage;

import org.epnoi.storage.column.domain.*;
import org.epnoi.storage.column.repository.*;
import org.epnoi.storage.document.domain.*;
import org.epnoi.storage.document.repository.*;
import org.epnoi.storage.graph.domain.*;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.graph.repository.*;
import org.epnoi.storage.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 23/12/15.
 */
@Component
public class UDM {

    private static final Logger LOG = LoggerFactory.getLogger(UDM.class);

    @Autowired
    SourceColumnRepository sourceColumnRepository;
    @Autowired
    SourceDocumentRepository sourceDocumentRepository;
    @Autowired
    SourceGraphRepository sourceGraphRepository;

    @Autowired
    DomainColumnRepository domainColumnRepository;
    @Autowired
    DomainDocumentRepository domainDocumentRepository;
    @Autowired
    DomainGraphRepository domainGraphRepository;

    @Autowired
    DocumentColumnRepository documentColumnRepository;
    @Autowired
    DocumentDocumentRepository documentDocumentRepository;
    @Autowired
    DocumentGraphRepository documentGraphRepository;

    @Autowired
    ItemColumnRepository itemColumnRepository;
    @Autowired
    ItemDocumentRepository itemDocumentRepository;
    @Autowired
    ItemGraphRepository itemGraphRepository;

    @Autowired
    PartColumnRepository partColumnRepository;
    @Autowired
    PartDocumentRepository partDocumentRepository;
    @Autowired
    PartGraphRepository partGraphRepository;

    @Autowired
    WordColumnRepository wordColumnRepository;
    @Autowired
    WordDocumentRepository wordDocumentRepository;
    @Autowired
    WordGraphRepository wordGraphRepository;

    @Autowired
    TopicColumnRepository topicColumnRepository;
    @Autowired
    TopicDocumentRepository topicDocumentRepository;
    @Autowired
    TopicGraphRepository topicGraphRepository;

    @Autowired
    RelationColumnRepository relationColumnRepository;
    @Autowired
    RelationDocumentRepository relationDocumentRepository;

    @Autowired
    AnalysisColumnRepository analysisColumnRepository;
    @Autowired
    AnalysisDocumentRepository analysisDocumentRepository;

    /******************************************************************************
     * Save
     ******************************************************************************/

    public void saveSource(Source source){
        LOG.debug("trying to save :" + source);
        // column
        sourceColumnRepository.save(ResourceUtils.map(source, SourceColumn.class));
        // document
        sourceDocumentRepository.save(ResourceUtils.map(source, SourceDocument.class));
        // graph : TODO Set unique Long id for node
        sourceGraphRepository.save(ResourceUtils.map(source, SourceNode.class));
        LOG.info("resource saved :" + source);
    }

    public void saveDomain(Domain domain){
        LOG.debug("trying to save :" + domain);
        // column
        domainColumnRepository.save(ResourceUtils.map(domain, DomainColumn.class));
        // document
        domainDocumentRepository.save(ResourceUtils.map(domain, DomainDocument.class));
        // graph : TODO Set unique Long id for node
        domainGraphRepository.save(ResourceUtils.map(domain, DomainNode.class));
        LOG.info("resource saved :" + domain);
    }

    public void saveDocument(Document document){
        LOG.debug("trying to save :" + document);
        // column
        documentColumnRepository.save(ResourceUtils.map(document, DocumentColumn.class));
        // document
        documentDocumentRepository.save(ResourceUtils.map(document, DocumentDocument.class));
        // graph : TODO Set unique Long id for node
        // graph : TODO Avoid content and tokens to save
        DocumentNode documentNode = ResourceUtils.map(document, DocumentNode.class);
        documentNode.setContent(null);
        documentNode.setTokens(null);
        documentGraphRepository.save(documentNode);
        LOG.info("resource saved :" + document);
    }

    public void saveItem(Item item){
        LOG.debug("trying to save :" + item);
        // column
        itemColumnRepository.save(ResourceUtils.map(item, ItemColumn.class));
        // document
        itemDocumentRepository.save(ResourceUtils.map(item, ItemDocument.class));
        // graph : TODO Set unique Long id for node
        // graph : TODO Avoid content and tokens to save
        ItemNode itemNode = ResourceUtils.map(item, ItemNode.class);
        itemNode.setContent(null);
        itemNode.setTokens(null);
        itemGraphRepository.save(itemNode);
        LOG.info("resource saved :" + item);
    }

    public void savePart(Part part){
        LOG.debug("trying to save :" + part);
        // column
        partColumnRepository.save(ResourceUtils.map(part, PartColumn.class));
        // document
        partDocumentRepository.save(ResourceUtils.map(part, PartDocument.class));
        // graph : TODO Set unique Long id for node
        // graph : TODO Avoid content and tokens to save
        PartNode partNode = ResourceUtils.map(part, PartNode.class);
        partNode.setContent(null);
        partNode.setTokens(null);
        partGraphRepository.save(partNode);
        LOG.info("resource saved :" + part);
    }

    public void saveWord(Word word){
        LOG.debug("trying to save :" + word);
        // column
        wordColumnRepository.save(ResourceUtils.map(word, WordColumn.class));
        // document
        wordDocumentRepository.save(ResourceUtils.map(word, WordDocument.class));
        // graph : TODO Set unique Long id for node
        wordGraphRepository.save(ResourceUtils.map(word, WordNode.class));
        LOG.info("resource saved :" + word);
    }

    public void saveRelation(Relation relation){
        LOG.debug("trying to save :" + relation);
        // column
        relationColumnRepository.save(ResourceUtils.map(relation, RelationColumn.class));
        // document
        relationDocumentRepository.save(ResourceUtils.map(relation, RelationDocument.class));
        LOG.info("resource saved :" + relation);
    }

    public void saveTopic(Topic topic){
        LOG.debug("trying to save :" + topic);
        // column
        topicColumnRepository.save(ResourceUtils.map(topic, TopicColumn.class));
        // document
        topicDocumentRepository.save(ResourceUtils.map(topic, TopicDocument.class));
        // graph : TODO Set unique Long id for node
        topicGraphRepository.save(ResourceUtils.map(topic, TopicNode.class));
        LOG.info("resource saved :" + topic);
    }

    public void saveAnalysis(Analysis analysis){
        LOG.debug("trying to save :" + analysis);
        // column
        analysisColumnRepository.save(ResourceUtils.map(analysis, AnalysisColumn.class));
        // document
        analysisDocumentRepository.save(ResourceUtils.map(analysis, AnalysisDocument.class));
        LOG.info("resource saved :" + analysis);
    }

    /******************************************************************************
     * Exist
     ******************************************************************************/

    public boolean existSource(String uri){
        return sourceColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existDomain(String uri){
        return domainColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existDocument(String uri){
        return documentColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existItem(String uri){
        return itemColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existPart(String uri){
        return partColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existWord(String uri){
        return wordColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existAnalysis(String uri){
        return analysisColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existRelation(String uri){
        return relationColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public boolean existTopic(String uri){
        return topicColumnRepository.exists(BasicMapId.id(ResourceUtils.URI,uri));
    }

    /******************************************************************************
     * Read
     ******************************************************************************/

    public Source readSource(String uri){
        return sourceColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public Domain readDomain(String uri){
        return domainColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public Document readDocument(String uri){
        return documentColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public Item readItem(String uri){
        return itemColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public Part readPart(String uri){
        return partColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public Word readWord(String uri){
        return wordColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public Relation readRelation(String uri){
        return relationColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public Topic readTopic(String uri){
        return topicColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI,uri));
    }

    public Analysis readAnalysis(String uri){
        return analysisColumnRepository.findOne(BasicMapId.id(ResourceUtils.URI,uri));
    }

    /******************************************************************************
     * Relate
     ******************************************************************************/
    // TODO review relations:: relation.ID to avoid duplicates

    public void relateDocumentToSource(String documentURI, String sourceURI, String date){
        LOG.debug("Trying to relate document: " + documentURI + " to source: " + sourceURI + " in: " + date);
        // Document
        DocumentNode documentNode = documentGraphRepository.findOneByUri(documentURI);
        // Source
        SourceNode sourceNode = sourceGraphRepository.findOneByUri(sourceURI);

        ProvidesSourceDocument relation = new ProvidesSourceDocument();
        relation.setDate(date);
        relation.setSource(sourceNode);
        relation.setDocument(documentNode);

        sourceNode.addProvideRelation(relation);
        sourceGraphRepository.save(sourceNode);
        LOG.info("Document: " + documentURI + " related to source: " + sourceURI + " in: " + date);
    }

    public void relateDocumentToDomain(String documentURI, String domainURI, String date){
        LOG.debug("Trying to relate document: " + documentURI + " to domain: " + domainURI + " in: " + date);
        // Document
        DocumentNode documentNode = documentGraphRepository.findOneByUri(documentURI);
        // Domain
        DomainNode domainNode = domainGraphRepository.findOneByUri(domainURI);

        ContainsDomainDocument relation = new ContainsDomainDocument();
        relation.setDocument(documentNode);
        relation.setDomain(domainNode);
        relation.setDate(date);

        domainNode.addContainRelation(relation);
        domainGraphRepository.save(domainNode);
        LOG.info("Document: " + documentURI + " related to domain: " + domainURI + " in: " + date);
    }

    public void relateDocumentToDocument(String documentURI1, String documentURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate document: " + documentURI1 + " to document: " + documentURI2 + " with weight: " + weight + " and domain: " + domainURI);
        // Document
        DocumentNode documentNode1 = documentGraphRepository.findOneByUri(documentURI1);
        // Document
        DocumentNode documentNode2 = documentGraphRepository.findOneByUri(documentURI2);


        SimilarDocument relation = new SimilarDocument();
        relation.setX(documentNode1);
        relation.setY(documentNode2);
        relation.setWeight(weight);
        relation.setDomain(domainURI);

        documentNode1.addSimilarRelation(relation);
        documentGraphRepository.save(documentNode1);
        documentNode2.addSimilarRelation(relation);
        documentGraphRepository.save(documentNode2);
        LOG.info("Document: " + documentURI1 + " related to document: " + documentURI2);
    }

    public void relateItemToDocument(String itemURI, String documentURI){
        LOG.debug("Trying to relate item: " + itemURI + " to document: " + documentURI);
        // Item
        ItemNode itemNode = itemGraphRepository.findOneByUri(itemURI);
        // Document
        DocumentNode documentNode = documentGraphRepository.findOneByUri(documentURI);

        BundleDocumentItem relation = new BundleDocumentItem();
        relation.setDocument(documentNode);
        relation.setItem(itemNode);

        documentNode.addBundleRelation(relation);
        documentGraphRepository.save(documentNode);
        LOG.info("Item: " + itemURI + " related to document: " + documentURI);
    }

    public void relateItemToItem(String itemURI1, String itemURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate item: " + itemURI1 + " to item: " + itemURI2+ " with weight: " + weight + " in domain: " + domainURI);
        // Item
        ItemNode itemNode1 = itemGraphRepository.findOneByUri(itemURI1);
        // Item
        ItemNode itemNode2 = itemGraphRepository.findOneByUri(itemURI2);


        SimilarItem relation = new SimilarItem();
        relation.setX(itemNode1);
        relation.setY(itemNode2);
        relation.setWeight(weight);
        relation.setDomain(domainURI);

        itemNode1.addSimilarRelation(relation);
        itemGraphRepository.save(itemNode1);
        itemNode2.addSimilarRelation(relation);
        itemGraphRepository.save(itemNode2);
        LOG.info("Item: " + itemURI1 + " related to item: " + itemURI2);
    }

    public void relateItemToPart(String itemURI, String partURI){
        LOG.debug("Trying to relate item: " + itemURI + " to part: " + partURI);
        // Part
        PartNode partNode = partGraphRepository.findOneByUri(partURI);
        // Item
        ItemNode itemNode = itemGraphRepository.findOneByUri(itemURI);

        DescribesPartItem relation = new DescribesPartItem();
        relation.setItem(itemNode);
        relation.setPart(partNode);

        partNode.addDescribeRelation(relation);
        partGraphRepository.save(partNode);
        LOG.info("Item: " + itemURI + " related to part: " + partURI);
    }

    public void relateWordToItem(String wordURI, String itemURI, Long times){
        LOG.debug("Trying to relate word: " + wordURI + " to item: " + itemURI+ " with times: " + times);
        // Word
        WordNode wordNode = wordGraphRepository.findOneByUri(wordURI);
        // Item
        ItemNode itemNode = itemGraphRepository.findOneByUri(itemURI);

        MentionsItemWord relation = new MentionsItemWord();
        relation.setItem(itemNode);
        relation.setWord(wordNode);
        relation.setTimes(times);

        itemNode.addMentionRelation(relation);
        itemGraphRepository.save(itemNode);
        LOG.info("Word: " + wordURI + " related to item: " + itemURI);
    }

    public void relateWordToPart(String wordURI, String partURI, Long times){
        LOG.debug("Trying to relate word: " + wordURI + " to part: " + partURI+ " with times: " + times );
        // Word
        WordNode wordNode = wordGraphRepository.findOneByUri(wordURI);
        // Part
        PartNode partNode = partGraphRepository.findOneByUri(partURI);

        MentionsPartWord relation = new MentionsPartWord();
        relation.setPart(partNode);
        relation.setWord(wordNode);
        relation.setTimes(times);

        partNode.addMentionRelation(relation);
        partGraphRepository.save(partNode);
        LOG.info("Word: " + wordURI + " related to part: " + partURI);
    }

    public void relateTopicToDocument(String topicURI, String documentURI, Double weight){
        LOG.debug("Trying to relate topic: " + topicURI + " to document: " + documentURI+ " with weight: " + weight);
        // Topic
        TopicNode topicNode = topicGraphRepository.findOneByUri(topicURI);
        // Document
        DocumentNode documentNode = documentGraphRepository.findOneByUri(documentURI);

        DealsDocumentTopic relation = new DealsDocumentTopic();
        relation.setDocument(documentNode);
        relation.setTopic(topicNode);
        relation.setWeight(weight);

        documentNode.addDealRelation(relation);
        documentGraphRepository.save(documentNode);
        LOG.info("Topic: " + topicURI + " related to document: " + documentURI);
    }

    public void relateTopicToItem(String topicURI, String itemURI, Double weight){
        LOG.debug("Trying to relate topic: " + topicURI + " to item: " + itemURI+ " with weight: " + weight );
        // Topic
        TopicNode topicNode = topicGraphRepository.findOneByUri(topicURI);
        // Item
        ItemNode itemNode = itemGraphRepository.findOneByUri(itemURI);

        DealsItemTopic relation = new DealsItemTopic();
        relation.setItem(itemNode);
        relation.setTopic(topicNode);
        relation.setWeight(weight);

        itemNode.addDealRelation(relation);
        itemGraphRepository.save(itemNode);
        LOG.info("Topic: " + topicURI + " related to item: " + itemURI);
    }

    public void relateTopicToPart(String topicURI, String partURI, Double weight){
        LOG.debug("Trying to relate topic: " + topicURI + " to part: " + partURI + " with weight: " + weight );
        // Topic
        TopicNode topicNode = topicGraphRepository.findOneByUri(topicURI);
        // Part
        PartNode partNode = partGraphRepository.findOneByUri(partURI);


        DealsPartTopic relation = new DealsPartTopic();
        relation.setPart(partNode);
        relation.setTopic(topicNode);
        relation.setWeight(weight);

        partNode.addDealRelation(relation);
        partGraphRepository.save(partNode);
        LOG.info("Topic: " + topicURI + " related to part: " + partURI);
    }

    public void relateWordToTopic(String wordURI, String topicURI, Double weight){
        LOG.debug("Trying to relate word: " + wordURI + " to topic: " + topicURI+ " with weight: " + weight);
        // Word
        WordNode wordNode = wordGraphRepository.findOneByUri(wordURI);
        // Topic
        TopicNode topicNode = topicGraphRepository.findOneByUri(topicURI);

        MentionsTopicWord relation = new MentionsTopicWord();
        relation.setTopic(topicNode);
        relation.setWord(wordNode);
        relation.setWeight(weight);

        topicNode.addMentionRelation(relation);
        topicGraphRepository.save(topicNode);
        LOG.info("Word: " + wordURI + " related to topic: " + topicURI);
    }

    public void relateDomainToTopic(String domainURI, String topicURI, String date, String analysisURI){
        LOG.debug("Trying to relate domain: " + domainURI + " to topic: " + topicURI+ " in date: " + date + " by analysis: " + analysisURI);
        // Domain
        DomainNode domainNode = domainGraphRepository.findOneByUri(domainURI);
        // Topic
        TopicNode topicNode = topicGraphRepository.findOneByUri(topicURI);

        EmergesInTopicDomain relation = new EmergesInTopicDomain();
        relation.setTopic(topicNode);
        relation.setDomain(domainNode);
        relation.setDate(date);
        relation.setAnalysis(analysisURI);

        topicNode.addEmergeRelation(relation);
        topicGraphRepository.save(topicNode);
        LOG.info("Domain: " + domainURI + " related to topic: " + topicURI);
    }



    /******************************************************************************
     * Delete
     ******************************************************************************/

    public void deleteSource(String uri){
        // column
        sourceColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        sourceDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        SourceNode source = sourceGraphRepository.findOneByUri(uri);
        sourceGraphRepository.delete(source);
    }

    public void deleteDomain(String uri){
        // column
        domainColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        domainDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        DomainNode domain = domainGraphRepository.findOneByUri(uri);
        domainGraphRepository.delete(domain);
    }

    public void deleteDocument(String uri){
        // column
        documentColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        documentDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        DocumentNode document = documentGraphRepository.findOneByUri(uri);
        documentGraphRepository.delete(document);
    }

    public void deleteItem(String uri){
        // column
        itemColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        itemDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        ItemNode item = itemGraphRepository.findOneByUri(uri);
        itemGraphRepository.delete(item);
    }

    public void deletePart(String uri){
        // column
        partColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        partDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        PartNode part = partGraphRepository.findOneByUri(uri);
        partGraphRepository.delete(part);
    }

    public void deleteWord(String uri){
        // column
        wordColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        wordDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        WordNode word = wordGraphRepository.findOneByUri(uri);
        wordGraphRepository.delete(word);
    }

    public void deleteTopic(String uri){
        // column
        topicColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        topicDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        TopicNode topic = topicGraphRepository.findOneByUri(uri);
        topicGraphRepository.delete(topic);
    }

    public void deleteRelation(String uri){
        // column
        relationColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        relationDocumentRepository.delete(uri);
        // graph : TODO remove relationships between WORD nodes
    }

    public void deleteAnalysis(String uri){
        // column
        analysisColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        analysisDocumentRepository.delete(uri);
        // graph : TODO remove TOPIC and/or RELATIONS of that analysis
    }

}
