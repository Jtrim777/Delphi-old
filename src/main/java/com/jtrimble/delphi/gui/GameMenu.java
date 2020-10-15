package com.jtrimble.delphi.gui;

import com.jtrimble.delphi.entity.Player;
import com.jtrimble.delphi.gui.component.MenuElement;
import com.jtrimble.delphi.gui.drag.DragDest;
import com.jtrimble.delphi.gui.drag.DragElement;
import com.jtrimble.delphi.gui.drag.DragSource;
import com.jtrimble.delphi.registry.ScreenManager;
import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.render.SceneImg;
import com.jtrimble.delphi.util.Hitbox;
import java.util.ArrayList;
import silverlib.game.GameUtils;
import silverlib.game.ISceneDrawable;
import silverlib.geo.Point;

public abstract class GameMenu<T> {

  protected MenuManager manager;
  protected int windowID;
  protected Player user;
  T context;
  T coContext;

  protected Type type;

  Point renderLocation;
  int width, height;
  Hitbox clickBox;
  protected boolean isDisplayed;

  private ArrayList<MenuElement> elements;
  private DragElement activeDrag;

  private int mx, my;

  GameMenu(Player user, T context, Type t) {
    this.user = user;
    this.context = context;

    this.type = t;

    this.isDisplayed = false;
    this.coContext = null;
    this.renderLocation = null;
    this.width = -1;
    this.height = -1;

    elements = new ArrayList<>();
    activeDrag = null;
    mx = 0;
    my = 0;
  }

  GameMenu(Player user, T context, T coContext, Type t) {
    this.user = user;
    this.context = context;
    this.coContext = coContext;

    this.type = t;

    this.isDisplayed = false;
    this.renderLocation = null;
    this.width = -1;
    this.height = -1;

    elements = new ArrayList<>();
    activeDrag = null;

    mx = 0;
    my = 0;
  }

  public void addElement(MenuElement e) {
    this.elements.add(e);
  }

  public void renderOn(SceneImg scene, int x, int y) {
    this.renderLocation = new Point(x, y);
    this.clickBox = new Hitbox(renderLocation, width, height);

    SceneImg base = ScreenManager.DEFAULT.getEntry(getScreenID());
    for (MenuElement elem : this.elements) {
      ISceneDrawable elemSprite = elem.getTexture();
      if (elemSprite != null) {
        elemSprite.drawOnScene(base, elem.renderX(), elem.renderY());
      }
    }

    if (activeDrag != null) {
      GameSprite dragSprite = activeDrag.getSprite();
      if (dragSprite != null) {
        base.placeSprite(dragSprite, mx, my);
      }
    }

    base.placeImage(base, x, y);
  }

  public void handleMouseClick(Point relPos, GameUtils.MButton button) {
    for (MenuElement elem : elements) {
      if (elem.pointInside(relPos)) {
        elem.handleClick(button);

        if (elem instanceof DragSource) {
          if (activeDrag == null) {
            handleDragTake((DragSource) elem);
          } else if (elem instanceof DragDest) {
            handleDragDropTake((DragSource & DragDest) elem);
          }
        } else if (elem instanceof DragDest && activeDrag != null) {
          handleDragDrop((DragDest) elem);
        }
      }
    }
  }

  private void handleDragTake(DragSource s) {
    if (activeDrag != null) {
      throw new IllegalStateException("Cannot take an item while already holding one");
    }

    DragElement dragger = s.getDraggableElement();
    if (dragger != null) {
      this.activeDrag = dragger;
    }
  }

  private void handleDragDrop(DragDest s) {
    if (activeDrag == null) {
      throw new IllegalStateException("Cannot drop an item when not holding one");
    }

    if (s.canDrop(this.activeDrag)) {
      s.dropOnto(activeDrag);
      this.activeDrag = null;
    }
  }

  private <T extends DragSource & DragDest> void handleDragDropTake(T s) {
    handleDragDrop(s);

    if (activeDrag == null) {
      handleDragTake(s);
    }
  }

  public void handleMouseMove(Point relPos) {
    this.mx = relPos.x();
    this.my = relPos.y();
  }

  public abstract boolean doesHaltGameplay();

  public abstract String getScreenID();

  public abstract void onDismiss();

  public void dismiss() {
    this.isDisplayed = false;
    this.onDismiss();

    manager.closeMenu(this);
  }

  public Point getRelativePosition(int mx, int my) {
    int tx = mx - renderLocation.x();
    int ty = my - renderLocation.y();

    return new Point(tx, ty);
  }

  public boolean pointWithinBounds(Point p) {
    if (width == -1 || renderLocation == null) {
      return false;
    }

    return this.clickBox.isInside(p);
  }

  public static String resolveResourceLocation(String[] args) {
    return "textures/menus/" + args[0] + ".png";
  }

  enum Type {
    HUD,
    WINDOW
  }
}
