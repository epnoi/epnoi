package org.epnoi.storage.graph.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.epnoi.storage.graph.domain.relationships.DealsPartTopic;
import org.epnoi.storage.graph.domain.relationships.DescribesPartItem;
import org.epnoi.storage.graph.domain.relationships.MentionsPartWord;
import org.epnoi.storage.graph.domain.relationships.SimilarPart;
import org.epnoi.storage.model.Part;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

/**
 * Created by cbadenes on 22/12/15.
 */
@NodeEntity(label = "Part")
@Data
@EqualsAndHashCode(exclude={"id"})
public class PartNode extends Part {

    @GraphId
    private Long id;

    @Index
    private String uri;

    // Undirected
    private List<SimilarPart> similar;

    // Outgoing
    private List<DealsPartTopic> deals;
    private List<DescribesPartItem> describes;
    private List<MentionsPartWord> mentions;


}
