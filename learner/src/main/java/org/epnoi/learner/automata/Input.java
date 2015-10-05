package org.epnoi.learner.automata;

public class Input<T> {
	T content;

	public Input(T content) {
		this.content = content;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Input [content=" + content + "]";
	}

}
