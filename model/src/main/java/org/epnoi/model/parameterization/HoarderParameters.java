package org.epnoi.model.parameterization;

public class HoarderParameters {
	private String path;
	private String URI;
	
	//-------------------------------------------------------------------------------------

	public String getPath() {
		return path;
	}
	
	//-------------------------------------------------------------------------------------

	public void setPath(String path) {
		this.path = path;
	}

	//-------------------------------------------------------------------------------------
	
	public String getURI() {
		return URI;
	}

	//-------------------------------------------------------------------------------------
	
	public void setURI(String uRI) {
		URI = uRI;
	}


	//-------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "HoarderParameters [path=" + path + ", URI=" + URI + "]";
	}

}
