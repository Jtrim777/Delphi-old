package com.jtrimble.delphi.gui;

import com.jtrimble.delphi.game.Delphi;
import com.jtrimble.delphi.game.GameState;
import com.jtrimble.delphi.game.data.GameData;
import com.jtrimble.delphi.gui.component.MenuButton;
import silverlib.geo.Point;

public class PregameMenu extends GameMenu<GameData> {

  boolean gamesDoExist = true;

  public PregameMenu(GameData context) {
    super(null, context, Type.WINDOW);

    this.addElement(new MenuButton(new Point(143, 216), 725, 90, this::startNewGame));

    MenuButton resumeButton = new MenuButton(new Point(143, 340), 725, 90, this::resumeOldGame);
    this.addElement(resumeButton);

    if (context.getSavesCount() == 0) {
      resumeButton.disable();
      gamesDoExist = false;
    }

    this.addElement(new MenuButton(new Point(143, 475), 320, 90, this::openOptions));
    this.addElement(new MenuButton(new Point(545, 475), 320, 90, Delphi.ACTIVE::quit));
  }

  @Override
  public boolean doesHaltGameplay() {
    return true;
  }

  @Override
  public void onDismiss() {

  }

  private void startNewGame() {
    Delphi.ACTIVE.loadWorldFromSave(context.createSave());

    this.dismiss();

    Delphi.ACTIVE.setState(GameState.PLAYING);
  }

  private void resumeOldGame() {
    GameMenu<GameData> ssMenu = new SelectSaveMenu(this.context);

    manager.pushFrame(this.windowID, ssMenu);
  }

  private void openOptions() {
    GameMenu<GameData> goMenu = new GameOptionsMenu(this.context);

    manager.pushFrame(this.windowID, goMenu);
  }

  @Override
  public String getScreenID() {
    return "pregame" + (gamesDoExist ? "_ra" : "");
  }
}
