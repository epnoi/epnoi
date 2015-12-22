package org.epnoi.storage.column;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface PartColumnRepository extends BaseColumnRepository<PartColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from parts where sense = ?0")
    Iterable<PartColumn> findBySense(String sense);


}
