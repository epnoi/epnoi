package org.epnoi.model.commons;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandLineTool {
	protected static HashMap<String, String> getOptions(String[] args) {
		HashMap<String, String> options = new HashMap<String, String>();
		ArrayList<String> rootArgs = new ArrayList<String>();
		// options.put("rootArgs", rootArgs);

		for (int i = 0; i < args.length; ++i) {
			if (args[i].charAt(0) != '-') {
				rootArgs.add(args[i]);
			} else if (i + 1 < args.length) {
				options.put(args[i], args[++i]);
			} else {
				throw new IllegalArgumentException();
			}
		}
		return options;
	}
}
