package com.jtrimble.delphi.util;

import com.jtrimble.delphi.entity.GameEntity;
import com.jtrimble.delphi.world.GameWorld;
import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Predicate;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;

public class GameUtil {

  public static Color adjustColorForLight(Color c, float light) {
    float r = (float)c.getRed() * light;
    float g = (float)c.getGreen() * light;
    float b = (float)c.getBlue() * light;

    try {
      return new Color((int)Math.min(r,255), (int)Math.min(g,255), (int)Math.min(b,255));
    } catch (IllegalArgumentException e) {
      return Color.white;
    }
  }

  public static ArrayList<GameEntity> entitiesAtPosition(PinPoint p, ArrayList<GameEntity> context) {
    ArrayList<GameEntity> out = new ArrayList<>();
    for (GameEntity de : context) {
      if (withinHitbox(p, de)) {
        out.add(de);
      }
    }

    return out;
  }

  public static boolean withinHitbox(PinPoint p, GameEntity de) {
    return de.getHitbox().isInside(p);
  }

  public static Point hopInDirection(Point p, MapDirection d) {
    return new Point(p.x()+d.dx, p.y()+d.dy);
  }

  public static PinPoint hopInDirection(PinPoint p, MapDirection d, double hopLength) {
    return new PinPoint(p.x()+((double)d.dx*hopLength), p.y()+((double)d.dy*hopLength));
  }

  public static double distanceBetween(Point p1, Point p2) {
    return Math.sqrt(Math.pow(p1.x()-p2.x(),2) + Math.pow(p1.y()-p2.y(),2));
  }

  public static double distanceBetween(PinPoint p1, PinPoint p2) {
    return Math.sqrt(Math.pow(p1.x()-p2.x(),2) + Math.pow(p1.y()-p2.y(),2));
  }

  public static double distanceBetween(GameEntity e1, GameEntity e2) {
    PinPoint p1 = e1.getLocation();
    PinPoint p2 = e2.getLocation();

    return Math.sqrt(Math.pow(p1.x()-p2.x(),2) + Math.pow(p1.y()-p2.y(),2));
  }

  public static double computeAngle(PinPoint p1, PinPoint p2) {
    return Math.atan2(p2.y()-p1.y(), p2.x()-p1.x());
  }


  public static boolean insideBounds(Point p, int minX, int minY, int maxX, int maxY) {
    return p.x() >= minX && p.x() < maxX && p.y() >= minY && p.y() < maxY;
  }
  public static boolean insideBounds(PinPoint p, int minX, int minY, int maxX, int maxY) {
    return p.x() >= minX && p.x() < maxX && p.y() >= minY && p.y() < maxY;
  }

  public static boolean insideMap(Point p, GameWorld map) {
    return insideBounds(p, 0, 0, map.getMapWidth(), map.getMapHeight());
  }
  public static boolean insideMap(PinPoint p, GameWorld map) {
    return insideBounds(p, 0, 0, map.getMapWidth(), map.getMapHeight());
  }

  public static <T> ArrayList<T> filterList(ArrayList<T> list, Predicate<T> pred) {
    ArrayList<T> out = new ArrayList<>();

    list.forEach(elem -> {
      if (pred.test(elem)) {
        out.add(elem);
      }
    });

    return out;
  }

}
