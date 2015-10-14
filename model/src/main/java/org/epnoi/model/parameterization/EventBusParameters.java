package org.epnoi.model.parameterization;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "eventBus")
@Data
public class EventBusParameters  {
	private String uri;
}
