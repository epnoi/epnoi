package org.epnoi.storage.column.domain;

import lombok.Data;
import org.epnoi.storage.model.Word;
import org.springframework.data.cassandra.mapping.Indexed;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by cbadenes on 22/12/15.
 */
@Table(value = "words")
@Data
public class WordColumn extends Word{

    @PrimaryKey
    private String uri;

    private String content;

    private String lemma;

    private String stem;

    private String pos;

    private String type;
}
