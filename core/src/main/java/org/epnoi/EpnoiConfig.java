package org.epnoi;

import org.epnoi.knowledgebase.KnowledgeBaseImpl;
import org.epnoi.model.modules.Profiles;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.model.parameterization.ParametersModelReader;
import org.epnoi.uia.annotation.AnnotationHandlerImpl;
import org.epnoi.uia.core.CoreImpl;
import org.epnoi.uia.domains.DomainsHandlerImpl;
import org.epnoi.uia.harvester.HarvestersHandlerImpl;
import org.epnoi.uia.informationhandler.InformationHandlerImpl;
import org.epnoi.uia.nlp.NLPHandlerImpl;
import org.epnoi.uia.search.SearchHandlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.logging.Logger;

@Configuration
@ComponentScan(basePackageClasses = {CoreImpl.class, NLPHandlerImpl.class, SearchHandlerImpl.class, AnnotationHandlerImpl.class, InformationHandlerImpl.class, DomainsHandlerImpl.class, KnowledgeBaseImpl.class, HarvestersHandlerImpl.class})

@PropertySource("classpath:epnoi.properties")
public class EpnoiConfig {
    private static final Logger logger = Logger.getLogger(EpnoiConfig.class
            .getName());

    public static final String EPNOI_PROPERTIES = "epnoi.configurable.properties";
    public static final String EPNOI_PROPERTIES_PATH = "epnoi.configurable.properties.configurationFilePath";


    @Autowired
    ConfigurableEnvironment configurableEnvironment;


    @Bean
    @Profile({Profiles.DEVELOP, Profiles.DEPLOYMENT})
    public PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Bean
    @Profile(Profiles.DEVELOP)
    public ParametersModel developParametersModel(@Value("${epnoi.config.path}") String configurationFilePath) {
        logger.info("Creating the parameters model");
        logger.info("The configuration property epnoi.config.path is set to "+configurationFilePath);

        ParametersModel parametersModel = null;
        if (configurableEnvironment != null) {
            org.springframework.core.env.PropertySource<?> epnoiPropertySources = configurableEnvironment.getPropertySources().get(EpnoiConfig.EPNOI_PROPERTIES);

            if (epnoiPropertySources != null && (epnoiPropertySources.getProperty(EpnoiConfig.EPNOI_PROPERTIES_PATH) != null)) {
                String deployPath = (String) configurableEnvironment.getPropertySources().get(EpnoiConfig.EPNOI_PROPERTIES).getProperty(EpnoiConfig.EPNOI_PROPERTIES_PATH);

                logger.info("The deployment path set in the configurable environment is set to " + deployPath);

                parametersModel = ParametersModelReader.read(deployPath);

            }
        } else {
            parametersModel = ParametersModelReader.read(configurationFilePath);
        }
        return parametersModel;


    }


}
/* LEFT FOR FUTURE USE
    @Bean
    @Profiles(DEPLOY_PROFILE)
    public ParametersModel deployParametersModel() {
        System.out.println("->" + configurableEnvironment.getPropertySources().get(EpnoiConfig.EPNOI_PROPERTIES).getProperty(EpnoiConfig.EPNOI_PROPERTIES_PATH));
        String deployPath = (String) configurableEnvironment.getPropertySources().get(EpnoiConfig.EPNOI_PROPERTIES).getProperty(EpnoiConfig.EPNOI_PROPERTIES_PATH);
        ParametersModel parametersModel =null;
        if(deployPath!=null) {


          ParametersModelReader.read(deployPath);
        }else{
            ParametersModelReader.read(deployPath);
        }
        return parametersModel;
    }
*/


