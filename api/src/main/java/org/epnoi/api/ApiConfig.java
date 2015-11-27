package org.epnoi.api;


import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.springframework.context.annotation.*;

@Configuration
//@Import(org.epnoi.EpnoiConfig.class)
@ComponentScan(basePackages = {"org.epnoi.api"})
@PropertySource("classpath:/epnoi.properties")

public class ApiConfig {
    @Profile("develop")
    @Bean
    public ApiListingResource apiListingResource() {
        return new ApiListingResource();
    }
    @Profile("develop")
    @Bean
    public SwaggerSerializers swaggerSerializer() {
        return new SwaggerSerializers();
    }
@Profile("develop")
    @Bean()
    public BeanConfig beanConfig() {
        System.out.println("=======================================================================================> bean");
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8002");
        beanConfig.setBasePath("/api");
        beanConfig.setResourcePackage("org.epnoi.api.rest.services.knowledgebase");
        beanConfig.setScan(true);
        return beanConfig;
}
}
