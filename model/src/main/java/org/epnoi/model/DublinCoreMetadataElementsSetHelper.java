package org.epnoi.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.epnoi.model.rdf.DublinCoreRDFHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DublinCoreMetadataElementsSetHelper {
	@JsonIgnore
	public static List<String> dcProperties = Arrays.asList(
			DublinCoreRDFHelper.TITLE_PROPERTY,
			DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
			DublinCoreRDFHelper.DATE_PROPERTY,
			DublinCoreRDFHelper.CREATOR_PROPERTY);
	@JsonIgnore
	public static List<String> dcDatatypeProperties = Arrays.asList(
			DublinCoreRDFHelper.TITLE_PROPERTY,
			DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
			DublinCoreRDFHelper.DATE_PROPERTY);
	@JsonIgnore
	public static List<String> dcObjectProperties = Arrays
			.asList(DublinCoreRDFHelper.CREATOR_PROPERTY);

	@JsonIgnore
	private static Map<String, String> namesResolutionTable = new HashMap<String, String>();
	static {
		namesResolutionTable.put(DublinCoreRDFHelper.TITLE_PROPERTY, "title");
		namesResolutionTable.put(DublinCoreRDFHelper.DESCRIPTION_PROPERTY,
				"description");
		namesResolutionTable.put(DublinCoreRDFHelper.DATE_PROPERTY, "date");
		namesResolutionTable.put(DublinCoreRDFHelper.CREATOR_PROPERTY,
				"creator");
	}

	private static Map<String, String> urisResolutionTable = new HashMap<String, String>();
	static {
		urisResolutionTable.put("title",
				DublinCoreRDFHelper.TITLE_PROPERTY);
		urisResolutionTable.put("description",
				DublinCoreRDFHelper.DESCRIPTION_PROPERTY);
		urisResolutionTable
				.put("date", DublinCoreRDFHelper.DATE_PROPERTY);
		urisResolutionTable.put("creator",
				DublinCoreRDFHelper.CREATOR_PROPERTY);
	}

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

	// --------------------------------------------------------------------------

	public static boolean isObjectProperty(String predicateURI) {

		return dcObjectProperties.contains(predicateURI);
	}

	// --------------------------------------------------------------------------

	public static boolean isDatatypeProperty(String predicateURI) {

		return dcDatatypeProperties.contains(predicateURI);
	}

	// --------------------------------------------------------------------------

	public static boolean isDublinCoreProperty(String property) {
		return dcProperties.contains(property);
	}

	// --------------------------------------------------------------------------

	public static String getPropertyName(String property) {

		return namesResolutionTable.get(property);
	}

	// --------------------------------------------------------------------------

	public static String getPropertyURI(String property) {

		return urisResolutionTable.get(property);
	}
}
