package com.brimstone.formula1_table_builder;

import java.util.ArrayList;
import java.util.Map;

public class Main {

  private static final String abbreviationsPath = "f1_data/task_data/abbreviations.txt";
  private static final String startPath = "f1_data/task_data/start.log";
  private static final String endPath = "f1_data/task_data/end.log";

  public static void main(String[] args) {
    Map<String, ArrayList<String>> abbriviationsMap = TableBuilder.parseFileAndFillMap(
        abbreviationsPath);
    Map<String, ArrayList<String>> startMap = TableBuilder.parseFileAndFillMap(startPath);
    Map<String, ArrayList<String>> endMap = TableBuilder.parseFileAndFillMap(endPath);

    TableBuilder.printTable(
        TableBuilder.buildTableOutOfMaps(abbriviationsMap, startMap, endMap));
  }
}
