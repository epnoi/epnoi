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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.stereotype.Component;

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

        DocumentProvidedBySource relation = new DocumentProvidedBySource();
        relation.setDate(date);
        relation.setSource(sourceNode);
        relation.setDocument(documentNode);
        sourceNode.addDocumentProvidedBySource(relation);
        LOG.debug("Saving relation: " + relation);
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

        ContainedDocument relation = new ContainedDocument();
        relation.setDocument(documentNode);
        relation.setDomain(domainNode);
        relation.setDate(date);
        domainNode.addContainedDocument(relation);
        LOG.debug("Saving relation: " + relation);
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


        SimilarDocument relation = new SimilarDocument();
        relation.setX(documentNode1);
        relation.setY(documentNode2);
        relation.setWeight(weight);
        relation.setDomain(domainURI);
        documentNode1.addSimilarDocument(relation);
        LOG.debug("Saving relation: " + relation);
        documentGraphRepository.save(documentNode1);

        LOG.info("Document: " + documentURI1 + " related to document: " + documentURI2);

        //Publish the events
        eventBus.post(Event.from(ResourceUtils.map(documentNode1,Document.class)), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));
    }

    public void relateItemToDocument(String itemURI, String documentURI){
        LOG.debug("Trying to relate item: " + itemURI + " to document: " + documentURI);
        // Item
        ItemNode itemNode = itemGraphRepository.findOneByUri(itemURI);
        // Document
        DocumentNode documentNode = documentGraphRepository.findOneByUri(documentURI);

        ItemBundledByDocument relation = new ItemBundledByDocument();
        relation.setDocument(documentNode);
        relation.setItem(itemNode);

        documentNode.addItemBundledByDocument(relation);
        LOG.debug("Saving relation: " + relation);
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

        SimilarItem relation = new SimilarItem();
        relation.setX(itemNode1);
        relation.setY(itemNode2);
        relation.setWeight(weight);
        relation.setDomain(domainURI);
        itemNode1.addSimilarItem(relation);
        LOG.debug("Saving relation: " + relation);
        itemGraphRepository.save(itemNode1);
        LOG.info("Item: " + itemURI1 + " related to item: " + itemURI2);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(itemNode1,Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.UPDATED));
    }


    public void relatePartToPart(String partURI1, String partURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate part: " + partURI1 + " to part: " + partURI2 + " with weight: " + weight + " in domain: " + domainURI);
        // Part
        PartNode partNode1 = partGraphRepository.findOneByUri(partURI1);
        // Part
        PartNode partNode2 = partGraphRepository.findOneByUri(partURI2);


        SimilarPart relation = new SimilarPart();
        relation.setX(partNode1);
        relation.setY(partNode2);
        relation.setWeight(weight);
        relation.setDomain(domainURI);
        partNode1.addSimilarPart(relation);
        LOG.debug("Saving relation: " + relation);
        partGraphRepository.save(partNode1);

        LOG.info("Part: " + partURI1 + " related to part: " + partURI2);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(partNode1,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
    }



    public void relateItemToPart(String itemURI, String partURI){
        LOG.debug("Trying to relate Part: " + partURI + " to Item: " + itemURI);
        // Part
        PartNode partNode = partGraphRepository.findOneByUri(partURI);
        // Item
        ItemNode itemNode = itemGraphRepository.findOneByUri(itemURI);

        ItemDescribedByPart relation = new ItemDescribedByPart();
        relation.setItem(itemNode);
        relation.setPart(partNode);
        partNode.addItemDescribedByPart(relation);
        LOG.debug("Saving relation: " + relation);
        partGraphRepository.save(partNode);
        LOG.info("Part: " + partURI + " related to Item: " + itemURI);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(partNode,Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
    }

    public void relateWordToItem(String wordURI, String itemURI, Long times){
        LOG.debug("Trying to relate word: " + wordURI + " to item: " + itemURI+ " with times: " + times);
        WordMentionedByItem relation = new WordMentionedByItem();
        relation.setItem(itemGraphRepository.findOneByUri(itemURI));
        relation.setWord(wordGraphRepository.findOneByUri(wordURI));
        relation.setTimes(times);
        relation.getItem().addWordMentionedByItem(relation);
        LOG.debug("Saving relation: " + relation);
        itemGraphRepository.save(relation.getItem());
        LOG.info("Word: " + wordURI + " related to item: " + itemURI);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(relation.getItem(),Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.UPDATED));
    }

    public void relateWordToPart(String wordURI, String partURI, Long times){
        LOG.debug("Trying to relate word: " + wordURI + " to part: " + partURI+ " with times: " + times );
        WordMentionedByPart relation = new WordMentionedByPart();
        relation.setPart(partGraphRepository.findOneByUri(partURI));
        relation.setWord(wordGraphRepository.findOneByUri(wordURI));
        relation.setTimes(times);
        relation.getPart().addWordMentionedByPart(relation);
        LOG.debug("Saving relation: " + relation);
        partGraphRepository.save(relation.getPart());
        LOG.info("Word: " + wordURI + " related to part: " + partURI);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(relation.getPart(),Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
    }

    public void relateWordToWord(String wordURI1, String wordURI2, Double weight, String domainURI){
        LOG.debug("Trying to relate word: " + wordURI1 + " to word: " + wordURI2 + " with weight: " + weight + " and domain: " + domainURI);
        PairedWord relation = new PairedWord();
        relation.setX(wordGraphRepository.findOneByUri(wordURI1));
        relation.setY(wordGraphRepository.findOneByUri(wordURI2));
        relation.setWeight(weight);
        relation.setDomain(domainURI);
        relation.getX().addPairedWord(relation);
        LOG.debug("Saving relation: " + relation);
        wordGraphRepository.save(relation.getX());
        LOG.info("Word: " + wordURI1 + " related to word: " + wordURI2);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(relation.getX(),Word.class)), RoutingKey.of(Resource.Type.WORD, Resource.State.UPDATED));
    }

    public void relateTopicToDocument(String topicURI, String documentURI, Double weight){
        LOG.debug("Trying to relate topic: " + topicURI + " to document: " + documentURI+ " with weight: " + weight);
        TopicDealtByDocument relation = new TopicDealtByDocument();
        relation.setDocument(documentGraphRepository.findOneByUri(documentURI));
        relation.setTopic(topicGraphRepository.findOneByUri(topicURI));
        relation.setWeight(weight);
        relation.getDocument().addTopicDealtByDocument(relation);
        LOG.debug("Saving relation: " + relation);
        documentGraphRepository.save(relation.getDocument());
        LOG.info("Topic: " + topicURI + " related to document: " + documentURI);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(relation.getDocument(),Document.class)), RoutingKey.of(Resource.Type.DOCUMENT, Resource.State.UPDATED));
    }

    public void relateTopicToItem(String topicURI, String itemURI, Double weight){
        LOG.debug("Trying to relate topic: " + topicURI + " to item: " + itemURI+ " with weight: " + weight );
        TopicDealtByItem relation = new TopicDealtByItem();
        relation.setItem(itemGraphRepository.findOneByUri(itemURI));
        relation.setTopic(topicGraphRepository.findOneByUri(topicURI));
        relation.setWeight(weight);
        relation.getItem().addTopicDealtByItem(relation);
        LOG.debug("Saving relation: " + relation);
        itemGraphRepository.save(relation.getItem());
        LOG.info("Topic: " + topicURI + " related to item: " + itemURI);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(relation.getItem(),Item.class)), RoutingKey.of(Resource.Type.ITEM, Resource.State.UPDATED));
    }

    public void relateTopicToPart(String topicURI, String partURI, Double weight){
        LOG.debug("Trying to relate topic: " + topicURI + " to part: " + partURI + " with weight: " + weight );
        TopicDealtByPart relation = new TopicDealtByPart();
        relation.setPart(partGraphRepository.findOneByUri(partURI));
        relation.setTopic(topicGraphRepository.findOneByUri(topicURI));
        relation.setWeight(weight);
        relation.getPart().addTopicDealtByPart(relation);
        LOG.debug("Saving relation: " + relation);
        partGraphRepository.save(relation.getPart());
        LOG.info("Topic: " + topicURI + " related to part: " + partURI);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(relation.getPart(),Part.class)), RoutingKey.of(Resource.Type.PART, Resource.State.UPDATED));
    }

    public void relateWordToTopic(String wordURI, String topicURI, Double weight){
        LOG.debug("Trying to relate word: " + wordURI + " to topic: " + topicURI+ " with weight: " + weight);
        WordMentionedByTopic relation = new WordMentionedByTopic();
        relation.setTopic(topicGraphRepository.findOneByUri(topicURI));
        relation.setWord(wordGraphRepository.findOneByUri(wordURI));
        relation.setWeight(weight);
        relation.getTopic().addWordMentionedByTopic(relation);
        LOG.debug("Saving relation: " + relation);
        topicGraphRepository.save(relation.getTopic());
        LOG.info("Word: " + wordURI + " related to topic: " + topicURI);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(relation.getTopic(),Topic.class)), RoutingKey.of(Resource.Type.TOPIC, Resource.State.UPDATED));
    }

    public void relateWordToDomain(String wordURI, String domainURI, String vector){
        LOG.debug("Trying to relate word: " + wordURI + " to domain: " + domainURI+ " with vector: " + vector);
        DomainInWord relation = new DomainInWord();
        relation.setDomain(domainGraphRepository.findOneByUri(domainURI));
        relation.setWord(wordGraphRepository.findOneByUri(wordURI));
        relation.setVector(vector);
        relation.getWord().addDomainInWord(relation);
        LOG.debug("Saving relation: " + relation);
        wordGraphRepository.save(relation.getWord());
        LOG.info("Word: " + wordURI + " related to domain: " + domainURI);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(relation.getWord(),Word.class)), RoutingKey.of(Resource.Type.WORD, Resource.State.UPDATED));
    }

    public void relateDomainToTopic(String domainURI, String topicURI, String date, String analysisURI){
        LOG.debug("Trying to relate domain: " + domainURI + " to topic: " + topicURI+ " in date: " + date + " by analysis: " + analysisURI);
        DomainInTopic relation = new DomainInTopic();
        relation.setTopic(topicGraphRepository.findOneByUri(topicURI));
        relation.setDomain(domainGraphRepository.findOneByUri(domainURI));
        relation.setDate(date);
        relation.setAnalysis(analysisURI);
        relation.getTopic().addDomainInTopic(relation);
        LOG.debug("Saving relation: " + relation);
        topicGraphRepository.save(relation.getTopic());
        LOG.info("Domain: " + domainURI + " related to topic: " + topicURI);

        //Publish the event
        eventBus.post(Event.from(ResourceUtils.map(relation.getTopic(),Topic.class)), RoutingKey.of(Resource.Type.TOPIC, Resource.State.UPDATED));
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
        wordGraphRepository.deletePairingInDomain(uri);
        LOG.info("Deleted SIMILAR relationships between Words in Domain: " + uri);
    }

    public void deleteEmbeddingWordsInDomain(String uri){
        LOG.debug("trying to delete EMBEDDED relationships from Words to Domain: " + uri);
        wordGraphRepository.deleteEmbeddingInDomain(uri);
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
