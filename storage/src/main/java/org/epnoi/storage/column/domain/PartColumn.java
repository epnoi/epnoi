package org.epnoi.storage.column.domain;

import lombok.Data;
import org.epnoi.storage.model.Part;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "parts")
@Data
public class PartColumn extends Part{

    @PrimaryKey
    private String uri;
}
