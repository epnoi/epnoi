package org.epnoi.storage.model;

import lombok.Data;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
public class Source extends Resource {

    private String url;

    private String name;

    private String description;

    private String protocol;

    private String domain;
}
