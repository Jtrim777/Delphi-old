package com.jtrimble.delphi.game;

public enum GameState {
  PLAYING((byte)0b00010111),
  PAUSED((byte)0b00001010),
  DIED((byte)0b00001010),
  PREGAME((byte)0b00001010),
  QUIT((byte)0b00000000);

  /* Flag mask for determining state allowances
  Bit 7:
  Bit 6:
  Bit 5:
  Bit 4: Escape does pause game
  Bit 3: Input passed to menus
  Bit 2: Input passed to world
  Bit 1: Menus should tick
  Bit 0: World should tick
   */
  byte flags;

  GameState(byte flags) {
    this.flags = flags;
  }

  boolean worldShouldTick() {
    return flags%2 == 1;
  }
  boolean menusShouldTick() {
    return (flags >> 1)%2 == 1;
  }
  boolean inputToWorld() {
    return (flags >> 2)%2 == 1;
  }
  boolean inputToMenus() {
    return (flags >> 3)%2 == 1;
  }
  boolean escapePausesGame() {
    return (flags >> 4)%2 == 1;
  }
}
