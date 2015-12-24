package org.epnoi.learner.filesystem;

import gate.Document;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.epnoi.model.*;
import org.epnoi.model.commons.Parameters;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Component
public class FilesystemHarvester {

    @Autowired
    private Core core;

    @Autowired
    private FilesystemHarvesterParameters parameters;

    private String datePattern = "MM/dd/yyyy";


    public String path = "/JUNK/drinventorcorpus/corpus";
    private boolean verbose;
    private boolean overwrite;
    private String corpusLabel;
    private String corpusURI;

    private static final Logger logger = Logger
            .getLogger(FilesystemHarvester.class.getName());


    // ----------------------------------------------------------------------------------------

    public FilesystemHarvester() {

    }


    // -------------------------------------------------------------------------------------------------------------------
    @PostConstruct
    public void init()
            throws EpnoiInitializationException {

        logger.info("Initializing the FilesystemHarvester with the following parameters " + this.parameters);
        this.path = (String) parameters
                .getParameterValue(FilesystemHarvesterParameters.FILEPATH);

        this.verbose = (boolean) parameters
                .getParameterValue(FilesystemHarvesterParameters.VERBOSE);

        this.corpusLabel = (String) parameters
                .getParameterValue(FilesystemHarvesterParameters.CORPUS_LABEL);

        this.corpusURI = (String) parameters
                .getParameterValue(FilesystemHarvesterParameters.CORPUS_URI);
        this.overwrite = (boolean) parameters
                .getParameterValue(FilesystemHarvesterParameters.OVERWRITE);

    }

    // -------------------------------------------------------------------------------------------------------------------


    private String _scanContent(String resourceURI) {
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, resourceURI);
        InputStream is = null;
        ContentHandler handler = null;
        try {
            is = new URL(resourceURI).openStream();

            Parser parser = new AutoDetectParser();
            handler = new BodyContentHandler(-1);

            ParseContext context = new ParseContext();
            context.set(Parser.class, parser);

            parser.parse(is, handler, metadata, new ParseContext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        String content = handler.toString();
        content = content.replaceAll("\\r\\n|\\r|\\n", " ");
        content = content.replaceAll("\\s+", " ");
        // System.out.println("----> " + content);
        return content;
    }

    // ----------------------------------------------------------------------------------------

    public List<Paper> run() {
        return harvest(this.path);
    }

    // ----------------------------------------------------------------------------------------

    public List<Paper> run(Parameters<Object> runtimeParameters) {
        return harvest(this.path);
    }


    // ----------------------------------------------------------------------------------------

    public List<Paper> harvest() {
        String directoryToHarvest = (String) this.parameters.getParameterValue(FilesystemHarvesterParameters.FILEPATH);
        List<Paper> harvestedPapers = new ArrayList<>();
        if (directoryToHarvest != null) {
            this.harvest(directoryToHarvest);
        }
        return harvestedPapers;
    }


    // ----------------------------------------------------------------------------------------

    public List<Paper> harvest(String directoryToHarvest) {
        List<Paper> harvestedPapers = new ArrayList<>();
        try {
            File harvestDirectory = new File(directoryToHarvest);

            String[] filesToHarvest = scanFilesToHarverst(harvestDirectory);

            // System.out.println("..........> "
            // + Arrays.toString(filesToHarvest));
            for (String fileToHarvest : filesToHarvest) {
                logger.info("Harvesting the file "
                        + harvestDirectory.getAbsolutePath() + "/"
                        + fileToHarvest);
                Context context = new Context();
                Paper paper = _harvestFile(directoryToHarvest + "/"
                        + fileToHarvest, fileToHarvest);
                harvestedPapers.add(paper);
                if (core.getInformationHandler().contains(paper.getUri(),
                        RDFHelper.PAPER_CLASS)) {
                    if (overwrite) {
                        _removePaper(paper);

                        _addPaper(paper);
                    } else {
                        logger.info("Skipping " + fileToHarvest
                                + " since it was already in the UIA");
                    }

                } else {
                    _addPaper(paper);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return harvestedPapers;
    }

    private void _addPaper(Paper paper) {
        // First the paper is added to the UIA
        core.getInformationHandler().put(paper, Context.getEmptyContext());

        // Later it is annotated as belonging to the harvested
        // corpus
        long startTme = System.currentTimeMillis();

        core.getAnnotationHandler().label(paper.getUri(), this.corpusURI);
        core.getAnnotationHandler().label(paper.getUri(), this.corpusLabel);

        long totalTime = Math.abs(startTme - System.currentTimeMillis());
        logger.info("It took " + totalTime
                + " ms to add it to the UIA and label it");
        // The annotated version of the paper is also stored in the
        // UIA

        startTme = System.currentTimeMillis();
        Document annotatedContent = null;
        try {
            annotatedContent = this.core.getNLPHandler()
                    .process(paper.getDescription());
        } catch (EpnoiResourceAccessException e) {

            e.printStackTrace();
        }

        Selector annotationSelector = new Selector();
        annotationSelector.setProperty(SelectorHelper.URI, paper.getUri());
        annotationSelector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI,
                paper.getUri() + "/"
                        + AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE);
        annotationSelector.setProperty(SelectorHelper.TYPE,
                RDFHelper.PAPER_CLASS);

        core.getInformationHandler().setAnnotatedContent(
                annotationSelector,
                new Content<Object>(annotatedContent,
                        AnnotatedContentHelper.CONTENT_TYPE_OBJECT_XML_GATE));

        totalTime = Math.abs(startTme - System.currentTimeMillis());
        logger.info("It took " + totalTime
                + "ms to add it to annotate its content and add it to the UIA");
    }

    // ----------------------------------------------------------------------------------------

    private void _removePaper(Paper paper) {
        logger.info("The paper was already in the UIA, lets delete it (and its associated annotation)");
        core.getAnnotationHandler().removeLabel(paper.getUri(), this.corpusURI);
        core.getAnnotationHandler().removeLabel(paper.getUri(), this.corpusLabel);
        core.getInformationHandler().remove(paper.getUri(),
                RDFHelper.PAPER_CLASS);
    }

    // ----------------------------------------------------------------------------------------

    private String[] scanFilesToHarverst(File directoryToHarvest) {
        String[] filesToHarvest = directoryToHarvest.list(new FilenameFilter() {

            public boolean accept(File current, String name) {
                File file = new File(current, name);
                return (file.isFile()) && (!file.isHidden());
            }

        });
        return filesToHarvest;
    }

    // ----------------------------------------------------------------------------------------

    protected String convertDateFormat(String dateExpression) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(dateExpression);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        return (dt1.format(date) + "^^xsd:date");

    }

    // ----------------------------------------------------------------------------------------

    public Paper _harvestFile(String filePath, String fileName) {

        Paper paper = new Paper();

        String fileContent = _scanContent("file://" + filePath);
        paper.setUri("file://" + filePath);
        paper.setTitle(fileName);
        paper.setDescription(fileContent);
        paper.setPubDate("2015-07-07");
        return paper;
    }

    // ----------------------------------------------------------------------------------------

    private String getDate(String filePath) {
        System.out.println("filePath> " + filePath);
        int bracketOpeningPosition = filePath.indexOf("[");
        int bracketClosingPosition = filePath.indexOf("]");
        String filePathDatePart = filePath.substring(
                bracketOpeningPosition + 1, bracketClosingPosition);
        return filePathDatePart;
    }

    // ----------------------------------------------------------------------------------------

    private void handleError(String errorMessage, String exceptionMessage) {
        if (exceptionMessage != null) {
            logger.severe(errorMessage);
        } else {
            logger.severe(errorMessage);
            logger.severe("The exception message was: " + errorMessage);
        }

    }


    // -------------------------------------------------------------------------------------------------------------------
/*
    public static void main(String[] args) {
        logger.info("Starting the harvesting!");

        FilesystemHarvester harvester = new FilesystemHarvester();
        FilesystemHarvesterParameters parameters = new FilesystemHarvesterParameters();

        parameters.setParameter(FilesystemHarvesterParameters.CORPUS_LABEL,
                "CGTestCorpus");

        parameters.setParameter(FilesystemHarvesterParameters.CORPUS_URI,
                "http://CGTestCorpus");
        parameters.setParameter(FilesystemHarvesterParameters.VERBOSE, true);

        parameters.setParameter(FilesystemHarvesterParameters.OVERWRITE, true);

        parameters.setParameter(FilesystemHarvesterParameters.FILEPATH,
                "/opt/epnoi/epnoideployment/firstReviewResources/CGCorpus");

        Core core = CoreUtility.getUIACore();
        try {
            harvester.init(core, parameters);
        } catch (EpnoiInitializationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<Paper> harvestedPapers = harvester.run();

        System.out
                .println("These are the resources annotated as belonging to the corpus");

        List<String> corpusURIs = core.getAnnotationHandler().getLabeledAs(
                "CGTestCorpus");
        for (String uri : corpusURIs) {
            System.out.println(" >" + uri);
        }
        System.out
                .println("==========================================================================");
        System.out
                .println("These are the resources annotated as belonging to the corpus");
        corpusURIs = core.getAnnotationHandler().getLabeledAs(
                "http://CGTestCorpus");
        for (String uri : corpusURIs) {
            System.out.println(" >" + uri);
        }

        logger.info("Ending the harvesting!");
    }
*/
}