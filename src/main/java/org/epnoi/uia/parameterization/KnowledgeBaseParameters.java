package org.epnoi.uia.parameterization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "knowledgeBase")
public class KnowledgeBaseParameters {
	private WordnetParameters wordnet;

	public WordnetParameters getWordnet() {
		return wordnet;
	}

	public void setWordnet(WordnetParameters wordnet) {
		this.wordnet = wordnet;
	}

}
