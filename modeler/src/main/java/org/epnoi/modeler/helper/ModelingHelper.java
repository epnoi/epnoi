package org.epnoi.modeler.helper;

import lombok.Data;
import org.epnoi.modeler.builder.AuthorBuilder;
import org.epnoi.modeler.builder.RegularResourceBuilder;
import org.epnoi.modeler.builder.TopicModelBuilder;
import org.epnoi.modeler.builder.WordEmbeddingBuilder;
import org.epnoi.storage.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 12/01/16.
 */
@Data
@Component
public class ModelingHelper {

    @Autowired
    SparkHelper sparkHelper;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    AuthorBuilder authorBuilder;

    @Autowired
    TimeGenerator timeGenerator;

    @Autowired
    TopicModelBuilder topicModelBuilder;

    @Autowired
    RegularResourceBuilder regularResourceBuilder;

    @Autowired
    WordEmbeddingBuilder wordEmbeddingBuilder;

    @Autowired
    UDM udm;
}
