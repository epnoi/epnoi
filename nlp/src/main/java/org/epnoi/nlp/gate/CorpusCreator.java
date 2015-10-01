package org.epnoi.nlp.gate;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.ResourceInstantiationException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class CorpusCreator {

	public Corpus create(String folderPath) {

		File folder = new File(folderPath);
		File[] allFiles = folder.listFiles();

		Corpus corpus = null;

		try {
			corpus = Factory.newCorpus("Test Data Corpus");
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// arraylist to store document resources
		ArrayList<Document> documentResList = new ArrayList<Document>();

		int i = 0; // variable to name each doc uniquely
		Integer uniqueColKey = 0; // unique column key for each arraylist of
									// each column
		int rowCount = 0;

		// processing of docs starts
		long docsStartTime = System.currentTimeMillis();
		int countFiles = 0; // keep counting till max cluster size
		int totalFilesProcessed = 0;
		for (File f : allFiles) { // for each file
			System.out.println("Creating the gate document for "
					+ f.getAbsolutePath());
			URL sourceUrl = null;
			try {
				sourceUrl = f.toURI().toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			countFiles++; // increment for each file
			totalFilesProcessed++;

			// feature map for creating documents
			FeatureMap params = Factory.newFeatureMap();
			params.put(Document.DOCUMENT_URL_PARAMETER_NAME, sourceUrl);
			params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");

			FeatureMap features = Factory.newFeatureMap();
			features.put("createdOn", new Date());
			i++; // increment i to name each doc and corpus uniquely
			// create document with specified params, features and unique name
			Document doc = null;
			try {
				doc = (Document) Factory.createResource(
						"gate.corpora.DocumentImpl", params, features,
						f.getName());
			} catch (ResourceInstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// add document in the corpus
			// System.out.println(".................................................> "+doc.getContent().toString());
			corpus.add(doc);
		}
		return corpus;
	}
}