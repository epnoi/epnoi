package org.epnoi.modeler.builder;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.Author;
import es.upm.oeg.epnoi.matching.metrics.domain.entity.Metadata;
import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.storage.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.collection.mutable.Buffer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 11/01/16.
 */
@Component
public class RegularResourceBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(RegularResourceBuilder.class);

    public RegularResource from(String uri, String title, String published, List<User> creators, String content){
        LOG.debug("Creating Regular Resource from URI: " + uri);
        Buffer bagOfWords = (Buffer) JavaConverters.asScalaBufferConverter(Arrays.asList(content.split(" "))).asScala();
        String url = uri;
        List<Author> authorList = creators.stream().map(user -> new Author(user.getUri(), user.getName(), user.getSurname())).collect(Collectors.toList());
        Buffer authors = (Buffer)JavaConverters.asScalaBufferConverter(authorList).asScala();
        Metadata metadata = new Metadata(title, published, authors);
        Seq<RegularResource> innerResources = null;
        return new RegularResource(uri, url, metadata, bagOfWords, innerResources);
    }

}
