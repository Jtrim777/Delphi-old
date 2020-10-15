package com.jtrimble.delphi.gui;

import com.jtrimble.delphi.game.Delphi;
import com.jtrimble.delphi.game.GameState;
import com.jtrimble.delphi.game.data.GameData;
import com.jtrimble.delphi.gui.component.BoxElement;
import com.jtrimble.delphi.gui.component.MenuButton;
import com.jtrimble.delphi.gui.component.MenuElement;
import java.awt.Color;
import silverlib.geo.Point;

public class SelectSaveMenu extends GameMenu<GameData> {

  private BoxElement coverBox;

  public SelectSaveMenu(GameData context) {
    super(null, context, Type.WINDOW);

    /* Cover box:
    Y: 214
    X: 137 + (saves * 147)
    Height: 303
    Width: 733 - (saves * 147)

     */
    int savesCount = context.getSavesCount();
    int boxX = 137 + (savesCount * 147);
    int boxW = 733 - (savesCount * 147);

    coverBox = new BoxElement(boxX, 214, boxW, 303, new Color(100,100,100));
    this.addElement(coverBox);

    for (int i = 0; i < savesCount; i++) {
      int finalI = i;
      MenuElement nButton = new MenuButton(new Point(137 + (savesCount * 147), 214),
          137, 303, () -> playGame(finalI));
      this.addElement(nButton);
    }
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

  private void playGame(int index) {

  }

  private void openOptions() {

  }

  @Override
  public String getScreenID() {
    return "select_save";
  }
}
