package org.epnoi.model;

public class Content<T> {
	private T content;
	private String type;

	// ----------------------------------------------------------------------------------------------

	public Content(T content, String format) {
		super();
		this.content = content;
		this.type = format;
	}

	public T getContent() {
		return content;
	}

	// ----------------------------------------------------------------------------------------------

	public void setContent(T content) {
		this.content = content;
	}

	// ----------------------------------------------------------------------------------------------

	public String getType() {
		return type;
	}

	// ----------------------------------------------------------------------------------------------

	public void setType(String format) {
		this.type = format;
	}

	// ----------------------------------------------------------------------------------------------

	public boolean isEmpty() {
		return ((this.content == null)||(this.type == null));
	}

	// ----------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Content [content=" + content + ", format=" + type + "]";
	}

}
