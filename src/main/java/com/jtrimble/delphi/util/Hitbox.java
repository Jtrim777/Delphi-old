package com.jtrimble.delphi.util;

import silverlib.geo.PinPoint;
import silverlib.geo.Point;

public class Hitbox {
  protected double minX, minY, maxX, maxY;

  public Hitbox(double minX, double minY, double maxX, double maxY) {
    this.minX = minX;
    this.minY = minY;
    this.maxX = maxX;
    this.maxY = maxY;
  }

  public Hitbox(PinPoint pos, double width, double height) {
    this.minX = pos.x() - (width/2);
    this.minY = pos.y() - (height/2);
    this.maxX = pos.x() + (width/2);
    this.maxY = pos.y() + (height/2);
  }

  public Hitbox(Point pos, double width, double height) {
    this.minX = pos.x() - (width/2);
    this.minY = pos.y() - (height/2);
    this.maxX = pos.x() + (width/2);
    this.maxY = pos.y() + (height/2);
  }

  public boolean doesIntersect(Hitbox other) {
    return (other.maxX >= this.minX && other.maxY >= this.minY)
        || (this.maxX >= other.minX && this.maxY >= other.minY);
  }

  public boolean isInside(PinPoint p) {
    return this.minX <= p.x() && this.minY <= p.y()
        && this.maxX >= p.x() && this.maxY >= p.y();
  }
  public boolean isInside(Point p) {
    return this.minX <= p.x() && this.minY <= p.y()
        && this.maxX >= p.x() && this.maxY >= p.y();
  }

  public double getMinX() {
    return minX;
  }

  public double getMinY() {
    return minY;
  }

  public double getMaxX() {
    return maxX;
  }

  public double getMaxY() {
    return maxY;
  }
}
