package org.epnoi.uia.core;

import org.epnoi.model.RelationHelper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.parameterization.ParametersModel;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by rgonza on 8/11/15.
 */
public class CoreSpring {
    public static void main(String[] args) {
        System.out.println("Entering!");
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(org.epnoi.EpnoiConfig.class);
        for(String bean: applicationContext.getBeanDefinitionNames()){
            System.out.println("Bean: "+ bean);
        }
        ParametersModel parametersModel = applicationContext.getBean(ParametersModel.class);
        System.out.println("This is the readed bean >"+parametersModel);
        System.out.println("Exiting!");
        Core core = applicationContext.getBean(Core.class);
        try {
            System.out.println("--->" + core.getKnowledgeBaseHandler().getKnowledgeBase().areRelated("depeche mode", "band", RelationHelper.HYPERNYM));
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
