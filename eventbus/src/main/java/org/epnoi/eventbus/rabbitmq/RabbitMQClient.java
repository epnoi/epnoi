package org.epnoi.eventbus.rabbitmq;

import com.rabbitmq.client.*;
import org.epnoi.model.Event;
import org.epnoi.model.modules.EventBusSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * Created by cbadenes on 09/10/15.
 */
public class RabbitMQClient {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQClient.class);

    private static String EXCHANGE_TYPE = "topic";

    private Connection connection;

    private Map<String,Channel> channels;


    public RabbitMQClient(){
        this.channels = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param uri e.g. amqp://userName:password@hostName:portNumber/virtualHost
     * @throws IOException
     * @throws TimeoutException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws URISyntaxException
     */
    public void connect(String uri) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);

        LOG.info("trying to connect to: " + uri);
        this.connection = factory.newConnection();
        LOG.info("connected to: " + uri);
    }

    public void disconnect() throws IOException, TimeoutException {
        if (!channels.isEmpty()){
            for (Channel channel: channels.values()){
                if (channel.isOpen()) channel.close();
            }
        }
        this.connection.close();
    }

    /**
     * Channel instances must not be shared between threads.
     * Applications should prefer using a Channel per thread instead of sharing the same Channel across multiple threads.
     * @return
     */
    public Channel newChannel(String exchange) throws IOException {

        if (channels.containsKey(exchange)){
            LOG.debug("reusing already created channel for exchange: " + exchange);
            return channels.get(exchange);
        }

        LOG.debug("creating a new channel for exchange: " + exchange);

        Channel channel = connection.createChannel();

        // a durable, non-autodelete exchange of "topic" type
        channel.exchangeDeclare(exchange, EXCHANGE_TYPE, true);

        // Handling unroutable messages
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode,
                                     String replyText,
                                     String exchange,
                                     String routingKey,
                                     AMQP.BasicProperties basicProperties,
                                     byte[] bytes) throws IOException {
                LOG.warn("Unexpected Message from routing-key: " + routingKey + " in exchange: " + exchange + " [" + bytes + "]");

            }
        });


        channels.put(exchange,channel);

        LOG.debug("new channel created for exchange: " + exchange);
        return channel;
    }

    /**
     *
     * @param channel
     * @param exchange
     * @param routingKey
     * @param message
     * @throws IOException
     */
    public void publish(Channel channel, String exchange, String routingKey, byte[] message) throws IOException {

        //maybe better externalize by publisher
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .contentType("text/plain")
                .deliveryMode(2) // persistent
                .priority(0)
                .build();

        channel.basicPublish(exchange, routingKey, properties, message);

        LOG.debug(" Message: [" + message + "] sent to exchange: '" + exchange + "' with routingKey: '" + routingKey + "'");
    }


    public void consume(String exchange, String queue, String bindingKey, final EventBusSubscriber subscriber) throws IOException {

        Channel channel = newChannel(exchange);

        //maybe better externalize to config file
        //a non-durable, non-exclusive, autodelete queue with a well-known name and a maximum length of 1000 messages
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-length", 1000); // x-max-length-bytes
        boolean durable     = false;
        boolean exclusive   = false;
        boolean autodelete  = true;
        channel.queueDeclare(queue, durable, exclusive, autodelete, args);

        channel.queueBind(queue, exchange, bindingKey);

        boolean autoAck = true;
        channel.basicConsume(queue, autoAck, new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {

                String routingKey   = envelope.getRoutingKey();
                String contentType  = properties.getContentType();
                long deliveryTag    = envelope.getDeliveryTag();

                LOG.debug(" Received message: [" + body + "] in routingKey: '" + routingKey + "'");

                subscriber.handle(Event.from(body));

                //maybe better Avoid Auto ACK. Handle manual ACK
                //channel.basicAck(deliveryTag, false);
            }
        });

        channels.put(String.valueOf(subscriber.hashCode()), channel);
    }


    public void clean(EventBusSubscriber subscriber) throws IOException, TimeoutException {
        String key = String.valueOf(subscriber.hashCode());
        if (channels.containsKey(key)){
            Channel channel = channels.get(key);
            channel.close();
            channels.remove(key);
        }
    }

}
