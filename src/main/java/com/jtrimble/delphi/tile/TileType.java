package com.jtrimble.delphi.tile;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.registry.TexturedRegistryEntry;
import silverlib.geo.PinPoint;

public interface TileType<T extends GameTile> extends TexturedRegistryEntry {
  T constructTile(JsonObject source);
  T createTile(PinPoint p);

  @Override
  default String getGroupID() {
    return "tiles";
  }
}
