package com.jtrimble.delphi.world;

import com.jtrimble.delphi.util.Hitbox;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;

public interface WorldObject {
  PinPoint getPosition();

  Hitbox getHitbox();
}
