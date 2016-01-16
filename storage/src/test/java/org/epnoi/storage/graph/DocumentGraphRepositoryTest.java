package org.epnoi.storage.graph;

import org.epnoi.storage.graph.domain.DocumentNode;
import org.epnoi.storage.graph.domain.relationships.TopicDealtByDocument;
import org.epnoi.storage.graph.repository.BaseGraphRepository;
import org.epnoi.storage.graph.repository.DocumentGraphRepository;
import org.epnoi.storage.graph.repository.DomainGraphRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

/**
 * Created by cbadenes on 22/12/15.
 */
public class DocumentGraphRepositoryTest extends BaseGraphRepositoryTest<DocumentNode> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentGraphRepositoryTest.class);

    @Autowired
    DocumentGraphRepository repository;

    @Autowired
    DomainGraphRepository domainRepository;

    @Override
    public BaseGraphRepository<DocumentNode> getRepository() {
        return repository;
    }

    @Override
    public DocumentNode getEntity() {
        DocumentNode node = new DocumentNode();
        node.setUri("documents/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        node.setFormat("zip");
        node.setLanguage("en");
        node.setTitle("This is an example");
        node.setSubject("semantic web, e-science");
        node.setDescription("for testing purposes");
        node.setRights("GNU General Public License v3.0");
        node.setContent("Miniopterus aelleni is a bat in the genus Miniopterus found in the Comoro Islands and Madagascar. It is a small, brown bat, with a forearm length of 35 to 41 mm (1.4 to 1.6 in). The long tragus (a projection in the outer ear) has a broad base and a blunt or rounded tip. The uropatagium (tail membrane) is sparsely haired. The palate is flat and there are distinct diastemata (gaps) between the upper canines and premolars. Populations of this species were previously included in Miniopterus manavi, but recent molecular studies revealed that M");
        node.setTokens("Miniopterus aelleni be a bat in the genus Miniopterus find in the Comoro Islands and Madagascar . It be a small , brown bat , with a forearm length of 35 to 41 mm ( 1.4 to 1.6 in ) . The long tragus ( a projection in the outer ear ) have a broad base and a blunt or round tip . The uropatagium ( tail membrane ) be sparsely haired . The palate be flat and there be distinct diastema ( gap ) between the upper canine and premolar . Populations of this specie be previously include in Miniopterus manavi , but recent molecular study reveal that M ");
        return node;
    }


    @Test
    public void getDeals(){
        Iterable<TopicDealtByDocument> deals = repository.dealsInAnalysis("http://epnoi.org/documents/fb58705e-65fe-41ad-8f92-b8802d3a43e2","http://epnoi.org/analyses/e8ac7764-f0ac-4e9b-841c-a4b07e076d5f");

        Iterator<TopicDealtByDocument> it = deals.iterator();
        while(it.hasNext()) {
            LOG.info("Deal: " + it.next());
        }
    }


    @Test
    public void similar(){
        repository.deleteSimilarRelationsInDomain("http://epnoi.org/domains/1f02ae0b-7d96-42c6-a944-25a3050bf1e2");
    }

}
