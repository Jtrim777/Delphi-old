package com.jtrimble.delphi.render;

import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.tile.GameTile;
import java.awt.Color;
import java.util.ArrayList;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;

public class LightEngine {
  private static final float FALLOFF = 1.05f;
  private static final double LOG_FLF = Math.log(FALLOFF);
  private static final float MIN_POW = 0.01f;

  private int[] lightMap;
  private boolean[] freezeMap;
  private int width;
  private ArrayList<LightSource> sources;
  private GameWorld map;

  public LightEngine(GameWorld world) {
    this.map = world;
    this.lightMap = new int[world.getMapWidth() * world.getMapHeight()];
    this.width = map.getMapWidth();
    this.sources = new ArrayList<>();
  }

  public void addLightSource(Point p, float l) {
    this.sources.add(new LightSource(l, p));
  }

  private void applyLight(LightSource source) {
    freezeMap = new boolean[lightMap.length];
    illuminate(source.x(), source.y(), source.luminance);

    double maxDist = maxDistance(source.luminance);

    for (Point p : Point.iterPoints(source.x()-(int)maxDist, source.y()-(int)maxDist,
        source.x()+(int)maxDist,source.y()+(int)maxDist)) {
      double thisDist = p.distanceTo(source.source);
      if (thisDist > maxDist) {continue;}
      double theta = Math.atan2(p.y()-source.y(), p.x()-source.x());

      illuminate(p.x(), p.y(), source.luminance
          * (float)(1.0/Math.pow(FALLOFF, thisDist))
          * getIntersectionFactor(source.x(), source.y(), theta, thisDist, source.luminance));
    }
  }

  private void illuminate(int x, int y, float pow) {
    if (!freezeMap[(y*width)+x]) {
      this.lightMap[(y*width)+x] += (int)(pow*255f);
    }
  }

//  private void castRay(Point cp, Point tp, double theta, double cLen, double tLen, float power) {
//
//    double nx = cp.x() + (cLen * Math.cos(theta));
//    double ny = cp.y() + (cLen * Math.sin(theta));
//
//    if (nx < 0 || nx >= world.getMapWidth() || ny < 0 || ny >= world.getMapHeight()) {
//      return;
//    }
//
//    float continuingPower = (float)(power * (1/Math.pow(FALLOFF, cLen)))
//        * world.getTransparencyAtPoint(new PinPoint(nx, ny));
//    if (continuingPower < 0.01) { return; }
//
//    if (cLen >= tLen) {
//      illuminate(tp.x(), tp.y(), continuingPower);
////      System.out.println(continuingPower);
//    } else {
//      castRay(cp, tp, theta, cLen+1, tLen, continuingPower);
//    }
//  }

  private float getIntersectionFactor(int sx, int sy, double theta, double len, float sp) {

    float rez = 1f;

    double cl = 0;
    while (cl < len) {
      double nx = sx + (cl * Math.cos(theta));
      double ny = sy + (cl * Math.sin(theta));

      GameTile tap = map.getTileAtPosition(new PinPoint(sx, sy));
      if (tap != null) {
        rez *= tap.getTransparency();
        tap.illuminate(rez*sp);
      }


      cl ++;
    }

    return rez;
  }

  private static float maxDistance(float power) {
    double lpm = Math.log(power/MIN_POW);

    return (float)(lpm/LOG_FLF);
  }

  public void applyLighting(SceneImg base) {
    for (LightSource source : this.sources) {
      applyLight(source);
    }

    int i=0;
    for (Color c : base.pixels()) {
      int r = c.getRed();
      int g = c.getGreen();
      int b = c.getBlue();

      r *= Math.min((float)lightMap[i]/255f, 1);
      g *= Math.min((float)lightMap[i]/255f, 1);
      b *= Math.min((float)lightMap[i]/255f, 1);

      base.set(i%width, i/width, new Color(r,g,b));
      i++;
    }
  }

  static class LightSource {
    protected float luminance;
    protected Point source;

    public LightSource(float luminance, Point source) {
      this.luminance = luminance;
      this.source = source;
    }

    int x() {
      return source.x();
    }
    int y() {
      return source.y();
    }
  }
}
