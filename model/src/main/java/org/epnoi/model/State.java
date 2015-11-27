package org.epnoi.model;

/**
 * Created by cbadenes on 26/11/15.
 */
public enum State {
    NEW("new"),
    OPENED("opened"),
    CLOSED("closed"),
    MARKED("marked"),
    ANALYZED("analyzed"),
    DELETED("deleted"),
    ANY("*");

    String keyValue;

    State(String key){ keyValue = key;}

    public String key(){ return keyValue;}
}
