package org.epnoi.learner.relations.patterns.lexical;

import gate.Annotation;
import gate.Document;
import gate.util.InvalidOffsetException;
import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.RelationalPatternGenerator;
import org.epnoi.model.DeserializedRelationalSentence;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.RelationalSentence;
import org.epnoi.nlp.gate.AnnotationsComparator;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;
import org.epnoi.uia.commons.GateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LexicalRelationalPatternGenerator implements RelationalPatternGenerator {
    private AnnotationsComparator annotationsComparator;

    public LexicalRelationalPatternGenerator() {
        this.annotationsComparator = new AnnotationsComparator();
    }

    public List<RelationalPattern> generate(
            RelationalSentence relationalSentence) {


        String serializedAnnotatedSentente = relationalSentence
                .getAnnotatedSentence();

        OffsetRangeSelector source = relationalSentence.getSource();

        OffsetRangeSelector target = relationalSentence.getTarget();

        Document annotatedSentence = GateUtils
                .deserializeGATEDocument(serializedAnnotatedSentente);

        List<RelationalPattern> generatedPatterns = getRelationalPatterns(source, target, annotatedSentence);
        return generatedPatterns;
    }

    public List<RelationalPattern> generate(
            DeserializedRelationalSentence relationalSentence) {


        OffsetRangeSelector source = relationalSentence.getSource();

        OffsetRangeSelector target = relationalSentence.getTarget();

        Document annotatedSentence = relationalSentence.getAnnotatedSentence();

        List<RelationalPattern> generatedPatterns = getRelationalPatterns(source, target, annotatedSentence);
        return generatedPatterns;
    }


    private List<RelationalPattern> getRelationalPatterns(OffsetRangeSelector source, OffsetRangeSelector target, Document annotatedSentence) {

       // System.out.println("generatePattern=========================================================================== ");

       // System.out.println("Sentence " + annotatedSentence.getContent().toString());

        Annotation sourceAnnotation = null;
        Annotation targetAnnotation = null;
        String sourceSF;

       // try {
          //  sourceSF = annotatedSentence.getContent().getContent(source.getStart(), source.getEnd()).toString();

            sourceAnnotation = (Annotation) annotatedSentence.getAnnotations().get(NLPAnnotationsConstants.TERM_CANDIDATE).get(source.getStart(), source.getEnd()).toArray()[0];
       /*
        } catch (InvalidOffsetException e) {
            sourceSF = "SOURCE_NOT_FOUND";
        }
*/
        String targetSF;

       // try {
          //  targetSF = annotatedSentence.getContent().getContent(target.getStart(), target.getEnd()).toString();
            targetAnnotation = (Annotation) annotatedSentence.getAnnotations().get(NLPAnnotationsConstants.TERM_CANDIDATE).get(target.getStart(),target.getEnd()).toArray()[0];
    /*
    } catch (InvalidOffsetException e) {
            (())   targetSF = "TARGET_NOT_FOUND";
        }
*/
       // System.out.println("[source]> " + sourceSF);
        //System.out.println("[target]> " + targetSF);


      return generate(sourceAnnotation,targetAnnotation, annotatedSentence);
    }

    private Integer _generatePatternWhileInExtreme(int position, List<Annotation> tokenAnnotations, Annotation extreme, String extremeLabel, LexicalRelationalPattern lexicalRelationalPattern, Document annotatedSentence) {
        while (position < tokenAnnotations.size() && extreme.overlaps(tokenAnnotations.get(position))) {
            position++;
        }
        LexicalRelationalPatternNode node = new LexicalRelationalPatternNode();
        String extremeSurfaceForm;
        try {
            extremeSurfaceForm = annotatedSentence.getContent().getContent(extreme.getStartNode().getOffset(), extreme.getEndNode().getOffset()).toString();

        } catch (InvalidOffsetException e) {
            extremeSurfaceForm = extremeLabel + " NOT_FOUND";
        }
        node.setGeneratedToken(extremeLabel);
        node.setOriginialToken(extremeSurfaceForm);
        lexicalRelationalPattern.getNodes().add(node);
        return position;
    }

    private void _generatePatternTillExtreme(int position, List<Annotation> tokenAnnotations, Annotation extreme, String extremeLabel, LexicalRelationalPattern lexicalRelationalPattern, Document annotatedSentence) {
        while (!(extreme.overlaps(tokenAnnotations.get(position)))) {

            LexicalRelationalPatternNode node = new LexicalRelationalPatternNode();
            node.setOriginialToken(tokenAnnotations.get(position).getFeatures()
                    .get("string").toString());

            if (_isAVerb((String) tokenAnnotations.get(position).getFeatures().get("category"))) {
                node.setGeneratedToken(tokenAnnotations.get(position).getFeatures()
                        .get("string").toString());
            } else {
                node.setGeneratedToken(tokenAnnotations.get(position).getFeatures()
                        .get("category").toString());
            }

            lexicalRelationalPattern.getNodes().add(node);


            position++;
        }

        _generatePatternWhileInExtreme(position, tokenAnnotations, extreme, extremeLabel,lexicalRelationalPattern, annotatedSentence);
    }


    // --------------------------------------------------------------------------------------------------------

    private boolean _isAVerb(String tag) {
        return (tag.equals("VBZ") || tag.equals("VBG") || tag.equals("VBN")
                || tag.equals("VBP") || tag.equals("VB") || tag.equals("VBD") || tag
                .equals("MD"));
    }

    // --------------------------------------------------------------------------------------------------------

    public List<RelationalPattern> generate(Annotation sourceAnnotation,
                                                   Annotation targetAnnotation, Document annotatedSentence) {


        LexicalRelationalPattern lexicalRelationalPattern = new LexicalRelationalPattern();

        List<RelationalPattern> generatedPatterns = new ArrayList<>();


        Integer position = 0;

        List<Annotation> tokenAnnotations = annotatedSentence.getAnnotations().get(
                NLPAnnotationsConstants.TOKEN).inDocumentOrder();

        while (!(sourceAnnotation.overlaps(tokenAnnotations.get(position)) || targetAnnotation.overlaps(tokenAnnotations.get(position)))) {
            position++;
        }


        //Add relation extreme
        if (sourceAnnotation.overlaps(tokenAnnotations.get(position))) {

            position= _generatePatternWhileInExtreme(position, tokenAnnotations, sourceAnnotation, "<SOURCE>", lexicalRelationalPattern, annotatedSentence);


            _generatePatternTillExtreme(position, tokenAnnotations, targetAnnotation, "<TARGET>", lexicalRelationalPattern, annotatedSentence);
        } else {
            position= _generatePatternWhileInExtreme(position, tokenAnnotations, targetAnnotation, "<TARGET>", lexicalRelationalPattern, annotatedSentence);
            _generatePatternTillExtreme(position, tokenAnnotations, sourceAnnotation, "<SOURCE>", lexicalRelationalPattern, annotatedSentence);
        }
       // System.out.println("---------------> "+lexicalRelationalPattern );
        generatedPatterns.add(lexicalRelationalPattern);

        return generatedPatterns;


    }

    // --------------------------------------------------------------------------------------------------------


}
