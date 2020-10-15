package com.jtrimble.delphi.game.data;

import java.util.ArrayList;
import java.util.List;

public class GameData {
  private List<GameSaveData> saves;
  private GameOptions options;

  public GameData() {
    this.saves = new ArrayList<>();
    this.options = new GameOptions();
  }

  public int getSavesCount() {
    return saves.size();
  }

  public GameSaveData createSave() {
    if (this.saves.size() < 5) {
      GameSaveData newSave = new GameSaveData();
      this.saves.add(newSave);

      return newSave;
    } else {
      throw new IllegalStateException("Saves are already at capacity");
    }
  }
}
