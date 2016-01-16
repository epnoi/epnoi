package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.WordNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface WordGraphRepository extends BaseGraphRepository<WordNode> {

    @Override
    WordNode findOneByUri(String uri);

    @Query("match (in:Word)-[s{domain:{0}}]->(out:Word) delete s")
    void deletePairingInDomain(String uri);

    @Query("match (in:Word)-[e:EMBEDDED_IN]->(domain{uri:{0}}) delete e")
    void deleteEmbeddingInDomain(String uri);

}
