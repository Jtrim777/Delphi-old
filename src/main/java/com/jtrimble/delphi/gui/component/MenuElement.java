package com.jtrimble.delphi.gui.component;

import com.jtrimble.delphi.render.SpriteProvider;
import silverlib.game.GameUtils.MButton;
import silverlib.geo.Point;

public interface MenuElement extends SpriteProvider {
  boolean pointInside(Point p);

  void handleClick(MButton button);

  int renderX();
  int renderY();
}
