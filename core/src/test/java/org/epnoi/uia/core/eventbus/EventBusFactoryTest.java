package org.epnoi.uia.core.eventbus;

import org.epnoi.model.modules.EventBus;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.uia.core.CoreUtility;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by cbadenes on 14/10/15.
 */
public class EventBusFactoryTest {

    @Test
    public void readParameters(){

        ParametersModel parameters = CoreUtility.readParameters();

        EventBus instance = EventBusFactory.newInstance(parameters);

       // Assert.assertTrue( instance instanceof ExternalEventBusImpl);

    }

}
