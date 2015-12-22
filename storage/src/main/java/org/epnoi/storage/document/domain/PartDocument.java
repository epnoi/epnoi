package org.epnoi.storage.document.domain;

import lombok.Data;
import org.epnoi.storage.model.Item;
import org.epnoi.storage.model.Part;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="parts")
@Data
public class PartDocument extends Part {

    @Id
    private String uri;
}
