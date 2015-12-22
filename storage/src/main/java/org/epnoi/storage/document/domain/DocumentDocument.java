package org.epnoi.storage.document.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="documents")
@Data
public class DocumentDocument extends org.epnoi.storage.model.Document{

    @Id
    private String uri;
}
