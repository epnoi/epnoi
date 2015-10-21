package org.epnoi.hoarder;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by cbadenes on 20/10/15.
 */
@SpringBootApplication
public class TestMain {


    @Test
    public void startContext() {
        // Initialize Spring Context
        ConfigurableApplicationContext context = SpringApplication.run(WebContextConfiguration.class, new String[]{});


    }

}