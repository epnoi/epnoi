package org.epnoi.storage.document.repository;

import org.epnoi.storage.model.Resource;
import org.epnoi.storage.model.Source;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 22/12/15.
 */
@NoRepositoryBean
public interface BaseDocumentRepository<T extends Resource> extends ElasticsearchRepository<T, String> {
}
