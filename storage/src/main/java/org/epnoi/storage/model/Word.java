package org.epnoi.storage.model;

import lombok.Data;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
public class Word extends Resource{

    private String content;

    private String lemma;

    private String stem;

    private String pos;

    private String type;
}
