package org.epnoi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*The missing bits
 * categories 
 * links
 * infoboxes 
 * multilanguage support?
 */



public class WikipediaPage implements Resource {
	private String uri;
	private String term;
	private String termDefinition;
	private List<String> sections;
	private Map<String, String> sectionsContent;

	// --------------------------------------------------------------------------------

	public WikipediaPage() {
		this.sections = new ArrayList<String>();
		this.sectionsContent = new HashMap<String, String>();
	}

	// --------------------------------------------------------------------------------

	//@XmlElement(name = "URI")
	public String getUri() {
		return uri;
	}

	// --------------------------------------------------------------------------------

	public void setUri(String uri) {
		this.uri = uri;
	}

	// --------------------------------------------------------------------------------

	public String getTerm() {
		return term;
	}

	// --------------------------------------------------------------------------------

	public void setTerm(String term) {
		this.term = term;
	}

	// --------------------------------------------------------------------------------

	public String getTermDefinition() {
		return termDefinition;
	}

	// --------------------------------------------------------------------------------

	public void setTermDefinition(String termDefinition) {
		this.termDefinition = termDefinition;
	}

	// --------------------------------------------------------------------------------

	public List<String> getSections() {
		return sections;
	}

	// --------------------------------------------------------------------------------

	public void setSections(List<String> sections) {
		this.sections = sections;
	}

	// --------------------------------------------------------------------------------

	public Map<String, String> getSectionsContent() {
		return sectionsContent;
	}

	// --------------------------------------------------------------------------------

	public void setSectionsContent(Map<String, String> sectionsContent) {
		this.sectionsContent = sectionsContent;
	}

	// --------------------------------------------------------------------------------

	public void addSection(String section) {
		this.sections.add(section);
	}

	// --------------------------------------------------------------------------------

	public void addSectionContent(String section, String content) {
		this.sectionsContent.put(section, content);
	}

	// --------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "WikipediaPage [uri=" + uri + ", term=" + term
				+ ", termDefinition=" + termDefinition + ", sections="
				+ sections + ", sectionsContent=" + sectionsContent + "]";
	}

	// --------------------------------------------------------------------------------

	
}
