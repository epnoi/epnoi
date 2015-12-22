package org.epnoi.storage.column.domain;

import lombok.Data;
import org.epnoi.storage.model.Domain;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 21/12/15.
 */
@Table(value = "domains")
@Data
public class DomainColumn extends Domain {

    @PrimaryKey
    private String uri;

}
