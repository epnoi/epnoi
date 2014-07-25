package org.epnoi.uia.annotation;

import java.util.ArrayList;
import java.util.List;

import org.epnoi.model.Annotation;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.InformationStoreHelper;
import org.epnoi.uia.informationstore.VirtuosoInformationStore;
import org.epnoi.uia.informationstore.dao.rdf.AnnotationOntologyRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.AnnotationRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.parameterization.VirtuosoInformationStoreParameters;

public class AnnotationHandlerImpl implements AnnotationHandler {
	private Core core;

	// ------------------------------------------------------------------------------

	public AnnotationHandlerImpl(Core core) {
		this.core = core;
	}

	// ------------------------------------------------------------------------------

	public void annotate(Annotation annotation, String URI) {
		annotation.setAnnotatesResource(URI);
		this.core.getInformationAccess().put(annotation, new Context());
	}

	// ------------------------------------------------------------------------------

	public List<String> getAnnotatedAs(String topicURI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT DISTINCT ?uri FROM <{GRAPH}>"
				+ "{ ?annotationURI <{ANNOTATES_DOCUMENT_PROPERTY}> ?uri . "
				+ "  ?annotationURI <{HAS_TOPIC_PROPERTY}> <" + topicURI
				+ "> ." + "}";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY)
				.replace("{HAS_TOPIC_PROPERTY}",
						AnnotationOntologyRDFHelper.HAS_TOPIC_PROPERTY);

		System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------

	public List<String> getAnnotatedAs(String topicURI, String type) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT DISTINCT ?uri FROM <{GRAPH}>"
				+ "{ ?annotationURI <{ANNOTATES_DOCUMENT_PROPERTY}> ?uri . "
				+ "  ?annotationURI <{HAS_TOPIC_PROPERTY}> <" + topicURI
				+ "> ." + " ?uri a <" + type + "> . " + "}";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY)
				.replace("{HAS_TOPIC_PROPERTY}",
						AnnotationOntologyRDFHelper.HAS_TOPIC_PROPERTY);

		System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------

	public List<Resource> getAnnotatedResources(Annotation annotation) {
		List<Resource> resources = new ArrayList<Resource>();
		return resources;
	}

	// ------------------------------------------------------------------------------

	@Override
	public void annotate(String URI, String topicURI) {
		Annotation annotation = new Annotation();
		annotation.setAnnotatesResource(URI);
		annotation.setHasTopic(topicURI);
		annotation.setURI(URI + "annotation" + topicURI.hashCode());

		System.out.println(".............................................>>> ");
		core.getInformationAccess().put(annotation, new Context());

	}

	// ------------------------------------------------------------------------------

	@Override
	public void annotate(String URI, String predicate, String topicURI) {
		Annotation annotation = new Annotation();
		annotation.setAnnotatesResource(URI);
		annotation.setHasTopic(topicURI);
		annotation.setPredicate(predicate);
		annotation.setURI(URI + "annotation" + topicURI.hashCode());

		System.out.println(".............................................>>> ");
		core.getInformationAccess().put(annotation, new Context());

	}

	// ------------------------------------------------------------------------------

	@Override
	public void label(String URI, String label) {
		Annotation annotation = new Annotation();
		annotation.setAnnotatesResource(URI);
		annotation.setLabel(label);
		annotation.setURI(URI + "label" + label.hashCode());
		core.getInformationAccess().put(annotation, new Context());

	}

	// ------------------------------------------------------------------------------

	@Override
	public void removeAnnotation(String URI, String topicURI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>"
				+ "{ ?uri a <{ANNOTATION_CLASS}> ; "
				+ " <{ANNOTATES_DOCUMENT_PROPERTY}> <" + URI + "> ; "
				+ " <{HAS_TOPIC_PROPERTY}> <" + topicURI + "> ." + "}";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY)
				.replace("{HAS_TOPIC_PROPERTY}",
						AnnotationOntologyRDFHelper.HAS_TOPIC_PROPERTY)
				.replace("{ANNOTATION_CLASS}",
						AnnotationRDFHelper.ANNOTATION_CLASS);

		System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		System.out.println(" AHORA TENDRIAMOS QUE BORRAR > " + queryResults);
		for (String annotationURI : queryResults) {
			core.getInformationAccess().remove(annotationURI,
					AnnotationRDFHelper.ANNOTATION_CLASS);
		}

	}

	// ------------------------------------------------------------------------------

	@Override
	public void removeLabel(String URI, String label) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>"
				+ "{ ?uri a <{ANNOTATION_CLASS}> ; "
				+ " <{ANNOTATES_DOCUMENT_PROPERTY}> <" + URI + "> ; "
				+ " <{LABEL_PROPERTY}> \"" + label + "\" ." + "}";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY)
				.replace("{LABEL_PROPERTY}", RDFHelper.LABEL_PROPERTY)
				.replace("{ANNOTATION_CLASS}",
						AnnotationRDFHelper.ANNOTATION_CLASS);

		System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		System.out.println(" AHORA TENDRIAMOS QUE BORRAR > " + queryResults);
		for (String annotationURI : queryResults) {
			core.getInformationAccess().remove(annotationURI,
					AnnotationRDFHelper.ANNOTATION_CLASS);
		}


	}

	// ------------------------------------------------------------------------------

	@Override
	public List<String> getLabeledAs(String label) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT  DISTINCT ?uri FROM <{GRAPH}>"
				+ "{ ?annotationURI <{ANNOTATES_DOCUMENT_PROPERTY}> ?uri . "
				+ "  ?annotationURI <{LABEL_PROPERTY}> \"" + label + "\" ."
				+ "}";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{LABEL_PROPERTY}", RDFHelper.LABEL_PROPERTY)
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY);

		System.out.println("----> QUERY EXPRESSION TO COPY " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------

	@Override
	public List<String> getLabeledAs(String label, String type) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT  DISTINCT ?uri FROM <{GRAPH}>"
				+ "{ ?annotationURI <{ANNOTATES_DOCUMENT_PROPERTY}> ?uri . "
				+ " ?annotationURI <{LABEL_PROPERTY}> \"" + label + "\" . "
				+ " ?uri a <" + type + "> . " + "}";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{LABEL_PROPERTY}", RDFHelper.LABEL_PROPERTY)
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY);

		System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------

	@Override
	public List<String> getAnnotations(String URI) {

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>"
				+ "{ ?uri a <{ANNOTATION_CLASS}> ; "
				+ " <{ANNOTATES_DOCUMENT_PROPERTY}> <" + URI + "> ; "
				+ " <{HAS_TOPIC_PROPERTY}> ?topic . "
						+ "} ";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY)
				.replace("{HAS_TOPIC_PROPERTY}",
						AnnotationOntologyRDFHelper.HAS_TOPIC_PROPERTY)
				.replace("{ANNOTATION_CLASS}",
						AnnotationRDFHelper.ANNOTATION_CLASS);

		System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------

	@Override
	public List<String> getLabels(String URI) {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>"
				+ "{ "
				+ "?uri a <{ANNOTATION_CLASS}> . "
				+ "?uri <{ANNOTATES_DOCUMENT_PROPERTY}> <" + URI + "> ."
				+ "?uri <{LABEL_PROPERTY}> ?label"
				+ " } ";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY)
				.replace("{LABEL_PROPERTY}", RDFHelper.LABEL_PROPERTY)
				.replace("{ANNOTATION_CLASS}",
						AnnotationRDFHelper.ANNOTATION_CLASS);

		System.out.println("ESTA ES ----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}
	
	// ------------------------------------------------------------------------------

	@Override
	public List<String> getAnnotations() {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>"
				+ "{ "
				+ "?uri a <{ANNOTATION_CLASS}> . "
				+ "?uri <HAS_TOPIC_PROPERTY> ?topic"
				+ " } ";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY)
				.replace("{HAS_TOPIC_PROPERTY}",
						AnnotationOntologyRDFHelper.HAS_TOPIC_PROPERTY)
				.replace("{ANNOTATION_CLASS}",
						AnnotationRDFHelper.ANNOTATION_CLASS);

		System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------
}
