package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.relationships.MentionsItemWord;
import org.epnoi.storage.graph.domain.relationships.MentionsPartWord;
import org.epnoi.storage.graph.domain.relationships.MentionsTopicWord;
import org.epnoi.storage.model.Word;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Word")
@Data
@EqualsAndHashCode(exclude={"id"})
public class WordNode extends Word {

    @GraphId
    private Long id;

    @Index
    private String uri;

    // Incoming
    private List<MentionsItemWord> mentionsFromItem;
    private List<MentionsPartWord> mentionsFromPart;
    private List<MentionsTopicWord> mentionsFromTopic;


}
