package org.epnoi.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.epnoi.uia.commons.StringUtils;

public class Relation implements Resource {
	private String URI;
	private String source;
	private String target;
	private String type;
	// This table contains the probability
	private Map<String, Double> provenanceRelationhoodTable;

	public Relation() {
		this.provenanceRelationhoodTable = new HashMap<String, Double>();
	}

	// ------------------------------------------------------------------------------------------------------------

	public String getSource() {
		return source;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setSource(String source) {
		this.source = source;
	}

	// ------------------------------------------------------------------------------------------------------------

	public String getTarget() {
		return target;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setTarget(String target) {
		this.target = target;
	}

	// ------------------------------------------------------------------------------------------------------------

	public String getURI() {
		return URI;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setURI(String uRI) {
		URI = uRI;
	}

	// ------------------------------------------------------------------------------------------------------------

	public double getRelationhood() {

		return calculateAverage(this.provenanceRelationhoodTable.values());
	}

	// ------------------------------------------------------------------------------------------------------------

	private double calculateAverage(Collection<Double> values) {
		if (values == null || values.isEmpty()) {
			return 0;
		}

		double sum = 0;
		for (Double value : values) {
			sum += value;
		}

		return sum / values.size();
	}

	// ------------------------------------------------------------------------------------------------------------

	public void addProvenanceSentence(String sentenceContent,
			double relationProbability) {
		this.provenanceRelationhoodTable.put(sentenceContent,
				relationProbability);

	}

	// ------------------------------------------------------------------------------------------------------------

	public static String buildURI(String source, String target, String type,
			String domain) {
		String uri = "http://" + domain + "/"
				+ StringUtils.replace(source, "[^a-zA-Z0-9]", "_") + "/"
				+ StringUtils.replace(source, "[^a-zA-Z0-9]", "_") + "/" + type;
		return uri;

	}

	// ------------------------------------------------------------------------------------------------------------

	public String getType() {
		return type;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setType(String type) {
		this.type = type;
	}

	// ------------------------------------------------------------------------------------------------------------

	public Map<String, Double> getProvenanceRelationhoodTable() {
		return provenanceRelationhoodTable;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setProvenanceRelationhoodTable(
			Map<String, Double> provenanceRelationhoodTable) {
		this.provenanceRelationhoodTable = provenanceRelationhoodTable;
	}

	// ------------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Relation [URI=" + URI + ", source=" + source + ", target="
				+ target + ", type=" + type + ", provenanceRelationhoodTable="
				+ provenanceRelationhoodTable + "]";
	}
}
