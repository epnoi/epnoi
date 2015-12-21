package org.epnoi.harvester.routes.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class ErrorHandler implements Processor {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        // the caused by exception is stored in a property on the exchange
        Throwable caused = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);

        log.error("Error processing message: {}", exchange.getIn(), caused);
    }
}

