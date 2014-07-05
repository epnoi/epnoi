package org.epnoi.uia.core;

import java.util.List;

import org.epnoi.model.Context;
import org.epnoi.model.User;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;

public class CoreMainAnnotationsTest {

	public static String TEST_USER_URI = "http://www.epnoi.org/users/testUser";

	public static void main(String[] args) {

		Core core = CoreUtility.getUIACore();

		User annotatedUser = new User();
		annotatedUser.setURI("http://annotatedUser");
		core.getInformationAccess().put(annotatedUser, new Context());
		core.getAnnotationHandler().label(annotatedUser.getURI(), "math");

		List<String> userURIs = core.getAnnotationHandler().getLabeledAs(
				"math", UserRDFHelper.USER_CLASS);
		System.out.println("USER_________________________________"
				+ userURIs.size());

		List<String> mathURIs = core.getAnnotationHandler().getLabeledAs(
				"math", RDFHelper.PAPER_CLASS);
		System.out.println("MATH_________________________________"
				+ mathURIs.size());

		for (String paperURI : core.getAnnotationHandler().getLabeledAs("math")) {
			System.out.println(paperURI+" Math paper> "
					+ core.getInformationAccess().getContent(
							paperURI, RDFHelper.PAPER_CLASS));
		}

		List<String> csURIs = core.getAnnotationHandler().getLabeledAs("cs",
				RDFHelper.PAPER_CLASS);
		System.out.println("CS____________________________________"
				+ csURIs.size());
		System.out.println("----> the content is "
				+ core.getInformationAccess().getContent(
						"oai:arXiv.org:0705.3833", RDFHelper.PAPER_CLASS));

		for (String paperURI : core.getAnnotationHandler().getLabeledAs("cs")) {
			System.out.println(paperURI+" CS paper> "
					+ core.getInformationAccess().getContent(
							paperURI, RDFHelper.PAPER_CLASS));
		}
		
		/*
		 * for(String paperURI:core.getAnnotationHandler().getLabeledAs("cs")){
		 * System.out.println("Math paper> "+paperURI); }
		 */

		List<String> phisicsURIs = core.getAnnotationHandler().getLabeledAs(
				"physics", RDFHelper.PAPER_CLASS);
		System.out.println("Physics____________________________________"
				+ phisicsURIs.size());

	}
}
