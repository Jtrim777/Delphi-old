package com.jtrimble.delphi.entity;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.util.Hitbox;
import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.registry.TextureManager;
import com.jtrimble.delphi.util.GameUtil;
import com.jtrimble.delphi.render.GameSprite;
import silverlib.geo.PinPoint;

public abstract class MonsterEntity extends LivingEntity {
  private String entityID;
  private double trackingDistance;
  private double size;
  private double movementSpeed;

  private Player target;

  public MonsterEntity(PinPoint loc, double maxHealth, double baseAttack,
                       double sight, double size, double speed, String spriteLoc) {
    super(loc, maxHealth, size, size);
    this.baseAttackStrength = baseAttack;
    this.entityID = spriteLoc;
    this.trackingDistance = sight;
    this.size = size;
    this.movementSpeed = speed;
    this.target = null;
  }

  @Override
  public GameSprite getSprite() {
    return TextureManager.DEFAULT.getEntry("entity", entityID);
  }

  @Override
  public void tick(GameWorld world) {
    if (this.target != null) {
      if (GameUtil.distanceBetween(this.location, target.location) > (trackingDistance*1.5)) {
        this.target = null;
      } else if (GameUtil.distanceBetween(this.location, target.location) > (2.5)) {
        this.moveTowards(target, world);
      } else {
        this.attack(target);
      }
    } else {
      for (GameEntity de : world.getEntities()) {
        if (de instanceof Player && GameUtil.distanceBetween(de, this) <= trackingDistance) {
          this.target = (Player)de;
          break;
        }
      }
    }

    super.tick(world);
  }

  private void moveTowards(GameEntity target, GameWorld map) {
    double theta = GameUtil.computeAngle(this.location, target.location);

    double stepX = movementSpeed * Math.cos(theta);
    double stepY = movementSpeed * Math.sin(theta);

    PinPoint nPos = new PinPoint(location.x()+stepX, location.y()+stepY);
    this.moveTo(nPos, map);
  }

  public static EntityType createEntityType(double maxHealth, double baseAttack,
      double sight, double size, double speed, float rarity, String id) {
    return new EntityType() {
      @Override
      public MonsterEntity constructEntity(JsonObject source) {
        return null;
      }

      @Override
      public MonsterEntity spawnNew(PinPoint location) {
        return new MonsterEntity(location, maxHealth, baseAttack, sight, size, speed, id) {};
      }

      @Override
      public String getID() {
        return id;
      }

      @Override
      public EntityTag[] getTags() {
        return new EntityTag[]{EntityTag.MONSTER};
      }

      @Override
      public float getSpawnFrequency() {
        return rarity;
      }
    };
  }
}
