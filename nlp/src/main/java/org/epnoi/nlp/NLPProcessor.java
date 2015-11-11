package org.epnoi.nlp;

import com.rits.cloning.Cloner;
import gate.Annotation;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.corpora.DocumentImpl;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.InvalidOffsetException;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.nlp.gate.ControllerCreator;

import java.util.List;
import java.util.logging.Logger;

public class NLPProcessor {
	private static final Logger logger = Logger.getLogger(NLPProcessor.class.getName());

	private static final long MIN_CONTENT_LENGHT = 4;
	private SerialAnalyserController controller = null;
	private ControllerCreator controllerCreator = null;
	private Corpus corpus = null;
	private Cloner cloner = new Cloner();

	private ParametersModel parameters;

	// ----------------------------------------------------------------------------------

	public Document process(String content) {
		Document document = null;
		try {
			document = Factory.newDocument(content);
			if (document.getContent().size() > NLPProcessor.MIN_CONTENT_LENGHT) {
				this.corpus.add(document);

				try {
					controller.execute();
				} catch (ExecutionException e) {
					document = _handleProcessingException(e);
				}
				corpus.remove(0);
			}

			Document clonedDocument = cloner.deepClone(document);
			release(document);
			return clonedDocument;
		} catch (ResourceInstantiationException e) {
			document = _handleResourceInstantiationException(e);
		}
		return document;
	}

	// ----------------------------------------------------------------------------------

	private Document _handleProcessingException(ExecutionException e) {
		Document document;
		document = new DocumentImpl();
		logger.severe("There was an error processing the document, an empty annotated document has been created");
		logger.severe(e.getMessage());
		return document;
	}

	// ----------------------------------------------------------------------------------

	private Document _handleResourceInstantiationException(Exception e) {
		Document document;
		document = new DocumentImpl();
		logger.severe(
				"There was an error locating resources while processing the document, an empty annotated document has been created");
		logger.severe(e.getMessage());
		return document;
	}

	// ----------------------------------------------------------------------------------

	public void release(Document document) {
		try {
			Factory.deleteResource(document);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------------------------

	public void init(ParametersModel parameters) {
		this.parameters=parameters;
		this.controllerCreator = new ControllerCreator();
		// MainFrame.getInstance().setVisible(true);
		this.controllerCreator.init(parameters);
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
		for (Annotation annotation : document.getAnnotations().get("TermCandidate")) {
			// System.out.println("The rule
			// :>"+annotation.getFeatures().get("rule"));
			annotation.getStartNode();
			try {
				System.out.println(document.getContent().getContent(annotation.getStartNode().getOffset(),
						annotation.getEndNode().getOffset()));
			} catch (InvalidOffsetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// ----------------------------------------------------------------------------------

	private static void showDependencies(Document document) {

		for (Annotation dependencyAnnotation : document.getAnnotations().get("Dependency")) {
			// System.out.println("The rule
			// :>"+annotation.getFeatures().get("rule"));

			List<Integer> ids = (List<Integer>) dependencyAnnotation.getFeatures().get("args");
			System.out.println(
					"--------------------------------------------------------------------------------------------------------------------------------");
			System.out.println(dependencyAnnotation.getFeatures().get("kind"));

			for (Integer id : ids) {

				System.out.println(document.getAnnotations().get(id).getFeatures().get("string"));

			}

			// System.out.println("> "+dependencyAnnotation);

		}

	}
/*
	private static void createDependencyGraph(Document document) {

		Graph<Integer, SyntacticPatternGraphEdge> patternGraph = new SimpleGraph<Integer, SyntacticPatternGraphEdge>(
				SyntacticPatternGraphEdge.class);

		for (Annotation dependencyAnnotation : document.getAnnotations().get("Dependency")) {


			List<Integer> ids = (List<Integer>) dependencyAnnotation.getFeatures().get("args");
			System.out.println(
					"--------------------------------------------------------------------------------------------------------------------------------");

			System.out.println();
			String kind = (String) dependencyAnnotation.getFeatures().get("kind");

			Integer source = ids.get(0);
			Integer target = ids.get(1);

			if (source != null && target != null) {
				patternGraph.addVertex(source);
				patternGraph.addVertex(target);
				patternGraph.addEdge(source, target, new SyntacticPatternGraphEdge(kind));
			} else {
				System.out.println("Source > " + source + " > " + "Target > " + target);
			}

		}
		System.out.println("--> " + patternGraph.toString());

	}
*/
	// ----------------------------------------------------------------------------------
/*FOR_TEST
	public static void main(String[] args) {

		System.out.println("TermCandidatesFinder test================================================================");

		Core core = CoreUtility.getUIACore();

		NLPProcessor termCandidatesFinder = new NLPProcessor();
		termCandidatesFinder.init(core);

		Document document = termCandidatesFinder
				.process("Bell, a company which is based in LA, makes and distributes computer products");

		String documentAsString = document.toXml();
		
		Document document2 = null;

		Utils.featureMap(gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, documentAsString,
				gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/xml");
		try {
			document2 = (Document) Factory.createResource("gate.corpora.DocumentImpl",
					Utils.featureMap(gate.Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, documentAsString,
							gate.Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, "text/xml"));
		} catch (ResourceInstantiationException e) {
			e.printStackTrace();
		}
		// System.out.println("mmm> " + document2.toXml());
		createDependencyGraph(document);
		System.out.println(">>> " + document.toString());

		System.out.println(
				"TermCandidatesFinder test is over!================================================================");
	}
*/
}