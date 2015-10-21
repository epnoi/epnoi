package org.epnoi.hoarder.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ErrorHandler implements Processor{

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        // exception stored in a exchange property
        Throwable caused = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);

        log.error("Error processing message: {}", exchange.getIn(), caused);
    }
}
