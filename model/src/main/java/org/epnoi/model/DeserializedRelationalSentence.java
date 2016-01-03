package org.epnoi.model;

import gate.Document;

import java.io.Serializable;

public class DeserializedRelationalSentence implements Serializable {


    private OffsetRangeSelector source;
    private OffsetRangeSelector target;
    private String sentence;
    private Document annotatedSentence;

    // --------------------------------------------------------------------------------------

    public DeserializedRelationalSentence(OffsetRangeSelector source, OffsetRangeSelector target, String sentence,
                                          Document annotatedSentence) {
        super();
        this.source = source;
        this.target = target;
        this.sentence = sentence;
        this.annotatedSentence = annotatedSentence;

    }

    public Document getAnnotatedSentence() {
        return annotatedSentence;
    }

    // --------------------------------------------------------------------------------------

    public void setAnnotatedSentence(Document annotatedSentence) {
        this.annotatedSentence = annotatedSentence;
    }

// --------------------------------------------------------------------------------------

    public OffsetRangeSelector getSource() {
        return source;
    }

    // --------------------------------------------------------------------------------------

    public void setSource(OffsetRangeSelector source) {
        this.source = source;
    }

    // --------------------------------------------------------------------------------------

    public OffsetRangeSelector getTarget() {
        return target;
    }

    // --------------------------------------------------------------------------------------

    public void setTarget(OffsetRangeSelector target) {
        this.target = target;
    }

    // --------------------------------------------------------------------------------------

    public String getSentence() {
        return sentence;
    }

    // --------------------------------------------------------------------------------------

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    // --------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "RelationalSentence [source=" + source + ", target=" + target
                + ", sentence=" + sentence
                + ", annotatedSentence="
                + annotatedSentence + "]";

    }


}
