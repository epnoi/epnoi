package org.epnoi.uia.core;

import org.epnoi.model.modules.Core;

public class CoreMainUtility {

	public static void main(String[] args) {
		System.out.println("Starting the test of the CoreUtility helper class");

		Core core = CoreUtility.getUIACore();
		System.out.println("Ending the test of the CoreUtility helper class");
	}

}
