package org.epnoi.uia.core.eventbus.rabbitmq;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Created by cbadenes on 13/10/15.
 */
@Category(IntegrationTest.class)
public class RabbitMQClientTest {

    private RabbitMQClient client;

    @Before
    public void setup(){
        this.client = new RabbitMQClient();
    }

    @After
    public void destroy(){

    }

    @Test
    public void connection() throws URISyntaxException, KeyManagementException, TimeoutException, NoSuchAlgorithmException, IOException, InterruptedException {

        String uri = "amqp://guest:guest@eventbus:5672/drinventor";

        this.client.connect(uri);

        Thread.sleep(1000);

        this.client.disconnect();

        Assert.assertTrue(true);
    }


}
