package org.epnoi.modeler.builder;

import es.cbadenes.lab.test.IntegrationTest;
import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.modeler.Config;
import org.epnoi.modeler.model.TopicData;
import org.epnoi.modeler.model.TopicDistribution;
import org.epnoi.modeler.model.TopicModel;
import org.epnoi.modeler.model.WordDistribution;
import org.epnoi.storage.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.URIGenerator;
import org.epnoi.storage.model.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by cbadenes on 11/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class TopicModelBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(TopicModelBuilderTest.class);

    @Autowired
    TopicModelBuilder topicModelBuilder;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    @Autowired
    UDM udm;

    @Autowired
    RegularResourceBuilder regularResourceBuilder;

    @Test
    public void basic(){

        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newSource());
        source.setCreationTime(timeGenerator.getNowAsISO());
        udm.saveSource(source);

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        domain.setName("test-domain");
        udm.saveDomain(domain);

        // Analysis
        Analysis analysis = new Analysis();
        analysis.setUri(uriGenerator.newAnalysis());
        analysis.setCreationTime(timeGenerator.getNowAsISO());
        analysis.setType("topic-model");
        analysis.setDomain(domain.getUri());
        analysis.setDescription("Topic Modeling using LDA");
        analysis.setConfiguration("");
        udm.saveAnalysis(analysis);

        // Document 1
        Document document1 = new Document();
        document1.setUri(uriGenerator.newDocument());
        document1.setCreationTime(timeGenerator.getNowAsISO());
        document1.setTitle("title-1");
        document1.setPublishedOn("20160112T12:07");
        udm.saveDocument(document1,source.getUri());
        udm.relateDocumentToDomain(document1.getUri(),domain.getUri(),document1.getCreationTime());

        // -> Item 1 from 1
        Item item11 = new Item();
        item11.setUri(uriGenerator.newItem());
        item11.setCreationTime(timeGenerator.getNowAsISO());
        udm.saveItem(item11,document1.getUri());

        // -> Item 2 from 1
        Item item12 = new Item();
        item12.setUri(uriGenerator.newItem());
        item12.setCreationTime(timeGenerator.getNowAsISO());
        udm.saveItem(item12,document1.getUri());

        // Document 2
        Document document2 = new Document();
        document2.setUri(uriGenerator.newDocument());
        document2.setCreationTime(timeGenerator.getNowAsISO());
        document2.setTitle("title-2");
        document2.setPublishedOn("20160112T12:07");
        udm.saveDocument(document2, source.getUri());
        udm.relateDocumentToDomain(document2.getUri(),domain.getUri(),document2.getCreationTime());

        // -> Item 1 from 2
        Item item21 = new Item();
        item21.setUri(uriGenerator.newItem());
        item21.setCreationTime(timeGenerator.getNowAsISO());
        udm.saveItem(item21,document2.getUri());

        // -> Item 2 from 2
        Item item22 = new Item();
        item22.setUri(uriGenerator.newItem());
        item22.setCreationTime(timeGenerator.getNowAsISO());
        udm.saveItem(item22,document2.getUri());

        // Creators
        User user = new User();
        user.setUri("http://epnoi.org/users/833a8399-29b5-4e75-9118-d434d9f0273e");
        List<User> creators = Arrays.asList(new User[]{user});


        List<RegularResource> rrs = new ArrayList<>();
        rrs.add(regularResourceBuilder.from(item11.getUri(),document1.getTitle(), document1.getPublishedOn(), creators, "house place forest"));
        rrs.add(regularResourceBuilder.from(item12.getUri(),document1.getTitle(), document1.getPublishedOn(), creators, "home joy forest"));
        rrs.add(regularResourceBuilder.from(item21.getUri(),document2.getTitle(), document2.getPublishedOn(), creators, "house person forest"));
        rrs.add(regularResourceBuilder.from(item22.getUri(),document2.getTitle(), document2.getPublishedOn(), creators, "house play home"));

        TopicModel model = topicModelBuilder.build(domain.getUri(), rrs);
        String creationTime = timeGenerator.getNowAsISO();


        LOG.info("Configuration: " + model.getConfiguration());


        Map<String,String> topicTable = new HashMap<>();
        for (TopicData topicData : model.getTopics()){

            // Save Topic
            Topic topic = new Topic();
            topic.setUri(uriGenerator.newTopic());
            topic.setAnalysis(analysis.getUri());
            topic.setCreationTime(creationTime);
            topic.setContent("content");
            udm.saveTopic(topic, domain.getUri(), analysis.getUri()); // Implicit relation to Domain (and Analysis)

            topicTable.put(topicData.getId(),topic.getUri());

            // Relate it to Words
            for (WordDistribution wordDistribution : topicData.getWords()){

                Optional<String> wordOptional = udm.findWordByLemma(wordDistribution.getWord());
                String wordURI;
                if (wordOptional.isPresent()){
                    wordURI = wordOptional.get();
                }else {
                    wordURI = uriGenerator.newWord();

                    // Create Word
                    Word word = new Word();
                    word.setUri(wordURI);
                    word.setLemma(wordDistribution.getWord());
                    udm.saveWord(word);

                }

                // Relate Topic to Word (mentions)
                udm.relateWordToTopic(wordURI,topic.getUri(),wordDistribution.getWeight());

            }
        }


        Set<String> resourceURIs = model.getResources().keySet();
        for (String resourceURI: resourceURIs){

            for (TopicDistribution topicDistribution: model.getResources().get(resourceURI)){
                // Relate resource (Item) to Topic
                String topicURI = topicTable.get(topicDistribution.getTopic());
                udm.relateTopicToItem(topicURI,resourceURI,topicDistribution.getWeight());
            }
        }
        LOG.info("Model built and saved: " + model);

    }

}
