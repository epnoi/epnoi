package org.epnoi.uia.core;

import org.epnoi.model.parameterization.ParametersModel;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by rgonza on 8/11/15.
 */
public class CoreSpring {
    public static void main(String[] args) {
        System.out.println("Entering!");
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(org.epnoi.EpnoiConfig.class);
        ParametersModel parametersModel = applicationContext.getBean(ParametersModel.class);
        System.out.println("This is the readed bean >"+parametersModel);
        System.out.println("Exiting!");
    }
}
