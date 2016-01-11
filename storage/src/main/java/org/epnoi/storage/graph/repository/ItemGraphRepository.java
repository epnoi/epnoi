package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.ItemNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ItemGraphRepository extends BaseGraphRepository<ItemNode> {

    // To avoid a class type exception
    @Override
    ItemNode findOneByUri(String uri);

    @Query("match (item)<-[:BUNDLES]-(document)<-[:CONTAINS]-(domain{uri:{0}}) return item")
    Iterable<ItemNode> findByDomain(String uri);

}
