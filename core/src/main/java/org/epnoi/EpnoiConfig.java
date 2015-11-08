package org.epnoi;

import org.epnoi.model.modules.Core;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.model.parameterization.ParametersModelReader;
import org.epnoi.uia.core.CoreMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackageClasses = {Core.class,})
//@PropertySource("classpath:epnoi.properties")
public class EpnoiConfig {

    @Autowired
    Environment environment;

    @Bean
    public ParametersModel parametersModel() {
        //String path = environment.getProperty("epnoi.config.path");
        String path = CoreMain.class.getResource("CoreUtility.xml").getPath();
        ParametersModel parametersModel = ParametersModelReader.read(path);

        return parametersModel;
    }
/*
    @Bean
    public Core core(ParametersModel parametersModel) {

        return new CoreImpl(parametersModel);
    }
    */
}
