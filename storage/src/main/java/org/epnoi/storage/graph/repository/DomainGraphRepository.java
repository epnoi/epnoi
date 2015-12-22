package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.DomainNode;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface DomainGraphRepository extends BaseGraphRepository<DomainNode> {

    // To avoid a class type exception
    @Override
    DomainNode findOneByUri(String uri);
}
