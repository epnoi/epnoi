package org.epnoi.uia.core.eventbus;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.parameterization.EventBusParameters;
import org.epnoi.model.parameterization.ParametersModel;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by cbadenes on 14/10/15.
 */
public class EventBusFactory {

    private static final Logger logger = Logger.getLogger(EventBusFactory.class.getName());

    public static EventBus newInstance(ParametersModel parameters){

        String transport = "local";

        try{
            transport    = StringUtils.substringBefore(parameters.getEventBus().getUri(),":").toLowerCase();
        }catch (Exception e){
            logger.warning("Error reading eventbus uri: " + e.getMessage() + ". Initializing local eventbus");
        }

        switch (transport){
            case "amqp" :   return new ExternalEventBusImpl(parameters.getEventBus().getUri());
            default:        return new InternalEventBusImpl();
        }

    }
}
