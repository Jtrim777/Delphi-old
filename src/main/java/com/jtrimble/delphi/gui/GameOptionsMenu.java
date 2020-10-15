package com.jtrimble.delphi.gui;

import com.jtrimble.delphi.game.Delphi;
import com.jtrimble.delphi.game.GameState;
import com.jtrimble.delphi.game.data.GameData;

public class GameOptionsMenu extends GameMenu<GameData> {

  public GameOptionsMenu(GameData context) {
    super(null, context, Type.WINDOW);
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

  }

  private void openOptions() {

  }

  @Override
  public String getScreenID() {
    return "select_save";
  }
}
