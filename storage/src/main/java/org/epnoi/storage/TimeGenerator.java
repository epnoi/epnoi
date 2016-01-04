package org.epnoi.storage;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by cbadenes on 04/01/16.
 */
@Component
public class TimeGenerator {


    private final SimpleDateFormat df;

    public TimeGenerator(){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
    }

    public String getNowAsISO(){
        return df.format(new Date());
    }


}
