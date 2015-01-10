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
	public SerialAnalyserController createController() {
		try {
			long startLoadResourcesTime = System.currentTimeMillis(); // start
																		// time
			String gateHomePath = Core.class.getResource("").getPath()+"/gate";
			String pluginsPath = gateHomePath+ "/plugins";
			String grammarsPath = Core.class.getResource("").getPath()+"/grammars/nounphrases";

			System.out.println("The gateHomePath is "+gateHomePath);
			System.out.println("The pluginsPath is "+pluginsPath);
			System.out.println("The grammarsPath is "+grammarsPath);
			
/*
			File gateHomeDirectory = new File(gateHomePath);
			File pDir = new File(pluginsPath);

			Gate.setPluginsHome(pDir);

			Gate.setGateHome(gateHomeDirectory);
			Gate.setUserConfigFile(new File(gateHomeDirectory, "user-gate.xml"));

			Gate.init(); // to prepare the GATE library
			
			URL anniePlugin = new File(pDir, "ANNIE").toURI().toURL(); // specify
																		// plugin
																		// to be
																		// loaded
			Gate.getCreoleRegister().registerDirectories(anniePlugin); // finally
																		// register
																		// the
																		// plugin
	*/
			SerialAnalyserController sac = (SerialAnalyserController) Factory
					.createResource("gate.creole.SerialAnalyserController");

			ProcessingResource aEngTokeniser = (ProcessingResource) Factory
					.createResource("gate.creole.tokeniser.DefaultTokeniser");
			
			
			
			ProcessingResource sentenceSplitter = (ProcessingResource) Factory
					.createResource("gate.creole.splitter.RegexSentenceSplitter");

			
			ProcessingResource POStagger = (ProcessingResource) Factory
					.createResource("gate.creole.POSTagger");

			
				
			
			
			
	
			
			FeatureMap mainGrammarFeature = Factory.newFeatureMap();
			mainGrammarFeature.put("grammarURL", new File(grammarsPath
					+ "/main.jape").toURI().toURL());
			
			LanguageAnalyser mainGrammarTransducer = (LanguageAnalyser) Factory
					.createResource("gate.creole.Transducer", mainGrammarFeature);
	
			
			
			/*
			FeatureMap startJapeFeature = Factory.newFeatureMap();
			startJapeFeature.put("grammarURL", new File(grammarsPath
					+ "starttag.jape").toURI().toURL());
			
			
		
			FeatureMap startJapeFeature = Factory.newFeatureMap();
			startJapeFeature.put("grammarURL", new File(grammarsPath
					+ "starttag.jape").toURI().toURL());
			
			FeatureMap endJapeFeature = Factory.newFeatureMap();
			endJapeFeature.put("grammarURL", new File(grammarsPath
					+ "value.jape").toURI().toURL());
			FeatureMap columnNameJapeFeature = Factory.newFeatureMap();
			columnNameJapeFeature.put("grammarURL", new File(grammarsPath
					+ "columnname.jape").toURI().toURL());

			LanguageAnalyser startTagJape = (LanguageAnalyser) Factory
					.createResource("gate.creole.Transducer", startJapeFeature);
			LanguageAnalyser endTagJape = (LanguageAnalyser) Factory
					.createResource("gate.creole.Transducer", endJapeFeature);
			LanguageAnalyser colNameJape = (LanguageAnalyser) Factory
					.createResource("gate.creole.Transducer",
							columnNameJapeFeature);
*/
			
			sac.add(aEngTokeniser);
			sac.add(sentenceSplitter);
			sac.add(POStagger);
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
		} catch (GateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
