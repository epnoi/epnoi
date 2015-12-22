package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.relationships.*;
import org.epnoi.storage.model.Topic;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Topic")
@Data
@EqualsAndHashCode(exclude={"id"})
public class TopicNode extends Topic {

    @GraphId
    private Long id;

    @Index
    private String uri;
    
    // Outgoing
    private List<EmergesInTopicDomain> emergesIn;
    private List<MentionsTopicWord> mentions;

    // Incoming
    private List<DealsDocumentTopic> dealsFromDocument;
    private List<DealsItemTopic> dealsFromItem;
    private List<DealsPartTopic> dealsFromPart;

}
