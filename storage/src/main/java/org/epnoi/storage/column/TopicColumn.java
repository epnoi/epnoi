package org.epnoi.storage.column;

import lombok.Data;
import org.epnoi.storage.model.Topic;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "topics")
@Data
public class TopicColumn extends Topic{

    @PrimaryKey
    private String uri;
}
