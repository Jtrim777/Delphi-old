package com.jtrimble.delphi.tile.util;

import silverlib.geo.PinPoint;

public class TileBounds {
  private double width, height;
  private PinPoint center;

  public TileBounds(double width, double height, PinPoint c) {
    this.width = width;
    this.height = height;
    this.center = c;
  }

  public double getMinX() {
    return center.x() - (width/2);
  }
  public double getMaxX() {
    return center.x() + (width/2);
  }
  public double getMinY() {
    return center.y() - (height/2);
  }
  public double getMaxY() {
    return center.y() + (height/2);
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public boolean isInside(PinPoint p) {
    return p.x() >= getMinX() && p.x() <= getMaxX()
        && p.y() <= getMaxY() && p.y() >= getMinY();
  }
}