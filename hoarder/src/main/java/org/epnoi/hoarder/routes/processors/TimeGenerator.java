package org.epnoi.hoarder.routes.processors;

import com.google.common.base.Joiner;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.epnoi.model.Record;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;


@Component
public class TimeGenerator implements Processor{


    private static final Logger LOG = LoggerFactory.getLogger(TimeGenerator.class);

    DateTimeZone timezone = DateTimeZone.forID("Zulu");//UTC

    DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis().withZone(timezone);

    DateTimeFormatter dateFormatter = ISODateTimeFormat.date();

    DecimalFormat decimalFormat = new DecimalFormat("00");

    @Override
    public void process(Exchange exchange) throws Exception {

        // Add current time
        long current = DateTime.now(timezone).getMillis();
        addProperty(exchange, Record.TIME,dateTimeFormatter.print(current));


        // Read published date
        String time = exchange.getProperty(Record.PUBLICATION_PUBLISHED, String.class);

        if ((time == null) || (time.trim().equals(""))){
            LOG.warn("no published date info for: {}! Collector timestamp used", Record.PUBLICATION_URI);
            time = dateTimeFormatter.print(current);
        }

        // Parse time (ISO-8601)
        DateTime dateTime;
        try{
             dateTime = dateTimeFormatter.parseDateTime(time);
        }catch (IllegalArgumentException e){
            dateTime = dateFormatter.parseDateTime(time);
        }

        // Add date in format: yyyy-mm-dd
        addProperty(exchange, Record.PUBLICATION_PUBLISHED_DATE, Joiner.on("-").join(dateTime.getYear(),decimalFormat.format(dateTime.getMonthOfYear()),decimalFormat.format(dateTime.getDayOfMonth())));

        // Add time in format: millis
        addProperty(exchange, Record.PUBLICATION_PUBLISHED_MILLIS,dateTime.getMillis());

    }


    private void addProperty(Exchange exchange, String key, Object value){
        exchange.setProperty(key, value);
        LOG.debug("Added Exchange Property: {}={}", key, value);
    }
}
