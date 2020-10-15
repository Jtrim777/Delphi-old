package com.jtrimble.delphi.entity;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.particle.ParticleEmitter;
import com.jtrimble.delphi.particle.ParticleTypes;
import com.jtrimble.delphi.util.GameUtil;
import com.jtrimble.delphi.util.MapVector;
import silverlib.geo.PinPoint;

public class SpellEntity extends ParticleEmitter {
  private MapVector direction;
  private boolean obsolete = false;

  private PinPoint anchor;
  private double rotation;
  private double orbitDistance;

  public SpellEntity(PinPoint location, MapVector direction) {
    super(location, ParticleTypes.SPELL, 2, 8,
        new MapVector(-direction.getDx(), -direction.getDy()).adjustingMagnitude(0.1), 0.1f,
        0.7f);
    this.direction = direction;
    this.anchor = null;
    this.setLuminance(0.5f);
  }

  public SpellEntity(PinPoint location, PinPoint anchor, double orbit) {
    super(location, ParticleTypes.SPELL, 2, 8, new MapVector(0,0), 0.1f, 0.1f);
    this.anchor = anchor;
    this.rotation = 0;
    this.orbitDistance = orbit;
    this.direction = null;
//    this.setLuminance(0.5f);
  }

  @Override
  public void tick(GameWorld world) {

    if (this.direction != null) {
      this.moveToForce(direction.addToPoint(this.getLocation()));

      if (!GameUtil.insideMap(this.getLocation(), world)) {
        this.obsolete = true;
      }
    } else {
      double nx = (Math.cos(rotation)*orbitDistance) + anchor.x();
      double ny = (Math.sin(rotation)*orbitDistance) + anchor.y();

      rotation = (rotation+0.05)%(2*Math.PI);

      this.setSpawnVector(new MapVector(Math.atan2(ny-anchor.y(), nx-anchor.x()), 0.1, true));

      this.moveToForce(new PinPoint(nx, ny));
    }

    super.tick(world);
  }

  @Override
  public boolean shouldDelete() {
    return super.shouldDelete() || obsolete;
  }

  public static class SpellEntityType implements EntityType<SpellEntity> {

    @Override
    public SpellEntity constructEntity(JsonObject source) {
      throw new IllegalStateException("Spell entities cannot be type-constructed");
    }

    @Override
    public SpellEntity spawnNew(PinPoint location) {
      throw new IllegalStateException("Spell entities cannot be type-constructed");
    }

    @Override
    public String getID() {
      return "spell";
    }

    @Override
    public EntityTag[] getTags() {
      return new EntityTag[]{EntityTag.NO_SAVE};
    }

    @Override
    public float getSpawnFrequency() {
      return 0;
    }

    @Override
    public void onRegister() {
      // Do not load texture
    }
  }
}
