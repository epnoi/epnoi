package org.epnoi.nlp.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import org.epnoi.model.parameterization.ParametersModel;

import java.io.File;
import java.net.MalformedURLException;

public class ControllerCreator {
	private ParametersModel parameters;

	// -----------------------------------------------------------------------------

	public void init(ParametersModel parameters) {
		this.parameters=parameters;
	}

	// -----------------------------------------------------------------------------

	public SerialAnalyserController createController() {
		// In this piece of code we just initialize the processing resources.
		// Gate + the associated plugins are initialized in the core
		// initialization
		try {

		//	System.out.println("......> " + this.core);
			String gateHomePath = this.parameters.getNlp()
					.getGatePath();
			String grammarsPath = gateHomePath + "/grammars/nounphrases";

			SerialAnalyserController controller = (SerialAnalyserController) Factory
					.createResource("gate.creole.SerialAnalyserController");

			ProcessingResource reseter = (ProcessingResource) Factory
					.createResource("gate.creole.annotdelete.AnnotationDeletePR");

			ProcessingResource englishTokeniser = (ProcessingResource) Factory
					.createResource("gate.creole.tokeniser.DefaultTokeniser");

			ProcessingResource sentenceSplitter = (ProcessingResource) Factory
					.createResource("gate.creole.splitter.RegexSentenceSplitter");

			ProcessingResource POStagger = (ProcessingResource) Factory
					.createResource("gate.creole.POSTagger");
			FeatureMap dependencyParserFeature = Factory.newFeatureMap();
			dependencyParserFeature.put("addConstituentAnnotations", false);
			dependencyParserFeature.put("reusePosTags", true);
			dependencyParserFeature.put("addDependencyFeatures", false);

			/*
			 * ProcessingResource dependencyParser = (ProcessingResource)
			 * Factory .createResource("gate.stanford.Parser",
			 * dependencyParserFeature);
			 */
			FeatureMap mainGrammarFeature = Factory.newFeatureMap();
			mainGrammarFeature.put("grammarURL", new File(grammarsPath
					+ "/main.jape").toURI().toURL());

			LanguageAnalyser mainGrammarTransducer = (LanguageAnalyser) Factory
					.createResource("gate.creole.Transducer",
							mainGrammarFeature);
			controller.add(reseter);
			controller.add(englishTokeniser);
			controller.add(sentenceSplitter);
			controller.add(POStagger);
			// sac.add(dependencyParser);
			controller.add(mainGrammarTransducer);

			return controller;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
