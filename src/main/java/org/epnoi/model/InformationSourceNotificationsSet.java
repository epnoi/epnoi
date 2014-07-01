package org.epnoi.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;



public class InformationSourceNotificationsSet implements Resource {

	private String URI;
	private String timestamp;
	private List<InformationSourceNotification> notifications = new ArrayList<InformationSourceNotification>();

	// ----------------------------------------------------------------------
	@XmlElement(name="URI")
	public String getURI() {
		return URI;
	}

	// ----------------------------------------------------------------------

	public void setURI(String uRI) {
		URI = uRI;
	}

	// ----------------------------------------------------------------------

	public String getTimestamp() {
		return timestamp;
	}

	// ----------------------------------------------------------------------

	public List<InformationSourceNotification> getNotifications() {
		return notifications;
	}

	// ----------------------------------------------------------------------

	public void setNotifications(
			List<InformationSourceNotification> notifications) {
		this.notifications = notifications;
	}

	// ----------------------------------------------------------------------

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "InformationSourceNotificationsSet [URI=" + URI + ", timestamp="
				+ timestamp + ", notifications=" + notifications + "]";
	}

}
