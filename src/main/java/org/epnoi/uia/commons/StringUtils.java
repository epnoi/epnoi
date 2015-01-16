package org.epnoi.uia.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	// ----------------------------------------------------------------------------------------------------------------------------------------

	public static String clean(String line, String regexp, String replacement) {

		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(line);

		while (matcher.find()) {
		//	System.out.println("------> " + matcher.group());
			line = line.replace(matcher.group(), replacement);
		}
		return line;
	}

	// ----------------------------------------------------------------------------------------------------------------------------------------

	public static String outerMatching(String line, String openingExp,
			char openingSymbol, char closingSymbol) {
		String matching = line;

		Pattern pattern = Pattern.compile(openingExp);
		Matcher matcher = pattern.matcher(line);

		while (matcher.find()) {
			//System.out.println("------> " + matcher.group());
			int initPosition = matcher.start();
			int endPosition = matcher.end() + 1;
			int numberOfStillUnclosedOpeningSymbols = 1;

			while (numberOfStillUnclosedOpeningSymbols > 0
					&& endPosition < line.length()) {
				if (line.charAt(endPosition) == closingSymbol)
					numberOfStillUnclosedOpeningSymbols--;
				if (line.charAt(endPosition) == openingSymbol)
					numberOfStillUnclosedOpeningSymbols++;
				endPosition++;
			}

			String detectedMatch = line.substring(initPosition, endPosition);
			//System.out.println("detected match! ---> " + detectedMatch);

			matching = matching.replace(detectedMatch, "");

		}

		return matching;
	}

	
	
	
	
	// ----------------------------------------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {

		// String regexp = "TEMPLATE\\[[^\\]]*]";
		/*
		 * String regexp2 = "\\([^\\)]*\\)"; //
		 * [[a-zA-Z0-9~@#\\^\\$&\\*\\(\\)-_\\+=\\[\\\{\\}\\|\\\,\\.\\?\\s]*\]";
		 * String line =
		 * "TEMPLATE[Infobox_Disease, Name = Autism, Image = Autism-stacking-cans 2nd edit.jpg, Caption = Repetitively stacking or lining up objects may indicate autism.<ref name=Johnson/>, DiseasesDB = 1142, ICD10 = (TEMPLATE), ICD9 = (TEMPLATE), ICDO =, OMIM = 209850, MedlinePlus = 001526, eMedicineSubj = med, eMedicineTopic = 3202, eMedicine_mult = (TEMPLATE), MeshID = D001321] Autism is a brain development (whatever I don't care) disorder that impairs social interaction and communication, TEMPLATE[Ioprwprowk] and causes restricted and repetitive behavior, all starting before a child is three years old. "
		 * ;
		 * 
		 * String lineWithoutTemplate = StringUtils.clean(line, regexp);
		 * 
		 * System.out.println("The result line without templates is " +
		 * lineWithoutTemplate);
		 */
		String line2 = "TEMPLATE[Infobox_Disease, Name = Autism, Image = [Autism-stacking-cans 2nd edit.jpg], Caption = Repetitively stacking or lining up objects may indicate autism.<ref name=Johnson/>, DiseasesDB = 1142, ICD10 = (TEMPLATE), ICD9 = (TEMPLATE), ICDO =, OMIM = 209850, MedlinePlus = 001526, eMedicineSubj = med, eMedicineTopic = [3202], eMedicine_mult = (TEMPLATE), MeshID = D001321] Autism is a brain development (whatever I don't care) disorder that impairs social interaction and communication, TEMPLATE[Ioprwprowk] and causes restricted and repetitive behavior, all starting before a child is three years old. ";
		String regexp = "TEMPLATE\\[";
		String lineWithoutTemplates = StringUtils.outerMatching(line2, regexp,
				'[', ']');

		System.out.println("The result line without templates is: "
				+ lineWithoutTemplates);
		String regexp2 = "\\(";
		String cleanLine = StringUtils.outerMatching(lineWithoutTemplates,
				regexp2, '(', ')');
		System.out.println("The cleaned result is: " + cleanLine);

	}

}
