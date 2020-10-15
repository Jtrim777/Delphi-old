package com.jtrimble.delphi.entity;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.util.Hitbox;
import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.render.WorldSpriteProvider;
import com.jtrimble.delphi.util.GameUtil;
import com.jtrimble.delphi.util.MapDirection;
import com.jtrimble.delphi.world.WorldObject;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;

public abstract class GameEntity implements WorldSpriteProvider, WorldObject {
  PinPoint location;
  float luminance;
  float appliedLight = 0;
  Hitbox hitbox;

  public GameEntity(PinPoint location, float luminance) {
    this.location = location;
    this.luminance = luminance;

    this.hitbox = new Hitbox(location, 20, 20);
  }
  public GameEntity(PinPoint location, float luminance, double w, double h) {
    this.location = location;
    this.luminance = luminance;

    this.hitbox = new Hitbox(location, w, h);
  }

  public abstract GameSprite getSprite();

  public int spriteX() {
    return (int)(this.location.x());
  }
  public int spriteY() {
    return (int)(this.location.y());
  }

  public PinPoint getLocation() {
    return location;
  }
  public Point getIntLocation() {
    return new Point((int)location.x(), (int) location.y());
  }

  public abstract boolean getHogsTile();

  public void moveToForce(PinPoint p) {
    this.location = p;
  }

  public void moveTo(PinPoint p, GameWorld map) {
    if (validatePosition(p, map)) {
      this.location = p;
    }
  }

  public void move(MapDirection dir, GameWorld map, double stepSize) {
    PinPoint np = GameUtil.hopInDirection(this.location, dir, stepSize);
    if (validatePosition(np, map)) {
      moveToForce(np);
    }
  }

  private boolean validatePosition(PinPoint p, GameWorld map) {

    if (!map.canMoveThroughPosition(p)) {
      return false;
    }

    // TODO: 2020-05-04 Check entity intersection
//    if (this.getHogsTile()) {
//      ArrayList<GameEntity> residents = GameUtil.entitiesAtPosition(p, world.getEntities());
//      for (GameEntity de : residents) {
//        if (de.getHogsTile() && de != this) {
//          return false;
//        }
//      }
//    }

    return true;
  }

  public boolean shouldDraw() {
    return true;
  }

  public void tick(GameWorld world) {

  }

  public Hitbox getHitbox() {
    return this.hitbox;
  }

  @Override
  public PinPoint getPosition() {
    return this.location;
  }

  public boolean shouldDelete() {
    return false;
  }

  public float getLuminance() {
    return luminance;
  }

  @Override
  public float getAppliedLight() {
    return appliedLight > luminance ? appliedLight : luminance;
  }

  @Override
  public void illuminate(float power) {
    if (power > appliedLight) {
      this.appliedLight = power;
    }
  }

  @Override
  public void resetLighting() {
    this.appliedLight = 0;
  }

  public void setLuminance(float luminance) {
    this.luminance = luminance;
  }

  public void configFromJson(JsonObject source) {

  }

  public static String resolveResourceLocation(String[] args) {
    String rest = "";
    for (int i = 1; i < args.length; i++) {
      rest += args[i] + "/";
    }

    if (rest.endsWith("/")) {
      rest = rest.substring(0, rest.length()-1);
    }

    return args[0] + "/entities/" + rest;
  }
}
