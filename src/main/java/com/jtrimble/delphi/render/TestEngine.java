package com.jtrimble.delphi.render;

import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.world.GameMap;
import com.jtrimble.delphi.tile.GameTiles;
import java.awt.Color;
import java.io.IOException;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;

public class TestEngine {

  public static void main(String[] args) throws IOException {
    GameWorld world = new GameWorld(GameMap.TEST, new PinPoint(5,5));

    LightEngine engine = new LightEngine(world);

    world.getTiles().add(GameTiles.DOOR.createTile(new PinPoint(160, 135)));
//    ((DoorTile)world.getTiles().get(0)).setOpen(true);

    engine.addLightSource(new Point(150,150), 10);

    SceneImg base = new SceneImg(300, 300, Color.WHITE);

    engine.applyLighting(base);

    base.save("test");
  }

}
