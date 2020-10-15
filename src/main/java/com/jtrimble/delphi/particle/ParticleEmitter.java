package com.jtrimble.delphi.particle;

import com.jtrimble.delphi.entity.GameEntity;
import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.util.MapVector;
import com.jtrimble.delphi.render.GameSprite;
import silverlib.geo.PinPoint;

public class ParticleEmitter extends GameEntity {
  private int spawnRate;
  private int spawnCount;
  private MapVector spawnVector;
  private float spawnMagVar;
  private float spawnDirVar;
  private ParticleType ptype;

  private int ticker;
  private int counter;

  public ParticleEmitter(PinPoint location, ParticleType ptype, int rate, int count,
                         MapVector vec, float magVar, float dirVar) {
    super(location, 0f, 0, 0);
    this.spawnRate = rate;
    this.spawnCount = count;
    this.spawnVector = vec;
    this.spawnMagVar = magVar;
    this.spawnDirVar = dirVar;
    this.ptype = ptype;
  }

  @Override
  public void tick(GameWorld world) {
//    if (ticker < spawnRate && counter < spawnCount) {
//      int toSpawn = (int)(Math.random() * (spawnCount-counter));
//      System.out.println("[Emitter] Spawning "+toSpawn+" particles");
//
//      while (toSpawn>0) {
//        Particle p = new Particle(this.getLocation(), generateVector(), ptype);
//
//        toSpawn--;
//      }
//      counter += toSpawn;
//      ticker++;
//    } else if (counter < spawnCount) {
//      int toSpawn = spawnCount - counter;
//      System.out.println("[Emitter] Spawning "+toSpawn+" particles");
//
//      while (toSpawn>0) {
//        Particle p = new Particle(this.getLocation(), generateVector(), ptype);
//
//        world.addParticle(p);
//        toSpawn--;
//      }
//      counter += toSpawn;
//      ticker++;
//    } else if (ticker < spawnRate) {
//      ticker ++;
//    } else {
//      ticker = 0;
//      counter = 0;
//    }

    if (ticker < spawnRate) {
      ticker ++;
    } else {
      int toSpawn = spawnCount;
      while (toSpawn>0) {
        Particle p = new Particle(this.getLocation(), generateVector(), ptype);
        world.addParticle(p);

        toSpawn--;
      }

      ticker = 0;
    }
  }

  private MapVector generateVector() {
    double angleVariance = (Math.random()-0.5)*spawnDirVar*Math.PI*2;
    double speedVariance = (Math.random()-0.5)*2*spawnMagVar*spawnVector.magnitude();

    double newAngle = spawnVector.angle() + angleVariance;
    double newMag = spawnVector.magnitude() + speedVariance;

    return new MapVector(newAngle, newMag, true);
  }

  @Override
  public boolean shouldDraw() {
    return false;
  }
  @Override
  public GameSprite getSprite() {
    return null;
  }
  @Override
  public boolean getHogsTile() {
    return false;
  }

  public void setSpawnVector(MapVector spawnVector) {
    this.spawnVector = spawnVector;
  }
}
