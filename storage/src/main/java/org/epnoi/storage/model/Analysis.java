package org.epnoi.storage.model;

import lombok.Data;
import lombok.ToString;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
public class Analysis extends Resource {

    private String type;

    private String description;

    private String configuration;

    private String domain;
}
