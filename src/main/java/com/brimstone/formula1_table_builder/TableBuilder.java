package com.brimstone.formula1_table_builder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TableBuilder {

  private static final int TABLE_BOARDS_LENGTH = " _ ".length();
  private static final int ITERATOR_AND_DOT_LENGTH = "00.".length();
  private static final int ELAPSED_TIME_STRING_LENGTH = "0:00.000".length();
  private static final int LAST_TOP_DRIVER_NUMBER = 15;
  private static final int TIME_INDEX_IN_STARTMAP_AND_ENDMAP = 0;

  protected static ArrayList<String> parseFileLine(String line) {
    return new ArrayList<>(Arrays.asList(line.split("_")));
  }

  protected static String extractAbbreviation(String key) {
    return key.substring(0, 3);
  }

  protected static Map<String, ArrayList<String>> parseFileAndFillMap(String path) {
    Map<String, ArrayList<String>> abbreviationPerDataMap = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
      String line = reader.readLine();

      while (line != null) {
        ArrayList<String> parsedLine = parseFileLine(line);

        abbreviationPerDataMap.put(extractAbbreviation(parsedLine.remove(0)), parsedLine);

        line = reader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return abbreviationPerDataMap;
  }

  protected static ArrayList<String> parseTimeLine(String time) {
    return new ArrayList<>(Arrays.asList(time.split(":")));
  }

  protected static long convertTimeToMilliseconds(String time) {
    ArrayList<String> parsedTime = parseTimeLine(time);
    return TimeUnit.HOURS.toMillis(Long.parseLong(parsedTime.get(0))) +
        TimeUnit.MINUTES.toMillis(Long.parseLong(parsedTime.get(1))) +
        Math.round(Float.parseFloat(parsedTime.get(2)) * 1000);
  }

  protected static String convertTimeToString(Long timeInMilliseconds) {
    Date date = new Date(timeInMilliseconds);
    DateFormat formatter = new SimpleDateFormat("m:ss.SSS");
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    return formatter.format(date);
  }

  protected static ArrayList<TableLine> sortTableCollection(ArrayList<TableLine> table) {
    table.sort(Comparator.comparing(o -> o.timeMillis));
    return table;
  }

  protected static TableLine buildTableLine(ArrayList<String> line, String startTime,
      String endTime) {
    TableLine tableLine = new TableLine();
    tableLine.name = line.get(0);
    tableLine.car = line.get(1);
    tableLine.timeMillis =
        convertTimeToMilliseconds(endTime) - convertTimeToMilliseconds(startTime);
    return tableLine;
  }

  protected static List<TableLine> buildTableOutOfMaps(
      Map<String, ArrayList<String>> abbriviationsMap, Map<String, ArrayList<String>> startMap,
      Map<String, ArrayList<String>> endMap) {
    ArrayList<TableLine> table = new ArrayList<>();
    abbriviationsMap.forEach((abbreviation, fileData) -> table.add(
        buildTableLine(fileData, startMap.get(abbreviation).get(TIME_INDEX_IN_STARTMAP_AND_ENDMAP),
            endMap.get(abbreviation).get(TIME_INDEX_IN_STARTMAP_AND_ENDMAP))));
    return sortTableCollection(table);
  }

  protected static void printTable(List<TableLine> table) {
    int maxNameLength = table.stream().max(Comparator.comparingInt(o -> o.name.length()))
        .get().name.length();
    int maxCarLength = table.stream().max(Comparator.comparingInt(o -> o.car.length()))
        .get().car.length();

    int rowNumber = 1;
    for (var line : table) {
      System.out.print(rowNumber + ". " + line.name + " ".repeat(
          maxNameLength - line.name.length() - Integer.toString(
              rowNumber).length() + 1) + " | ");
      System.out.print(line.car + " ".repeat(maxCarLength - line.car.length()) + " | ");
      System.out.println(convertTimeToString(line.timeMillis));

      if (rowNumber == LAST_TOP_DRIVER_NUMBER) {
        System.out.println("-".repeat(
            maxNameLength + maxCarLength + ITERATOR_AND_DOT_LENGTH + TABLE_BOARDS_LENGTH * 2
                + ELAPSED_TIME_STRING_LENGTH));
      }
      rowNumber++;
    }
  }
}
