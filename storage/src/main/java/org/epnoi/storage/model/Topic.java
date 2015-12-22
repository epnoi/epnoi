package org.epnoi.storage.model;

import lombok.Data;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
public class Topic extends Resource{

    private String content;

    private String analysis;
}
