package com.jtrimble.delphi.registry;

import com.jtrimble.delphi.event.EventListener;
import com.jtrimble.delphi.event.RegistryEvent;
import com.jtrimble.delphi.render.SceneImg;
import com.jtrimble.delphi.util.GameLogger;
import com.jtrimble.delphi.util.ResourceLocation;
import java.io.IOException;

public class ScreenManager extends GameRegistry<SceneImg> {

  public static ScreenManager DEFAULT;

  public ScreenManager() {
    super("screens", SceneImg.class);

    ScreenManager.DEFAULT = this;

    ResourceLoader<SceneImg> resourceLoader =
        new ResourceLoader<>(ResourceLocation.RESOURCE_ROOT + "/screens", ".png",
            SceneImg::loadFile, true);

    GameLogger.DEFAULT.log("Loading Screens:", "ScreenManager");

    int ll = GameLogger.DEFAULT.getLogLevel();
    GameLogger.DEFAULT.advanceLogLevel(1);
    resourceLoader.loadResources().forEach(this::addEntry);
    GameLogger.DEFAULT.returnLogLevel(ll);
  }

  public void addEntry(String id, ResourceLocation entry) throws IOException {
    super.addEntry(id, (SceneImg) entry.loadImage());
  }

  public void addEntry(String id, SceneImg entry) {
    GameLogger.DEFAULT.log("Loaded screen with ID "+id, "ScreenManager");

    super.addEntry(id, entry);
  }

  @EventListener
  public static void createScreenManager(RegistryEvent.CreateRegistries event) {
    event.create(new ScreenManager());
  }
}
