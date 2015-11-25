package org.epnoi.uia.core.eventbus;

import com.rabbitmq.client.Channel;
import org.epnoi.model.Event;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusPublisher;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.uia.core.eventbus.rabbitmq.RabbitMQClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 * Created by cbadenes on 09/10/15.
 */
public class ExternalEventBusImpl implements EventBus {

    private static final Logger logger = Logger.getLogger(ExternalEventBusImpl.class.getName());

    private static final String API_EXCHANGE = "epnoi.api";

    private final RabbitMQClient client;

    private final ConcurrentHashMap<EventBusPublisher, Channel> channels;

    private final String uri;

    public ExternalEventBusImpl(String uri){
        this.uri = uri;
        this.client = new RabbitMQClient();
        this.channels = new ConcurrentHashMap<>();
    }

    @Override
    public void init() {
        try {
            this.client.connect(uri);
        } catch (IOException | TimeoutException | NoSuchAlgorithmException | KeyManagementException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe(EventBusSubscriber subscriber) {
        logger.info("subscribing " + subscriber);
        try {
            this.client.consume(API_EXCHANGE, subscriber.group(), subscriber.topic(), subscriber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publish(EventBusPublisher publisher, Event event) {
        try {
            if (!channels.containsKey(publisher)){
                    channels.put(publisher, client.newChannel(API_EXCHANGE));
            }

            logger.fine("publisher: " + publisher + "publishing the event: " + event);
            this.client.publish(channels.get(publisher),API_EXCHANGE,publisher.topic(),event.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        try {
            this.client.disconnect();
        } catch (TimeoutException e) {
            logger.warning("Timeout trying to disconnect from MessageBroker");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
