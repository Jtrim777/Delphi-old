package com.jtrimble.delphi.item;

import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.render.SpriteProvider;

public class ItemStack implements SpriteProvider {
  private GameItem item;
  private int amt;

  public ItemStack(GameItem item, int amt) {
    this.item = item;
    this.amt = amt;
  }

  public ItemStack(GameItem item) {
    this.item = item;
    this.amt = 1;
  }

  public GameItem getItem() {
    return item;
  }

  public int getCount() {
    return amt;
  }

  public void setAmt(int amt) {
    this.amt = amt;
  }

  public void add(int count) {
    this.amt += count;
  }

  @Override
  public GameSprite getSprite() {
    return null;
  }
}
