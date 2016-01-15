package org.epnoi.storage;

import org.epnoi.model.Event;
import org.epnoi.model.Resource;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.column.domain.*;
import org.epnoi.storage.column.repository.*;
import org.epnoi.storage.document.domain.*;
import org.epnoi.storage.document.repository.*;
import org.epnoi.storage.graph.domain.*;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.graph.repository.*;
import org.epnoi.storage.model.*;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 23/12/15.
 */
@Component
public class UDM {

    private static final Logger LOG = LoggerFactory.getLogger(UDM.class);

    @Autowired
    EventBus eventBus;

    @Autowired
    SessionFactory sessionFactory;

    Neo4jSession session;

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

    @Value("${epnoi.neo4j.contactpoints}")
    String neo4jHost;

    @Value("${epnoi.neo4j.port}")
    String neo4jPort;

    @PostConstruct
    public void setup(){
        this.session = (Neo4jSession) sessionFactory.openSession("http://"+neo4jHost+":"+neo4jPort);
    }

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
        //Publish the event
        eventBus.post(Event.from(source), RoutingKey.of(org.epnoi.model.Resource.Type.SOURCE, org.epnoi.model.Resource.State.CREATED));
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
        //Publish the event
        eventBus.post(Event.from(domain), RoutingKey.of(org.epnoi.model.Resource.Type.DOMAIN, org.epnoi.model.Resource.State.CREATED));
    }

    public void saveDocument(Document document, String sourceURI){
        LOG.debug("trying to save :" + document + " from source: " + sourceURI);

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

        LOG.info("resource created :" + document);

        //Publish Document.Created
        eventBus.post(Event.from(document), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.CREATED));

        // relate to Source
        relateDocumentToSource(document.getUri(),sourceURI,document.getCreationTime());
    }

    public void saveItem(Item item, String documentURI){
        LOG.debug("trying to save :" + item + " from document: " + documentURI);

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

        //Publish Item.Created
        eventBus.post(Event.from(item), RoutingKey.of(Resource.Type.ITEM, Resource.State.CREATED));

        // relate to Document
        relateItemToDocument(item.getUri(),documentURI);
    }

    public void savePart(Part part, String itemURI){
        LOG.debug("trying to save :" + part + " from Item: " + itemURI);

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

        //Publish the event
        eventBus.post(Event.from(part), RoutingKey.of(Resource.Type.PART, Resource.State.CREATED));

        // Relate to Item
        relateItemToPart(itemURI,part.getUri());
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
        //Publish the event
        eventBus.post(Event.from(word), RoutingKey.of(Resource.Type.WORD, Resource.State.CREATED));
    }

    public void saveRelation(Relation relation){
        LOG.debug("trying to save :" + relation);
        // column
        relationColumnRepository.save(ResourceUtils.map(relation, RelationColumn.class));
        // document
        relationDocumentRepository.save(ResourceUtils.map(relation, RelationDocument.class));
        LOG.info("resource saved :" + relation);
        //Publish the event
        eventBus.post(Event.from(relation), RoutingKey.of(Resource.Type.RELATION, Resource.State.CREATED));
    }

    public void saveTopic(Topic topic, String domainURI, String analysisURI){
        LOG.debug("trying to save :" + topic);

        // column
        topicColumnRepository.save(ResourceUtils.map(topic, TopicColumn.class));

        // document
        topicDocumentRepository.save(ResourceUtils.map(topic, TopicDocument.class));

//        Transaction transaction = session.beginTransaction();
//        session.save(ResourceUtils.map(topic, TopicNode.class));
//        transaction.commit();

        // graph : TODO Set unique Long id for node
        topicGraphRepository.save(ResourceUtils.map(topic, TopicNode.class));

        LOG.info("resource saved :" + topic);

        //Publish the event
        eventBus.post(Event.from(topic), RoutingKey.of(Resource.Type.TOPIC, Resource.State.CREATED));

        // Relate topic to domain
        relateDomainToTopic(domainURI, topic.getUri(), topic.getCreationTime(), analysisURI);
    }

    public void saveAnalysis(Analysis analysis){
        LOG.debug("trying to save :" + analysis);
        // column
        analysisColumnRepository.save(ResourceUtils.map(analysis, AnalysisColumn.class));
        // document
        analysisDocumentRepository.save(ResourceUtils.map(analysis, AnalysisDocument.class));
        LOG.info("resource saved :" + analysis);
        //Publish the event
        eventBus.post(Event.from(analysis), RoutingKey.of(Resource.Type.ANALYSIS, Resource.State.CREATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(sourceNode,Source.class)), RoutingKey.of(Resource.Type.SOURCE, Resource.State.UPDATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(domainNode,Domain.class)), RoutingKey.of(Resource.Type.DOMAIN, Resource.State.UPDATED));
    }

    public void relateDocumentToDocument(String documentURI1, String documentURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate document: " + documentURI1 + " to document: " + documentURI2 + " with weight: " + weight + " and domain: " + domainURI);
        // Document
        DocumentNode documentNode1 = documentGraphRepository.findOneByUri(documentURI1);
        // Document
        DocumentNode documentNode2 = documentGraphRepository.findOneByUri(documentURI2);


        SimilarDocument relation1 = new SimilarDocument();
        relation1.setX(documentNode1);
        relation1.setY(documentNode2);
        relation1.setWeight(weight);
        relation1.setDomain(domainURI);
        documentNode1.addSimilarRelation(relation1);
        documentGraphRepository.save(documentNode1);

        SimilarDocument relation2 = new SimilarDocument();
        relation2.setX(documentNode2);
        relation2.setY(documentNode1);
        relation2.setWeight(weight);
        relation2.setDomain(domainURI);
        documentNode2.addSimilarRelation(relation2);
        documentGraphRepository.save(documentNode2);
        LOG.info("Document: " + documentURI1 + " related to document: " + documentURI2);

        //Publish the events
        eventBus.post(Event.from(ResourceUtils.map(documentNode1,Document.class)), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));
        eventBus.post(Event.from(ResourceUtils.map(documentNode2,Document.class)), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(documentNode,Document.class)), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));
    }

    public void relateItemToItem(String itemURI1, String itemURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate item: " + itemURI1 + " to item: " + itemURI2+ " with weight: " + weight + " in domain: " + domainURI);
        // Item
        ItemNode itemNode1 = itemGraphRepository.findOneByUri(itemURI1);
        // Item
        ItemNode itemNode2 = itemGraphRepository.findOneByUri(itemURI2);


        SimilarItem relation1 = new SimilarItem();
        relation1.setX(itemNode1);
        relation1.setY(itemNode2);
        relation1.setWeight(weight);
        relation1.setDomain(domainURI);
        itemNode1.addSimilarRelation(relation1);
        itemGraphRepository.save(itemNode1);

        SimilarItem relation2 = new SimilarItem();
        relation2.setX(itemNode2);
        relation2.setY(itemNode1);
        relation2.setWeight(weight);
        relation2.setDomain(domainURI);
        itemNode2.addSimilarRelation(relation2);
        itemGraphRepository.save(itemNode2);
        LOG.info("Item: " + itemURI1 + " related to item: " + itemURI2);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(itemNode1,Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.UPDATED));
        eventBus.post(Event.from(ResourceUtils.map(itemNode2,Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.UPDATED));
    }


    public void relatePartToPart(String partURI1, String partURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate part: " + partURI1 + " to part: " + partURI2 + " with weight: " + weight + " in domain: " + domainURI);
        // Part
        PartNode partNode1 = partGraphRepository.findOneByUri(partURI1);
        // Part
        PartNode partNode2 = partGraphRepository.findOneByUri(partURI2);


        SimilarPart relation1 = new SimilarPart();
        relation1.setX(partNode1);
        relation1.setY(partNode2);
        relation1.setWeight(weight);
        relation1.setDomain(domainURI);
        partNode1.addSimilarRelation(relation1);
        partGraphRepository.save(partNode1);

        SimilarPart relation2 = new SimilarPart();
        relation2.setX(partNode2);
        relation2.setY(partNode1);
        relation2.setWeight(weight);
        relation2.setDomain(domainURI);
        partNode2.addSimilarRelation(relation2);
        partGraphRepository.save(partNode2);
        LOG.info("Part: " + partURI1 + " related to part: " + partURI2);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(partNode1,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
        eventBus.post(Event.from(ResourceUtils.map(partNode2,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(partNode,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(itemNode,Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.UPDATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(partNode,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
    }

    public void relateWordToWord(String wordURI1, String wordURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate word: " + wordURI1 + " to word: " + wordURI2 + " with weight: " + weight + " and domain: " + domainURI);
        // Word
        WordNode wordNode1 = wordGraphRepository.findOneByUri(wordURI1);
        // Word
        WordNode wordNode2 = wordGraphRepository.findOneByUri(wordURI2);


        SimilarWord relation = new SimilarWord();
        relation.setX(wordNode1);
        relation.setY(wordNode2);
        relation.setWeight(weight);
        relation.setDomain(domainURI);

        wordNode1.addSimilarRelation(relation);
        wordGraphRepository.save(wordNode1);
        wordNode2.addSimilarRelation(relation);
        wordGraphRepository.save(wordNode2);
        LOG.info("Word: " + wordURI1 + " related to word: " + wordURI2);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(wordNode1,Word.class)), RoutingKey.of(Resource.Type.WORD, Resource.State.UPDATED));
        eventBus.post(Event.from(ResourceUtils.map(wordNode2,Word.class)), RoutingKey.of(Resource.Type.WORD, Resource.State.UPDATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(documentNode,Document.class)), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(itemNode,Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.UPDATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(partNode,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(topicNode,Topic.class)), RoutingKey.of(Resource.Type.TOPIC, Resource.State.UPDATED));
    }

    public void relateWordToDomain(String wordURI, String domainURI, String vector){
        LOG.debug("Trying to relate word: " + wordURI + " to domain: " + domainURI+ " with vector: " + vector);
        // Word
        WordNode wordNode = wordGraphRepository.findOneByUri(wordURI);
        // Topic
        DomainNode domainNode = domainGraphRepository.findOneByUri(domainURI);

        EmbeddedWordInDomain relation = new EmbeddedWordInDomain();
        relation.setDomain(domainNode);
        relation.setWord(wordNode);
        relation.setVector(vector);

        wordNode.addEmbeddedRelation(relation);
        wordGraphRepository.save(wordNode);
        LOG.info("Word: " + wordURI + " related to domain: " + domainURI);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(wordNode,Word.class)), RoutingKey.of(Resource.Type.WORD, Resource.State.UPDATED));
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(topicNode,Topic.class)), RoutingKey.of(Resource.Type.TOPIC, Resource.State.UPDATED));
    }


    /******************************************************************************
     * Find
     ******************************************************************************/

    public List<String> findDocumentsByDomain(String uri){
        LOG.debug("Finding documents in domain: " + uri);
        List<String> uris = new ArrayList<>();
        documentGraphRepository.findByDomain(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Documents: " + uris);
        return uris;
    }

    public List<Relationship> findDealsByDocumentAndAnalysis(String documentURI, String analysisURI){
        LOG.debug("Finding deals for document: " + documentURI + " in analysis: " + analysisURI);
        List<Relationship> relationships = new ArrayList<>();
        documentGraphRepository.dealsInAnalysis(documentURI,analysisURI).forEach(x -> relationships.add(new Relationship(x.getTopic().getUri(),x.getWeight())));
        LOG.info("Deals: " + relationships);
        return relationships;
    }


    public List<String> findItemsByDomain(String uri){
        LOG.debug("Finding items in domain: " + uri);
        List<String> uris = new ArrayList<>();
        itemGraphRepository.findByDomain(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Items: " + uris);
        return uris;
    }

    public List<String> findItemsByDocument(String uri){
        LOG.debug("Finding items in document: " + uri);
        List<String> uris = new ArrayList<>();
        itemGraphRepository.findByDocument(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Items: " + uris);
        return uris;
    }

    public Optional<String> findItemByPart(String uri){
        LOG.debug("Finding item in part: " + uri);
        List<String> uris = new ArrayList<>();
        itemGraphRepository.findByPart(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Items: " + uris);
        if ((uris != null) && (!uris.isEmpty())) return Optional.of(uris.get(0));
        return Optional.empty();
    }

    public List<Relationship> findDealsByItemAndAnalysis(String itemURI, String analysisURI){
        LOG.debug("Finding deals for item: " + itemURI + " in analysis: " + analysisURI);
        List<Relationship> relationships = new ArrayList<>();
        itemGraphRepository.dealsInAnalysis(itemURI,analysisURI).forEach(x -> relationships.add(new Relationship(x.getTopic().getUri(),x.getWeight())));
        LOG.info("Deals: " + relationships);
        return relationships;
    }


    public List<String> findPartsByDomain(String uri){
        LOG.debug("Finding parts in domain: " + uri);
        List<String> uris = new ArrayList<>();
        partGraphRepository.findByDomain(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Parts: " + uris);
        return uris;
    }

    public List<String> findPartsByItem(String uri){
        LOG.debug("Finding parts in item: " + uri);
        List<String> uris = new ArrayList<>();
        partGraphRepository.findByItem(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Parts: " + uris);
        return uris;
    }

    public List<String> findTopicsByDomain(String uri){
        LOG.debug("Finding topics in domain: " + uri);
        List<String> uris = new ArrayList<>();
        topicGraphRepository.findByDomain(uri).forEach(x -> uris.add(x.getUri()));
        LOG.info("Topics: " + uris);
        return uris;
    }

    public List<Relationship> findDealsByPartAndAnalysis(String partURI, String analysisURI){
        LOG.debug("Finding deals for part: " + partURI + " in analysis: " + analysisURI);
        List<Relationship> relationships = new ArrayList<>();
        partGraphRepository.dealsInAnalysis(partURI,analysisURI).forEach(x -> relationships.add(new Relationship(x.getTopic().getUri(),x.getWeight())));
        LOG.info("Deals: " + relationships);
        return relationships;
    }

    public Optional<String> findWordByContent(String content){
        return emptyOrFirst(wordColumnRepository.findByContent(content));
    }

    public Optional<String> findWordByLemma(String lemma){
        return emptyOrFirst(wordColumnRepository.findByLemma(lemma));
    }

    public Optional<String> findWordByPos(String pos){
        return emptyOrFirst(wordColumnRepository.findByPos(pos));
    }

    public Optional<String> findWordByStem(String stem){
        return emptyOrFirst(wordColumnRepository.findByStem(stem));
    }

    public Optional<String> findWordByType(String type){
        return emptyOrFirst(wordColumnRepository.findByType(type));
    }

    private Optional<String> emptyOrFirst(Iterable<? extends org.epnoi.storage.model.Resource> resources){
        if (resources == null || !resources.iterator().hasNext()) return Optional.empty();
        return Optional.of(resources.iterator().next().getUri());
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

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(source,Source.class)), RoutingKey.of(Resource.Type.SOURCE, Resource.State.DELETED));
    }

    public void deleteDomain(String uri){
        // column
        domainColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        domainDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        DomainNode domain = domainGraphRepository.findOneByUri(uri);
        domainGraphRepository.delete(domain);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(domain,Domain.class)), RoutingKey.of(Resource.Type.DOMAIN, Resource.State.DELETED));
    }

    public void deleteDocument(String uri){
        // column
        documentColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        documentDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        DocumentNode document = documentGraphRepository.findOneByUri(uri);
        documentGraphRepository.delete(document);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(document,Document.class)), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.DELETED));
    }

    public void deleteItem(String uri){
        // column
        itemColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        itemDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        ItemNode item = itemGraphRepository.findOneByUri(uri);
        itemGraphRepository.delete(item);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(item,Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.DELETED));
    }

    public void deletePart(String uri){
        // column
        partColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        partDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        PartNode part = partGraphRepository.findOneByUri(uri);
        partGraphRepository.delete(part);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(part,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.DELETED));
    }

    public void deleteWord(String uri){
        // column
        wordColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        wordDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        WordNode word = wordGraphRepository.findOneByUri(uri);
        wordGraphRepository.delete(word);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(word,Word.class)), RoutingKey.of(Resource.Type.WORD, Resource.State.DELETED));
    }

    public void deleteTopic(String uri){
        // column
        topicColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        topicDocumentRepository.delete(uri);
        // graph : TODO Get id directly from URI
        TopicNode topic = topicGraphRepository.findOneByUri(uri);
        topicGraphRepository.deleteAndDetach(uri);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(topic,Topic.class)), RoutingKey.of(Resource.Type.TOPIC, Resource.State.DELETED));
    }

    public void deleteRelation(String uri){
        // column
        relationColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        relationDocumentRepository.delete(uri);
        // graph : TODO remove relationships between WORD nodes

        //Publish the event
        Relation relation = new Relation();
        relation.setUri(uri);
        eventBus.post(Event.from(relation), RoutingKey.of(Resource.Type.RELATION, Resource.State.DELETED));
    }

    public void deleteAnalysis(String uri){
        // column
        analysisColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        analysisDocumentRepository.delete(uri);
        // graph : TODO remove TOPIC and/or RELATIONS of that analysis

        //Publish the event
        Analysis analysis = new Analysis();
        analysis.setUri(uri);
        eventBus.post(Event.from(analysis), RoutingKey.of(Resource.Type.ANALYSIS, Resource.State.DELETED));
    }

    public void deleteSimilarsInDomain(String uri){
        LOG.debug("trying to delete SIMILAR relationships in domain: " + uri);
        domainGraphRepository.deleteSimilarRelations(uri);
        LOG.info("Deleted SIMILAR relationships of Item: " + uri);
    }

    public void deleteSimilarsBetweenDocumentsInDomain(String uri){
        LOG.debug("trying to delete SIMILAR relationships between Documents in Domain: " + uri);
        documentGraphRepository.deleteSimilarRelationsInDomain(uri);
        LOG.info("Deleted SIMILAR relationships between Documents in Domain: " + uri);
    }

    public void deleteSimilarsBetweenItemsInDomain(String uri){
        LOG.debug("trying to delete SIMILAR relationships between Items in Domain: " + uri);
        itemGraphRepository.deleteSimilarRelationsInDomain(uri);
        LOG.info("Deleted SIMILAR relationships between Items in Domain: " + uri);
    }

    public void deleteSimilarsBetweenPartsInDomain(String uri){
        LOG.debug("trying to delete SIMILAR relationships between Parts in Domain: " + uri);
        partGraphRepository.deleteSimilarRelationsInDomain(uri);
        LOG.info("Deleted SIMILAR relationships between Parts in Domain: " + uri);
    }

    public void deleteSimilarsBetweenWordsInDomain(String uri){
        LOG.debug("trying to delete SIMILAR relationships between Words in Domain: " + uri);
        wordGraphRepository.deleteSimilarRelationsInDomain(uri);
        LOG.info("Deleted SIMILAR relationships between Words in Domain: " + uri);
    }

    public void deleteEmbeddingWordsInDomain(String uri){
        LOG.debug("trying to delete EMBEDDED relationships from Words to Domain: " + uri);
        wordGraphRepository.deleteEmbeddedRelationsInDomain(uri);
        LOG.info("Deleted EMBEDDED relationships from Words to Domain: " + uri);
    }

    public void deleteAll(){
        analysisColumnRepository.deleteAll();
        analysisDocumentRepository.deleteAll();

        documentColumnRepository.deleteAll();
        documentDocumentRepository.deleteAll();
        documentGraphRepository.deleteAll();

        domainColumnRepository.deleteAll();
        domainDocumentRepository.deleteAll();
        domainGraphRepository.deleteAll();

        itemColumnRepository.deleteAll();
        itemDocumentRepository.deleteAll();
        itemGraphRepository.deleteAll();

        partColumnRepository.deleteAll();
        partDocumentRepository.deleteAll();
        partGraphRepository.deleteAll();

        relationColumnRepository.deleteAll();
        relationDocumentRepository.deleteAll();

        sourceColumnRepository.deleteAll();
        sourceDocumentRepository.deleteAll();
        sourceGraphRepository.deleteAll();

        topicColumnRepository.deleteAll();
        topicDocumentRepository.deleteAll();
        topicGraphRepository.deleteAll();

        wordColumnRepository.deleteAll();
        wordDocumentRepository.deleteAll();
        wordGraphRepository.deleteAll();
    }

}
