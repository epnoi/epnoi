package org.epnoi.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epnoi.uia.informationstore.dao.rdf.DublinCoreRDFHelper;

public class DublinCoreMetadataElementsSet {
	public static List<String> dcProperties = Arrays.asList(
			DublinCoreRDFHelper.TITLE_PROPERTY,
			DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
			DublinCoreRDFHelper.DATE_PROPERTY,
			DublinCoreRDFHelper.CREATOR_PROPERTY);

	public static List<String> dcDatatypeProperties = Arrays.asList(
			DublinCoreRDFHelper.TITLE_PROPERTY,
			DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
			DublinCoreRDFHelper.DATE_PROPERTY);

	public static List<String> dcObjectProperties = Arrays
			.asList(DublinCoreRDFHelper.CREATOR_PROPERTY);
	/*
	 * NOT IMPLEMENTED 1.1 NAMESPACE Properties in the /elements/1.1/ namespace
	 * contributor , coverage , format , identifier , language , publisher ,
	 * relation , rights , source , subject , type
	 */

	/*
	 * NOT IMPLEMENTED /TERMS/ namespace abstract , accessRights , accrualMethod
	 * , accrualPeriodicity , accrualPolicy , alternative , audience , available
	 * , bibliographicCitation , conformsTo , contributor , coverage , created ,
	 * creator , date , dateAccepted , dateCopyrighted , dateSubmitted ,
	 * description , educationLevel , extent , format , hasFormat , hasPart ,
	 * hasVersion , identifier , instructionalMethod , isFormatOf , isPartOf ,
	 * isReferencedBy , isReplacedBy , isRequiredBy , issued , isVersionOf ,
	 * language , license , mediator , medium , modified , provenance ,
	 * publisher , references , relation , replaces , requires , rights ,
	 * rightsHolder , source , spatial , subject , tableOfContents , temporal ,
	 * title , type , valid
	 */

	private Map<String, List<String>> dublinCoreProperties = new HashMap<>();

	// --------------------------------------------------------------------------

	public boolean isDublinCoreProperty(String property) {
		return dcProperties.contains(property);
	}

	// --------------------------------------------------------------------------

	public Map<String, List<String>> getDublinCoreProperties() {
		return dublinCoreProperties;
	}

	// --------------------------------------------------------------------------

	public void setDublinCoreProperties(
			Map<String, List<String>> dublinCoreProperties) {
		this.dublinCoreProperties = dublinCoreProperties;
	}

	// --------------------------------------------------------------------------

	public boolean isObjectProperty(String predicateURI) {

		return dcObjectProperties.contains(predicateURI);
	}

	// --------------------------------------------------------------------------

	public boolean isDatatypeProperty(String predicateURI) {

		return dcDatatypeProperties.contains(predicateURI);
	}

	// --------------------------------------------------------------------------

	public void addPropertyValue(String propertyURI, String value) {

		List<String> values = this.dublinCoreProperties.get(propertyURI);
		if (values == null) {
			values = new ArrayList<String>();
			this.dublinCoreProperties.put(propertyURI, values);
		}
		values.add(value);

	}

	// --------------------------------------------------------------------------

	public List<String> getPropertyValues(String propertyURI) {
		return this.dublinCoreProperties.get(propertyURI);
	}

	// --------------------------------------------------------------------------

	public String getPropertyFirstValue(String propertyURI) {

		return this.dublinCoreProperties.get(propertyURI).get(0);
	}

	@Override
	public String toString() {
		return "DublinCoreMetadataElementsSet [dublinCoreProperties="
				+ dublinCoreProperties + "]";
	}

	// --------------------------------------------------------------------------

}
