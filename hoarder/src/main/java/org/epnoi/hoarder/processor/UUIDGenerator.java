package org.epnoi.hoarder.processor;

import org.epnoi.hoarder.AbstractRouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGenerator implements Processor {


    @Override
    public void process(Exchange exchange) throws Exception {
        String uuid = UUID.randomUUID().toString();
        exchange.setProperty(AbstractRouteBuilder.PUBLICATION_UUID,uuid);
    }
}
