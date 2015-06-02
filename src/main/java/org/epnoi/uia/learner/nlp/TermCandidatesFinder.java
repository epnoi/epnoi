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

import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.learner.nlp.gate.ControllerCreator;

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

	public static void main(String[] args) {

		System.out
				.println("TermCandidatesFinder test================================================================");

		Core core = CoreUtility.getUIACore();

		TermCandidatesFinder termCandidatesFinder = new TermCandidatesFinder();
		termCandidatesFinder.init(core);
		Document document = termCandidatesFinder
				.findTermCandidates("My  taylor is rich, and my pretty mom is in the big kitchen");

		String documentAsString = document.toXml();
		System.out.println("---");
		System.out.println(documentAsString);
		System.out.println("---");
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
		System.out.println("mmm>  " + document2.getAnnotations());
		showTerms(document2);

		System.out
				.println("TermCandidatesFinder test is over!================================================================");
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
}