package org.epnoi.storage.model;

import lombok.Data;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
public class Item extends Resource{

    private String format;

    private String language;

    private String title;

    private String subject;

    private String description;

    private String url;

    private String content;

    private String tokens;
}