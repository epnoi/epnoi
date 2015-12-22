package org.epnoi.storage.document;

import lombok.Data;
import org.epnoi.storage.model.Domain;
import org.epnoi.storage.model.Topic;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="topics")
@Data
public class TopicDocument extends Topic {

    @Id
    private String uri;
}
