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

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>"
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
	public void label(String URI, String label) {
		// TODO Auto-generated method stub

	}

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
		for(String annotationURI: queryResults){
			core.getInformationAccess().remove(annotationURI, AnnotationRDFHelper.ANNOTATION_CLASS);
		}
		
		
	}

	@Override
	public void removeLabel(String URI, String label) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getLabeledAs(String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAnnotations(String URI) {

		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>"
				+ "{ ?uri a <{ANNOTATION_CLASS}> ; "
				+ " <{ANNOTATES_DOCUMENT_PROPERTY}> <" + URI + "> . } ";

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

	@Override
	public List<String> getLabels(String URI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAnnotations() {
		InformationStore informationStore = this.core
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>"
				+ "{ ?uri a <{ANNOTATION_CLASS}> . } ";

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
	
	

}
