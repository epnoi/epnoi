package org.epnoi.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Login {

	private String user;
	private String password;

	@Override
	public String toString() {
		return "Login[ user:" + this.user + ", password: " + this.password
				+ "]";
	}

	public String getUser() {
		return user;
	}

	// -----------------------------------------------------------------------

	public void setUser(String user) {
		this.user = user;
	}

	// -----------------------------------------------------------------------

	public String getPassword() {
		return password;
	}

	// -----------------------------------------------------------------------

	public void setPassword(String password) {
		this.password = password;
	}

	// -----------------------------------------------------------------------

	@Override
	public boolean equals(Object other) {
		if (other instanceof Login) {
			return (this.user.equals(((Login) other).getUser()) && this.password
					.equals(((Login) other).getPassword()));

		}
		return false;
	}

}
