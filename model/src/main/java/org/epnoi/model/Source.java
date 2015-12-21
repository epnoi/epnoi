package org.epnoi.model;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class Source implements Resource {

	private String uri;

	private String url;

	public String protocol(){
		return StringUtils.substringBefore(url,":");
	}

	public String name(){
		return StringUtils.substringBetween(url,"//","/");
	}

	public String server(){
		return StringUtils.substringBefore(url, "?");
	}

}
