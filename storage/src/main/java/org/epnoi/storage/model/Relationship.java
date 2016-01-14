package org.epnoi.storage.model;

import lombok.Data;

/**
 * Created by cbadenes on 13/01/16.
 */
@Data
public class Relationship {

    String uri;

    Double weight;

    public Relationship(String uri, Double weight){
        this.uri = uri;
        this.weight = weight;
    }

}
