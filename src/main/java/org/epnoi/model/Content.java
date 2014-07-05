package org.epnoi.model;

public class Content<T> {
	private T content;
	private String format;

	// ----------------------------------------------------------------------------------------------

	public Content(T content, String format) {
		super();
		this.content = content;
		this.format = format;
	}

	public T getContent() {
		return content;
	}

	// ----------------------------------------------------------------------------------------------

	public void setContent(T content) {
		this.content = content;
	}

	// ----------------------------------------------------------------------------------------------

	public String getFormat() {
		return format;
	}

	// ----------------------------------------------------------------------------------------------

	public void setFormat(String format) {
		this.format = format;
	}
	
	// ----------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Content [content=" + content + ", format=" + format + "]";
	}

	

}
