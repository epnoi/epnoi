package org.epnoi.modeler.models.word;

import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.model.Resource;
import org.epnoi.modeler.executor.ModelingTask;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.storage.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 11/01/16.
 */
public class WordEmbeddingModeler extends ModelingTask {

    private static final Logger LOG = LoggerFactory.getLogger(WordEmbeddingModeler.class);

    private static final String ANALYSIS_TYPE       = "topic-model";

    public WordEmbeddingModeler(Domain domain, ModelingHelper modelingHelper) {
        super(domain, modelingHelper);
    }


    @Override
    public void run() {

        // TODO use a factory to avoid this explicit flow

        try{
            //TODO Optimize using Spark.parallel
            List<RegularResource> regularResources = helper.getUdm().findDocumentsByDomain(domain.getUri()).stream().
                    map(uri -> helper.getUdm().readDocument(uri)).
                    map(document -> helper.getRegularResourceBuilder().from(document.getUri(), document.getTitle(), document.getAuthoredOn(), helper.getAuthorBuilder().composeFromMetadata(document.getAuthoredBy()), document.getTokens())).
                    collect(Collectors.toList());

            if ((regularResources == null) || (regularResources.isEmpty()))
                throw new RuntimeException("No documents found in domain: " + domain.getUri());

            // Clean Similar relations
            helper.getUdm().deleteSimilarsBetweenWordsInDomain(domain.getUri());

            // Clean Embedded relations
            helper.getUdm().deleteEmbeddingWordsInDomain(domain.getUri());

            // Create the analysis
            Analysis analysis = newAnalysis("Word-Embedding","W2V",Resource.Type.DOCUMENT.name());

            // Build W2V Model
            W2VModel model = helper.getWordEmbeddingBuilder().build(analysis.getUri(), regularResources);

            // Make relations
            //TODO Optimize using Spark.parallel
            model.getVocabulary().stream().forEach(word -> relateWord(word,model));

            // Save the analysis
            helper.getUdm().saveAnalysis(analysis);

        }catch (RuntimeException e){
            LOG.warn(e.getMessage(),e);
        } catch (Exception e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    private void relateWord(String word, W2VModel model){
        String wordURI = findOrCreateWord(word);

        // EMBEDDED relation
        float[] vector = model.getRepresentation(word);
        helper.getUdm().relateWordToDomain(wordURI,domain.getUri(),Arrays.toString(vector));

        // SIMILAR relations
        // TODO this relation should be done in the COMPARATOR module
        model.find(word).stream().filter(sim -> sim.getWeight() > 0.5).
                forEach(sim ->
                        helper.getUdm().relateWordToWord(wordURI,findOrCreateWord(sim.getWord()),sim.getWeight(),domain.getUri()));

    }


    private String findOrCreateWord(String word){
        Optional<String> result = helper.getUdm().findWordByLemma(word);
        String wordURI;
        if (!result.isPresent()){
            // Create Word
            Word wordData = new Word();
            wordData.setUri(helper.getUriGenerator().newWord());
            wordData.setCreationTime(helper.getTimeGenerator().getNowAsISO());
            wordData.setLemma(word);
            helper.getUdm().saveWord(wordData);
            wordURI = wordData.getUri();
        }else{
            wordURI = result.get();
        }
        return wordURI;
    }



}
