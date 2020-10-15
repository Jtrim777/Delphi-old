package com.jtrimble.delphi.render;

public interface WorldSpriteProvider extends SpriteProvider {
  int spriteX();
  int spriteY();

  float getLuminance();
  float getAppliedLight();
  void illuminate(float power);
  void resetLighting();
  default boolean ignoresLighting() {
    return false;
  }
}
