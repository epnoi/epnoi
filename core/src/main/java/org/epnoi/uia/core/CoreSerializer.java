package org.epnoi.uia.core;

import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CoreSerializer {
	// ------------------------------------------------------------------------------------
	public static void serialize(String filename, Core core) throws EpnoiResourceAccessException {

		// save the object to file

		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(core);

			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// read the object from file
		// save the object to file

	}

	// ------------------------------------------------------------------------------------

	public static Core deserialize(String filename) throws EpnoiResourceAccessException {
		Core model = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			model = (Core) in.readObject();
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EpnoiResourceAccessException(ex.getMessage());
		}
		return model;
	}

	// ------------------------------------------------------------------------------------

	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();

		try {
			CoreSerializer.serialize("/opt/data/core.bin", core);
		} catch (EpnoiResourceAccessException e) { // TODO Auto-generated catch
													// block
			e.printStackTrace();
		}

		Core readedCore = null;
		try {
			readedCore = (Core) CoreSerializer.deserialize("/opt/data/core.bin");
		} catch (EpnoiResourceAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Readed model " + core.toString());
	}
}
