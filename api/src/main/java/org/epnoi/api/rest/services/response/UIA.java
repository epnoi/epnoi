package org.epnoi.api.rest.services.response;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class UIA {
	private String timestamp;
	private ArrayList<InformationStore> informationStores = new ArrayList<InformationStore>();
	private KnowledgeBase knowledgeBase;
	
	
	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public ArrayList<InformationStore> getInformationStores() {
		return informationStores;
	}

	public void setInformationStores(
			ArrayList<InformationStore> informationStores) {
		this.informationStores = informationStores;
	}

	public void addInformationStores(InformationStore informationStore) {
		this.informationStores.add(informationStore);
	}
	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}

	public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}


	
}
