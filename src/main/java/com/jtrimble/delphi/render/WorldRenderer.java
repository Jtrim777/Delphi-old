package com.jtrimble.delphi.render;

import com.jtrimble.delphi.world.GameWorld;
import java.util.ArrayList;
import silverlib.img.Img;

public class WorldRenderer {
  GameWorld world;
  SceneImg base;
  LightEngine lightEngine;

  public WorldRenderer(GameWorld world) {
    this.world = world;
    this.base = world.getMap().getTexture();
  }

  // TODO: 2020-05-03 Always render entities, only update tiles if they've changed. Apply lighting
  public Img renderWorld() {
    SceneImg canvas = base.copy();

    drawCollection(canvas, world.getTiles());
    drawCollection(canvas, world.getEntities());
    drawCollection(canvas, world.getParticles());

    return canvas;
  }

  public SceneImg renderWorld2() {
    return base.copy();
  }

  private void drawCollection(SceneImg canvas, ArrayList<? extends WorldSpriteProvider> objs) {
    for (WorldSpriteProvider obj : objs) {
      canvas.placeSprite(obj.getSprite(), obj.spriteX(), obj.spriteY());
    }
  }
}
