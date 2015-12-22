package org.epnoi.storage.document;

import lombok.Data;
import org.epnoi.storage.model.Relation;
import org.epnoi.storage.model.Topic;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="relations")
@Data
public class RelationDocument extends Relation {

    @Id
    private String uri;
}
