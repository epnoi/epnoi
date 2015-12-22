package org.epnoi.storage.graph;

import org.epnoi.storage.graph.domain.DomainNode;
import org.epnoi.storage.graph.repository.BaseGraphRepository;
import org.epnoi.storage.graph.repository.DomainGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class DomainGraphRepositoryTest extends BaseGraphRepositoryTest<DomainNode> {

    @Autowired
    DomainGraphRepository repository;

    @Override
    public BaseGraphRepository<DomainNode> getRepository() {
        return repository;
    }

    @Override
    public DomainNode getEntity() {
        DomainNode node = new DomainNode();
        node.setUri("domains/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        node.setDescription("for testing purposes");
        node.setName("test");
        return node;
    }
}
