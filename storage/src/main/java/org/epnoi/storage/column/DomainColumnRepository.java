package org.epnoi.storage.column;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

/**
 * Created by cbadenes on 21/12/15.
 */
public interface DomainColumnRepository extends BaseColumnRepository<DomainColumn> {

    //Future Version of Spring-Data-Cassandra will implements native queries

    @Query("select * from domains where name = ?0")
    Iterable<DomainColumn> findByName(String name);
    

}
