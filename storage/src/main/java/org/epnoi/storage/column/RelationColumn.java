package org.epnoi.storage.column;

import lombok.Data;
import org.epnoi.storage.model.Relation;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "relations")
@Data
public class RelationColumn extends Relation{

    @PrimaryKey
    private String uri;
}
