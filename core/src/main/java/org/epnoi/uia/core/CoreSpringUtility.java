package org.epnoi.uia.core;

import org.epnoi.EpnoiConfig;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.Profiles;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by rgonza on 8/11/15.
 */
public class CoreSpringUtility {
    private static final Logger logger = Logger.getLogger(CoreSpringUtility.class
            .getName());

    public static Core getCore(String configFilePath) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();


        applicationContext.getEnvironment().setActiveProfiles(Profiles.DEVELOP);
        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();

        Map epnoiProperties = new HashMap();
        epnoiProperties.put(EpnoiConfig.EPNOI_PROPERTIES_PATH, configFilePath);
        propertySources.addFirst(new MapPropertySource(EpnoiConfig.EPNOI_PROPERTIES, epnoiProperties));
        applicationContext.register(org.epnoi.EpnoiConfig.class);
        applicationContext.refresh();

        List<String> beans = new ArrayList<>();
        for (String bean : applicationContext.getBeanDefinitionNames()) {
            beans.add("   Bean: " + bean);
        }
        logger.info("Initializing the Spring context with the following beans: \n"+String.join("\n",beans));

        Core core = applicationContext.getBean(Core.class);
        return core;
    }

    public static Core getCore() {
        return CoreSpringUtility.getCore(null);
    }
/* TEST
    public static void main(String[] args) {
        System.out.println("Entering!");


        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();


        applicationContext.getEnvironment().setActiveProfiles(EpnoiConfig.DEPLOY_PROFILE);
        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();

        Map myMap = new HashMap();
        myMap.put(EpnoiConfig.EPNOI_PROPERTIES_PATH, "thepath");
        propertySources.addFirst(new MapPropertySource(EpnoiConfig.EPNOI_PROPERTIES, myMap));


        applicationContext.register(org.epnoi.EpnoiConfig.class);
        applicationContext.refresh();


        for (String bean : applicationContext.getBeanDefinitionNames()) {
            System.out.println("Bean: " + bean);
        }

        ParametersModel parametersModel = applicationContext.getBean(ParametersModel.class);
        System.out.println("This is the readed bean >"+parametersModel);
        System.out.println("Exiting!");
        Core core = applicationContext.getBean(Core.class);

        try {
            System.out.println("--->" + core.getKnowledgeBaseHandler().getKnowledgeBase().areRelated("depeche mode", "band", RelationHelper.HYPERNYMY));
        } catch (EpnoiInitializationException e) {
            e.printStackTrace();
        } catch (EpnoiResourceAccessException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("000000> "+core.getNLPHandler().process("My mom is in the kitchen"));
        } catch (EpnoiResourceAccessException e) {
            e.printStackTrace();
        }
        }

    }
*/
}
