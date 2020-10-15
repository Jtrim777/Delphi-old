package com.jtrimble.delphi.gui.component;

import com.jtrimble.delphi.render.GameSprite;
import silverlib.game.GameUtils.MButton;
import silverlib.geo.Point;

public class MenuButton implements MenuElement {
  private Point origin;
  private int width;
  private int height;
  private MenuButton.Action action;
  private boolean enabled = true;

  public MenuButton(Point origin, int width, int height,
      Action action) {
    this.origin = origin;
    this.width = width;
    this.height = height;
    this.action = action;
  }

  @Override
  public boolean pointInside(Point p) {
    return p.x() >= origin.x() && p.x() < origin.x() + width
        && p.y() >= origin.y() && p.y() < origin.y() + height;
  }

  public void enable() {
    this.enabled = true;
  }

  public void disable() {
    this.enabled = false;
  }

  public void toggleState() {
    this.enabled = !enabled;
  }

  @Override
  public void handleClick(MButton button) {
    if (enabled) {
      action.act();
    }
  }

  @Override
  public int renderX() {
    return origin.x();
  }

  @Override
  public int renderY() {
    return origin.y();
  }

  @Override
  public GameSprite getSprite() {
    return null;
  }

  public interface Action {
    void act();
  }
}
