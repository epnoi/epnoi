package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.SourceNode;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface SourceGraphRepository extends BaseGraphRepository<SourceNode> {

    // To avoid a class type exception
    @Override
    SourceNode findOneByUri(String uri);

}
