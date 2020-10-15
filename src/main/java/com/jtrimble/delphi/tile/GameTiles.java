package com.jtrimble.delphi.tile;

import com.jtrimble.delphi.event.EventListener;
import com.jtrimble.delphi.event.RegistryEvent;
import com.jtrimble.delphi.registry.RegistryTargetGroup;
import com.jtrimble.delphi.tile.ChestTile.ChestTileType;
import com.jtrimble.delphi.tile.DoorTile.DoorTileType;
import com.jtrimble.delphi.tile.MonsterSpawnerTile.MonsterSpawnerTileType;
import com.jtrimble.delphi.tile.SpawnerTile.SpawnerTileType;
import com.jtrimble.delphi.tile.WallTile.WallTileType;

@RegistryTargetGroup
public class GameTiles {
  public static final TileType WALL = new WallTileType();
  public static final TileType CHEST = new ChestTileType();
  public static final TileType DOOR = new DoorTileType();
  public static final TileType SPAWNER = new SpawnerTileType();
  public static final TileType MONSTER_SPAWNER = new MonsterSpawnerTileType();

  @EventListener
  public static void registerTiles(RegistryEvent.Register<TileType> event) {
    event.getRegistry().scanClass(GameTiles.class);
  }
}
