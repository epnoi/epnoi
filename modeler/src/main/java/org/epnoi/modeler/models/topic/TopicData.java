package org.epnoi.modeler.models.topic;

import lombok.Data;
import org.epnoi.modeler.models.WordDistribution;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by cbadenes on 11/01/16.
 */
@Data
public class TopicData implements Serializable {

    String id;

    String label;

    LinkedList<WordDistribution> words = new LinkedList<>();

    public boolean isEmpty(){
        return words == null || words.isEmpty();
    }

    public TopicData add (String word, Double weight ){
        WordDistribution wordDistribution = new WordDistribution();
        wordDistribution.setWord(word);
        wordDistribution.setWeight(weight);
        words.add(wordDistribution);
        return this;
    }
}
