package org.epnoi.uia.core.eventbus.rabbitmq;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by cbadenes on 26/11/15.
 */
public class RabbitMQCondition implements Condition{
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return conditionContext.getEnvironment().getProperty("epnoi.eventbus.uri").startsWith("amqp");
    }
}
