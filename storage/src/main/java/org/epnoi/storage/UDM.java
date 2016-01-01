package org.epnoi.storage;

import org.epnoi.storage.column.domain.*;
import org.epnoi.storage.column.repository.*;
import org.epnoi.storage.document.domain.*;
import org.epnoi.storage.document.repository.*;
import org.epnoi.storage.graph.domain.*;
import org.epnoi.storage.graph.repository.*;
import org.epnoi.storage.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 23/12/15.
 */
@Component
public class UDM {

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
        // column
        sourceColumnRepository.save(ResourceUtils.map(source, SourceColumn.class));
        // document
        sourceDocumentRepository.save(ResourceUtils.map(source, SourceDocument.class));
        // graph : TODO Set unique Long id for node
        sourceGraphRepository.save(ResourceUtils.map(source, SourceNode.class));
    }

    public void saveDomain(Domain domain){
        // column
        domainColumnRepository.save(ResourceUtils.map(domain, DomainColumn.class));
        // document
        domainDocumentRepository.save(ResourceUtils.map(domain, DomainDocument.class));
        // graph : TODO Set unique Long id for node
        domainGraphRepository.save(ResourceUtils.map(domain, DomainNode.class));
    }

    public void saveDocument(Document document){
        // column
        documentColumnRepository.save(ResourceUtils.map(document, DocumentColumn.class));
        // document
        documentDocumentRepository.save(ResourceUtils.map(document, DocumentDocument.class));
        // graph : TODO Set unique Long id for node
        documentGraphRepository.save(ResourceUtils.map(document, DocumentNode.class));
    }

    public void saveItem(Item item){
        // column
        itemColumnRepository.save(ResourceUtils.map(item, ItemColumn.class));
        // document
        itemDocumentRepository.save(ResourceUtils.map(item, ItemDocument.class));
        // graph : TODO Set unique Long id for node
        itemGraphRepository.save(ResourceUtils.map(item, ItemNode.class));
    }

    public void savePart(Part part){
        // column
        partColumnRepository.save(ResourceUtils.map(part, PartColumn.class));
        // document
        partDocumentRepository.save(ResourceUtils.map(part, PartDocument.class));
        // graph : TODO Set unique Long id for node
        partGraphRepository.save(ResourceUtils.map(part, PartNode.class));
    }

    public void saveWord(Word word){
        // column
        wordColumnRepository.save(ResourceUtils.map(word, WordColumn.class));
        // document
        wordDocumentRepository.save(ResourceUtils.map(word, WordDocument.class));
        // graph : TODO Set unique Long id for node
        wordGraphRepository.save(ResourceUtils.map(word, WordNode.class));
    }

    public void saveRelation(Relation relation){
        // column
        relationColumnRepository.save(ResourceUtils.map(relation, RelationColumn.class));
        // document
        relationDocumentRepository.save(ResourceUtils.map(relation, RelationDocument.class));
    }

    public void saveTopic(Topic topic){
        // column
        topicColumnRepository.save(ResourceUtils.map(topic, TopicColumn.class));
        // document
        topicDocumentRepository.save(ResourceUtils.map(topic, TopicDocument.class));
        // graph : TODO Set unique Long id for node
        topicGraphRepository.save(ResourceUtils.map(topic, TopicNode.class));
    }

    public void saveAnalysis(Analysis analysis){
        // column
        analysisColumnRepository.save(ResourceUtils.map(analysis, AnalysisColumn.class));
        // document
        analysisDocumentRepository.save(ResourceUtils.map(analysis, AnalysisDocument.class));
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
     * Delete
     ******************************************************************************/

    public void deleteSource(String uri){
        // column
        sourceColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        sourceDocumentRepository.delete(uri);
        // graph : TODO Set unique Long id for node
        Long nodeId = 0L;// calculate from URI
        sourceGraphRepository.delete(nodeId);
    }

    public void deleteDomain(String uri){
        // column
        domainColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        domainDocumentRepository.delete(uri);
        // graph : TODO Set unique Long id for node
        Long nodeId = 0L;// calculate from URI
        domainGraphRepository.delete(nodeId);
    }

    public void deleteDocument(String uri){
        // column
        documentColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        documentDocumentRepository.delete(uri);
        // graph : TODO Set unique Long id for node
        Long nodeId = 0L;// calculate from URI
        documentGraphRepository.delete(nodeId);
    }

    public void deleteItem(String uri){
        // column
        itemColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        itemDocumentRepository.delete(uri);
        // graph : TODO Set unique Long id for node
        Long nodeId = 0L;// calculate from URI
        itemGraphRepository.delete(nodeId);
    }

    public void deletePart(String uri){
        // column
        partColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        partDocumentRepository.delete(uri);
        // graph : TODO Set unique Long id for node
        Long nodeId = 0L;// calculate from URI
        partGraphRepository.delete(nodeId);
    }

    public void deleteWord(String uri){
        // column
        wordColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        wordDocumentRepository.delete(uri);
        // graph : TODO Set unique Long id for node
        Long nodeId = 0L;// calculate from URI
        wordGraphRepository.delete(nodeId);
    }

    public void deleteTopic(String uri){
        // column
        topicColumnRepository.delete(BasicMapId.id(ResourceUtils.URI,uri));
        // document
        topicDocumentRepository.delete(uri);
        // graph : TODO Set unique Long id for node
        Long nodeId = 0L;// calculate from URI
        topicGraphRepository.delete(nodeId);
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
