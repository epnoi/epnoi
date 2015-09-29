package org.epnoi.model;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class ProofSerializer extends JsonSerializer<InformationSourceNotificationsSet> {
    
  

	public void serialize(InformationSourceNotificationsSet object, JsonGenerator jsonGenerator, 
            SerializerProvider serializerProvider) throws IOException {
InformationSourceNotificationsSet informationSourceNotificationSet = (InformationSourceNotificationsSet)object;
		System.out.println("Entrar entrra");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("timestamp", informationSourceNotificationSet.getTimestamp());
        jsonGenerator.writeEndObject();
    }
}