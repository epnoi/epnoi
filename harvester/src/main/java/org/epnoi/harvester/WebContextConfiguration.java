package org.epnoi.harvester;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.List;

/**
 * Created by cbadenes on 01/12/15.
 */
@Configuration
@ComponentScan({"org.epnoi.uia.core.eventbus","org.epnoi.harvester", "org.epnoi.storage"})
//@ComponentScan( basePackages = "org.epnoi" )
@PropertySource("classpath:epnoi.properties")
public class WebContextConfiguration {


    @Autowired
    List<RouteBuilder> builders;

    @Bean
    public SpringCamelContext camelContext(ApplicationContext applicationContext) throws Exception {
        SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
        for(RouteBuilder builder : builders){
            camelContext.addRoutes(builder);
        }
        return camelContext;
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
