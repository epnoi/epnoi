package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.DocumentNode;
import org.epnoi.storage.graph.domain.relationships.BundleDocumentItem;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DocumentGraphRepository extends BaseGraphRepository<DocumentNode> {

    // To avoid a class type exception
    DocumentNode findOneByUri(String uri, int depth);


    @Query("match (item)<-[:BUNDLES]-(document{uri:{0}}) return item")
    Iterable<BundleDocumentItem> getItems(String uri);

}
