package com.jtrimble.delphi.render;

import com.jtrimble.delphi.registry.RegistryEntry;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import silverlib.geo.Point;
import silverlib.img.Img;

public class SceneImg extends silverlib.game.SceneImg implements RegistryEntry {

  private String id = null;

  public SceneImg(String fpth) throws IOException {
    super(fpth);
  }

  private SceneImg(File source) throws IOException {
    super(source);
  }

  public static SceneImg loadFile(File file) {
    try {
      return new SceneImg(file);
    } catch (IOException e) {
      return null;
    }
  }

  public SceneImg(int i, int i1) {
    super(i, i1);
  }

  public SceneImg(int i, int i1, Color color) {
    super(i, i1, color);
  }

  public SceneImg(silverlib.game.SceneImg old) {
    super(old);
  }

  @Override
  public String getID() {
    return id;
  }

  @Override
  public void onRegister() {

  }

  public SceneImg setID(String nid) {
    this.id = nid;

    return this;
  }

  public SceneImg copy() {
    SceneImg replica = new SceneImg(this.width(), this.height());

    for (Point p : Point.iterPoints(0,0,this.width(),this.height())) {
      Color c = this.get(p.x(), p.y());
      replica.set(p.x(), p.y(), c);
    }

    return replica;
  }

  public void placeImage(Img img, int x, int y) {
    for (int j=0; j<img.height(); j++) {
      for (int i=0; i<img.width(); i++) {
        this.set(i+x, j+y, img.get(i, j));
      }
    }
  }
}
