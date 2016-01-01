package org.epnoi.storage.model;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by cbadenes on 01/01/16.
 */
public class ResourceUtils {

    public static final String URI = "uri";

    public static <T,S> T map (S resource, Class<T> clazz){
        try {
            T wrapper = clazz.newInstance();
            BeanUtils.copyProperties(wrapper,resource);
            return wrapper;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error copying fields",e);
        }
    }
}
