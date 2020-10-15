package com.jtrimble.delphi.util;

import silverlib.geo.PinPoint;

public class MapVector {
  private double dx;
  private double dy;

  public MapVector(double dx, double dy) {
    this.dx = dx;
    this.dy = dy;
  }

  public MapVector(double angle, double magnitude, boolean useAngle) {
    this(useAngle ? Math.cos(angle)*magnitude : angle, useAngle ? Math.sin(angle)*magnitude :
        angle);
  }

  public double getDx() {
    return dx;
  }

  public double getDy() {
    return dy;
  }

  public double magnitude() {
    return Math.sqrt(dx*dx + dy*dy);
  }

  public double angle() {
    double raw = Math.atan2(dy, dx)%(2*Math.PI);

    return raw >= 0 ? raw : (2*Math.PI)+raw;
  }

  public PinPoint addToPoint(PinPoint p) {
    return new PinPoint(p.x()+dx, p.y()+dy);
  }

  public MapVector adjustingMagnitude(double m) {
    return new MapVector(this.angle(), m, true);
  }
}

