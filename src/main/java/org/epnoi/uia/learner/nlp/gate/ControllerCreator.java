package org.epnoi.uia.learner.nlp.gate;

import gate.Factory;
import gate.FeatureMap;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;

import org.epnoi.uia.core.Core;

public class ControllerCreator {
	private Core core;

	// -----------------------------------------------------------------------------

	public void init(Core core) {
		this.core = core;
	}

	public SerialAnalyserController createController() {
		// In this piece of code we just initialize the processing resources.
		// Gate + the associated plugins are initialized in the core
		// initialization
		try {
			long startLoadResourcesTime = System.currentTimeMillis(); // start
																		// //
																		// time
			String grammarsPath = ControllerCreator.class.getResource(
					"grammars/nounphrases").getPath();

			String gateHomePath = this.core.getParameters().getGatePath();

			SerialAnalyserController sac = (SerialAnalyserController) Factory
					.createResource("gate.creole.SerialAnalyserController");

			ProcessingResource englishTokeniser = (ProcessingResource) Factory
					.createResource("gate.creole.tokeniser.DefaultTokeniser");

			ProcessingResource sentenceSplitter = (ProcessingResource) Factory
					.createResource("gate.creole.splitter.RegexSentenceSplitter");

			ProcessingResource POStagger = (ProcessingResource) Factory
					.createResource("gate.creole.POSTagger");

			ProcessingResource dependencyParser = (ProcessingResource) Factory
					.createResource("gate.stanford.Parser");

			FeatureMap mainGrammarFeature = Factory.newFeatureMap();
			mainGrammarFeature.put("grammarURL", new File(grammarsPath
					+ "/main.jape").toURI().toURL());

			LanguageAnalyser mainGrammarTransducer = (LanguageAnalyser) Factory
					.createResource("gate.creole.Transducer",
							mainGrammarFeature);

			/*
			 * FeatureMap startJapeFeature = Factory.newFeatureMap();
			 * startJapeFeature.put("grammarURL", new File(grammarsPath +
			 * "starttag.jape").toURI().toURL());
			 * 
			 * 
			 * 
			 * FeatureMap startJapeFeature = Factory.newFeatureMap();
			 * startJapeFeature.put("grammarURL", new File(grammarsPath +
			 * "starttag.jape").toURI().toURL());
			 * 
			 * FeatureMap endJapeFeature = Factory.newFeatureMap();
			 * endJapeFeature.put("grammarURL", new File(grammarsPath +
			 * "value.jape").toURI().toURL()); FeatureMap columnNameJapeFeature
			 * = Factory.newFeatureMap();
			 * columnNameJapeFeature.put("grammarURL", new File(grammarsPath +
			 * "columnname.jape").toURI().toURL());
			 * 
			 * LanguageAnalyser startTagJape = (LanguageAnalyser) Factory
			 * .createResource("gate.creole.Transducer", startJapeFeature);
			 * LanguageAnalyser endTagJape = (LanguageAnalyser) Factory
			 * .createResource("gate.creole.Transducer", endJapeFeature);
			 * LanguageAnalyser colNameJape = (LanguageAnalyser) Factory
			 * .createResource("gate.creole.Transducer", columnNameJapeFeature);
			 */

			sac.add(englishTokeniser);
			sac.add(sentenceSplitter);
			sac.add(POStagger);
			sac.add(dependencyParser);
			sac.add(mainGrammarTransducer);

			long endLoadResourcesTime = System.currentTimeMillis(); // end time
			long loadResourcesTime = endLoadResourcesTime
					- startLoadResourcesTime; // total time
			System.out.println("Time to load Processing resources: "
					+ loadResourcesTime + "ms");
			return sac;
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
