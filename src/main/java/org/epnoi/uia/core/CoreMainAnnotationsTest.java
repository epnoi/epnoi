package org.epnoi.uia.core;

import java.util.List;

public class CoreMainAnnotationsTest {

	public static String TEST_USER_URI = "http://www.epnoi.org/users/testUser";

	public static void main(String[] args) {

		Core core = CoreUtility.getUIACore();
		List<String> mathURIs=core.getAnnotationHandler().getLabeledAs("math");	
		System.out.println("MATH_________________________________"+mathURIs.size());
	
		/*
		for(String paperURI:core.getAnnotationHandler().getLabeledAs("math")){
			System.out.println("Math paper> "+paperURI);
		}
	*/	
		List<String> csURIs=core.getAnnotationHandler().getLabeledAs("cs");	
		System.out.println("CS____________________________________"+csURIs.size());
		/*
		for(String paperURI:core.getAnnotationHandler().getLabeledAs("cs")){
			System.out.println("Math paper> "+paperURI);
		}
		*/
		
		List<String> phisicsURIs=core.getAnnotationHandler().getLabeledAs("physics");	
		System.out.println("CS____________________________________"+phisicsURIs.size());
	}

}
