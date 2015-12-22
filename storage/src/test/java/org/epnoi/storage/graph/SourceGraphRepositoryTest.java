package org.epnoi.storage.graph;

import org.epnoi.storage.graph.domain.SourceNode;
import org.epnoi.storage.graph.repository.BaseGraphRepository;
import org.epnoi.storage.graph.repository.SourceGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class SourceGraphRepositoryTest extends BaseGraphRepositoryTest<SourceNode> {

    @Autowired
    SourceGraphRepository repository;

    @Override
    public BaseGraphRepository<SourceNode> getRepository() {
        return repository;
    }

    @Override
    public SourceNode getEntity() {
        SourceNode node = new SourceNode();
        node.setUri("sources/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        node.setName("test");
        node.setDescription("for testing purposes");
        node.setUrl("http://epnoi.org");
        node.setProtocol("oaipmh");
        node.setDomain("domains/72ce5395-6268-439a-947e-802229e7f022");
        return node;
    }
}
