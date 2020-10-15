package com.jtrimble.delphi.registry;

import static com.jtrimble.delphi.game.Delphi.LOGGER;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jtrimble.delphi.util.GameLogger.LogLevel;
import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.util.ResourceLocation;
import java.io.FileNotFoundException;
import java.io.IOException;
import silverlib.geo.Point;
import silverlib.geo.Rect;

public interface TexturedRegistryEntry extends RegistryEntry {
  String getGroupID();

  @Override
  default void onRegister() {
    final String logLocale = "RegistryEntry<" + getGroupID() + ":" + getID() + ">";
    LOGGER.log("Registering textures...", logLocale);
    try {
      ResourceLocation modelSource = new ResourceLocation(null, "models", getGroupID(), getID()+".json");
      JsonArray modelNames = modelSource.loadAsJsonArray();

      for (JsonElement je : modelNames) {
        String model = je.getAsString();

        try {
          TextureManager.DEFAULT.addEntry(getGroupID(), getID()+"_"+model,
              new ResourceLocation(null, "textures", getGroupID(), getID(), model+".png"));
        } catch (IOException ey) {
          LOGGER.log("Unable to load texture for model \""+model+"\"", logLocale, LogLevel.WARNING);
          GameSprite placeholder = new GameSprite();
          placeholder.addShape(new Rect(new Point(0,0), 20), 0, 0);

          TextureManager.DEFAULT.addEntry(getGroupID(), getID()+"_"+model, placeholder);
        }
      }

    } catch (FileNotFoundException e) {
      try {
        TextureManager.DEFAULT.addEntry(getGroupID(), getID(),
            new ResourceLocation(null, "textures", getGroupID(), getID()+".png"));
      } catch (IOException ex) {
        LOGGER.log("Unable to load texture for entry", logLocale, LogLevel.WARNING);
        GameSprite placeholder = new GameSprite();
        placeholder.addShape(new Rect(new Point(0,0), 20), 0, 0);

        TextureManager.DEFAULT.addEntry(getGroupID(), getID(), placeholder);
      }
    }
  }
}
