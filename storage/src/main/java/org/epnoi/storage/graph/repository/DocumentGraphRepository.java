package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.DocumentNode;
import org.epnoi.storage.graph.domain.ItemNode;
import org.epnoi.storage.graph.domain.relationships.BundleDocumentItem;
import org.epnoi.storage.graph.domain.relationships.ContainsDomainDocument;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DocumentGraphRepository extends BaseGraphRepository<DocumentNode> {

    // To avoid a class type exception
    @Override
    DocumentNode findOneByUri(String uri);

    @Query("match (document)<-[:CONTAINS]-(domain{uri:{0}}) return document")
    Iterable<DocumentNode> findByDomain(String uri);

}
