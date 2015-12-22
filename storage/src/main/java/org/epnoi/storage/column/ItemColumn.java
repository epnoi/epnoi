package org.epnoi.storage.column;

import lombok.Data;
import org.epnoi.storage.model.Item;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "items")
@Data
public class ItemColumn extends Item {

    @PrimaryKey
    private String uri;

}
