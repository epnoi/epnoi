package org.epnoi;

import org.epnoi.knowledgebase.KnolwedgeBaseImpl;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.model.parameterization.ParametersModelReader;
import org.epnoi.sources.InformationSourcesHandlerImpl;
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
@ComponentScan(basePackageClasses = {CoreImpl.class, NLPHandlerImpl.class, SearchHandlerImpl.class, AnnotationHandlerImpl.class, InformationSourcesHandlerImpl.class, InformationHandlerImpl.class, DomainsHandlerImpl.class, KnolwedgeBaseImpl.class, HarvestersHandlerImpl.class})
@PropertySource("classpath:/epnoi.properties")
public class EpnoiConfig {
    private static final Logger logger = Logger.getLogger(EpnoiConfig.class
            .getName());
    public static final String DEPLOY_PROFILE = "deploy";
    public static final String DEVELOP_PROFILE = "develop";
    public static final String EPNOI_PROPERTIES = "epnoi.configurable.properties";
    public static final String EPNOI_PROPERTIES_PATH = "epnoi.configurable.properties.configurationFilePath";


    @Autowired
    ConfigurableEnvironment configurableEnvironment;

    @Bean
    public
    static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${epnoi.config.path}")
    String configurationFilePath;


    @Bean
    @Profile(DEVELOP_PROFILE)
    public ParametersModel developParametersModel() {
        logger.info("Creating the parameters model");
        String deployPath = (String) configurableEnvironment.getPropertySources().get(EpnoiConfig.EPNOI_PROPERTIES).getProperty(EpnoiConfig.EPNOI_PROPERTIES_PATH);
        logger.info("The deployment path set in the configurable environment is set to " + deployPath);
        ParametersModel parametersModel = null;
        if (deployPath != null) {
            parametersModel = ParametersModelReader.read(deployPath);
        } else {
            parametersModel = ParametersModelReader.read(configurationFilePath);
        }

        return parametersModel;
    }


}
/* LEFT FOR FUTURE USE
    @Bean
    @Profile(DEPLOY_PROFILE)
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


