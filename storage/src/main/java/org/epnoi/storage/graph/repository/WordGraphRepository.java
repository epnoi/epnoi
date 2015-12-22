package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.WordNode;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface WordGraphRepository extends BaseGraphRepository<WordNode> {

    // To avoid a class type exception
    @Override
    Iterable<WordNode> findByUri(String uri);

}
