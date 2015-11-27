package org.epnoi.hoarder.routes.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.epnoi.hoarder.routes.SourceProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGenerator implements Processor {


    @Override
    public void process(Exchange exchange) throws Exception {
        String uuid = UUID.randomUUID().toString();
        exchange.setProperty(SourceProperty.PUBLICATION_UUID,uuid);
    }
}
