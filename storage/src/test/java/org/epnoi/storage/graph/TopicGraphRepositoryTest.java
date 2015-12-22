package org.epnoi.storage.graph;

import org.epnoi.storage.graph.domain.TopicNode;
import org.epnoi.storage.graph.repository.BaseGraphRepository;
import org.epnoi.storage.graph.repository.TopicGraphRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class TopicGraphRepositoryTest extends BaseGraphRepositoryTest<TopicNode> {

    @Autowired
    TopicGraphRepository repository;

    @Override
    public BaseGraphRepository<TopicNode> getRepository() {
        return repository;
    }

    @Override
    public TopicNode getEntity() {
        TopicNode node = new TopicNode();
        node.setUri("topics/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        node.setContent("molecular color graphic rendering");
        node.setAnalysis("analysis/72ce5395-6268-439a-947e-802229e7f022");
        return node;
    }
}
