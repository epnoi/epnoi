package org.epnoi.hoarder.services;

import org.epnoi.model.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 21/10/15.
 */
@Component
public class SourceService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceService.class);

    public SourceService(){

    }

    public List<Source> newSource(Source source){
        LOG.debug("listing Sources ..");
        List<Source> sourcesList = new ArrayList<>();

        return sourcesList;
    }


    public List<Source> listSources(){
        LOG.debug("listing Sources ..");
        List<Source> sourcesList = new ArrayList<>();

        return sourcesList;
    }


    public Source getSource(String id){
        LOG.debug("getting Source '" + id + "'");
        return null;
    }


}
