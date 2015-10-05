package org.epnoi.uia.core;

import java.util.logging.Logger;

import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;

public class CoreMain {
	private static final Logger logger = Logger.getLogger(CoreMain.class
			.getName());

	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();
		try {
			System.out.println("---->"
					+core.getNLPHandler().process("My taylor is rich"));
		} catch (EpnoiResourceAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
