package com.jtrimble.delphi.event;

import java.lang.reflect.Type;

public interface GameEvent {
  String getID();

  default boolean listenerTypeMatches(Type t) {
    return this.getClass() == t;
  }
}
