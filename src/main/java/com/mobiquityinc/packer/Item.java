package com.mobiquityinc.packer;

/**
 * Class to represent an Item to be added to the bag.
 * Contains item index, weight and cost.
 * Attributes have package visibility for better readability on Packer class.
 */
public class Item implements Comparable<Item> {
  int index;
  float weight;
  int cost;

  public Item (int index, float weight, int cost) {
    this.index = index;
    this.weight = weight;
    this.cost = cost;
  }

  @Override
  public int compareTo(Item o) {
    return Float.compare(this.weight, o.weight);
  }

  @Override
  public String toString() {
    return index + "";
  }
}