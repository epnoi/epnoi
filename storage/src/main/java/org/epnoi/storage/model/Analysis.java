package org.epnoi.storage.model;

import lombok.Data;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
public class Analysis extends Resource {

    private String type;

    private String description;

    private String configuration;

    private Object report;

    private String domain;
}
