package org.epnoi.learner.terms;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import org.epnoi.model.AnnotatedWord;
import org.epnoi.model.OffsetRangeSelector;
import org.epnoi.model.TermMetadata;
import org.epnoi.nlp.gate.NLPAnnotationsConstants;

import java.util.*;

public class TermCandidateBuilder {
    private final String symbolPatter = "[^\\w\\s]";

    private Document document;

    static final Comparator<Annotation> ANNOTATION_ORDER = new Comparator<Annotation>() {
        public int compare(Annotation annotation1, Annotation annotation2) {
            return annotation1.getStartNode().getOffset()
                    .compareTo(annotation2.getStartNode().getOffset());
        }
    };

    // ------------------------------------------------------------------------------------------------------------

    public TermCandidateBuilder(Document document) {
        this.document = document;
    }

    // ------------------------------------------------------------------------------------------------------------

    public AnnotatedWord<TermMetadata> buildTermCandidate(Annotation annotation) {

        Long startOffset = annotation.getStartNode().getOffset();
        Long endOffset = annotation.getEndNode().getOffset();

        AnnotatedWord<TermMetadata> termCandidate = _buildTermCandidateFromRange(startOffset, endOffset);

        return termCandidate;
    }

    // ------------------------------------------------------------------------------------------------------------

    public AnnotatedWord<TermMetadata> buildTermCandidate(OffsetRangeSelector range) {


        Long startOffset = range.getStart();
        Long endOffset = range.getEnd();


        AnnotatedWord<TermMetadata> termCandidate = _buildTermCandidateFromRange(startOffset, endOffset);

        return termCandidate;
    }

    private AnnotatedWord<TermMetadata> _buildTermCandidateFromRange(Long startOffset, Long endOffset) {
        AnnotatedWord<TermMetadata> termCandidate = new AnnotatedWord<TermMetadata>(
                new TermMetadata());


        AnnotationSet annotations = this.document.getAnnotations();

        ArrayList<String> words = new ArrayList<String>();

        List<Annotation> tokenAnnotations = new ArrayList<Annotation>();
        for (Annotation tokenAnnotation : annotations.get(NLPAnnotationsConstants.TOKEN, startOffset,
                endOffset)) {
            tokenAnnotations.add(tokenAnnotation);
        }

        Collections.sort(tokenAnnotations, ANNOTATION_ORDER);

        for (Annotation tokenAnnotation : tokenAnnotations) {
            if (!isNoise(tokenAnnotation)) {
                words.add(((String) tokenAnnotation.getFeatures().get(NLPAnnotationsConstants.TOKEN_STRING))
                        .toLowerCase());
            }
        }

        termCandidate.getAnnotation().setWords(
                Arrays.copyOf(words.toArray(), words.size(), String[].class));
        termCandidate.getAnnotation().setLength(words.size());

        String word = this._generateWord(words);

        termCandidate.setWord(word);
        return termCandidate;
    }

    private boolean isNoise(Annotation annotation) {
        String surfaceForm = (String) annotation.getFeatures().get(NLPAnnotationsConstants.TOKEN_STRING);
        return surfaceForm.matches(this.symbolPatter);
    }

    // ------------------------------------------------------------------------------------------------------------

    private String _generateWord(List<String> words) {
        String word = "";
        Iterator<String> wordsIt = words.iterator();
        while (wordsIt.hasNext()) {
            word += wordsIt.next();
            if (wordsIt.hasNext()) {
                word += " ";
            }
        }
        return word;
    }

    // ------------------------------------------------------------------------------------------------------------

    public AnnotatedWord<TermMetadata> generateSubTermCandidate(String[] words) {
        AnnotatedWord<TermMetadata> termCandidate = new AnnotatedWord<TermMetadata>(
                new TermMetadata());
        termCandidate.getAnnotation().setLength(words.length);
        termCandidate.getAnnotation().setWords(words);
        termCandidate.setWord(this._generateWord(Arrays.asList(words)));

        return termCandidate;
    }

    // ------------------------------------------------------------------------------------------------------------

    public List<AnnotatedWord<TermMetadata>> splitTermCandidate(
            AnnotatedWord<TermMetadata> termCandidate) {
        List<AnnotatedWord<TermMetadata>> termCandidates = new ArrayList<AnnotatedWord<TermMetadata>>();

        String[] words = termCandidate.getAnnotation().getWords();
        List<String[]> listSubtermsWords = _generateSubtermsWords(words);
        for (String[] subtermWords : listSubtermsWords) {
            termCandidates.add(generateSubTermCandidate(subtermWords));
        }

        return termCandidates;
    }

    // ------------------------------------------------------------------------------------------------------------

    private List<String[]> _generateSubtermsWords(String[] words) {
        ArrayList<String[]> subtermWords = new ArrayList<String[]>();
        int lenght = words.length;

        for (int i = 0; i < lenght; i++) {
            for (int j = i; j < lenght; j++) {

                String[] aux = Arrays.copyOfRange(words, i, j + 1);
                if (aux.length < lenght) {
                    subtermWords.add(aux);
                }
            }

        }

        return subtermWords;
    }

    // ------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        TermCandidateBuilder termBuilder = new TermCandidateBuilder(null);
        AnnotatedWord<TermMetadata> termCandidate = new AnnotatedWord<TermMetadata>(
                new TermMetadata());
        termCandidate.getAnnotation().setWords(
                new String[]{"a", "b", "c", "d"});
        System.out.println(termBuilder.splitTermCandidate(termCandidate));

        for (int i = 0; i < 100; i++) {
            System.out.println(i + "->" + (Math.log(i) / Math.log(2)));
        }
    }
}
