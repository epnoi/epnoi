package org.epnoi.model.parameterization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "virtuosoInformationStore")
public class VirtuosoInformationStoreParameters extends InformationStoreParameters {
	private String graph;
	private String user;
	private String password;

	//------------------------------------------------------------------------------
	
	public String getGraph() {
		return graph;
	}
	
	//------------------------------------------------------------------------------
	
	public void setGraph(String graph) {
		this.graph = graph;
	}
	
	//------------------------------------------------------------------------------

	public String getUser() {
		return user;
	}

	//------------------------------------------------------------------------------
	
	public void setUser(String user) {
		this.user = user;
	}
	
	//------------------------------------------------------------------------------

	public String getPassword() {
		return password;
	}

	//------------------------------------------------------------------------------
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	//------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "Virtuoso Information Store [host:" + super.getHost() + " path: "
				+ super.getPath() + "  port:" + super.getPort() + " graph: "
				+ this.graph + ", user: "+this.user+", password: "+this.password +"]";
	}
	
	//------------------------------------------------------------------------------
	


}
