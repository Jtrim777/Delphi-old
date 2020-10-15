package com.jtrimble.delphi.item;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Inventory {
  ArrayList<ItemStack> contents;
  int size;

  public Inventory(ArrayList<ItemStack> contents, int size) {
    this.contents = contents;
    this.size = size;
  }

  public Inventory(int size) {
    this.size = size;

    this.contents = new ArrayList<>(size);
    for (int i=0;i<size;i++) {
      contents.add(null);
    }
  }

  public Inventory(JsonObject source) {

  }

  public boolean slotHasItem(int slot) {
    if (slot < 0 || slot > size) {
      throw new IllegalArgumentException("Slot index "+slot+" is out of bounds for inventory of " +
          "size" +size);
    }

    return this.contents.get(slot) != null;
  }

  public ItemStack getStackInSlot(int slot) {
    if (slot < 0 || slot > size) {
      throw new IllegalArgumentException("Slot index "+slot+" is out of bounds for inventory of " +
          "size" +size);
    }

    return this.contents.get(slot);
  }

  public boolean addItem(GameItem in) {
    for (int i=0; i<10; i++) {
      ItemStack sis = contents.get(i);

      if (sis != null && sis.getItem() == in) {
        sis.add(1);
        return true;
      }
    }

    for (int i=0; i<10; i++) {
      ItemStack sis = contents.get(i);

      if (sis == null) {
        ItemStack newStack = new ItemStack(in);
        contents.set(i, newStack);
        return true;
      }
    }

    return false;
  }

  public boolean addItems(GameItem in, int count) {
    for (int i=0; i<10; i++) {
      ItemStack sis = contents.get(i);

      if (sis != null && sis.getItem() == in) {
        sis.add(count);
        return true;
      }
    }

    for (int i=0; i<10; i++) {
      ItemStack sis = contents.get(i);

      if (sis == null) {
        ItemStack newStack = new ItemStack(in, count);
        contents.set(i, newStack);
        return true;
      }
    }

    return false;
  }

  public boolean addStack(ItemStack in) {
    for (int i=0; i<10; i++) {
      ItemStack sis = contents.get(i);

      if (sis != null && sis.getItem() == in.getItem()) {
        sis.add(in.getCount());
        return true;
      }
    }

    for (int i=0; i<10; i++) {
      ItemStack sis = contents.get(i);

      if (sis == null) {
        contents.set(i, in);
        return true;
      }
    }

    return false;
  }

  public ItemStack removeAt(int slot) {
    if (slot < 0 || slot > size) {
      throw new IllegalArgumentException("Slot index "+slot+" is out of bounds for inventory of " +
          "size" +size);
    }

    ItemStack result = contents.get(slot);
    contents.set(slot, null);

    return result;
  }

  public void removeItem(GameItem type, int count) {
    for (int i=0;i<10;i++) {
      ItemStack stack = contents.get(i);
      if (stack == null) {continue;}

      if (stack.getItem() == type) {
        stack.add(-count);
        if (stack.getCount() <= 0) {
          contents.set(i, null);
        }
      }
    }
  }

  public void removeItem(GameItem type) {
    for (int i=0;i<10;i++) {
      ItemStack stack = contents.get(i);
      if (stack == null) {continue;}

      if (stack.getItem() == type) {
        stack.add(-1);
        if (stack.getCount() <= 0) {
          contents.set(i, null);
        }
      }
    }
  }

  @Override
  public String toString() {
    String inv = "{";

    for (int i=0; i<10; i++) {
      ItemStack is = contents.get(i);

      if (is == null) {
        inv += i+": EMPTY, ";
      } else {
        inv += String.format("%d: %s (%d), ", i, is.getItem().getName(), is.getCount());
      }
    }

    inv = inv.substring(0, inv.length()-2);
    inv += "}";

    return inv;
  }

  public void placeStackInSlot(ItemStack is, int slot) {
    if (slot < 0 || slot > size) {
      throw new IllegalArgumentException("Slot index "+slot+" is out of bounds for inventory of " +
          "size" +size);
    }

    if (!slotHasItem(slot)) {
      this.contents.set(slot, is);
    }
  }

  public void mergeStackIntoSlot(ItemStack is, int slot) {
    if (slot < 0 || slot > size) {
      throw new IllegalArgumentException("Slot index "+slot+" is out of bounds for inventory of " +
          "size" +size);
    }

    if (!slotHasItem(slot)) {
      this.contents.set(slot, is);
    } else if (this.getStackInSlot(slot).getItem() == is.getItem()) {
      this.getStackInSlot(slot).add(is.getCount());
    }
  }

  public int getSize() {
    return size;
  }
}
