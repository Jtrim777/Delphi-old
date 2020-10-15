package com.jtrimble.delphi.registry;

import com.jtrimble.delphi.entity.EntityType;
import com.jtrimble.delphi.event.EventListener;
import com.jtrimble.delphi.event.RegistryEvent;
import com.jtrimble.delphi.item.GameItem;
import com.jtrimble.delphi.particle.ParticleType;
import com.jtrimble.delphi.tile.TileType;

public class GameRegistries {
  @EventListener
  public static void createRegistries(RegistryEvent.CreateRegistries event) {
    event.create(new GameRegistry<>("items", GameItem.class));
    event.create(new GameRegistry<>("tiles", TileType.class));
    event.create(new GameRegistry<>("entities", EntityType.class));
    event.create(new GameRegistry<>("particles", ParticleType.class));
  }
}
