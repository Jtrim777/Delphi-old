package com.jtrimble.delphi.util;

public enum MapDirection {
  UP_LEFT(-1,-1),
  LEFT(-1,0),
  DOWN_LEFT(-1,1),
  UP(0,-1),
  DOWN(0,1),
  UP_RIGHT(1,-1),
  RIGHT(1,0),
  DOWN_RIGHT(1,1);

  public int dx;
  public int dy;

  MapDirection(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }
}
