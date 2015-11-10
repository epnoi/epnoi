package org.epnoi.uia.core;

import gate.Annotation;
import gate.Document;
import gate.util.InvalidOffsetException;
import org.epnoi.model.Context;
import org.epnoi.model.User;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;

import java.util.List;

public class CoreMainAnnotationsTest {

	public static String TEST_USER_URI = "http://www.epnoi.org/users/testUser";

	public static void main(String[] args) {

		Core core = CoreUtility.getUIACore();

		User annotatedUser = new User();
		annotatedUser.setUri("http://annotatedUser");
		core.getInformationHandler().put(annotatedUser, new Context());
		core.getAnnotationHandler().label(annotatedUser.getUri(), "math");

		/*
		 * List<String> userURIs = core.getAnnotationHandler().getLabeledAs(
		 * "math", UserRDFHelper.USER_CLASS);
		 * System.out.println("USER_________________________________" +
		 * userURIs.size());
		 * 
		 * List<String> mathURIs = core.getAnnotationHandler().getLabeledAs(
		 * "math", RDFHelper.PAPER_CLASS);
		 * System.out.println("MATH_________________________________" +
		 * mathURIs.size());
		 * 
		 * 
		 * List<String> csURIs = core.getAnnotationHandler().getLabeledAs("cs",
		 * RDFHelper.PAPER_CLASS);
		 * System.out.println("CS____________________________________" +
		 * csURIs.size());
		 */
		/*
		 * System.out.println("----> the content is " +
		 * core.getInformationAccess().getContent( "oai:arXiv.org:0705.3833",
		 * RDFHelper.PAPER_CLASS));
		 */
		// for (String paperURI :
		// core.getAnnotationHandler().getLabeledAs("cs")) {
		/*
		 * ystem.out.println(paperURI+" CS paper> " +
		 * core.getInformationAccess().getContent( paperURI,
		 * RDFHelper.PAPER_CLASS));
		 */

		// }

		/*
		 * for(String paperURI:core.getAnnotationHandler().getLabeledAs("cs")){
		 * System.out.println("Math paper> "+paperURI); }
		 */

		System.out
				.println("------------------------------------------------------------------------------");
		List<String> phisicsURIs = core.getAnnotationHandler().getLabeledAs(
				"Physics   Physics Education", RDFHelper.PAPER_CLASS);
		System.out.println("G 2 2____________________________________"
				+ phisicsURIs);

		// _whatever("oai:arXiv.org:0711.3503", core);

		// String uri = "oai:arXiv.org:1012.2513";

		// System.out.println(uri+" exist? "+core.getInformationAccess().contains(uri,RDFHelper.PAPER_CLASS));

		System.out.println("Este es el type > "
				+ core.getInformationHandler().get("http://testResearchObject"));

	}

	private static void _whatever(String URI, Core core) {
		/*
		Content<String> content = core.getInformationHandler().getContent(URI);

		Content<String> annotatedContent = core.getInformationHandler()
				.getAnnotatedContent(URI, URI);

		System.out.println("->> " + annotatedContent.getContent());

		XMLUtils.writeToFile(annotatedContent.getContent(), URI
				+ "GATEdocument.xml");

		if (annotatedContent.getContent() != null) {
			*/
			/*
			 * String gateHomePath = TermCandidatesFinder.class.getResource("")
			 * .getPath() + "/gate"; String pluginsPath = gateHomePath +
			 * "/plugins"; String grammarsPath =
			 * TermCandidatesFinder.class.getResource("") .getPath() +
			 * "/grammars/nounphrases";
			 * 
			 * System.out.println("The gateHomePath is " + gateHomePath);
			 * System.out.println("The pluginsPath is " + pluginsPath);
			 * System.out.println("The grammarsPath is " + grammarsPath);
			 * 
			 * File gateHomeDirectory = new File(gateHomePath); File pDir = new
			 * File(pluginsPath);
			 * 
			 * Gate.setPluginsHome(pDir);
			 * 
			 * Gate.setGateHome(gateHomeDirectory); Gate.setUserConfigFile(new
			 * File(gateHomeDirectory, "user-gate.xml"));
			 * 
			 * try { Gate.init(); } catch (GateException e1) {
			 * 
			 * e1.printStackTrace(); }
			 */
			/*
			 * System.out .println(
			 * "..............................................................> "
			 * + annotatedContent.getContent());
			 */
			/*
			 * TermCandidatesFinder termCandidatesFinder = new
			 * TermCandidatesFinder(); termCandidatesFinder.init();
			 * showTerms(termCandidatesFinder.findTermCandidates(content
			 * .getContent()));
			 */
		/*
			Document document = null;
			try {
				document = (Document) Factory
						.createResource(
								"gate.corpora.DocumentImpl",
								Utils.featureMap(
										gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
										annotatedContent.getContent(),
										gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME,
										"text/xml"));
				System.out.println("---> annotations ---> "
						+ document.getAnnotations());

			} catch (ResourceInstantiationException e) {
				e.printStackTrace();

			}
			showTerms(document);
		}
		*/
	}

	private static void showTerms(Document document) {
		for (Annotation annotation : document.getAnnotations().get(
				"TermCandidate")) {
			// System.out.println("The rule :>"+annotation.getFeatures().get("rule"));
			annotation.getStartNode();
			try {
				System.out.println(document.getContent().getContent(
						annotation.getStartNode().getOffset(),
						annotation.getEndNode().getOffset()));
			} catch (InvalidOffsetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
