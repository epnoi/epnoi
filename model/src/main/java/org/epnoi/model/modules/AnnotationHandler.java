package org.epnoi.model.modules;

import org.epnoi.model.Annotation;

import java.util.List;

public interface AnnotationHandler {

	public Annotation annotate(String URI, String topicURI);

	public Annotation annotate(String URI, String predicateURI, String topicURI);

	public Annotation label(String URI, String label);

	public void removeAnnotation(String URI, String topicURI);

	public void removeLabel(String URI, String label);

	public List<String> getAnnotatedAs(String topicURI);

	public List<String> getLabeledAs(String label);

	public List<String> getAnnotatedAs(String topicURI, String type);

	public List<String> getLabeledAs(String label, String type);

	public List<String> getAnnotations(String topicURI);

	public List<String> getLabels(String URI);

	public List<String> getAnnotations();

}
