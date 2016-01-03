package org.epnoi.learner.relations.patterns.lexical;

import org.epnoi.learner.relations.patterns.RelationalPattern;
import org.epnoi.learner.relations.patterns.RelationalPatternsModel;
import org.epnoi.learner.relations.patterns.RelationalPatternsModelCreationParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RelaxedBigramSoftPatternModel implements RelationalPatternsModel {

    private static final int MAX_PATTERN_LENGTH = 20;
    private Map<String, Double> unigramProbability;
    private Map<String, Map<String, Double>> bigramProbability;
    private RelationalPatternsModelCreationParameters parmeters;
    // private int maxPatternLength;
    // private LexicalRelationalModelCreationParameters parameters;
    private double interpolationConstant; // Set to this value using the
    // experimental value set in
    // Generic Soft Pattern
    // Models for Definitional
    // Question Answering

    private double interpolationContantInverse;
    // ---------------------------------------------------------------------------------------------------------

    public RelaxedBigramSoftPatternModel() {
        this.unigramProbability = new HashMap<String, Double>();
        this.bigramProbability = new HashMap<String, Map<String, Double>>();
        this.interpolationConstant=1-interpolationConstant;

    }

    protected RelaxedBigramSoftPatternModel(

            Map<String, Double> unigramProbability,
            Map<String, Map<String, Double>> bigramProbability,
            double interpolationConstant) {
        // this.maxPatternLength = (Integer) this.parameters
        // .getParameterValue(LexicalRelationalModelCreationParameters.MAX_PATTERN_LENGTH);

        this.bigramProbability = bigramProbability;
        this.unigramProbability = unigramProbability;
        this.interpolationConstant = interpolationConstant;

    }

    // ---------------------------------------------------------------------------------------------------------

    public double getUnigramProbability(String nodeToken) {
        Double unigramProbabilities = this.unigramProbability.get(nodeToken);
        if (unigramProbabilities != null) {
            return unigramProbabilities;
        } else {
            return 0d;
        }
    }

    // ---------------------------------------------------------------------------------------------------------

    public double getBigramProbability(String nodeToken,
                                       String followingNodeToken) {
        Map<String, Double> nodeBigramProbabilities = this.bigramProbability
                .get(nodeToken);
        if (nodeBigramProbabilities != null) {
            Double probability = nodeBigramProbabilities
                    .get(followingNodeToken);
            if (probability != null) {
                return probability;
            } else {
                return 0d;
            }
        } else {
            return 0d;
        }
    }

    // ---------------------------------------------------------------------------------------------------------

    @Override
    public double calculatePatternProbability(RelationalPattern pattern) {

        double probability = 1;
        LexicalRelationalPattern relationalPattern = (LexicalRelationalPattern) pattern;
        // System.out.println(relationalPattern);

        List<LexicalRelationalPatternNode> nodes = relationalPattern.getNodes();


        if ((relationalPattern.getLength() < 2)
                || (relationalPattern.getLength() > MAX_PATTERN_LENGTH)) {
            return 0d;
        } else {// Generic case...

            String nextNodeToken = null;
            String nodeToken = relationalPattern.getNodes().get(0)
                    .getGeneratedToken();

            int position = 0;
            int patternLength = nodes.size();

            while (position < patternLength) {

                nodeToken = relationalPattern.getNodes().get(position)
                        .getGeneratedToken();

                if (position < patternLength - 1) {
                    nextNodeToken = nodes.get(position + 1).getGeneratedToken();

                    probability *= (this.interpolationConstant * this
                            .getUnigramProbability(nextNodeToken))
                            + ((this.interpolationConstant) * this
                            .getBigramProbability(nodeToken,
                                    nextNodeToken));
                    
                }

                position++;
            }

        }

        return probability;

    }

    // ----------------------------------------------------------------------------------------------------------------

    public void show() {
        System.out.println("The model's vocabulary cardinality is "
                + this.unigramProbability.size());
        System.out.println("They are " + this.unigramProbability.keySet());
        System.out
                .println("Unigrams=========================================================================================");
        for (Entry<String, Double> unigramProbability : this.unigramProbability
                .entrySet()) {
            System.out.println("<" + unigramProbability.getKey() + "|"
                    + unigramProbability.getValue() + ">");
        }
        System.out.println();
        System.out
                .println("Bigrams=========================================================================================");
        System.out.println();
        for (Entry<String, Map<String, Double>> entry : this.bigramProbability
                .entrySet()) {
            for (Entry<String, Double> innerEntry : entry.getValue().entrySet()) {
                System.out.println("<" + entry.getKey() + ","
                        + innerEntry.getKey() + ">= " + innerEntry.getValue());
            }
        }

    }

    // ---------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "BigramSoftPatternModel [unigramProbability="
                + _unigramProbabilityToString() + ", bigramProbability="
                + _bigramProbabilityToString() + ", parmeters=" + parmeters
                + ", interpolation_constant=" + interpolationConstant + "]";
    }

    // ---------------------------------------------------------------------------------------------------------

    private String _unigramProbabilityToString() {
        String result = "";
        for (Entry<String, Double> entry : this.unigramProbability.entrySet()) {
            result += ", " + entry.getValue() + " " + entry.getValue();
        }
        return result;
    }

    // ---------------------------------------------------------------------------------------------------------

    private String _bigramProbabilityToString() {
        String result = "";
        for (Entry<String, Map<String, Double>> entry : this.bigramProbability
                .entrySet()) {
            for (Entry<String, Double> innerEntry : entry.getValue().entrySet()) {
                result += ",(" + entry.getValue() + "," + entry.getValue()
                        + "]= " + innerEntry.getValue();
            }
        }
        return result;
    }

    // ---------------------------------------------------------------------------------------------------------

    private double getInterpolationConstant() {
        return this.interpolationConstant;
    }

}
