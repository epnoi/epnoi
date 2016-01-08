package org.epnoi.storage.model;

import lombok.Data;
import lombok.ToString;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(callSuper = true)
public class Source extends Resource {

    private String name;

    private String description;

    private String url;

    private String protocol;

    private String domain;
}
