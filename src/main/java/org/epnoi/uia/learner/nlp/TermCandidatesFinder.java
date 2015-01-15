package org.epnoi.uia.learner.nlp;

import org.epnoi.uia.learner.nlp.gate.ControllerCreator;

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

public class TermCandidatesFinder {
	private static final long MIN_CONTENT_LENGHT = 4;
	private SerialAnalyserController controller = null;
	private Corpus corpus = null;

	// ----------------------------------------------------------------------------------

	public synchronized Document findTermCandidates(String content) {
		Document doc = null;
		try {
			doc = Factory.newDocument(content);
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (doc.getContent().size() > TermCandidatesFinder.MIN_CONTENT_LENGHT) {

			this.corpus.add(doc);

			// controller.setCorpus(
			try {
				controller.execute();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("---------------> " + content);
				System.out.println("---------------> " + doc.toString());
				doc = new DocumentImpl();
			}
			corpus.remove(0);
		}
		return doc;

	}

	// ----------------------------------------------------------------------------------

	public void init() {
		ControllerCreator controllerCreator = new ControllerCreator();
		// MainFrame.getInstance().setVisible(true);
		this.controller = controllerCreator.createController();

		try {
			this.corpus = Factory.newCorpus("Test Data Corpus");
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.controller.setCorpus(this.corpus);
		/*
		 * CorpusCreator corpusCreator = new CorpusCreator();
		 * 
		 * String gateHomePath = TermCandidatesFinder.class.getResource("")
		 * .getPath() + "/gate"; String documentsPath =
		 * TermCandidatesFinder.class.getResource("") .getPath() + "/documents";
		 * String resultsPath = gateHomePath + "/results";
		 */
	}

	// ----------------------------------------------------------------------------------

	public static void main(String[] args) {

		System.out
				.println("TermCandidatesFinder test================================================================");

		TermCandidatesFinder app = new TermCandidatesFinder();
		app.init();
		Document document = app
				.findTermCandidates("My  taylor is rich, and my pretty mom is in the big kitchen");

		String documentAsString = document.toXml();
		System.out.println("---");
		System.out.println(documentAsString);
		System.out.println("---");
		Document document2 = null;
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