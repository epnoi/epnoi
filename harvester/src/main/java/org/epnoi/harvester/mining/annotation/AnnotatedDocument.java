package org.epnoi.harvester.mining.annotation;

import edu.upf.taln.dri.lib.exception.DRIexception;
import edu.upf.taln.dri.lib.exception.InternalProcessingException;
import edu.upf.taln.dri.lib.model.Document;
import edu.upf.taln.dri.lib.model.ext.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by cbadenes on 07/01/16.
 */
public class AnnotatedDocument implements Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(AnnotatedDocument.class);

    public static final String ABSTRACT_SECTION= "Abstract";

    private final Document document;
    private final List<Sentence> sentences;
    private final String name;
//    private final SectionReader sectionReader;

    public AnnotatedDocument(Document document)  {
        try{
            // Name of Document
            this.name               = document.getName();

            // Raw document
            this.document           = document;

            // Sentences
            this.sentences          = document.extractSentences(SentenceSelectorENUM.ALL);

//            this.sectionReader      = new SectionReader(document.extractSections(false));

        }catch (DRIexception e){
            throw new RuntimeException(e);
        }
    }

    public String getContent() {
        try {
            return document.getRawText();
        } catch (InternalProcessingException e) {
            LOG.error("Error getting raw text from: " + document, e);
            throw new RuntimeException(e);
        }
    }

    public String getTitle(){
        try{
            return this.document.extractHeader().getTitle().trim();
        } catch (InternalProcessingException e) {
            LOG.error("Error getting title from: " + document, e);
            throw new RuntimeException(e);
        }
    }

    public String getDoi(){
        try{
            return this.document.extractHeader().getDoi();
        } catch (InternalProcessingException e) {
            LOG.error("Error getting doi from: " + document, e);
            throw new RuntimeException(e);
        }
    }

    public String getYear(){
        try{
            return this.document.extractHeader().getYear();
        } catch (InternalProcessingException e) {
            LOG.error("Error getting year from: " + document, e);
            throw new RuntimeException(e);
        }
    }

    public List<AnnotatedAuthor> getAuthors(){
        try{
            return  this.document.extractHeader().getAuthorList().stream().map(AnnotatedAuthor::new).collect(Collectors.toList());
        } catch (InternalProcessingException e) {
            LOG.error("Error getting year from: " + document, e);
            throw new RuntimeException(e);
        }
    }

    public List<String> listSections() {
        try{
            List<String> sections = document.extractSections(false).stream().map(s -> s.getName()).collect(Collectors.toList());
            sections.add(ABSTRACT_SECTION);
            return sections;
        } catch (DRIexception e) {
            LOG.error("Error getting sections from: " + document, e);
            throw new RuntimeException(e);
        }
    }


    public String getAbstractContent(){
        try{
            return join(this.document.extractSentences(SentenceSelectorENUM.ONLY_ABSTRACT).stream());
        } catch (InternalProcessingException e) {
            LOG.error("Error getting name from: " + document, e);
            throw new RuntimeException(e);
        }
    }

    public String getApproachContent(){
        return join(this.sentences.stream().filter(sentence -> sentence.getRhetoricalClass().equals(RhetoricalClassENUM.DRI_Approach)));
    }

    public String getBackgroundContent(){
        return join(this.sentences.stream().filter(sentence -> sentence.getRhetoricalClass().equals(RhetoricalClassENUM.DRI_Background)));
    }

    public String getChallengeContent(){
        return join(this.sentences.stream().filter(sentence -> sentence.getRhetoricalClass().equals(RhetoricalClassENUM.DRI_Challenge)));
    }

    public String getFutureWorkContent(){
        return join(this.sentences.stream().filter(sentence -> sentence.getRhetoricalClass().equals(RhetoricalClassENUM.DRI_FutureWork)));
    }

    public String getOutcomeContent(){
        return join(this.sentences.stream().filter(sentence -> sentence.getRhetoricalClass().equals(RhetoricalClassENUM.DRI_Outcome)));
    }

    public String getSummaryByCentroidContent(int lines){
        try{
            return join(document.extractSummary(lines, SummaryTypeENUM.CENTROID_SECT).stream());
        } catch (InternalProcessingException e) {
            LOG.error("Error getting summary by centroid from: " + document, e);
            throw new RuntimeException(e);
        }
    }

    public String getSummaryByTitleSimContent(int lines){
        try{
            return join(document.extractSummary(lines, SummaryTypeENUM.TITILE_SIM).stream());
        } catch (InternalProcessingException e) {
            LOG.error("Error getting summary by title from: " + document, e);
            throw new RuntimeException(e);
        }
    }


//    public String getContentOfSection(String section){
//        LOG.debug("reading section: '"+section+"' from: " + name);
//        try{
//            if (section.equalsIgnoreCase(ABSTRACT_SECTION)) return getAbstractContent();
//            return join(document.extractSections(false).stream().filter(s -> sectionReader.isContainedIn(s, section)).flatMap(s -> s.getSentences().stream()));
//        } catch (DRIexception  e) {
//            LOG.error("Error getting text from: " + document, e);
//            throw new RuntimeException(e);
//        }
//    }

    private String join(Stream<Sentence> sentences){
        return sentences.map(s -> s.getText()).collect(Collectors.joining(" "));
    }
}
