package org.epnoi.hoarder;

import groovy.lang.GroovyClassLoader;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.Main;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.io.IOException;

/**
 * Created by cbadenes on 20/10/15.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class TestMain {

    @Value("${camel.config.groovy}")
    File groovyFile;


    @Autowired
    RouteBuilder routeBuilder;

    public static void main(String[] args) throws Exception {
        // Initialize Spring Context
        ConfigurableApplicationContext context = SpringApplication.run(TestMain.class, args);

        // Launch Camel Context
        Main main = new Main();
        main.enableHangupSupport();
        main.setApplicationContext((AbstractApplicationContext) context);
        main.addRouteBuilder(context.getBean(AbstractRouteBuilder.class));
        main.run();

    }

    @Bean
    public RouteBuilder groovyRouteBuilder() throws IOException, IllegalAccessException, InstantiationException {
        // Loading groovy class
        GroovyClassLoader gcl = new GroovyClassLoader();
        Class clazz = gcl.parseClass(groovyFile);
        return (AbstractRouteBuilder) clazz.newInstance();
    }


    @Bean
    public SpringCamelContext camelContext(ApplicationContext applicationContext) throws Exception {
        SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
        camelContext.addRoutes(routeBuilder);
        return camelContext;
    }



}