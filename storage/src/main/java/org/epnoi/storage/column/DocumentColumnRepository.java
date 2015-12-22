package org.epnoi.storage.column;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface DocumentColumnRepository extends BaseColumnRepository<DocumentColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from documents where title = ?0")
    Iterable<DocumentColumn> findByTitle(String title);

    @Query("select * from documents where format = ?0")
    Iterable<DocumentColumn> findByFormat(String format);

    @Query("select * from documents where language = ?0")
    Iterable<DocumentColumn> findByLanguage(String language);

    @Query("select * from documents where subject = ?0")
    Iterable<DocumentColumn> findBySubject(String subject);

    @Query("select * from documents where rights = ?0")
    Iterable<DocumentColumn> findByRights(String rights);
}
