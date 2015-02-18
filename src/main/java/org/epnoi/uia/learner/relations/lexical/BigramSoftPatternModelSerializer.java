package org.epnoi.uia.learner.relations.lexical;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BigramSoftPatternModelSerializer {
	// ------------------------------------------------------------------------------------
	public static void serialize(String filename, BigramSoftPatternModel model) {
		
		// save the object to file
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(model);

			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// read the object from file
		// save the object to file

	}

	// ------------------------------------------------------------------------------------

	public static BigramSoftPatternModel deserialize(String filename) {
		BigramSoftPatternModel model = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			model = (BigramSoftPatternModel) in.readObject();
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return model;
	}

	// ------------------------------------------------------------------------------------

	public static void main(String[] args) {

	}
}
