package com.jtrimble.delphi.gui.component;

import com.jtrimble.delphi.render.GameSprite;
import java.awt.Color;
import silverlib.game.GameUtils.MButton;
import silverlib.geo.Point;
import silverlib.geo.Rect;

public class BoxElement implements MenuElement {

  private int x;
  private int y;
  private int w;
  private int h;

  private Color fillColor;

  public BoxElement(int x, int y, int w, int h, Color fill) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;

    this.fillColor = fill;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setW(int w) {
    this.w = w;
  }

  public void setH(int h) {
    this.h = h;
  }

  @Override
  public boolean pointInside(Point p) {
    return false;
  }

  @Override
  public void handleClick(MButton button) {

  }

  @Override
  public int renderX() {
    return x;
  }

  @Override
  public int renderY() {
    return y;
  }

  @Override
  public GameSprite getSprite() {
    GameSprite base = new GameSprite();
    Rect box = new Rect(new Point(0,0), w, h);

    box.setFill(fillColor);

    base.addShape(box, 0, 0);


    return base;
  }
}
