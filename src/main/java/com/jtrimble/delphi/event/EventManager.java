package com.jtrimble.delphi.event;

import static org.reflections.ReflectionUtils.withModifier;
import static com.jtrimble.delphi.game.Delphi.LOGGER;

import com.jtrimble.delphi.util.GameLogger.LogLevel;
import com.jtrimble.delphi.util.GameUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class EventManager {
  public static EventManager main;

  private ArrayList<Method> listeners;

  public EventManager(String mainPackage) {
    this.listeners = new ArrayList<>();

    Reflections mainReflector = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage(mainPackage))
        .setScanners(new MethodAnnotationsScanner()));

    this.listeners.addAll(ReflectionUtils.getAll(
        mainReflector.getMethodsAnnotatedWith(EventListener.class),
        withModifier(Modifier.PUBLIC), withModifier(Modifier.STATIC)));

    EventManager.main = this;
  }

  public void fire(Class<? extends GameEvent> eventType) {
    if (!eventType.isAnnotationPresent(SingletonEvent.class)) {
      throw new IllegalArgumentException("Only SingletonEvents may be fired by their type");
    }

    GameEvent eventInstance;
    try {
      eventInstance = eventType.getConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      throw new RuntimeException("An unexpected error occurred when firing event of type "+eventType.getName());
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
      throw new RuntimeException("GameEvent "+eventType.getName()+" does not provide a default constructor");
    }

    ArrayList<Method> relevantListeners = GameUtil.filterList(listeners,
        lst -> eventInstance.listenerTypeMatches(getListenerType(lst)));

    LOGGER.log("Firing event "+eventInstance.getID()+"...", "EventManager");

    for (Method listener : relevantListeners) {
      try {
        LOGGER.log("Found listener "+listener.getName(), 1,"EventManager");
        int lll = LOGGER.getLogLevel();
        LOGGER.advanceLogLevel(2);
        listener.invoke(null, eventInstance);
        LOGGER.returnLogLevel(lll);
        LOGGER.log("Fired", 2,"EventManager");
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        throw new RuntimeException("An unexpected error occurred when firing event of type "
            +eventType.getName()+": Method "+listener.getName()+" had invalid access");
      } catch (InvocationTargetException e) {
        LOGGER.log("Listener did not follow the proper format, must accept an event instance"
            + " and no other arguments", 2, "EventManager", LogLevel.ERROR);
        e.printStackTrace();
      }
    }
  }

  public void fire(GameEvent event) {
    ArrayList<Method> relevantListeners = GameUtil.filterList(listeners,
        lst -> event.listenerTypeMatches(getListenerType(lst)));

    LOGGER.log("Firing event "+event.getID()+"...", "EventManager");

    for (Method listener : relevantListeners) {
      try {
        LOGGER.log("Found listener "+listener.getName(), 1,"EventManager");
//        LOGGER.log("Method sig: "+listener.);
        int lll = LOGGER.getLogLevel();
        LOGGER.advanceLogLevel(2);
        listener.invoke(null, event);
        LOGGER.returnLogLevel(lll);
        LOGGER.log("Fired", 2,"EventManager");
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        throw new RuntimeException("An unexpected error occurred when firing event of type "
            +event.getID()+": Method "+listener.getName()+" had invalid access");
      } catch (InvocationTargetException e) {
        LOGGER.log("Listener did not follow the proper format, must accept an event instance"
            + " and no other arguments", 2, "EventManager", LogLevel.ERROR);
        e.printStackTrace();
      }
    }
  }

  public void registerPackage(String packageName) {
    Reflections reflector = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forPackage(packageName))
        .setScanners(new MethodAnnotationsScanner()));

    this.listeners.addAll(ReflectionUtils.getAll(
        reflector.getMethodsAnnotatedWith(EventListener.class),
        withModifier(Modifier.PUBLIC), withModifier(Modifier.STATIC)));

    LOGGER.log("Package "+packageName+" was registered to receive events", "EventManager");
  }

  private static Type getListenerType(Method m) {
    Type rez = m.getGenericParameterTypes()[0];
//    LOGGER.log(rez.toString(), "EventManager", LogLevel.DEBUG);
    return rez;
  }
}
