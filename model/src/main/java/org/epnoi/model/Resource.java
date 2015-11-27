package org.epnoi.model;

import org.codehaus.jackson.annotate.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public interface Resource extends Serializable {

	String getUri();
	void setUri(String uri);

	enum Type{
		SOURCE("source"),
		DOMAIN("domain"),
		DOCUMENT("document"),
		ITEM("item"),
		PART("part"),
		MODEL("model"),
		RELATION("relation"),
		WORD("word"),
		ANY("*");

		String keyValue;

		Type(String key){ keyValue = key;}

		public String key(){ return keyValue;}

	}

	enum State {
		NEW("new"),
		OPENED("opened"),
		CLOSED("closed"),
		MARKED("marked"),
		ANALYZED("analyzed"),
		DELETED("deleted"),
		ANY("*");

		String keyValue;

		State(String key){ keyValue = key;}

		public String key(){ return keyValue;}
	}

}
