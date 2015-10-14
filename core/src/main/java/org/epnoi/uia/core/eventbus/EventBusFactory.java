package org.epnoi.uia.core.eventbus;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.parameterization.ParametersModel;

import java.io.IOException;

/**
 * Created by cbadenes on 14/10/15.
 */
public class EventBusFactory {



    public static EventBus newInstance(ParametersModel parameters){

        String uri          = parameters.getEventBus().getUri();
        String transport    = Strings.isNullOrEmpty(uri)? "" : StringUtils.substringBefore(uri,":");

        switch (transport.toLowerCase()){
            case "amqp" :   return new ExternalEventBusImpl(uri);
            default:        return new InternalEventBusImpl();
        }

    }
}
