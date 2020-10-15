package com.jtrimble.delphi.entity;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.world.GameWorld;
import silverlib.geo.PinPoint;

public abstract class LivingEntity extends GameEntity {
  double health;
  final double maxHealth;
  boolean isDead = false;
  double baseAttackStrength = 1.0;
  double attackCooldown = 0;

  public LivingEntity(PinPoint loc, double maxHealth) {
    super(loc, 0);
    this.health = maxHealth;
    this.maxHealth = maxHealth;
  }

  public LivingEntity(PinPoint loc, double maxHealth, double w, double h) {
    super(loc, 0, w, h);
    this.health = maxHealth;
    this.maxHealth = maxHealth;
  }

  public void hurt(double dmg) {
    this.health -= dmg;
    if (this.health <= 0) {
      this.isDead = true;
    }
  }

  public void heal(double amt) {
    this.health += amt;
    this.health = Math.min(maxHealth, health);
  }

  @Override
  public boolean getHogsTile() {
    return true;
  }

  @Override
  public boolean shouldDelete() {
    return isDead;
  }

  public void attack(LivingEntity target) {
    if (attackCooldown == 0) {
      target.hurt(baseAttackStrength);
      attackCooldown = 15;
    }
  }

  @Override
  public void tick(GameWorld world) {
    if (this.attackCooldown > 0) {
      attackCooldown--;
    }
    super.tick(world);
  }

  @Override
  public void configFromJson(JsonObject source) {
    this.health = source.get("health").getAsDouble();

    super.configFromJson(source);
  }
}
