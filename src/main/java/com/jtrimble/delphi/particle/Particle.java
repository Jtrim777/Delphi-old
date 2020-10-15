package com.jtrimble.delphi.particle;

import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.render.WorldSpriteProvider;
import com.jtrimble.delphi.util.MapVector;
import silverlib.geo.PinPoint;
import silverlib.geo.Rect;

public class Particle implements WorldSpriteProvider {
  protected PinPoint pos;
  protected MapVector dir;
  protected int lifetime;
  protected int ticksExisted;
  protected ParticleType type;
  protected double[] params;
  private float appliedLight = 0;

  public Particle(PinPoint pos, MapVector dir, ParticleType type) {
    this.pos = pos;
    this.dir = dir;
    this.type = type;
    this.lifetime = type.determineLifetime();
    this.ticksExisted = 0;
    this.params = type.initParams();
  }

  public void tick(){
    this.pos = dir.addToPoint(this.pos);
    this.dir = this.type.getNewDirection(this);
    this.ticksExisted++;
  }

  public GameSprite getSprite() {
    GameSprite base = new GameSprite();
    Rect shape = new Rect(0,0,1,1);
    shape.setFill(this.type.getColor(this, 0));

    base.addShape(shape,0,0);
    return base;
  }

  public boolean shouldRemove() {
    return ticksExisted >= lifetime;
  }

  public PinPoint getPosition() {
    return pos;
  }

  public int spriteX() {
    return (int)(pos.x());
  }
  public int spriteY() {
    return (int)(pos.y());
  }

  @Override
  public float getLuminance() {
    return 0f;
  }

  @Override
  public float getAppliedLight() {
    return appliedLight;
  }

  @Override
  public void illuminate(float power) {
    if (power > appliedLight) {
      appliedLight = power;
    }
  }

  @Override
  public void resetLighting() {
    appliedLight = 0;
  }

  @Override
  public boolean ignoresLighting() {
    return true;
  }

  public void makeDead() {
    this.ticksExisted = this.lifetime+1;
  }
}
