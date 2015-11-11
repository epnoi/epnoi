package org.epnoi.uia.core.eventbus;

import org.epnoi.model.modules.EventBus;
import org.epnoi.model.parameterization.EventBusParameters;
import org.epnoi.model.parameterization.ParametersModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by cbadenes on 14/10/15.
 */
public class EventBusFactoryTest {

    @Test
    public void simulateInternalParameters(){

        ParametersModel parameters = new ParametersModel();

        EventBusParameters parameter = new EventBusParameters();
        parameter.setUri("local://");
        parameters.setEventBus(parameter);

        EventBus instance = EventBusFactory.newInstance(parameters);

        Assert.assertTrue( instance instanceof InternalEventBusImpl);

    }


    @Test
    public void simulateExternalParameters(){

        ParametersModel parameters = new ParametersModel();

        EventBusParameters parameter = new EventBusParameters();
        parameter.setUri("amqp://");
        parameters.setEventBus(parameter);

        EventBus instance = EventBusFactory.newInstance(parameters);

        Assert.assertTrue( instance instanceof ExternalEventBusImpl);
    }

    @Test
    public void emptyParameters(){

        ParametersModel parameters = new ParametersModel();

        EventBus instance = EventBusFactory.newInstance(parameters);

        Assert.assertTrue( instance instanceof InternalEventBusImpl);
    }

}
