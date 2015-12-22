package org.epnoi.storage.graph.repository;

import org.epnoi.storage.model.Resource;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 22/12/15.
 */
@NoRepositoryBean
public interface BaseGraphRepository<T extends Resource> extends GraphRepository<T>{

    T findOneByUri(String uri);

}
