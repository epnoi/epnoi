package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.PartNode;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface PartGraphRepository extends BaseGraphRepository<PartNode> {

    // To avoid a class type exception
    @Override
    PartNode findOneByUri(String uri);

}
