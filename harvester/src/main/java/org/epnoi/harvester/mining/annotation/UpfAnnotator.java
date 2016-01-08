package org.epnoi.harvester.mining.annotation;

import edu.upf.taln.dri.lib.Factory;
import edu.upf.taln.dri.lib.exception.DRIexception;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by cbadenes on 07/01/16.
 */
@Component
public class UpfAnnotator {

    private static final Logger LOG = LoggerFactory.getLogger(UpfAnnotator.class);

    @Value("${epnoi.upf.miner.config}")
    String driConfigPath;

    @PostConstruct
    public void setup() throws DRIexception {

        // Set property file path
        Factory.setDRIPropertyFilePath(driConfigPath);

        // Enable bibliography entry parsing
        Factory.setEnableBibEntryParsing(true);

        // Initialize
        Factory.initFramework();
    }

    public AnnotatedDocument annotate(String documentPath) throws DRIexception {
        LOG.info("Ready to parse: " + documentPath);

        String extension = FilenameUtils.getExtension(documentPath.toString()).toLowerCase();
        switch(extension){
            case "pdf":
                LOG.info("parsing PDF document: " + documentPath);
                return new AnnotatedDocument(Factory.getPDFloader().parsePDF(documentPath));
            case "xml":
            case "htm":
            case "html":
                LOG.info("parsing XML document: " + documentPath);
                return new AnnotatedDocument(Factory.createNewDocument(documentPath));
            default:
                LOG.info("parsing document: " + documentPath);
                return new AnnotatedDocument(Factory.getPlainTextLoader().parsePlainText(new File(documentPath)));
        }
    }

}
