package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.TopicNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface TopicGraphRepository extends BaseGraphRepository<TopicNode> {

    // To avoid a class type exception
    @Override
    TopicNode findOneByUri(String uri);

    @Query("match (topic)-[:EMERGES]->(domain{uri:{0}}) return topic")
    Iterable<TopicNode> findByDomain(String uri);

    @Query("match (topic{uri:{0}}) detach delete topic")
    void deleteAndDetach(String uri);

}
