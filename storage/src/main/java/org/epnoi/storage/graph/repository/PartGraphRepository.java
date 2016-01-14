package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.PartNode;
import org.epnoi.storage.graph.domain.relationships.DealsPartTopic;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface PartGraphRepository extends BaseGraphRepository<PartNode> {

    // To avoid a class type exception
    @Override
    PartNode findOneByUri(String uri);

    @Query("match (part)-[:DESCRIBES]->(item)<-[:BUNDLES]-(document)<-[:CONTAINS]-(domain{uri:{0}}) return part")
    Iterable<PartNode> findByDomain(String uri);

    @Query("match (part)-[:DESCRIBES]->(item{uri:{0}}) return part")
    Iterable<PartNode> findByItem(String uri);

    @Query("match (part{uri:{0}})-[d:DEALS]->(topic)-[e:EMERGES{analysis:{1}}]->(domain) return d")
    Iterable<DealsPartTopic> dealsInAnalysis(String part, String analysis);

    @Query("match (in:Part)-[s{domain:{0}}]->(out:Part) delete s")
    void deleteSimilarRelationsInDomain(String uri);

}
