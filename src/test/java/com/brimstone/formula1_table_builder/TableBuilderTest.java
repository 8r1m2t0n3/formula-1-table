package com.brimstone.formula1_table_builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TableBuilderTest extends TableBuilder {

  @Test
  void extractAbbreviation_shouldReturnAbbreviationExtractedFromString() {
    Assertions.assertEquals("PGS",
        extractAbbreviation("PGS_Pierre Gasly_SCUDERIA TORO ROSSO HONDA"));
    Assertions.assertEquals("PGS", extractAbbreviation("PGS2018-05-24_12:08:36.586"));
  }

  @Test
  void convertTimeToMilliseconds_shouldReturnCorrectTimeInMilliseconds() {
    Assertions.assertEquals(45765506, convertTimeToMilliseconds("12:42:45.506"));
    Assertions.assertEquals(44843727, convertTimeToMilliseconds("12:27:23.727"));
    Assertions.assertEquals(0, convertTimeToMilliseconds("00:00:00.000"));
  }

  @Test
  void calculateElapsedTime_shouldReturnCorrectTimeString() {
    Assertions.assertEquals("15:21.779", convertTimeToString((long) 45765506 - 44843727));
    Assertions.assertEquals("0:00.001", convertTimeToString((long) 1));
    Assertions.assertEquals("0:00.000", convertTimeToString((long) 0));
  }

  @Test
  void parseFileLine_shouldReturnArrayWhichContainsStringsWhichWereSeparatedByUnderscores() {
    Assertions.assertEquals(List.of("PGS2018-05-24", "12:08:36.586"),
        parseFileLine("PGS2018-05-24_12:08:36.586"));
    Assertions.assertEquals(List.of("EOF", "Esteban Ocon", "FORCE INDIA MERCEDES"),
        parseFileLine("EOF_Esteban Ocon_FORCE INDIA MERCEDES"));
  }

  @Test
  void parseTimeLine_shouldReturnArrayOfStringsWhichWereSeparatedFromTimeStringByColon() {
    Assertions.assertEquals(List.of("0", "00", "00.000"), parseTimeLine("0:00:00.000"));
    Assertions.assertEquals(List.of("12", "04", "34.560"), parseTimeLine("12:04:34.560"));
  }

  @Test
  void buildTableOutOfMaps_ShouldReturnCorrectTable() {
    Map<String, ArrayList<String>> abbriviationsMap = new HashMap<>();
    abbriviationsMap.put("RGH", new ArrayList<>(List.of("Romain Grosjean", "HAAS FERRARI")));
    abbriviationsMap.put("FAM",
        new ArrayList<>(List.of("Fernando Alonso", "MCLAREN RENAULT")));
    abbriviationsMap.put("LSW",
        new ArrayList<>(List.of("Lance Stroll", "WILLIAMS MERCEDES")));

    Map<String, ArrayList<String>> startMap = new HashMap<>();
    startMap.put("RGH", new ArrayList<>(List.of("12:05:14.511")));
    startMap.put("FAM", new ArrayList<>(List.of("12:13:04.512")));
    startMap.put("LSW", new ArrayList<>(List.of("12:06:13.511")));

    Map<String, ArrayList<String>> endMap = new HashMap<>();
    endMap.put("RGH", new ArrayList<>(List.of("12:06:27.441")));
    endMap.put("FAM", new ArrayList<>(List.of("12:14:17.169")));
    endMap.put("LSW", new ArrayList<>(List.of("12:07:26.834")));

    List<TableLine> table = buildTableOutOfMaps(abbriviationsMap, startMap, endMap);

    Assertions.assertEquals("Fernando Alonso", table.get(0).name);
    Assertions.assertEquals("MCLAREN RENAULT", table.get(0).car);
    Assertions.assertEquals(Long.valueOf(72657), table.get(0).timeMillis);

    Assertions.assertEquals("Romain Grosjean", table.get(1).name);
    Assertions.assertEquals("HAAS FERRARI", table.get(1).car);
    Assertions.assertEquals(Long.valueOf(72930), table.get(1).timeMillis);

    Assertions.assertEquals("Lance Stroll", table.get(2).name);
    Assertions.assertEquals("WILLIAMS MERCEDES", table.get(2).car);
    Assertions.assertEquals(Long.valueOf(73323), table.get(2).timeMillis);
  }

  @Test
  void parseFileAndFillMap_shouldReturnFilledWithCorrectDataMap() {
    Map<String, ArrayList<String>> abbreviationsTestMap = new HashMap<>();
    abbreviationsTestMap.put("DRR",
        new ArrayList<>(List.of("Daniel Ricciardo", "RED BULL RACING TAG HEUER")));
    abbreviationsTestMap.put("SVF", new ArrayList<>(List.of("Sebastian Vettel", "FERRARI")));
    abbreviationsTestMap.put("SPF",
        new ArrayList<>(List.of("Sergio Perez", "FORCE INDIA MERCEDES")));

    Map<String, ArrayList<String>> startTestMap = new HashMap<>();
    startTestMap.put("DRR", new ArrayList<>(List.of("12:14:12.054")));
    startTestMap.put("SVF", new ArrayList<>(List.of("12:02:58.917")));
    startTestMap.put("SPF", new ArrayList<>(List.of("12:12:01.035")));

    Map<String, ArrayList<String>> endTestMap = new HashMap<>();
    endTestMap.put("DRR", new ArrayList<>(List.of("12:15:24.067")));
    endTestMap.put("SVF", new ArrayList<>(List.of("12:04:03.332")));
    endTestMap.put("SPF", new ArrayList<>(List.of("12:13:13.883")));

    Assertions.assertEquals(abbreviationsTestMap,
        parseFileAndFillMap("formula1_data/test_data/abbreviationsTest.txt"));
    Assertions.assertEquals(startTestMap,
        parseFileAndFillMap("formula1_data/test_data/startTest.log"));
    Assertions.assertEquals(endTestMap, parseFileAndFillMap("formula1_data/test_data/endTest.log"));
  }
}
