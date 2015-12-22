package org.epnoi.storage.model;

import lombok.Data;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
public class Relation extends Resource {

    private String type;

    private String describes;

    private String content;

    private String analysis;

}
