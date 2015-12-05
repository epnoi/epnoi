package org.epnoi.api;


import org.epnoi.model.modules.Profiles;
import org.springframework.context.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Import(org.epnoi.EpnoiConfig.class)
//@Import(org.epnoi.api.ThriftConfig.class)
@ComponentScan(basePackages = {"org.epnoi.api"})
@PropertySource("classpath:epnoi.properties")

public class ThriftConfig {
  //  @Profile(Profiles.DEVELOP)



}
