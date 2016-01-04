package org.epnoi.harvester.mining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 04/01/16.
 */
@Component
public class TextMiner {

    private static final Logger LOG = LoggerFactory.getLogger(TextMiner.class);

    //TODO this component should redirect to NLP internal service

    public void parse(String path){
        LOG.info("Ready to parse: " + path);
    }

}
