package org.epnoi.harvester.mining.parser;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by cbadenes on 07/01/16.
 */
@Data
public class Token implements Serializable {

    String word;

    String pos;

    String lemma;

    boolean stopWord;

    public boolean isValid(){
        return !stopWord
                && lemma.length()>2
                && pos.toLowerCase().startsWith("n");
    }
}
