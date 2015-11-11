package org.epnoi.model.parameterization;

import javax.xml.bind.*;
import java.io.*;

public class ParametersModelReader {
	public static void write(ParametersModel model, String fileName) {

		try {
			JAXBContext context = JAXBContext.newInstance(ParametersModel.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// m.marshal(this.model, System.out);

			Writer writer = null;
			try {
				writer = new FileWriter(fileName);
				m.marshal(model, writer);
			} finally {
				try {
					writer.close();
				} catch (Exception exception) {
				}
			}
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ParametersModel read(String modelFilePath) {
		ParametersModel model = null;

		try {
			JAXBContext context = JAXBContext.newInstance(ParametersModel.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			Unmarshaller um = context.createUnmarshaller();
			System.out.println("----> "+modelFilePath);
			model = (ParametersModel) um
					.unmarshal(new FileReader(modelFilePath));
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// After unmarshalling the model, auxiliary data structures must be
		// initialized
	
		return model;
	}
}
