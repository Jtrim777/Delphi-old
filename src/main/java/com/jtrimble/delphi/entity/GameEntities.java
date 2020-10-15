package com.jtrimble.delphi.entity;

import com.jtrimble.delphi.entity.Player.PlayerEntityType;
import com.jtrimble.delphi.entity.SpellEntity.SpellEntityType;
import com.jtrimble.delphi.entity.TNTEntity.TNTEntityType;
import com.jtrimble.delphi.entity.TorchEntity.TorchEntityType;
import com.jtrimble.delphi.event.EventListener;
import com.jtrimble.delphi.event.RegistryEvent;
import com.jtrimble.delphi.registry.RegistryManager;
import com.jtrimble.delphi.registry.RegistryTargetGroup;

@RegistryTargetGroup
public class GameEntities {
  public static final EntityType PLAYER = new PlayerEntityType();

  public static final EntityType TORCH = new TorchEntityType();
  public static final EntityType TNT = new TNTEntityType();

  public static final EntityType SPELL = new SpellEntityType();

  public static final EntityType GOOMBA = MonsterEntity.createEntityType(
      5, 0.5, 5, 1, 0.1, 0.75f, "goomba");

  @EventListener
  public static void registerEntities(RegistryEvent.Register<EntityType> event) {
    event.getRegistry().scanClass(GameEntities.class);
  }
}
