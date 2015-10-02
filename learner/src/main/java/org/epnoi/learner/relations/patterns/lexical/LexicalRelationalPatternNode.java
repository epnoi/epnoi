package org.epnoi.learner.relations.patterns.lexical;

public class LexicalRelationalPatternNode {
	private String originialToken;
	private String generatedToken;

	// ---------------------------------------------------------------------------

	public String getOriginialToken() {
		return originialToken;
	}

	// ---------------------------------------------------------------------------

	public void setOriginialToken(String originialToken) {
		this.originialToken = originialToken;
	}

	// ---------------------------------------------------------------------------

	public String getGeneratedToken() {
		return generatedToken;
	}

	// ---------------------------------------------------------------------------

	public void setGeneratedToken(String generatedToken) {
		this.generatedToken = generatedToken;
	}
	
	// ---------------------------------------------------------------------------

	@Override
	public String toString() {
		return "LexicalRelationalPatternNode [originialToken=" + originialToken
				+ ", generatedToken=" + generatedToken + "]";
	}

	// ---------------------------------------------------------------------------

	
}
