package org.epnoi.api;


import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.epnoi.model.modules.Profiles;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;

@Configuration
//@ComponentScan("org.epnoi.api")
@Import(org.epnoi.EpnoiConfig.class)

@ComponentScan(basePackages = {"org.epnoi.api.thrift","org.epnoi.api.rest"})
@PropertySource("classpath:epnoi.properties")

public class ApiConfig {

//Swagger related beans

    @Profile(Profiles.DEVELOP)
    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public ApiListingResource apiListingResource() {
        return new ApiListingResource();
    }


    @Profile(Profiles.DEVELOP)
    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public SwaggerSerializers swaggerSerializer() {
        return new SwaggerSerializers();
    }

    @Profile(Profiles.DEVELOP)
    @Bean
    @Scope(BeanDefinition.SCOPE_SINGLETON)
    public BeanConfig beanConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("0.0.1");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080/epnoi/rest");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("org.epnoi.api.rest.services.knowledgebase,org.epnoi.api.rest.services.nlp, org.epnoi.api.rest.services.search,org.epnoi.api.rest.services.uia");
        beanConfig.setScan(true);
        return beanConfig;
    }

}
