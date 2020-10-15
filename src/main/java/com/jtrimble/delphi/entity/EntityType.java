package com.jtrimble.delphi.entity;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.registry.TexturedRegistryEntry;
import silverlib.geo.PinPoint;

public interface EntityType<T extends GameEntity> extends TexturedRegistryEntry {
  T constructEntity(JsonObject source);
  T spawnNew(PinPoint location);
  String getID();
  EntityTag[] getTags();

  float getSpawnFrequency();

  default boolean hasTag(EntityTag tag) {
    for (EntityTag t : getTags()) {
      if (tag.equals(t)) {
        return true;
      }
    }

    return false;
  }

  @Override
  default String getGroupID() {
    return "entities";
  }
}
