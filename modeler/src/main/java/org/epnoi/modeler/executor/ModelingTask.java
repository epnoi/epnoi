package org.epnoi.modeler.executor;

import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.storage.model.Analysis;
import org.epnoi.storage.model.Domain;

/**
 * Created by cbadenes on 13/01/16.
 */
public abstract class ModelingTask implements Runnable{

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

}
