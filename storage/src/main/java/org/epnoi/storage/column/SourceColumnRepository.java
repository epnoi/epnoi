package org.epnoi.storage.column;

import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface SourceColumnRepository extends BaseColumnRepository<SourceColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from sources where name = ?0")
    Iterable<SourceColumn> findByName(String name);
}
