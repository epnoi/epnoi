package org.epnoi.uia.learner.nlp;

import gate.Annotation;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.InvalidOffsetException;

public class TermCandidatesFinder {
	private SerialAnalyserController controller = null;
	private Corpus corpus = null;

	public Document findTermCandidates(String content) {
		Document doc = null;
		try {
			doc = Factory.newDocument(content);
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.corpus.add(doc);

		// controller.setCorpus(
		try {
			controller.execute();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		corpus.remove(0);
		System.out.println("csize----> " + corpus.size());
		System.out.println("---> " + doc.toXml());
		return doc;

	}

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
		CorpusCreator corpusCreator = new CorpusCreator();

		String gateHomePath = TermCandidatesFinder.class.getResource("")
				.getPath() + "/gate";
		String documentsPath = TermCandidatesFinder.class.getResource("")
				.getPath() + "/documents";
		String resultsPath = gateHomePath + "/results";
*/
	}

	public static void main(String[] args) {

		System.out
				.println("TermCandidatesFinder test================================================================");

		TermCandidatesFinder app = new TermCandidatesFinder();
		app.init();
		Document document = app
				.findTermCandidates("My  taylor is rich, and my pretty mom is in the big kitchen");

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

		System.out
				.println("TermCandidatesFinder test is over!================================================================");
	}

}
