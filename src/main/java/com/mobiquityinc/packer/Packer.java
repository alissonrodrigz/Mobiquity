package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Packer {

  private static final int PACKAGE_MAX_WEIGHT = 100;
  private static final int MAX_ITEMS = 15;
  private static final int ITEM_MAX_WEIGHT = 100;
  private static final int ITEM_MAX_COST = 100;

  public static String pack(String filePath) throws APIException {
    File file = new File(filePath);

    if (!file.exists()) {
      throw new APIException("File " + filePath + " does not exist!");
    }

    StringBuilder packResult = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(file))){
      String line;
      while ((line = br.readLine()) != null) {
        int indexOfColon = line.indexOf(":");
        int packageWeight = Integer.valueOf(line.substring(0, indexOfColon).trim());
        if (packageWeight > PACKAGE_MAX_WEIGHT) {
          throw new APIException(
              "Package max weight exceeds the maximum weight allowed: " + "\n" + line);
        }

        List<Item> items =
            extractItemsFromFileLine(line.substring(indexOfColon + 1, line.length() - 1).trim());

        packResult.append(fillBagWithItems(packageWeight, items)).append("\n");
      }
    } catch (APIException e) {
      throw e;
    } catch (Exception e) {
      throw new APIException("Unexpected exception: " + filePath, e);
    }

    return packResult.toString();
  }

  private static List<Item> extractItemsFromFileLine(String itemsStr) throws APIException {
    String[] itemsArray = itemsStr.split(" ");
    if (itemsArray.length > MAX_ITEMS) {
      throw new APIException("Line items exceeds maximum allowed: " + "\n" + itemsStr);
    }
    List<Item> items = new ArrayList<>();
    for (String itemStr : itemsArray) {
      String[] splittedItemStr = itemStr.replaceAll("[()€]", "").split(",");
      int index = Integer.valueOf(splittedItemStr[0]);
      float weight = Float.valueOf(splittedItemStr[1]);
      int cost = Integer.valueOf(splittedItemStr[2]);
      if (weight > ITEM_MAX_WEIGHT || cost > ITEM_MAX_COST) {
        throw new APIException("Item weight or cost exceeds maximum allowed: " + "\n" + itemStr);
      }
      items.add(new Item(index, weight, cost));
    }
    return items;
  }

  private static String fillBagWithItems(int packageWeight, List<Item> items) {
    int itemsCount = items.size();
    int previousValue;
    int currentValue;
    int[][] costsTable = new int[itemsCount + 1][packageWeight + 1];

    // As defined in Item.compareTo(), the sort will be made from lowest to highest weight
    Collections.sort(items);

    // starts on the position of the item with lowest weight
    int init = new Float(items.get(0).weight).intValue() + 1;

    // populate costs table.
    for (int i = 1; i <= itemsCount; i++) {
      for (int capacity = init; capacity <= packageWeight; capacity++) {
        previousValue = costsTable[i - 1][capacity];
        if (items.get(i - 1).weight > capacity) {
          currentValue = 0;
        } else {
          int remainingCapacity = capacity - new Float(items.get(i - 1).weight).intValue();
          currentValue = items.get(i - 1).cost + costsTable[i - 1][remainingCapacity];
        }
        if (previousValue > currentValue) {
          costsTable[i][capacity] = previousValue;
        } else {
          costsTable[i][capacity] = currentValue;
        }
      }
    }

    // check which items were "added" to the bag, sort by index and return result
    int currentWeight = packageWeight;
    List<Integer> itensAddedIndexes = new ArrayList<>();
    for (int k = itemsCount; k > 0; k--) {
      if (costsTable[k][currentWeight] > costsTable[k - 1][currentWeight]) {
        itensAddedIndexes.add(items.get(k - 1).index);
        currentWeight = currentWeight - new Float(items.get(k - 1).weight).intValue();
      }
    }

    Collections.sort(itensAddedIndexes);

    String commaSeparatedNumbers = itensAddedIndexes.stream()
        .map(item -> item.toString())
        .collect(Collectors.joining(","));

    return commaSeparatedNumbers.isEmpty() ? "-" : commaSeparatedNumbers;
  }
}
