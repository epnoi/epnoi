package org.epnoi.storage.column.domain;

import lombok.Data;
import org.epnoi.storage.model.Document;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "documents")
@Data
public class DocumentColumn extends Document {

    @PrimaryKey
    private String uri;

}
