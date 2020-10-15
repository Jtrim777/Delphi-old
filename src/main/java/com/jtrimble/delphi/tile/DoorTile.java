package com.jtrimble.delphi.tile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jtrimble.delphi.entity.Player;
import com.jtrimble.delphi.item.GameItems;
import com.jtrimble.delphi.registry.TextureManager;
import com.jtrimble.delphi.tile.util.TileBounds;
import com.jtrimble.delphi.util.Hitbox;
import com.jtrimble.delphi.util.ResourceLocation;
import com.jtrimble.delphi.render.GameSprite;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;
import silverlib.geo.Rect;

import java.io.IOException;

public class DoorTile extends GameTile {
  private static final double DOOR_WIDTH_FACTOR = 0.7;

  private boolean isOpen;
  private boolean isUnlocked;
  private Orientation orientation;

  public DoorTile(int x, int y) {
    super(x, y, 20*DOOR_WIDTH_FACTOR, 20);

    this.isOpen = false;
    this.isUnlocked = true;
    this.orientation = Orientation.VERTICAL;
  }

  @Override
  public float getTransparency() {
    return isOpen ? 0.95f : 0.1f;
  }

  @Override
  public boolean doesBlockMovement() {
    return !isOpen;
  }

  @Override
  public boolean canInteractWith() {
    return true;
  }

  @Override
  public void interactWith(Player actor) {
    if (actor.isHoldingItem(GameItems.KEY) && !this.isUnlocked) {
      this.isUnlocked = true;
      actor.removeItem(GameItems.KEY, 1);
    } else if (this.isUnlocked) {
      this.isOpen = !this.isOpen;
    }
  }

  public void setOrientation(Orientation orientation) {
    this.orientation = orientation;

    this.setBounds(new Hitbox(this.center,
        20 * (orientation == Orientation.VERTICAL ? DOOR_WIDTH_FACTOR : 1),
        20 * (orientation == Orientation.VERTICAL ? 1 : DOOR_WIDTH_FACTOR)));
  }

  @Override
  public GameSprite getSprite() {
    String textureID = String.format("%s_%s%s_%s",
        this.getRegistryName(),
        this.isOpen ? "open" : "closed",
        this.isUnlocked ? "" : "-lkd",
        this.orientation == Orientation.VERTICAL ? "vert" : "hz");

    return TextureManager.DEFAULT.getEntry("tiles", textureID);
  }

  public void setOpen(boolean open) {
    isOpen = open;
  }

  @Override
  public String getRegistryName() {
    return "door";
  }

  public enum Orientation {
    HORIZONTAL,
    VERTICAL
  }

  public static class DoorTileType implements TileType<DoorTile> {
    @Override
    public DoorTile constructTile(JsonObject source) {
      try {
        Point p = new Point(source.get("location").getAsString());

        DoorTile tile = new DoorTile(p.x(), p.y());

        if (source.has("locked")) {
          tile.isUnlocked = !source.get("locked").getAsBoolean();
        }
        if (source.has("orientation")) {
          String orient = source.get("orientation").getAsString();
          if (orient.equals("horizontal")) {
            tile.orientation = Orientation.HORIZONTAL;
          }
        }

        return tile;

      } catch (Exception e) {
        throw new IllegalArgumentException("The format of the supplied Door constructor was " +
            "faulty: "+source.toString());
      }
    }

    @Override
    public DoorTile createTile(PinPoint p) {
      return new DoorTile((int)p.x(), (int)p.y());
    }

    @Override
    public String getID() {
      return "door";
    }
  }
}
