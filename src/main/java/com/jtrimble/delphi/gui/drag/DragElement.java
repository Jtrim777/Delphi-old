package com.jtrimble.delphi.gui.drag;

import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.render.SpriteProvider;

public class DragElement<T extends SpriteProvider> implements SpriteProvider {
  private T element;

  public DragElement(T element) {
    this.element = element;
  }

  public T getElement() {
    return element;
  }

  @Override
  public GameSprite getSprite() {
    return element.getSprite();
  }
}
