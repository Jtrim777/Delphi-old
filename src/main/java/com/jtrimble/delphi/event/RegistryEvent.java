package com.jtrimble.delphi.event;

import com.jtrimble.delphi.registry.GameRegistry;
import com.jtrimble.delphi.registry.RegistryEntry;
import com.jtrimble.delphi.registry.RegistryManager;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class RegistryEvent implements GameEvent {

  @Override
  public String getID() {
    return "registry";
  }

  @SingletonEvent
  public static class CreateRegistries extends RegistryEvent {

    @Override
    public String getID() {
      return super.getID() + ".create_registries";
    }

    public void create(GameRegistry registry) {
      RegistryManager.main.addRegistry(registry);
    }
  }

  public static class Register<T extends RegistryEntry> extends RegistryEvent {
    GameRegistry<T> relevantRegistry;

    public Register(GameRegistry<T> registry) {
      relevantRegistry = registry;
    }

    @Override
    public String getID() {
      return super.getID() + ".register["+relevantRegistry.getName()+"]";
    }

    public GameRegistry<T> getRegistry() {
      return relevantRegistry;
    }

    @Override
    public boolean listenerTypeMatches(Type t) {
      if (!(t instanceof ParameterizedType)) {
        return false;
      }

      return ((ParameterizedType)t).getActualTypeArguments()[0] == relevantRegistry.getMemberType();
    }
  }
}
