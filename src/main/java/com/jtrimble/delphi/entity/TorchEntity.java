package com.jtrimble.delphi.entity;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.registry.TextureManager;
import com.jtrimble.delphi.render.GameSprite;
import silverlib.geo.PinPoint;

public class TorchEntity extends GameEntity {

  public TorchEntity(PinPoint loc) {
    super(loc, 4f, 5, 20);
  }

  @Override
  public GameSprite getSprite() {
    return TextureManager.DEFAULT.getEntry("entities", "torch");
  }

  @Override
  public boolean getHogsTile() {
    return false;
  }

  public static class TorchEntityType implements EntityType<TorchEntity> {

    @Override
    public TorchEntity constructEntity(JsonObject source) {
      try {
        PinPoint p = new PinPoint(source.get("location").getAsString());
        TorchEntity entity = new TorchEntity(p);
        return entity;
      } catch (Exception e) {
        throw new IllegalArgumentException("Torch JSON source had bad format: "+source.toString());
      }
    }

    @Override
    public TorchEntity spawnNew(PinPoint location) {
      return new TorchEntity(location);
    }

    @Override
    public String getID() {
      return "torch";
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
