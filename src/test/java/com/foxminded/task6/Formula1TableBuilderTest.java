package com.foxminded.task6;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class Formula1TableBuilderTest extends Formula1TableBuilder {

	@Test
	void extractAbbreviation_shouldReturnAbbreviationExtractedFromString() {
		assertEquals("PGS", extractAbbreviation("PGS_Pierre Gasly_SCUDERIA TORO ROSSO HONDA"));
		assertEquals("PGS", extractAbbreviation("PGS2018-05-24_12:08:36.586"));
	}

	@Test
	void convertTimeToMilliseconds_shouldReturnCorrectTimeInMilliseconds() {
		assertEquals(45765506, convertTimeToMilliseconds("12:42:45.506"));
		assertEquals(44843727, convertTimeToMilliseconds("12:27:23.727"));
		assertEquals(0, convertTimeToMilliseconds("00:00:00.000"));
	}

	@Test
	void calculateElapsedTime_shouldReturnCorrectTimeString() {
		assertEquals("15:21.779", convertTimeToString((long) 45765506 - 44843727));
		assertEquals("0:00.001", convertTimeToString((long) 1));
		assertEquals("0:00.000", convertTimeToString((long) 0));
	}

	@Test
	void parseFileLine_shouldReturnArrayWhichContainsStringsWhichWereSeparatedByUnderscores() {
		assertEquals(Arrays.asList("PGS2018-05-24", "12:08:36.586"), parseFileLine("PGS2018-05-24_12:08:36.586"));
		assertEquals(Arrays.asList("EOF", "Esteban Ocon", "FORCE INDIA MERCEDES"), parseFileLine("EOF_Esteban Ocon_FORCE INDIA MERCEDES"));
	}
	
	@Test
	void parseTimeLine_shouldReturnArrayOfStringsWhichWereSeparatedFromTimeStringByColon() {
		assertEquals(Arrays.asList("0", "00", "00.000"), parseTimeLine("0:00:00.000"));
		assertEquals(Arrays.asList("12", "04", "34.560"), parseTimeLine("12:04:34.560"));
	}
	
	@Test
	void buildTableOutOfMaps_ShouldReturnCorrectTable() {
		Map<String, ArrayList<String>> abbriviationsMap = new HashMap<String, ArrayList<String>>();
		abbriviationsMap.put("RGH", new ArrayList<>(Arrays.asList("Romain Grosjean", "HAAS FERRARI")));
		abbriviationsMap.put("FAM", new ArrayList<>(Arrays.asList("Fernando Alonso", "MCLAREN RENAULT")));
		abbriviationsMap.put("LSW", new ArrayList<>(Arrays.asList("Lance Stroll", "WILLIAMS MERCEDES")));

		Map<String, ArrayList<String>> startMap = new HashMap<String, ArrayList<String>>();
		startMap.put("RGH", new ArrayList<>(Arrays.asList("12:05:14.511")));
		startMap.put("FAM", new ArrayList<>(Arrays.asList("12:13:04.512")));
		startMap.put("LSW", new ArrayList<>(Arrays.asList("12:06:13.511")));

		Map<String, ArrayList<String>> endMap = new HashMap<String, ArrayList<String>>();
		endMap.put("RGH", new ArrayList<>(Arrays.asList("12:06:27.441")));
		endMap.put("FAM", new ArrayList<>(Arrays.asList("12:14:17.169")));
		endMap.put("LSW", new ArrayList<>(Arrays.asList("12:07:26.834")));

		List<TableLine> table = buildTableOutOfMaps(abbriviationsMap, startMap, endMap);

		assertEquals("Fernando Alonso", table.get(0).name);
		assertEquals("MCLAREN RENAULT", table.get(0).car);
		assertEquals(Long.valueOf(72657), table.get(0).timeMillis);

		assertEquals("Romain Grosjean", table.get(1).name);
		assertEquals("HAAS FERRARI", table.get(1).car);
		assertEquals(Long.valueOf(72930), table.get(1).timeMillis);

		assertEquals("Lance Stroll", table.get(2).name);
		assertEquals("WILLIAMS MERCEDES", table.get(2).car);
		assertEquals(Long.valueOf(73323), table.get(2).timeMillis);
	}
	
	@Test
	void parseFileAndFillMap_shouldReturnFilledWithCorrectDataMap() {
		Map<String, ArrayList<String>> abbreviationsTestMap = new HashMap<String, ArrayList<String>>();
		abbreviationsTestMap.put("DRR", new ArrayList<>(Arrays.asList("Daniel Ricciardo", "RED BULL RACING TAG HEUER")));
		abbreviationsTestMap.put("SVF", new ArrayList<>(Arrays.asList("Sebastian Vettel", "FERRARI")));
		abbreviationsTestMap.put("SPF", new ArrayList<>(Arrays.asList("Sergio Perez", "FORCE INDIA MERCEDES")));
		
		Map<String, ArrayList<String>> startTestMap = new HashMap<String, ArrayList<String>>();
		startTestMap.put("DRR", new ArrayList<>(Arrays.asList("12:14:12.054")));
		startTestMap.put("SVF", new ArrayList<>(Arrays.asList("12:02:58.917")));
		startTestMap.put("SPF", new ArrayList<>(Arrays.asList("12:12:01.035")));
		
		Map<String, ArrayList<String>> endTestMap = new HashMap<String, ArrayList<String>>();
		endTestMap.put("DRR", new ArrayList<>(Arrays.asList("12:15:24.067")));
		endTestMap.put("SVF", new ArrayList<>(Arrays.asList("12:04:03.332")));
		endTestMap.put("SPF", new ArrayList<>(Arrays.asList("12:13:13.883")));
		
		assertEquals(abbreviationsTestMap, parseFileAndFillMap("formula1_data/test_data/abbreviationsTest.txt"));
		assertEquals(startTestMap, parseFileAndFillMap("formula1_data/test_data/startTest.log"));
		assertEquals(endTestMap, parseFileAndFillMap("formula1_data/test_data/endTest.log"));
	}
}
