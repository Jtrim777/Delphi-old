package com.jtrimble.delphi.game;

import com.google.gson.JsonObject;
import com.jtrimble.delphi.entity.Player;
import com.jtrimble.delphi.event.EventManager;
import com.jtrimble.delphi.game.data.GameData;
import com.jtrimble.delphi.game.data.GameSaveData;
import com.jtrimble.delphi.gui.MenuManager;
import com.jtrimble.delphi.render.SceneImg;
import com.jtrimble.delphi.world.GameMap;
import com.jtrimble.delphi.world.GameWorld;
import com.jtrimble.delphi.registry.*;
import com.jtrimble.delphi.render.WorldRenderer;
import com.jtrimble.delphi.util.GameLogger;
import com.jtrimble.delphi.util.SaveFileManager;
import silverlib.game.Game;
import silverlib.game.GameUtils;
import silverlib.geo.PinPoint;
import silverlib.geo.Point;
import silverlib.img.Img;

import java.io.IOException;

public class Delphi extends Game {
  public static Delphi ACTIVE;
  public static GameLogger LOGGER;

  private GameWorld world;
  private GameState state;
  private String activeMap;
  private Player player;
  private MenuManager menuManager;
  private RegistryManager registryManager;
  private EventManager eventManager;
  private WorldRenderer renderer;
  private GameData gameData;

  public Delphi() throws IOException, IllegalAccessException {
    LOGGER = new GameLogger();
    LOGGER.supressDebug();

    this.eventManager = new EventManager("com.jtrimble.delphi");
    this.registryManager = new RegistryManager();
    registryManager.populateRegistries();

    this.menuManager = new MenuManager();

    if (SaveFileManager.gameDataExists()) {
      JsonObject gameData = SaveFileManager.loadGameData();
    } else {
      this.activeMap = "pregame";
      this.world = new GameWorld(new GameMap(activeMap), new PinPoint(-10, -10));
      this.state = GameState.PREGAME;
      this.player = world.getPlayer();

    }

    this.renderer = new WorldRenderer(this.world);
  }

  public void start() {
    this.activate(1100, 800, 1d/34d);
  }

  @Override
  public void onTick() {
    if (this.state.worldShouldTick()) {
      this.world.tick();
    }

    if (this.state.menusShouldTick()) {
      this.menuManager.tickAll();
    }
  }

  @Override
  public boolean worldShouldEnd() {
    return this.state == GameState.QUIT;
  }

  @Override
  public SceneImg render() {
    SceneImg base = new SceneImg(this.getEmptyScene());
    Img world = renderer.renderWorld();

    base.overlayImg(world);
    menuManager.renderAll(base);

    return base;
//    return renderer.renderWorld2();
  }

  @Override
  public void onKeyEvent(GameUtils.KEventType kEventType, String s) {
    if (this.state.inputToWorld()) {
      // TODO: 2020-05-03 Delegate key events to world
    }
    if (this.state.inputToMenus() && kEventType == GameUtils.KEventType.RELEASED) {
      this.menuManager.delegateKeyInput(s);
    }
  }

  @Override
  public void onMouseEvent(GameUtils.MEventType mEventType, GameUtils.MButton mButton, Point point) {
    if (this.state.inputToWorld()) {
      // TODO: 2020-05-03 Delegate mouse events to world
    }
    if (this.state.inputToMenus()) {
      if (mEventType == GameUtils.MEventType.CLICKED) {
        this.menuManager.delegateMouseClick(point, mButton);
      } else if (mEventType == GameUtils.MEventType.MOVED) {
        this.menuManager.delegateMouseMove(point);
      }
    }
  }

  public GameWorld getWorld() {
    return world;
  }

  public GameState getState() {
    return state;
  }

  public void setState(GameState state) {
    this.state = state;
  }

  public void quit() {

  }

  public void loadWorldFromSave(GameSaveData save) {

  }
}
