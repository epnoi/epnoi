package org.epnoi.storage.column;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface AnalysisColumnRepository extends BaseColumnRepository<AnalysisColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from analyses where type = ?0")
    Iterable<AnalysisColumn> findByType(String type);

    @Query("select * from analyses where description = ?0")
    Iterable<AnalysisColumn> findByDescription(String description);

    @Query("select * from analyses where configuration = ?0")
    Iterable<AnalysisColumn> findByConfiguration(String configuration);

}
