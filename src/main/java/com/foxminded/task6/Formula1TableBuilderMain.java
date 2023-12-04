package com.foxminded.task6;

import java.util.*;

public class Formula1TableBuilderMain {
	
	private static final String abbriviationsPath = "formula1_data/task_data/abbreviations.txt";
	private static final String startPath = "formula1_data/task_data/start.log";
	private static final String endPath = "formula1_data/task_data/end.log";
	
	public static void main(String[] args) {
		Map<String, ArrayList<String>> abbriviationsMap = Formula1TableBuilder.parseFileAndFillMap(abbriviationsPath);
		Map<String, ArrayList<String>> startMap = Formula1TableBuilder.parseFileAndFillMap(startPath);
		Map<String, ArrayList<String>> endMap = Formula1TableBuilder.parseFileAndFillMap(endPath);

		Formula1TableBuilder.printTable(Formula1TableBuilder.buildTableOutOfMaps(abbriviationsMap, startMap, endMap));
	}
}
