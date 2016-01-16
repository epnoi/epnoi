package org.epnoi.modeler.models.topic;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.model.Resource;
import org.epnoi.modeler.scheduler.ModelingTask;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.WordDistribution;
import org.epnoi.storage.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 11/01/16.
 */
public class TopicModeler extends ModelingTask {

    private static final Logger LOG = LoggerFactory.getLogger(TopicModeler.class);

    private static final String ANALYSIS_TYPE       = "topic-model";

    public TopicModeler(Domain domain, ModelingHelper modelingHelper) {
        super(domain, modelingHelper);
    }


    @Override
    public void run() {
        try{
            //TODO Use of factory to avoid this explicit flow!
            LOG.info("ready to create a new topic model for domain: " + domain);

            // Delete previous Topics
            helper.getUdm().findTopicsByDomain(domain.getUri()).stream().forEach(topic -> helper.getUdm().deleteTopic(topic));

            // Documents
            helper.getUdm().deleteSimilarsBetweenDocumentsInDomain(domain.getUri());
            buildModelfor(Resource.Type.DOCUMENT);

            // Items
            helper.getUdm().deleteSimilarsBetweenItemsInDomain(domain.getUri());
            buildModelfor(Resource.Type.ITEM);

            // Parts
            helper.getUdm().deleteSimilarsBetweenPartsInDomain(domain.getUri());
            buildModelfor(Resource.Type.PART);
        } catch (RuntimeException e){
            LOG.warn(e.getMessage(),e);
        } catch (Exception e){
            LOG.warn(e.getMessage(),e);
        }
    }


    private void buildModelfor(Resource.Type resourceType){
        LOG.info("Building a topic model for " + resourceType.name() + " of domain: " + domain);

        List<RegularResource> regularResources = new ArrayList<>();

        switch(resourceType){
            //TODO Optimize using Spark.parallel
            case DOCUMENT: regularResources = helper.getUdm().
                    findDocumentsByDomain(
                            domain.getUri()).stream().
                            map(uri -> helper.getUdm().readDocument(uri)).
                            map(document -> helper.getRegularResourceBuilder().from(document.getUri(), document.getTitle(), document.getAuthoredOn(), helper.getAuthorBuilder().composeFromMetadata(document.getAuthoredBy()), document.getTokens())).
                            collect(Collectors.toList());
                break;
            //TODO Optimize using Spark.parallel
            case ITEM: regularResources = helper.getUdm().
                    findItemsByDomain(domain.getUri()).stream().
                            map(uri -> helper.getUdm().readItem(uri)).
                            map(item -> helper.getRegularResourceBuilder().from(item.getUri(), item.getTitle(), item.getAuthoredOn(), helper.getAuthorBuilder().composeFromMetadata(item.getAuthoredBy()), item.getTokens())).
                            collect(Collectors.toList());
                break;
            //TODO Optimize using Spark.parallel
            case PART: regularResources = helper.getUdm().
                    findPartsByDomain(domain.getUri()).stream().
                            map(uri -> helper.getUdm().readPart(uri)).
                    // TODO Improve metainformation of Part
                            map(part -> helper.getRegularResourceBuilder().from(part.getUri(), part.getSense(), part.getCreationTime(), new ArrayList<User>(), part.getTokens())).
                            collect(Collectors.toList());
                break;
        }

        if ((regularResources == null) || (regularResources.isEmpty()))
            throw new RuntimeException("No " + resourceType.name() + "s found in domain: " + domain.getUri());

        // Create the analysis
        Analysis analysis = newAnalysis("Topic-Model","Evolutionary Algorithm along with LDA",resourceType.name());

        // Persist Topic and Relations
        TopicModel model = helper.getTopicModelBuilder().build(analysis.getUri(), regularResources);
        persistModel(analysis,model,resourceType);

        // Save the analysis
        analysis.setConfiguration(model.getConfiguration().toString());
        helper.getUdm().saveAnalysis(analysis);
    }

    private void persistModel(Analysis analysis, TopicModel model, Resource.Type resourceType){

        String creationTime = helper.getTimeGenerator().getNowAsISO();
        Map<String,String> topicTable = new HashMap<>();
        for (TopicData topicData : model.getTopics()){

            // Save Topic
            Topic topic = new Topic();
            topic.setUri(helper.getUriGenerator().newTopic());
            topic.setAnalysis(analysis.getUri());
            topic.setCreationTime(creationTime);
            topic.setContent(String.join(",",topicData.getWords().stream().map(wd -> wd.getWord()).collect(Collectors.toList())));
            helper.getUdm().saveTopic(topic, domain.getUri(), analysis.getUri()); // Implicit relation to Domain (and Analysis)

            topicTable.put(topicData.getId(),topic.getUri());

            // Relate it to Words
            for (WordDistribution wordDistribution : topicData.getWords()){

                Optional<String> wordOptional = helper.getUdm().findWordByLemma(wordDistribution.getWord());
                String wordURI;
                if (wordOptional.isPresent()){
                    wordURI = wordOptional.get();
                }else {
                    wordURI = helper.getUriGenerator().newWord();

                    // Create Word
                    Word word = new Word();
                    word.setUri(wordURI);
                    word.setLemma(wordDistribution.getWord());
                    helper.getUdm().saveWord(word);

                }

                // Relate Topic to Word (mentions)
                helper.getUdm().relateWordToTopic(wordURI,topic.getUri(),wordDistribution.getWeight());
            }
        }


        Set<String> resourceURIs = model.getResources().keySet();
        for (String resourceURI: resourceURIs){

            for (TopicDistribution topicDistribution: model.getResources().get(resourceURI)){
                // Relate resource (Item) to Topic
                String topicURI = topicTable.get(topicDistribution.getTopic());
                switch(resourceType){
                    case DOCUMENT: helper.getUdm().relateTopicToDocument(topicURI,resourceURI,topicDistribution.getWeight());
                        break;
                    case ITEM: helper.getUdm().relateTopicToItem(topicURI,resourceURI,topicDistribution.getWeight());
                        break;
                    case PART: helper.getUdm().relateTopicToPart(topicURI,resourceURI,topicDistribution.getWeight());
                        break;
                }
            }
        }
        LOG.info("Topic Model saved in ddbb: " + model);
    }
}
