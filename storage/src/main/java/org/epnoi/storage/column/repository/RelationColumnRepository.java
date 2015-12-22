package org.epnoi.storage.column.repository;

import org.epnoi.storage.column.domain.RelationColumn;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface RelationColumnRepository extends BaseColumnRepository<RelationColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from relations where type = ?0")
    Iterable<RelationColumn> findByType(String type);

    @Query("select * from relations where describes = ?0")
    Iterable<RelationColumn> findByDescribes(String describes);
}
