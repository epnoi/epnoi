package org.epnoi.model.modules;

import lombok.Data;

/**
 * Created by cbadenes on 26/11/15.
 */
@Data
public class BindingKey {

    String key;

    String group;


    private BindingKey(String key, String group){
        this.key = key;
        this.group = group;
    }

    public static BindingKey of(RoutingKey routingKey, String groupKey){
        return new BindingKey(routingKey.key,groupKey);
    }

}
