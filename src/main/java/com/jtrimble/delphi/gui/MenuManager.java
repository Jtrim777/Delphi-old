package com.jtrimble.delphi.gui;

import com.jtrimble.delphi.render.SceneImg;
import java.awt.Color;
import silverlib.game.GameUtils;
import silverlib.geo.Point;

import java.util.ArrayList;

public class MenuManager {
  public static final int WINDOW_PADDING = 40;

  private ArrayList<GameMenu<?>> menus;

  private int windowCount;

  public MenuManager() {
    menus = new ArrayList<>();
    windowCount = 0;
  }

  public boolean gameplayShouldHalt() {
    for (GameMenu<?> menu : menus) {
      if (menu.doesHaltGameplay()) {
        return true;
      }
    }

    return false;
  }

  public void openMenu(GameMenu<?> m) {
    menus.add(m);
    m.manager = this;

    if (m.type == GameMenu.Type.WINDOW) {
      windowCount ++;
      m.windowID = windowCount;
    }
  }

  public void closeMenu(GameMenu<?> menu) {
    menu.dismiss();
    menus.removeIf((mx) -> mx == menu);
  }

  public GameMenu<?> getMenuWithID(int windowID) {
    GameMenu<?> relevantMenu = null;
    for (GameMenu<?> m : menus) {
      if (m.windowID == windowID) {
        relevantMenu = m;
      }
    }

    if (relevantMenu == null) {
      throw new IllegalArgumentException("No such window with ID "+windowID);
    }

    return relevantMenu;
  }

  public <C> void pushFrame(int windowID, GameMenu<C> frame) {
    GameMenu<C> relevantMenu = (GameMenu<C>)getMenuWithID(windowID);

    if (relevantMenu instanceof MenuNavigationStack) {
      ((MenuNavigationStack<C>) relevantMenu).pushFrame(frame);
    } else {
      menus.removeIf((menu) -> menu.windowID == windowID);
      MenuNavigationStack<C> stack = new MenuNavigationStack<>(relevantMenu);
      stack.pushFrame(frame);
      menus.add(stack);
    }
  }

  public <C> void popFrame(int windowID) {
    GameMenu<C> relevantMenu = (GameMenu<C>)getMenuWithID(windowID);

    if (relevantMenu instanceof MenuNavigationStack) {
      ((MenuNavigationStack<C>) relevantMenu).popFrame();
    } else {
      throw new IllegalStateException("Cannot pop root menu frame");
    }
  }

  public void dismissWindows() {
    menus.removeIf((menu) -> menu.type == GameMenu.Type.WINDOW);
    windowCount = 0;
  }

  public void renderAll(SceneImg scene) {
    int xc = scene.width()/2;
    int yc = scene.height()/2;

    int seenWindows = 0;
    for (GameMenu menu : menus) {
      if (menu.type == GameMenu.Type.WINDOW) {
        seenWindows++;
        Point drawLoc;

        if (windowCount == 1) {
          drawLoc = new Point(xc, yc);
        } else if (seenWindows == 1) {
          drawLoc = new Point(xc - ((menu.width/2)+WINDOW_PADDING), yc);
        } else {
          drawLoc = new Point(xc + ((menu.width/2)+WINDOW_PADDING), yc);
        }

//        System.out.println("Rendering window menu at "+drawLoc.toString());

        menu.renderOn(scene, drawLoc.x(), drawLoc.y());
      } else {
        menu.renderOn(scene, menu.renderLocation.x(), menu.renderLocation.y());
      }
    }
  }

  public SceneImg renderAll() {
    SceneImg base = new SceneImg(1100, 800, new Color(0,0,0,0));

    this.renderAll(base);

    return base;
  }

  public void delegateMouseClick(Point mouse, GameUtils.MButton mButton) {
    System.out.println("Delegating "+mButton.name()+" click @ "+mouse.toString());

    for (GameMenu<?> menu : menus) {
      if (menu.type == GameMenu.Type.WINDOW && menu.pointWithinBounds(mouse)) {
        menu.handleMouseClick(menu.getRelativePosition(mouse.x(), mouse.y()), mButton);
      }
    }
  }

  public void delegateMouseMove(Point mouse) {
    for (GameMenu<?> menu : menus) {
      if (menu.type == GameMenu.Type.WINDOW && menu.pointWithinBounds(mouse)) {
        menu.handleMouseMove(menu.getRelativePosition(mouse.x(), mouse.y()));
      }
    }
  }

  // TODO: Handle key input
  public void delegateKeyInput(String key) {

  }

  // TODO: 2020-05-03 Call update methods on relevant menus
  public void tickAll() {

  }
}
