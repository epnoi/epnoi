package org.epnoi.eventbus.guava;

import com.google.common.base.Strings;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by cbadenes on 26/11/15.
 */
public class GuavaCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

        String eventBusUri = conditionContext.getEnvironment().getProperty("epnoi.eventbus.uri");

        return Strings.isNullOrEmpty(eventBusUri) || eventBusUri.startsWith("local");
    }
}
