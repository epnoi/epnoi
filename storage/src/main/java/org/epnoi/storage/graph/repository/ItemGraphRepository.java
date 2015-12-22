package org.epnoi.storage.graph.repository;

import org.epnoi.storage.graph.domain.ItemNode;
import org.springframework.stereotype.Repository;

/**
 * Created by cbadenes on 22/12/15.
 */
@Repository
public interface ItemGraphRepository extends BaseGraphRepository<ItemNode> {

    // To avoid a class type exception
    @Override
    Iterable<ItemNode> findByUri(String uri);

}
