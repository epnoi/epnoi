package org.epnoi.model.search;

import java.util.ArrayList;
import java.util.List;

public class Facet {
	private String name;
	private List<FacetValue> values = new ArrayList<FacetValue>();

	// ------------------------------------------------------------------------
	
	public String getName() {
		return name;
	}

	// ------------------------------------------------------------------------
	
	public void setName(String name) {
		this.name = name;
	}

	// ------------------------------------------------------------------------

	public List<FacetValue> getValues() {
		return values;
	}

	// ------------------------------------------------------------------------
	
	public void setValues(List<FacetValue> values) {
		this.values = values;
	}

	// ------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "SelectionFacet [name=" + name + ", values=" + values + "]";
	}

	// ------------------------------------------------------------------------
}
