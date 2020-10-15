package com.jtrimble.delphi.gui;

import com.jtrimble.delphi.render.SceneImg;
import java.util.ArrayList;
import java.util.List;
import silverlib.game.GameUtils.MButton;
import silverlib.geo.Point;

public class MenuNavigationStack<Context> extends GameMenu<Context> {

  private GameMenu<Context> activeMenu;
  private List<GameMenu<Context>> stack;

  public MenuNavigationStack(GameMenu<Context> root) {
    super(root.user, root.context, root.type);

    this.activeMenu = root;
    this.stack = new ArrayList<>();
    stack.add(root);

    this.windowID = root.windowID;
    this.manager = root.manager;

    setPropertiesFromActive();
  }

  private void setPropertiesFromActive() {
    this.renderLocation = activeMenu.renderLocation;
    this.width = activeMenu.width;
    this.height = activeMenu.height;
    this.clickBox = activeMenu.clickBox;
  }

  @Override
  public boolean doesHaltGameplay() {
    return activeMenu.doesHaltGameplay();
  }

  @Override
  public String getScreenID() {
    return activeMenu.getScreenID();
  }

  @Override
  public void onDismiss() {
    activeMenu.onDismiss();
  }

  public void pushFrame(GameMenu<Context> frame) {
    this.activeMenu.isDisplayed = false;

    this.stack.add(frame);
    this.activeMenu = frame;

    this.activeMenu.windowID = this.windowID;
    this.activeMenu.isDisplayed = true;
    setPropertiesFromActive();
  }

  public void popFrame() {
    if (this.stack.size() == 1) {
      throw new IllegalStateException("Cannot pop root menu!");
    }

    this.activeMenu.dismiss();

    this.stack.remove(this.stack.size() - 1);
    this.activeMenu = this.stack.get(this.stack.size() - 1);

    this.activeMenu.isDisplayed = true;
    setPropertiesFromActive();
  }

  @Override
  public void renderOn(SceneImg scene, int x, int y) {
    activeMenu.renderOn(scene, x, y);
  }

  @Override
  public void handleMouseClick(Point relPos, MButton button) {
    activeMenu.handleMouseClick(relPos, button);
  }

  @Override
  public void handleMouseMove(Point relPos) {
    activeMenu.handleMouseMove(relPos);
  }

  @Override
  public void dismiss() {
    activeMenu.dismiss();
  }

  @Override
  public boolean pointWithinBounds(Point p) {
    return activeMenu.pointWithinBounds(p);
  }
}
