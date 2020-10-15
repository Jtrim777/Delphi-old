package com.jtrimble.delphi.render;

import silverlib.game.ISceneDrawable;

public interface SpriteProvider {
  GameSprite getSprite();
  default ISceneDrawable getTexture() {
    return getSprite();
  }
}
