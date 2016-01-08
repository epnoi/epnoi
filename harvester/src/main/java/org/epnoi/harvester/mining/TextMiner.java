package org.epnoi.harvester.mining;

import edu.upf.taln.dri.lib.exception.DRIexception;
import org.apache.commons.lang3.StringUtils;
import org.epnoi.harvester.mining.annotation.AnnotatedDocument;
import org.epnoi.harvester.mining.annotation.UpfAnnotator;
import org.epnoi.harvester.mining.parser.StanfordParser;
import org.epnoi.harvester.mining.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 04/01/16.
 * TODO this component should be a client of the NLP internal service
 */
@Component
public class TextMiner {

    private static final Logger LOG = LoggerFactory.getLogger(TextMiner.class);

    @Autowired
    UpfAnnotator annotator;

    @Autowired
    StanfordParser parser;


    public AnnotatedDocument annotate(String documentPath){
        try {
            return annotator.annotate(documentPath);
        } catch (DRIexception drIexception) {
            throw new RuntimeException(drIexception);
        }
    }

    public List<Token> parse(String text){
        try {
            return parser.parse(text);
        } catch (Exception e) {
            LOG.error("Error parsing text: " + StringUtils.substring(text,0,10) + " ...",e);
            return new ArrayList<>();
        }
    }

}
