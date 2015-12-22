package org.epnoi.storage.document.domain;

import lombok.Data;
import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Source;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by cbadenes on 22/12/15.
 */
@Document(indexName="research", type="analyses")
@Data
public class AnalysisDocument extends Analysis{

    @Id
    private String uri;
}
