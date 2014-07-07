package org.epnoi.uia.annotation;

import java.util.List;

import org.epnoi.model.Annotation;
import org.epnoi.model.Resource;

public interface AnnotationHandler {

	public void annotate(String URI, String topicURI);

	public void annotate(String URI, String predicateURI, String topicURI);

	public void label(String URI, String label);

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
