package org.epnoi.learner.relations.extractor.parallel;

import java.io.Serializable;

/**
 * Created by rgonza on 28/10/15.
 */
public class SimpleTestFunction implements Serializable{
    private String separator;

    public SimpleTestFunction(String separator) {
        this.separator = separator;
    }

    public String test(String input){
       
        return "hello"+separator+input;
    }
}
