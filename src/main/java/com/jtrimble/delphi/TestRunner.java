package com.jtrimble.delphi;

import com.jtrimble.delphi.game.Delphi;
import java.io.IOException;

public class TestRunner {

  public static void main(String[] args) throws IOException, IllegalAccessException {
    Delphi game = new Delphi();

    game.start();
  }
}
