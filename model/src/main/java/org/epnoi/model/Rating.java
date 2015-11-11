package org.epnoi.model;

public class Rating implements Resource {
	public static final String WORKFLOW_RATING ="WORKFLOW";
	public static final String FILE_RATING ="FILE";
	
	String uri;
	Long id;
	String ownerResource;
	String ownerUri;
	String ratedElement;
	Long ratedElementID;
	Integer ratingValue;
	String resource;
	String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		id = id;
	}

	public String getOwnerResource() {
		return ownerResource;
	}

	public void setOwnerResource(String ownerResource) {
		this.ownerResource = ownerResource;
	}

	public String getOwnerURI() {
		return ownerUri;
	}

	public void setOwnerURI(String ownerURI) {
		this.ownerUri = ownerURI;
	}

	public String getRatedElement() {
		return ratedElement;
	}

	public void setRatedElement(String ratedElement) {
		this.ratedElement = ratedElement;
	}

	public Long getRatedElementID() {
		return ratedElementID;
	}

	public void setRatedElementID(Long ratedElementID) {
		this.ratedElementID = ratedElementID;
	}

	public Integer getRatingValue() {
		return ratingValue;
	}

	public void setRatingValue(Integer ratingValue) {
		this.ratingValue = ratingValue;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getOwnerUri() {
		return ownerUri;
	}

	public void setOwnerUri(String ownerUri) {
		this.ownerUri = ownerUri;
	}

	@Override
	public String toString() {
		return "Rating[ "+this.getUri()+" ]";
	}
	
}
