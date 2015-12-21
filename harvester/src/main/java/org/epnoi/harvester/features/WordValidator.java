package org.epnoi.harvester.features;

import com.google.common.base.CharMatcher;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * Created by cbadenes on 19/06/15.
 */
public class WordValidator {

    public static boolean isValid(String word){

        return word.length() > 4
                && !StandardAnalyzer.STOP_WORDS_SET.contains(word)
                && CharMatcher.JAVA_LETTER.matchesAllOf(word)
                && CharMatcher.ASCII.matchesAllOf(word);
    }

}