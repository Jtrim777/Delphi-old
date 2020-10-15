package com.jtrimble.delphi.gui.drag;

public interface DragDest {
  void dropOnto(DragElement elem);
  boolean canDrop(DragElement elem);
}
