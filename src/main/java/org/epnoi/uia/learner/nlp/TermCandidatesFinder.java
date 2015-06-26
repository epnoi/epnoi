package org.epnoi.uia.learner.nlp;

import gate.Annotation;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.Utils;
import gate.corpora.DocumentImpl;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.InvalidOffsetException;

import java.util.List;

import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.learner.nlp.gate.ControllerCreator;
import org.epnoi.uia.learner.relations.patterns.syntactic.SyntacticPatternGraphEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

public class TermCandidatesFinder {
	private Core core;
	private static final long MIN_CONTENT_LENGHT = 4;
	private SerialAnalyserController controller = null;
	private Corpus corpus = null;

	// ----------------------------------------------------------------------------------

	public Document findTermCandidates(String content) {
		Document document = null;
		try {
			
			document = Factory.newDocument(content);
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		if (document.getContent().size() > TermCandidatesFinder.MIN_CONTENT_LENGHT) {
			
			this.corpus.add(document);

			try {
				
				controller.execute();
			
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				document = new DocumentImpl();
			}
			corpus.remove(0);
		}

		return document;
	}

	// ----------------------------------------------------------------------------------

	public void init(Core core) {
		this.core = core;
		ControllerCreator controllerCreator = new ControllerCreator();
		// MainFrame.getInstance().setVisible(true);
		controllerCreator.init(core);
		this.controller = controllerCreator.createController();

		try {
			this.corpus = Factory.newCorpus("Working Corpus");
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.controller.setCorpus(this.corpus);
	}

	
	// ----------------------------------------------------------------------------------

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

	// ----------------------------------------------------------------------------------

	private static void showDependencies(Document document) {

		for (Annotation dependencyAnnotation : document.getAnnotations().get(
				"Dependency")) {
			// System.out.println("The rule :>"+annotation.getFeatures().get("rule"));

			List<Integer> ids = (List<Integer>) dependencyAnnotation
					.getFeatures().get("args");
			System.out
					.println("--------------------------------------------------------------------------------------------------------------------------------");
			System.out.println(dependencyAnnotation.getFeatures().get("kind"));

			for (Integer id : ids) {

				System.out.println(document.getAnnotations().get(id)
						.getFeatures().get("string"));

			}

			// System.out.println("> "+dependencyAnnotation);

		}

	}

	private static void createDependencyGraph(Document document) {

		Graph<Integer, SyntacticPatternGraphEdge> patternGraph = new SimpleGraph<Integer, SyntacticPatternGraphEdge>(
				SyntacticPatternGraphEdge.class);

		for (Annotation dependencyAnnotation : document.getAnnotations().get(
				"Dependency")) {
			// System.out.println("The rule :>"+annotation.getFeatures().get("rule"));

			List<Integer> ids = (List<Integer>) dependencyAnnotation
					.getFeatures().get("args");
			System.out
					.println("--------------------------------------------------------------------------------------------------------------------------------");

			System.out.println();
			String kind = (String) dependencyAnnotation.getFeatures().get(
					"kind");

			// for (Integer id : ids) {

			/*
			 * System.out.println("S>" +
			 * document.getAnnotations().get(ids.get(0)).getFeatures()
			 * .get("string")); System.out.println("T>" +
			 * document.getAnnotations().get(ids.get(1)).getFeatures()
			 * .get("string"));
			 */
			Integer source = ids.get(0);
			Integer target = ids.get(1);

			if (source != null && target != null) {
				patternGraph.addVertex(source);
				patternGraph.addVertex(target);
				patternGraph.addEdge(source, target,
						new SyntacticPatternGraphEdge(kind));
			} else {
				System.out.println("Source > " + source + " > " + "Target > "
						+ target);
			}

		}
		System.out.println("--> " + patternGraph.toString());

	}
	
	// ----------------------------------------------------------------------------------

		public static void main(String[] args) {

			System.out
					.println("TermCandidatesFinder test================================================================");

			Core core = CoreUtility.getUIACore();

			TermCandidatesFinder termCandidatesFinder = new TermCandidatesFinder();
			termCandidatesFinder.init(core);
			
			
			/*
			 * Document document = termCandidatesFinder .findTermCandidates(
			 * "My  taylor is rich, and my pretty mom is in the big kitchen");
			 */
			/*
			 * Document document = termCandidatesFinder .findTermCandidates(
			 * "Bills on ports and immigration were submitted by Senator Brownback, Republican of Kansas"
			 * );
			 */
			Document document = termCandidatesFinder
					.findTermCandidates("Bell, a company which is based in LA, makes and distributes computer products");

			String documentAsString = document.toXml();
			/*
			 * System.out.println("---"); System.out.println(documentAsString);
			 * System.out.println("---");
			 */
			Document document2 = null;

			Utils.featureMap(gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
					documentAsString,
					gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/xml");
			try {
				document2 = (Document) Factory
						.createResource(
								"gate.corpora.DocumentImpl",
								Utils.featureMap(
										gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,
										documentAsString,
										gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME,
										"text/xml"));
			} catch (ResourceInstantiationException e) {
				e.printStackTrace();
			}
			// System.out.println("mmm>  " + document2.toXml());
			createDependencyGraph(document);
			System.out.println(">>> "+document.toString());

			System.out
					.println("TermCandidatesFinder test is over!================================================================");
		}

}