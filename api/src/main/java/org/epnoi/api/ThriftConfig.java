package org.epnoi.api;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(org.epnoi.EpnoiConfig.class)
//@Import(org.epnoi.api.ThriftConfig.class)
@ComponentScan(basePackages = {"org.epnoi.api"})
@PropertySource("classpath:epnoi.properties")

public class ThriftConfig {
  //  @Profile(Profiles.DEVELOP)



}
