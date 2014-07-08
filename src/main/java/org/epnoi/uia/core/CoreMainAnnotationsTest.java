package org.epnoi.uia.core;

import gate.Annotation;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.Utils;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.util.List;

import org.epnoi.model.Content;
import org.epnoi.model.Context;
import org.epnoi.model.User;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.learner.nlp.TermCandidatesFinder;

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
		/*
		 * for (String paperURI :
		 * core.getAnnotationHandler().getLabeledAs("math")) {
		 * System.out.println(paperURI+" Math paper> " +
		 * core.getInformationAccess().getContent( paperURI,
		 * RDFHelper.PAPER_CLASS));
		 * 
		 * }
		 */
		List<String> csURIs = core.getAnnotationHandler().getLabeledAs("cs",
				RDFHelper.PAPER_CLASS);
		System.out.println("CS____________________________________"
				+ csURIs.size());
		/*
		 * System.out.println("----> the content is " +
		 * core.getInformationAccess().getContent( "oai:arXiv.org:0705.3833",
		 * RDFHelper.PAPER_CLASS));
		 */
		for (String paperURI : core.getAnnotationHandler().getLabeledAs("cs")) {
			/*
			 * ystem.out.println(paperURI+" CS paper> " +
			 * core.getInformationAccess().getContent( paperURI,
			 * RDFHelper.PAPER_CLASS));
			 */

		}

		/*
		 * for(String paperURI:core.getAnnotationHandler().getLabeledAs("cs")){
		 * System.out.println("Math paper> "+paperURI); }
		 */

		List<String> phisicsURIs = core.getAnnotationHandler().getLabeledAs(
				"physics", RDFHelper.PAPER_CLASS);
		System.out.println("Physics____________________________________"
				+ phisicsURIs.size());

		_whatever("oai:arXiv.org:0705.3659", core);
	}

	private static void _whatever(String URI, Core core) {
		Content<String> annotatedContent = core.getInformationAccess()
				.getAnnotatedContent(URI, RDFHelper.PAPER_CLASS);

		
		System.out.println("->> "+annotatedContent.getContent());
		
		if (annotatedContent.getContent() != null) {

			String gateHomePath = TermCandidatesFinder.class.getResource("")
					.getPath() + "/gate";
			String pluginsPath = gateHomePath + "/plugins";
			String grammarsPath = TermCandidatesFinder.class.getResource("")
					.getPath() + "/grammars/nounphrases";

			System.out.println("The gateHomePath is " + gateHomePath);
			System.out.println("The pluginsPath is " + pluginsPath);
			System.out.println("The grammarsPath is " + grammarsPath);

			File gateHomeDirectory = new File(gateHomePath);
			File pDir = new File(pluginsPath);

			Gate.setPluginsHome(pDir);

			Gate.setGateHome(gateHomeDirectory);
			Gate.setUserConfigFile(new File(gateHomeDirectory, "user-gate.xml"));

			try {
				Gate.init();
			} catch (GateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // to prepare the GATE library

			/*
			 * System.out .println(
			 * "..............................................................> "
			 * + annotatedContent.getContent());
			 */
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
				System.out.println("---> annotations ---> " + document.getAnnotations());
				
			} catch (ResourceInstantiationException e) { // TODO Auto-generated
															// catch block
															// e.printStackTrace();
															// }

				

				/*
				 * String content = annotatedContent.getContent(); Document
				 * document = new DocumentImpl(); XMLInputFactory inputFactory =
				 * XMLInputFactory.newInstance(); InputStream is = new
				 * ByteArrayInputStream(content.getBytes()); try {
				 * XMLStreamReader xsr = inputFactory.createXMLStreamReader(is);
				 * 
				 * xsr.next(); DocumentStaxUtils.readGateXmlDocument(xsr,
				 * document); } catch (XMLStreamException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */

			}
			showTerms(document);
		}
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
