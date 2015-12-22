package org.epnoi.storage.column;

import lombok.Data;
import org.epnoi.storage.model.Source;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 21/12/15.
 */
@Table(value = "sources")
@Data
public class SourceColumn extends Source {

    @PrimaryKey
    private String uri;

}
