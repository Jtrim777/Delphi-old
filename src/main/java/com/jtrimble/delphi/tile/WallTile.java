package com.jtrimble.delphi.tile;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.entity.Player;
import com.jtrimble.delphi.item.GameItems;
import com.jtrimble.delphi.registry.TextureManager;
import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.tile.util.TileBounds;
import com.jtrimble.delphi.util.Hitbox;
import silverlib.err.ArgFormatException;
import silverlib.err.PointFormatException;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;

public class WallTile extends GameTile {

  public WallTile(int x, int y, int sx, int sy) {
    super(x,y,0,0);

    if (sx != x) {
      int w = Math.abs(sx - x);
      int h = 15;
      int tx = Math.min(x, sx) + (w/2);
      this.center = new PinPoint(tx, y);
      this.setBounds(new Hitbox(this.center, w,h));
    } else {
      int w = 15;
      int h = Math.abs(sy - y);
      int ty = Math.min(y, sy) + (h/2);
      this.center = new PinPoint(x, ty);
      this.setBounds(new Hitbox(this.center, w,h));
    }
  }

  public static WallTile fromDesc(String desc) throws ArgFormatException {
    if (!desc.contains("->")) {
      throw new ArgFormatException(desc, "(x1,y1)->(x2,y2)");
    }

    try {
      String[] parts = desc.split("->");
      Point a = new Point(parts[0]);
      Point b = new Point(parts[1]);

      return new WallTile(a.x(), a.y(),b.x(), b.y());
    } catch (PointFormatException e) {
      throw new ArgFormatException(desc, "(x1,y1)->(x2,y2)");
    }
  }

  @Override
  public float getTransparency() {
    return 0;
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
  public GameSprite getSprite() {
    return null;
  }

  @Override
  public boolean shouldDraw() {
    return false;
  }

  @Override
  public String getRegistryName() {
    return "wall";
  }

  public static class WallTileType implements TileType<WallTile> {
    @Override
    public WallTile constructTile(JsonObject source) {
      try {
        Point p = new Point(source.get("start").getAsString());
        Point p2 = new Point(source.get("end").getAsString());

        WallTile tile = new WallTile(p.x(), p.y(), p2.x(), p2.y());

        return tile;

      } catch (Exception e) {
        throw new IllegalArgumentException("The format of the supplied Wall constructor was " +
            "faulty: "+source.toString());
      }
    }

    @Override
    public WallTile createTile(PinPoint p) {
      throw new IllegalArgumentException("Cannot construct a wall from a single location");
    }

    @Override
    public String getID() {
      return "wall";
    }

    @Override
    public void onRegister() {
      return;
    }
  }
}
