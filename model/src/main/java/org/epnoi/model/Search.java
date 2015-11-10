package org.epnoi.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class Search implements Resource {

	private String uri;
	private String title;
	private String description;
	ArrayList<String> expressions;

	// --------------------------------------------------------------------------------

	public Search() {
		this.expressions = new ArrayList<String>();
	}

	// --------------------------------------------------------------------------------

	public ArrayList<String> getExpressions() {
		return expressions;
	}

	// --------------------------------------------------------------------------------

	public void setExpressions(ArrayList<String> expressions) {
		this.expressions = expressions;
	}

	// --------------------------------------------------------------------------------

	public void addExpression(String expression) {
		this.expressions.add(expression);
	}

	// --------------------------------------------------------------------------------

	public String getDescription() {
		return description;
	}

	// --------------------------------------------------------------------------------

	public void setDescription(String description) {
		this.description = description;
	}

	// --------------------------------------------------------------------------------


	public String getUri() {
		return this.uri;
	}

	// --------------------------------------------------------------------------------

	public void setUri(String uRI) {
		uri = uRI;
	}

	// --------------------------------------------------------------------------------

	public String getTitle() {
		return title;
	}

	// --------------------------------------------------------------------------------

	public void setTitle(String title) {
		this.title = title;
	}

	// --------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Search [uri=" + uri + ", title=" + title + ", description="
				+ description + ", expressions=" + expressions + "]";
	}

}
