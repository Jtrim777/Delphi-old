package com.jtrimble.delphi.tile;

import com.jtrimble.delphi.entity.Player;
import com.jtrimble.delphi.gui.MenuManager;
import com.jtrimble.delphi.registry.TextureManager;
import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.render.WorldSpriteProvider;
import com.jtrimble.delphi.util.Hitbox;
import com.jtrimble.delphi.world.WorldObject;
import silverlib.geo.PinPoint;

public abstract class GameTile implements WorldSpriteProvider, WorldObject {
  protected PinPoint center;
  protected Hitbox bounds;

  protected float luminance;
  protected float appliedLight = 0;

  public GameTile(double x, double y, double w, double h) {
    this.center = new PinPoint(x,y);
    this.bounds = new Hitbox(center, w, h);

    this.luminance = 0f;
  }

  public GameSprite getSprite() {
    GameSprite baseSprite = TextureManager.DEFAULT.getEntry("tiles", this.getRegistryName());

    return baseSprite;
  }
  public boolean shouldDraw() {
    return true;
  }

  public int spriteX() {
    return (int)Math.round(this.bounds.getMinX());
  }
  public int spriteY() {
    return (int)Math.round(this.bounds.getMinY());
  }

  @Override
  public Hitbox getHitbox() {
    return bounds;
  }

  @Override
  public float getLuminance() {
    return 0;
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

  public PinPoint getPosition() {
    return center;
  }

  public boolean pointIntersects(PinPoint p) {
    return this.bounds.isInside(p);
  }

  @Override
  public String toString() {
    return "["+this.getRegistryName()+"] ("+center.x()+", "+center.y()+")";
  }

  public abstract float getTransparency();
  public abstract boolean doesBlockMovement();
  public abstract boolean canInteractWith();
  public void interactWith(Player actor) {

  }
  public boolean hasMenu() {
    return false;
  }
  public void openMenu(Player actor, MenuManager manager) {

  }
  public abstract String getRegistryName();

  public void setBounds(Hitbox bounds) {
    this.bounds = bounds;
  }

  public static String resolveResourceLocation(String[] args) {
    String rest = "";
    for (int i = 1; i < args.length; i++) {
      rest += args[i] + "/";
    }

    if (rest.endsWith("/")) {
      rest = rest.substring(0, rest.length()-1);
    }

    return args[0] + "/tiles/" + rest;
  }
}
