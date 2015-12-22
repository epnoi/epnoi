package org.epnoi.storage.document;

import lombok.Data;
import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="items")
@Data
public class ItemDocument extends Item{

    @Id
    private String uri;
}
