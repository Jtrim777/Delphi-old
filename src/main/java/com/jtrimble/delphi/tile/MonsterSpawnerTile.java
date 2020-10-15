package com.jtrimble.delphi.tile;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.entity.EntityTag;
import com.jtrimble.delphi.entity.EntityType;
import com.jtrimble.delphi.entity.GameEntity;
import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.registry.RegistryManager;
import com.jtrimble.delphi.util.LootTable;
import java.util.ArrayList;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;

public class MonsterSpawnerTile extends SpawnerTile {
  public MonsterSpawnerTile(int x, int y) {
    super(x, y, null);
  }

  @Override
  public ArrayList<GameEntity> doSpawn() {
    ArrayList<GameEntity> out = new ArrayList<>();
    LootTable<EntityType> monsters = new LootTable<>(100);
    RegistryManager.main.getRegistry(EntityType.class).forEach(entityType -> {
      if (entityType.hasTag(EntityTag.MONSTER)) {
        monsters.addMember(entityType, entityType.getSpawnFrequency());
      }
    });

    double spawnChance = 1;
    for (int i=0; i<5; i++) {
      if (spawnChance < Math.random()) {
        spawnChance -= 0.1;
        continue;
      }

      try {
        EntityType monster = monsters.selectOne();

        GameEntity nEntity = monster.spawnNew(new PinPoint( center.x(), center.y()));
        out.add(nEntity);
        spawnChance -= 0.19;
      } catch (Exception e) {
        System.out.println(this.toString()+" :: Unable to spawn entity");
      }
    }

    if (out.size() > 1) {
      double distributeAngle = 0;
      double dda = (2*Math.PI)/out.size();
      for (GameEntity de : out) {
        double nx = (double)center.x() + Math.cos(distributeAngle);
        double ny = (double)center.y() + Math.sin(distributeAngle);

        de.moveToForce(new PinPoint(nx, ny));
        distributeAngle += dda;
      }
    }

    return out;
  }

  @Override
  public boolean hasRequiredSpace(GameWorld map) {
    // TODO: 2020-05-03 Needs reimplementation
    for (int dy=-1;dy<=1;dy++){
      for (int dx=-1;dx<=1;dx++){
        PinPoint np = new PinPoint(center.x()+dx, center.y()+dy);

        if (!map.canMoveThroughPosition(np)) {
          return false;
        }
      }
    }

    return true;
  }

  public static class MonsterSpawnerTileType implements TileType<MonsterSpawnerTile> {

    @Override
    public MonsterSpawnerTile constructTile(JsonObject source) {
      Point p;
      try {
        p = new Point(source.get("location").getAsString());
      } catch (Exception e) {
        throw new IllegalArgumentException("The format of the supplied Spawner constructor was " +
            "faulty: "+source.toString());
      }

      return new MonsterSpawnerTile(p.x(), p.y());
    }

    @Override
    public MonsterSpawnerTile createTile(PinPoint p) {
      return new MonsterSpawnerTile((int)p.x(), (int)p.y());
    }

    @Override
    public String getID() {
      return "monster_spawner";
    }
  }
}
