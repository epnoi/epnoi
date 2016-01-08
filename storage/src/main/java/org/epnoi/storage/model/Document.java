package org.epnoi.storage.model;

import lombok.Data;
import lombok.ToString;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(exclude = {"content","tokens"}, callSuper = true)
public class Document extends Resource {

    private String publishedOn;

    private String publishedBy;

    private String authoredOn;

    private String authoredBy;

    private String contributedBy;

    private String retrievedFrom;

    private String retrievedOn;

    private String format;

    private String language;

    private String title;

    private String subject;

    private String description;

    private String rights;

    private String type;

    private String content;

    private String tokens;
}
