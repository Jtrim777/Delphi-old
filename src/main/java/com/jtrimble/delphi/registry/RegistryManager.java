package com.jtrimble.delphi.registry;

import static com.jtrimble.delphi.game.Delphi.LOGGER;

import com.jtrimble.delphi.event.EventManager;
import com.jtrimble.delphi.event.GameEvent;
import com.jtrimble.delphi.event.RegistryEvent;
import java.util.HashMap;

public class RegistryManager {
  public static RegistryManager main;

  public static final String ENTITIES = "entities";
  public static final String TILES = "tiles";
  public static final String ITEMS = "items";
  public static final String TEXTURES = "textures";
  public static final String SCREENS = "screens";

  private HashMap<String, GameRegistry> registries;

  public RegistryManager() {
    registries = new HashMap<String, GameRegistry>();

    RegistryManager.main = this;
    EventManager.main.fire(RegistryEvent.CreateRegistries.class);
  }

  public void addRegistry(GameRegistry reg) {
    LOGGER.log("Created registry ["+reg.getName()+"] for member type "+reg.getMemberType().getName(),
        "RegistryManager");

    this.registries.put(reg.getName(), reg);
  }

  public void populateRegistries() {
    LOGGER.log("Populating registries...", "RegistryManager");
    LOGGER.advanceLogLevel(1);
    for (GameRegistry registry : registries.values()) {
      GameEvent registrationEvent = new RegistryEvent.Register<>(registry);

      LOGGER.log("Registering entries for registry ["+registry.getName()+"]...", "RegistryManager");
      int lll = LOGGER.getLogLevel();
      LOGGER.advanceLogLevel(1);
      EventManager.main.fire(registrationEvent);
      LOGGER.returnLogLevel(lll);
    }
    LOGGER.resetLogLevel();
  }

  public GameRegistry getRegistry(String key) {
    GameRegistry result = registries.get(key);

    if (result != null) {
      return result;
    } else {
      throw new NoSuchRegistryException("No registry exists with key "+key);
    }
  }

  public <U extends RegistryEntry> GameRegistry<U> getRegistry(Class<U> memberType) {

    for (GameRegistry reg : registries.values()) {
      if (reg.getMemberType() == memberType) {
        return reg;
      }
    }

    throw new NoSuchRegistryException("No registry exists with member type "+memberType.getName());
  }
}
