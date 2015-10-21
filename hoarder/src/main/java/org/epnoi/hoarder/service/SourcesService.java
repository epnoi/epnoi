package org.epnoi.hoarder.service;

import org.epnoi.model.Source;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by cbadenes on 21/10/15.
 */
@Component
public class SourcesService {

    private static final Logger LOG = Logger.getLogger(SourcesService.class.getName());

    public SourcesService(){

    }


    public List<Source> listSources(){
        LOG.fine("listing Sources ..");
        List<Source> sourcesList = new ArrayList<>();

        return sourcesList;
    }


    public Source getSource(String id){
        LOG.fine("getting Source '" + id + "'");
        return null;
    }


}
