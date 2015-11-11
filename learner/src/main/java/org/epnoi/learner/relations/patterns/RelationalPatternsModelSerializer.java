package org.epnoi.learner.relations.patterns;

import org.epnoi.learner.relations.patterns.lexical.RelaxedBigramSoftPatternModel;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RelationalPatternsModelSerializer {
	// ------------------------------------------------------------------------------------
	public static void serialize(String filename, RelationalPatternsModel model)
			throws EpnoiResourceAccessException {

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

	public static RelationalPatternsModel deserialize(String filename)
			throws EpnoiResourceAccessException {
		RelaxedBigramSoftPatternModel model = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			model = (RelaxedBigramSoftPatternModel) in.readObject();
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EpnoiResourceAccessException(ex.getMessage());
		}
		return model;
	}

	// ------------------------------------------------------------------------------------

	public static void main(String[] args) {
		RelaxedBigramSoftPatternModel newModel = new RelaxedBigramSoftPatternModel();
	/*
		try {
			RelationalPatternsModelSerializer.serialize(
					"/home/rgonza/Desktop/model.bin", newModel);
		} catch (EpnoiResourceAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		RelaxedBigramSoftPatternModel readedModel=null;
		try {
			readedModel = (RelaxedBigramSoftPatternModel) RelationalPatternsModelSerializer
					.deserialize("/opt/epnoi/epnoideployment/firstReviewResources/lexicalModel/model.bin");
		} catch (EpnoiResourceAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Readed model " + readedModel.toString());
	}
}
