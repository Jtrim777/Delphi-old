package com.jtrimble.delphi.registry;

import static com.jtrimble.delphi.game.Delphi.LOGGER;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withModifier;

import com.jtrimble.delphi.util.GameLogger.LogLevel;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.reflections.ReflectionUtils;

public class GameRegistry<T extends RegistryEntry> {
  private HashMap<String, T> registry;
  private String name;
  private Class<T> memberType;

  public GameRegistry(String name, Class<T> memberType) {
    this.name = name;
    this.memberType = memberType;
    this.registry = new HashMap<>();
  }

  public String getName() {
    return name;
  }

  void addEntry(String id, T entry) {
    if (this.registry.containsKey(id)) {
      throw new IllegalArgumentException("The ID \""+id+"\" already exists in this registry");
    }

    LOGGER.log("Adding entry with ID \""+id+"\"", "Registry<"+name+">");

    int lll = LOGGER.getLogLevel();
    LOGGER.advanceLogLevel(1);
    entry.onRegister();
    LOGGER.returnLogLevel(lll);

    this.registry.put(id, entry);
  }

  public final void register(T entry) {
    this.addEntry(entry.getID(), entry);
  }

  @SafeVarargs
  public final void registerAll(T... entries) {
    for (T entry : entries) {
      this.addEntry(entry.getID(), entry);
    }
  }

  public final void scanClass(Class source) {
    LOGGER.log("Scanning class "+source.getName()+" for registry entries...",
        "Registry<"+getName()+">");

    if (source.isAnnotationPresent(RegistryTargetGroup.class)) {
      LOGGER.log("Class is RegistryTargetGroup; Grabbing fields...",
          "Registry<"+getName()+">");
      int lll = LOGGER.getLogLevel();
      LOGGER.advanceLogLevel(1);
      for (Field f : ReflectionUtils.getAllFields(source,
          withModifier(Modifier.PUBLIC), withModifier(Modifier.STATIC), withModifier(Modifier.FINAL))) {

        if (f.getType() == this.memberType) {
          try {
            this.register(this.memberType.cast(f.get(null)));
          } catch (IllegalAccessException e) {
            LOGGER.log("Unable to access value for field "+f.getName(),
                "Registry<"+getName()+">", LogLevel.WARNING);
            e.printStackTrace();
          }
        }
      }
      LOGGER.returnLogLevel(lll);
    } else {
      int lll = LOGGER.getLogLevel();
      LOGGER.advanceLogLevel(1);
      for (Field f : ReflectionUtils.getFields(source, withAnnotation(RegistryTarget.class),
          withModifier(Modifier.STATIC), withModifier(Modifier.FINAL))) {
        if (f.getType() == this.memberType) {
          try {
            String id = ((RegistryTarget)f.getAnnotation(RegistryTarget.class)).id();
            this.addEntry(id, this.memberType.cast(f.get(null)));
          } catch (IllegalAccessException e) {
            LOGGER.log("Unable to access value for field "+f.getName(),
                "Registry<"+getName()+">", LogLevel.WARNING);
            e.printStackTrace();
          }
        }
      }
      LOGGER.returnLogLevel(lll);
    }
  }

  public T getEntry(String id) {
    return this.registry.get(id);
  }

  public T getEntry(String group, String id) {
    return this.getEntry(group+":"+id);
  }

  public void forEach(Consumer<T> action) {
    BiConsumer<String, T> mod = (k, i) -> action.accept(i);

    registry.forEach(mod);
  }

  public void forEach(BiConsumer<String, T> action) {

    registry.forEach(action);
  }

  public Class<T> getMemberType() {
    return memberType;
  }
}
