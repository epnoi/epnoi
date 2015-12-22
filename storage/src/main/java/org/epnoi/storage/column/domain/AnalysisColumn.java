package org.epnoi.storage.column.domain;

import lombok.Data;
import org.epnoi.storage.model.Analysis;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "analyses")
@Data
public class AnalysisColumn extends Analysis{

    @PrimaryKey
    private String uri;
}
