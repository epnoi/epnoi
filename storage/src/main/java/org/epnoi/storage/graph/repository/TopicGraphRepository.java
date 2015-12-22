package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.TopicNode;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface TopicGraphRepository extends BaseGraphRepository<TopicNode> {

    // To avoid a class type exception
    @Override
    Iterable<TopicNode> findByUri(String uri);

}
