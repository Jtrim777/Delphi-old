package com.jtrimble.delphi.item;

import com.jtrimble.delphi.entity.GameEntity;
import com.jtrimble.delphi.registry.TextureManager;
import com.jtrimble.delphi.registry.TexturedRegistryEntry;
import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.render.SpriteProvider;

public class GameItem implements TexturedRegistryEntry, SpriteProvider {
  private String name;
  private float luminance;
  private float rarity;
  private boolean isLoot = false;
  private boolean placeable;
  private Class<? extends GameEntity> placeEntity;
  private double attackStrength;

  public GameItem(String name) {
    this.name = name;
    this.luminance = 0;
    this.rarity = 0.5f;
    this.placeable = false;
    this.placeEntity = null;
    this.attackStrength = 1;
  }

  public GameItem setLuminance(float luminance) {
    this.luminance = luminance;
    return this;
  }

  public GameItem setRarity(float rarity) {
    this.rarity = rarity;
    return this;
  }

  public GameItem makePlaceable(Class<? extends GameEntity> entity) {
    this.placeable = true;
    this.placeEntity = entity;

    return this;
  }

  public GameItem makeLoot() {
    this.isLoot = true;
    return this;
  }

  public GameItem setAttackStrength(double strength) {
    this.attackStrength = strength;
    return this;
  }

  public String getName() {
    return name;
  }

  public float getLuminance() {
    return luminance;
  }

  public float getRarity() {
    return rarity;
  }

  public boolean isLoot() {
    return isLoot;
  }

  public double getAttackStrength() {
    return attackStrength;
  }

  public boolean isPlaceable() {
    return placeable;
  }

  public Class<? extends GameEntity> getPlaceEntity() {
    return placeEntity;
  }

  public GameSprite getSprite() {
    return TextureManager.DEFAULT.getEntry("items", name);
  }

  @Override
  public String getGroupID() {
    return "items";
  }

  @Override
  public String getID() {
    return getName();
  }

  public static String resolveResourceLocation(String[] args) {
    String rest = "";
    for (int i = 1; i < args.length; i++) {
      rest += args[i] + "/";
    }

    if (rest.endsWith("/")) {
      rest = rest.substring(0, rest.length()-1);
    }

    return args[0] + "/items/" + rest;
  }
}
