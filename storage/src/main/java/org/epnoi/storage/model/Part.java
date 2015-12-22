package org.epnoi.storage.model;

import lombok.Data;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
public class Part extends Resource {

    private String sense;

    private String content;

    private String tokens;

}
