package com.jtrimble.delphi.render;

import com.jtrimble.delphi.registry.RegistryEntry;
import java.io.IOException;

public class GameSprite extends silverlib.game.GameSprite implements RegistryEntry {

  public GameSprite() {
    super();
  }

  public GameSprite(String filepath) throws IOException {
    super(filepath);
  }

  @Override
  public String getID() {
    return null;
  }

  @Override
  public void onRegister() {

  }
}
