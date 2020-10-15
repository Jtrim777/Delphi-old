package com.jtrimble.delphi.item;

import com.jtrimble.delphi.entity.TNTEntity;
import com.jtrimble.delphi.entity.TorchEntity;
import com.jtrimble.delphi.event.EventListener;
import com.jtrimble.delphi.event.RegistryEvent;
import com.jtrimble.delphi.registry.RegistryTargetGroup;

@RegistryTargetGroup
public class GameItems {
  public static final GameItem TORCH = new GameItem("torch")
      .setLuminance(4)
      .makePlaceable(TorchEntity.class)
      .makeLoot();

  public static final GameItem KEY = new GameItem("key")
      .setRarity(0.8f)
      .makeLoot();

  public static final GameItem BRONZE_COIN = new GameItem("bronze_coin")
      .setRarity(0.3f)
      .makeLoot();

  public static final GameItem SILVER_COIN = new GameItem("silver_coin")
      .setRarity(0.55f)
      .makeLoot();

  public static final GameItem GOLD_COIN = new GameItem("gold_coin")
      .setRarity(0.8f)
      .makeLoot();

  public static final GameItem SWORD = new GameItem("sword")
      .setRarity(0.7f)
      .setAttackStrength(4);

  public static final GameItem HEALING_POTION = new GameItem("healing_potion")
      .setRarity(0.8f);

  public static final GameItem DYNAMITE = new GameItem("tnt")
      .setRarity(0.85f)
      .makePlaceable(TNTEntity.class)
      .makeLoot();

  @EventListener
  public static void registerItems(RegistryEvent.Register<GameItem> event) {
    event.getRegistry().scanClass(GameItems.class);
  }
}
