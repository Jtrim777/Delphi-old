package com.jtrimble.delphi.gui.component;

import com.jtrimble.delphi.gui.drag.DragDest;
import com.jtrimble.delphi.gui.drag.DragElement;
import com.jtrimble.delphi.gui.drag.DragSource;
import com.jtrimble.delphi.item.Inventory;
import com.jtrimble.delphi.item.ItemStack;
import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.util.Hitbox;
import silverlib.game.GameUtils.MButton;
import silverlib.geo.Point;

public class InventorySlot implements MenuElement, DragSource, DragDest {
  public static final int SIZE = 40;

  private Point position;
  private Hitbox clickBox;
  private Inventory source;
  private Inventory coInv;
  private int slotNum;

  public InventorySlot(Point position, Inventory source, int slotNum) {
    this.position = position;
    this.source = source;
    this.slotNum = slotNum;
    this.coInv = null;

    this.clickBox = new Hitbox(position, SIZE, SIZE);
  }

  public void setCoInventory(Inventory coi) {
    this.coInv = coi;
  }

  @Override
  public boolean pointInside(Point p) {
    return clickBox.isInside(p);
  }

  @Override
  public void handleClick(MButton button) {
    if (button == MButton.RIGHT && coInv != null && source.slotHasItem(slotNum)) {
      coInv.addStack(source.getStackInSlot(slotNum));
      source.removeAt(slotNum);
    }
  }

  @Override
  public int renderX() {
    return position.x();
  }

  @Override
  public int renderY() {
    return position.y();
  }

  @Override
  public void dropOnto(DragElement elem) {
    if (canDrop(elem)) {
      ItemStack is = (ItemStack)elem.getElement();

      source.mergeStackIntoSlot(is, slotNum);
    }
  }

  @Override
  public boolean canDrop(DragElement elem) {
    if (elem.getElement() instanceof ItemStack) {
      return !source.slotHasItem(slotNum)
          || source.getStackInSlot(slotNum).getItem() == ((ItemStack) elem.getElement()).getItem();
    } else {
      return false;
    }
  }

  @Override
  public DragElement getDraggableElement() {
    if (source.slotHasItem(slotNum)) {
      return new DragElement<>(source.getStackInSlot(slotNum));
    } else {
      return null;
    }
  }

  @Override
  public GameSprite getSprite() {
    if (source.slotHasItem(slotNum)) {
      return source.getStackInSlot(slotNum).getSprite();
    } else {
      return null;
    }
  }
}
