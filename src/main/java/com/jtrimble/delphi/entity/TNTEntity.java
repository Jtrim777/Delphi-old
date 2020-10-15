package com.jtrimble.delphi.entity;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.registry.TextureManager;
import com.jtrimble.delphi.render.GameSprite;
import silverlib.geo.PinPoint;

public class TNTEntity extends GameEntity {
  int fuse = 70;
  int blastRadius = 1;

  public TNTEntity(PinPoint location) {
    super(location, 0.5f, 10, 10);
  }

  @Override
  public float getLuminance() {
    return fuse > 0 ? 0.5f : 2f;
  }

  @Override
  public GameSprite getSprite() {
    return TextureManager.DEFAULT.getEntry("entities", "tnt");
  }

  @Override
  public boolean getHogsTile() {
    return false;
  }

  @Override
  public void tick(GameWorld world) {
    // TODO: 2020-05-04 How to delete walls?
//    fuse--;
//
//    if (fuse == 0) {
//      for (int dy=-1; dy<=1; dy++) {
//        for (int dx=-1; dx<=1; dx++) {
//          int nx = (int)location.x() + dx;
//          int ny = (int)location.y() + dy;
//          int tileIndex = GameUtil.getTileIndex(nx,ny,world.getMapWidth());
//
//          world.getTiles().set(tileIndex, new FloorTile(world.getTiles().get(tileIndex)));
//        }
//      }
//    }
  }

  @Override
  public boolean shouldDelete() {
    return fuse == 0;
  }

  public static class TNTEntityType implements EntityType<TNTEntity> {

    @Override
    public TNTEntity constructEntity(JsonObject source) {
      try {
        PinPoint p = new PinPoint(source.get("location").getAsString());
        TNTEntity entity = new TNTEntity(p);
        return entity;
      } catch (Exception e) {
        throw new IllegalArgumentException("TNT JSON source had bad format: "+source.toString());
      }
    }

    @Override
    public TNTEntity spawnNew(PinPoint location) {
      return new TNTEntity(location);
    }

    @Override
    public String getID() {
      return "tnt";
    }

    @Override
    public EntityTag[] getTags() {
      return new EntityTag[]{EntityTag.ITEM};
    }

    @Override
    public float getSpawnFrequency() {
      return 0;
    }
  }
}
