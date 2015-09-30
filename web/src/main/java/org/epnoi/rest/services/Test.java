package org.epnoi.rest.services;

import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;

public class Test {
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
