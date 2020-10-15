package com.jtrimble.delphi.tile;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.entity.EntityType;
import com.jtrimble.delphi.entity.GameEntity;
import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.registry.RegistryManager;
import java.util.ArrayList;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;

public class SpawnerTile extends GameTile {
  EntityType spawnEntity;

  public SpawnerTile(int x, int y, EntityType spawnEntity) {
    super(x, y, 0,0);
    this.spawnEntity = spawnEntity;
  }

  @Override
  public float getTransparency() {
    return 0;
  }

  @Override
  public boolean doesBlockMovement() {
    return false;
  }

  @Override
  public boolean canInteractWith() {
    return false;
  }

  public EntityType getSpawnEntityType() {
    return spawnEntity;
  }

  public ArrayList<GameEntity> doSpawn()  {
    ArrayList<GameEntity> out = new ArrayList<>();

    try {
      GameEntity nEntity = spawnEntity.spawnNew(new PinPoint( center.x(), center.y()));
      out.add(nEntity);
    } catch (Exception e) {
      System.out.println(this.toString()+" :: Unable to spawn entity");
    }

    return out;
  }

  public boolean hasRequiredSpace(GameWorld map) {
    return true;
  }

  @Override
  public String getRegistryName() {
    return "spawner";
  }

  public static class SpawnerTileType implements TileType<SpawnerTile> {

    @Override
    public SpawnerTile constructTile(JsonObject source) {
      Point p;
      String entityName;
      try {
        p = new Point(source.get("location").getAsString());

        entityName = source.get("spawnEntity").getAsString();
      } catch (Exception e) {
        throw new IllegalArgumentException("The format of the supplied Spawner constructor was " +
            "faulty: "+source.toString());
      }

      EntityType spawnEntity = RegistryManager.main.getRegistry(EntityType.class).getEntry(entityName);

      if (spawnEntity == null) {
        throw new IllegalArgumentException("No entity exists with ID "+entityName);
      }

      return new SpawnerTile(p.x(), p.y(), spawnEntity);
    }

    @Override
    public SpawnerTile createTile(PinPoint p) {
      return new SpawnerTile((int)p.x(), (int)p.y(), null);
    }

    @Override
    public String getID() {
      return "spawner";
    }
  }
}
