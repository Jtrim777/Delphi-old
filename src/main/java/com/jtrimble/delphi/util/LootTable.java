package com.jtrimble.delphi.util;

import java.util.ArrayList;
import java.util.Random;

public class LootTable<T> {
  ArrayList<WeightPair> members;
  int baseWeight;

  public LootTable() {
    members = new ArrayList<>();
    baseWeight = -1;
  }

  public LootTable(int baseWeight) {
    this.members = new ArrayList<>();
    this.baseWeight = baseWeight;
  }

  public LootTable<T> addMember(T item, int weight) {
    members.add(new WeightPair(item, weight));
    return this;
  }

  public LootTable<T> addMember(T item, float rarity) {
    members.add(new WeightPair(item, (int)((1-rarity)*baseWeight)));
    return this;
  }

  public LootTable<T> adjustQuality(float factor) {
    int adjFactor = (baseWeight/2)-(int)(factor*baseWeight);

    for (WeightPair member : members) {
      if (member.weight >= (baseWeight/2)) {
        member.weight += adjFactor;
      } else {
        member.weight -= adjFactor;
      }
    }

    return this;
  }

  public void voidMember(T item) {
    members.removeIf((wp) -> wp.item == item);
  }

  public T selectOne() {
    ArrayList<T> die = new ArrayList<>();

    for (WeightPair wp : members) {
      for (int i=0; i<wp.weight; i++) {
        die.add(wp.item);
      }
    }

    return die.get(new Random().nextInt(die.size()));
  }

  public LootTable<T> copy() {
    LootTable<T> copy = new LootTable<T>();
    copy.baseWeight = this.baseWeight;
    copy.members = (ArrayList<WeightPair>)this.members.clone();

    return copy;
  }

  private class WeightPair {
    T item;
    int weight;

    public WeightPair(T item, int weight) {
      this.item = item;
      this.weight = weight;
    }
  }
}
