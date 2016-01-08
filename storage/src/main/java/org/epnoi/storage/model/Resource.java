package org.epnoi.storage.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by cbadenes on 22/12/15.
 */
@Data
@ToString
public class Resource implements Serializable{

    private String uri;

    private String creationTime;

}
