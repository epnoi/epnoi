package org.epnoi.storage.column;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface TopicColumnRepository extends BaseColumnRepository<TopicColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from topics where analysis = ?0")
    Iterable<TopicColumn> findByAnalysis(String analysis);
}
