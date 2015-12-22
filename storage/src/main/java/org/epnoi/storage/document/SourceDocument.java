package org.epnoi.storage.document;

import lombok.Data;
import org.epnoi.storage.model.Source;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="sources")
@Data
public class SourceDocument extends Source{

    @Id
    private String uri;
}
