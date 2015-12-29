package org.epnoi.uia.annotation;

import org.epnoi.model.Annotation;
import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.commons.StringUtils;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.AnnotationHandler;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.modules.InformationStoreHelper;
import org.epnoi.model.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.model.rdf.AnnotationOntologyRDFHelper;
import org.epnoi.model.rdf.AnnotationRDFHelper;
import org.epnoi.model.rdf.RDFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class AnnotationHandlerImpl implements AnnotationHandler {
	private static final Logger logger = Logger.getLogger(AnnotationHandlerImpl.class.getName());
	@Autowired
	private Core core;

	// ------------------------------------------------------------------------------

	public AnnotationHandlerImpl(){
	}

	// ------------------------------------------------------------------------------
	@PostConstruct
	public void init() throws EpnoiInitializationException {
		logger.info("Initializing the annotations handler");
	}

	public void annotate(Annotation annotation, String URI) {
		annotation.setAnnotatesResource(URI);
		this.core.getInformationHandler().put(annotation, new Context());
	}

	// ------------------------------------------------------------------------------

	public List<String> getAnnotatedAs(String topicURI) {
		InformationStore informationStore = this.core.getInformationHandler()
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

		//System.out.println("QUERY EXPRESSION ----------> " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------

	public List<String> getAnnotatedAs(String topicURI, String type) {
		InformationStore informationStore = this.core.getInformationHandler()
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

	//	System.out.println("QUERY EXPRESSION ----------> " + queryExpression);
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
	public Annotation annotate(String URI, String topicURI) {
		Annotation annotation = new Annotation();
		annotation.setAnnotatesResource(URI);
		annotation.setHasTopic(topicURI);
		annotation.setUri(URI + "annotation" + topicURI.hashCode());

		System.out.println(".............................................>>> ");
		core.getInformationHandler().put(annotation, new Context());
		return annotation;
	}

	// ------------------------------------------------------------------------------

	@Override
	public Annotation annotate(String URI, String predicate, String topicURI) {
		Annotation annotation = new Annotation();
		annotation.setAnnotatesResource(URI);
		annotation.setHasTopic(topicURI);
		annotation.setPredicate(predicate);
		annotation.setUri(URI + "annotation" + topicURI.hashCode());

		core.getInformationHandler().put(annotation, new Context());
		return annotation;
	}

	// ------------------------------------------------------------------------------

	@Override
	public Annotation label(String URI, String label) {
		Annotation annotation = new Annotation();
		annotation.setAnnotatesResource(URI);
		annotation.setLabel(label);
		annotation.setUri(URI + "label" + label.hashCode());
		core.getInformationHandler().put(annotation, new Context());
		//System.out.println("annotation " + annotation);
		return annotation;
	}

	// ------------------------------------------------------------------------------

	@Override
	public void removeAnnotation(String URI, String topicURI) {
		InformationStore informationStore = this.core.getInformationHandler()
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

		// System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		System.out.println(" AHORA TENDRIAMOS QUE BORRAR > " + queryResults);
		for (String annotationURI : queryResults) {
			core.getInformationHandler().remove(annotationURI,
					AnnotationRDFHelper.ANNOTATION_CLASS);
		}

	}

	// ------------------------------------------------------------------------------

	@Override
	public void removeLabel(String URI, String label) {
		InformationStore informationStore = this.core.getInformationHandler()
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
		System.out.println("======> "+queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		for (String annotationURI : queryResults) {
			core.getInformationHandler().remove(annotationURI,
					AnnotationRDFHelper.ANNOTATION_CLASS);
		}

	}

	// ------------------------------------------------------------------------------

	@Override
	public List<String> getLabeledAs(String label) {
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);
		String cleanLabel = StringUtils.cleanOddCharacters(label);
		String queryExpression = "SELECT  DISTINCT ?uri FROM <{GRAPH}>"
				+ "{ ?annotationURI <{ANNOTATES_DOCUMENT_PROPERTY}> ?uri . "
				+ "  ?annotationURI <{LABEL_PROPERTY}> \"" + cleanLabel
				+ "\" ." + "}";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{LABEL_PROPERTY}", RDFHelper.LABEL_PROPERTY)
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY);

		//System.out.println("----> QUERY EXPRESSION TO COPY " +
		// queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------

	@Override
	public List<String> getLabeledAs(String label, String type) {
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String cleanLabel = StringUtils.cleanOddCharacters(label);

		String queryExpression = "SELECT  DISTINCT ?uri FROM <{GRAPH}>"
				+ "{ ?annotationURI <{ANNOTATES_DOCUMENT_PROPERTY}> ?uri . "
				+ " ?annotationURI <{LABEL_PROPERTY}> \"" + cleanLabel
				+ "\" . " + " ?uri a <" + type + "> . " + "}";

		queryExpression = queryExpression
				.replace(
						"{GRAPH}",
						((VirtuosoInformationStoreParameters) informationStore
								.getParameters()).getGraph())
				.replace("{LABEL_PROPERTY}", RDFHelper.LABEL_PROPERTY)
				.replace("{ANNOTATES_DOCUMENT_PROPERTY}",
						AnnotationOntologyRDFHelper.ANNOTATES_DOCUMENT_PROPERTY);

//		System.out.println("QUERY EXPRESSION ----------> " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------

	@Override
	public List<String> getAnnotations(String URI) {

		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>"
				+ "{ ?uri a <{ANNOTATION_CLASS}> ; "
				+ " <{ANNOTATES_DOCUMENT_PROPERTY}> <" + URI + "> ; "
				+ " <{HAS_TOPIC_PROPERTY}> ?topic . " + "} ";

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

		// System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------

	@Override
	public List<String> getLabels(String URI) {
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>" + "{ "
				+ "?uri a <{ANNOTATION_CLASS}> . "
				+ "?uri <{ANNOTATES_DOCUMENT_PROPERTY}> <" + URI + "> ."
				+ "?uri <{LABEL_PROPERTY}> ?label" + " } ";

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

		// System.out.println("ESTA ES ----> QUERY EXPRESSION " +
		// queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------

	@Override
	public List<String> getAnnotations() {
		InformationStore informationStore = this.core.getInformationHandler()
				.getInformationStoresByType(
						InformationStoreHelper.RDF_INFORMATION_STORE).get(0);

		String queryExpression = "SELECT ?uri FROM <{GRAPH}>" + "{ "
				+ "?uri a <{ANNOTATION_CLASS}> . "
				+ "?uri <HAS_TOPIC_PROPERTY> ?topic" + " } ";

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

		// System.out.println("----> QUERY EXPRESSION " + queryExpression);
		List<String> queryResults = informationStore.query(queryExpression);

		return queryResults;
	}

	// ------------------------------------------------------------------------------
}
