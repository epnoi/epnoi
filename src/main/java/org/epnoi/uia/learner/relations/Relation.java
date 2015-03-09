package org.epnoi.uia.learner.relations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.epnoi.model.Term;
import org.epnoi.uia.commons.StringUtils;

public class Relation {
	private String URI;
	private Term source;
	private Term target;
	private Term type;
	// This table contains the probability
	private Map<String, Double> provenanceRelationhoodTable;

	public Relation() {
		this.provenanceRelationhoodTable = new HashMap<String, Double>();
	}

	// ------------------------------------------------------------------------------------------------------------

	public Term getSource() {
		return source;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setSource(Term source) {
		this.source = source;
	}

	// ------------------------------------------------------------------------------------------------------------

	public Term getTarget() {
		return target;
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setTarget(Term target) {
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
	
	@Override
	public String toString() {
		return "Relation [URI=" + URI + ", source=" + source.getAnnotatedTerm().getWord() + ", target="
				+ target.getAnnotatedTerm().getWord() + ", type=" + type + ", provenanceRelationhoodTable="
				+ provenanceRelationhoodTable + "]";
	}
	
	// ------------------------------------------------------------------------------------------------------------
	
		

}
