package org.epnoi.storage.model;

import lombok.Data;
import lombok.ToString;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString(exclude = {"content","tokens"}, callSuper = true)
public class Part extends Resource {

    private String sense;

    private String content;

    private String tokens;

}
