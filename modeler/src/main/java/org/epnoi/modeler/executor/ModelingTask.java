package org.epnoi.modeler.executor;

import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.topic.TopicModeler;
import org.epnoi.modeler.models.word.WordEmbeddingModeler;
import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 13/01/16.
 */
public class ModelingTask implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger("ModelingTask");

    protected final Domain domain;
    protected final ModelingHelper helper;

    public ModelingTask(Domain domain, ModelingHelper modelingHelper){
        this.domain = domain;
        this.helper = modelingHelper;
    }

    protected Analysis newAnalysis(String type, String configuration, String description){

        Analysis analysis = new Analysis();
        analysis.setUri(helper.getUriGenerator().newAnalysis());
        analysis.setCreationTime(helper.getTimeGenerator().getNowAsISO());
        analysis.setDomain(domain.getUri());
        analysis.setType(type);
        analysis.setDescription(description);
        analysis.setConfiguration(configuration);
        return analysis;
    }

    @Override
    public void run() {
        LOG.info("New modeling task running...");
        // TODO Parallelize by beans. Take into account transactions in UDM
        new WordEmbeddingModeler(domain,helper).run();
        new TopicModeler(domain,helper).run();
    }
}
