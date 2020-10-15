package com.jtrimble.delphi.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.render.SceneImg;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import silverlib.io.IO;

public class ResourceLocation {
  public static String RESOURCE_ROOT = "src/main/resources/com/jtrimble/delphi";

  private String[] pathComponents;
  private Class resourceType;

  public ResourceLocation(Class type, String... args) {
    this.resourceType = type;
    this.pathComponents = args;

    if (resourceType != null) {
      try {
        Method resolver = this.resourceType.getMethod("resolveResourceLocation", String[].class);
      } catch (NoSuchMethodException e) {
        throw new IllegalArgumentException("The `type` Class must support the static method " +
            "resolveResourceLocation(String[] args)");
      }
    }
  }

  public String getAbsolutePath() {
    if (resourceType == null) {
      String out = "";
      for (String pt : this.pathComponents) {
        out += pt + "/";
      }
      out = out.substring(0, out.length()-1);
      return RESOURCE_ROOT + "/" + out;
    }

    try {
      Method resolver = this.resourceType.getMethod("resolveResourceLocation", String[].class);

      try{
        return RESOURCE_ROOT + "/" + (String)resolver.invoke(null, (Object) this.pathComponents);
      } catch (IllegalArgumentException e) {
        System.out.println(resolver.toString());
        throw e;
      }
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      throw new IllegalStateException("Somehow, this was initialized with the wrong class");
    }
  }

  public FileReader createReader() throws FileNotFoundException {
     return new FileReader(this.getAbsolutePath());
  }

  public JsonObject loadAsJson() throws FileNotFoundException {
    return new Gson().fromJson(this.createReader(), JsonObject.class);
  }

  public JsonArray loadAsJsonArray() throws FileNotFoundException {
    return new Gson().fromJson(this.createReader(), JsonArray.class);
  }

  public String loadAsText() throws IOException {
    String apath = this.getAbsolutePath();
//    LOGGER.log(apath, "ResourceLocation", LogLevel.DEBUG);

    return IO.readFile(apath);
  }

  public SceneImg loadImage() throws IOException {
    return new SceneImg(this.getAbsolutePath());
  }

  public GameSprite loadSprite() throws IOException {
    return new GameSprite(this.getAbsolutePath());
  }

  public static void setResourceRoot(String root) {
    ResourceLocation.RESOURCE_ROOT = root;
  }
}
