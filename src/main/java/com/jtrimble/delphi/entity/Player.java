package com.jtrimble.delphi.entity;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.util.Hitbox;
import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.item.GameItem;
import com.jtrimble.delphi.item.Inventory;
import com.jtrimble.delphi.registry.TextureManager;
import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.util.MapDirection;
import silverlib.geo.PinPoint;

public class Player extends LivingEntity {
  public static final double SPEED = 1;

  Inventory inventory;
  GameItem holding;
  int selectedHand = 0;

  public Player(PinPoint location) {
    super(location, 10, 16, 32);

    inventory = new Inventory(10);
    holding = null;
  }

  @Override
  public GameSprite getSprite() {
    return TextureManager.DEFAULT.getEntry("entities", "player_still_right");
  }

  public void removeItem(GameItem type, int count) {
    inventory.removeItem(type, count);

    resetHolding();
    resetLuminance();
  }

  public void selectHandItem(int indx) {
    if (indx > 9) {
      return;
    }

    int tInd = indx-1;
    if (tInd < 0) { tInd = 9; }

    this.selectedHand = tInd;

    resetHolding();
    resetLuminance();
  }

  private void printInventory() {
    System.out.println("Player Inventory: "+this.inventory.toString());
  }

  public void printDebug() {
    System.out.println(this.toString()+ String.format(" [Luminance: %.2f; Health: %.2f]",
        this.luminance, this.health));
    printInventory();
    System.out.println();
  }

  public MapDirection getDirectionForCommand(char s) {
    switch (s) {
      case 'w':
        return MapDirection.UP;
      case 'a':
        return MapDirection.LEFT;
      case 's':
        return MapDirection.DOWN;
      case 'd':
        return MapDirection.RIGHT;
      default:
        return null;
    }
  }

  public boolean giveItem(GameItem item) {
    boolean result = inventory.addItem(item);

    resetHolding();
    resetLuminance();

    return result;
  }

  @Override
  public String toString() {
    return String.format("[PLAYER @ (%f, %f); Holding %s (%d)]", location.x(), location.y(),
        this.holding == null ? "nothing" : this.holding.getName(), selectedHand);
  }

  public boolean isHoldingItem(GameItem item) {
    return this.holding == item;
  }

  public boolean heldItemIsPlaceable() {
    return this.holding != null && this.holding.isPlaceable();
  }

  public GameItem getHolding() {
    return holding;
  }

  public Inventory getInventory() {
    return inventory;
  }

  private double getWeaponStrength() {
    if (this.holding == null) {
      return 1;
    } else {
      return this.holding.getAttackStrength();
    }
  }

  private void resetLuminance() {
    if (this.holding == null || this.holding.getLuminance() == 0) {
      this.luminance = 1f;
    } else {
      this.luminance = this.holding.getLuminance();
    }
  }
  private void resetHolding() {
    if (inventory.slotHasItem(selectedHand)) {
      this.holding = inventory.getStackInSlot(selectedHand).getItem();
    } else {
      this.holding = null;
    }
  }

  @Override
  public void attack(LivingEntity target) {
    if (this.getWeaponStrength() != 1) {
      if (this.attackCooldown == 0) {
        target.hurt(baseAttackStrength * this.getWeaponStrength());
        attackCooldown = 15;
      }
    } else {
      super.attack(target);
    }
  }

  public void move(MapDirection dir, GameWorld map) {
    super.move(dir, map, SPEED);
  }

  @Override
  public void configFromJson(JsonObject source) {
    this.inventory = new Inventory(source.get("inventory").getAsJsonObject());
    resetHolding();
    resetLuminance();
    super.configFromJson(source);
  }

  public static class PlayerEntityType implements EntityType<Player> {

    @Override
    public Player constructEntity(JsonObject source) {
      try {
        PinPoint p = new PinPoint(source.get("location").getAsString());
        Player player = new Player(p);

        player.configFromJson(source);
        return player;
      } catch (Exception e) {
        throw new IllegalArgumentException("Player JSON source had bad format: "+source.toString());
      }
    }

    @Override
    public Player spawnNew(PinPoint location) {
      return new Player(location);
    }

    @Override
    public String getID() {
      return "player";
    }

    @Override
    public EntityTag[] getTags() {
      return new EntityTag[]{EntityTag.PLAYER};
    }

    @Override
    public float getSpawnFrequency() {
      return 0;
    }
  }
}
