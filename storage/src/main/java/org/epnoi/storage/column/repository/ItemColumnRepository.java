package org.epnoi.storage.column.repository;

import org.epnoi.storage.column.domain.ItemColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface ItemColumnRepository extends BaseColumnRepository<ItemColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from items where format = ?0")
    Iterable<ItemColumn> findByFormat(String format);

    @Query("select * from items where language = ?0")
    Iterable<ItemColumn> findByLanguage(String language);

    @Query("select * from items where title = ?0")
    Iterable<ItemColumn> findByTitle(String title);

    @Query("select * from items where subject = ?0")
    Iterable<ItemColumn> findBySubject(String subject);

    @Query("select * from items where url = ?0")
    Iterable<ItemColumn> findByUrl(String url);
}
