package com.jtrimble.delphi.tile;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.entity.Player;
import com.jtrimble.delphi.gui.GameMenu;
import com.jtrimble.delphi.gui.InventoryMenu;
import com.jtrimble.delphi.gui.MenuManager;
import com.jtrimble.delphi.item.GameItem;
import com.jtrimble.delphi.item.Inventory;
import com.jtrimble.delphi.registry.RegistryManager;
import com.jtrimble.delphi.util.LootTable;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;

public class ChestTile extends GameTile {

  private float lootQuality = 0.5f;
  private int lootRolls = 10;
  private Inventory loot = new Inventory(10);
  private boolean hardsetLoot = false;

  public ChestTile(int x, int y) {
    super(x, y, 20, 20);
  }

  @Override
  public float getTransparency() {
    return 0.8f;
  }

  @Override
  public boolean doesBlockMovement() {
    return true;
  }

  @Override
  public boolean canInteractWith() {
    return false;
  }

  @Override
  public void interactWith(Player actor) {

  }

  @Override
  public String getRegistryName() {
    return "chest";
  }

  public void setLootQuality(float lootQuality) {
    this.lootQuality = lootQuality;
  }

  public void setLootRolls(int lootRolls) {
    this.lootRolls = lootRolls;
  }

  public void generateLoot() {
    LootTable<GameItem> lootMap = createLootTable();
    if (lootQuality != 0.5) {
      lootMap.adjustQuality(lootQuality);
    }

    for (int i = 0; i < lootRolls; i++) {
      GameItem chosen = lootMap.selectOne();
      if (chosen != null) {
        loot.addItem(chosen);
      }
    }
  }

  @Override
  public boolean hasMenu() {
    return true;
  }

  @Override
  public void openMenu(Player actor, MenuManager manager) {
    GameMenu pinv = new InventoryMenu(actor, actor.getInventory(), this.loot, "Player");
    GameMenu cinv = new InventoryMenu(actor, this.loot, actor.getInventory(), "Chest");

    manager.openMenu(pinv);
    manager.openMenu(cinv);
  }

  private static LootTable<GameItem> createLootTable() {
    LootTable<GameItem> baseTable = new LootTable<GameItem>(100)
        .addMember(null, 20);

    RegistryManager.main.getRegistry(GameItem.class).forEach(gameItem -> {
      if (gameItem.isLoot()) {
        baseTable.addMember(gameItem, gameItem.getRarity());
      }
    });

    return baseTable;
  }

  public static class ChestTileType implements TileType<ChestTile> {
    @Override
    public ChestTile constructTile(JsonObject source) {
      try {
        Point p = new Point(source.get("location").getAsString());

        ChestTile tile = new ChestTile(p.x(), p.y());

        if (source.has("lootQuality")) {
          tile.lootQuality = source.get("lootQuality").getAsFloat();
        }
        if (source.has("lootRolls")) {
          tile.lootRolls = source.get("lootRolls").getAsInt();
        }
        if (source.has("loot")) {
          tile.hardsetLoot = true;
          tile.loot = new Inventory(source.get("loot").getAsJsonObject());
        } else {
          tile.generateLoot();
        }

        return tile;

      } catch (Exception e) {
        throw new IllegalArgumentException("The format of the supplied Chest constructor was " +
            "faulty: "+source.toString());
      }
    }

    @Override
    public ChestTile createTile(PinPoint p) {
      return new ChestTile((int)p.x(), (int)p.y());
    }

    @Override
    public String getID() {
      return "chest";
    }
  }
}
