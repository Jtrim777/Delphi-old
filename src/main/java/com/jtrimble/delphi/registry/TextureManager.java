package com.jtrimble.delphi.registry;

import com.jtrimble.delphi.event.EventListener;
import com.jtrimble.delphi.event.RegistryEvent;
import com.jtrimble.delphi.render.GameSprite;
import com.jtrimble.delphi.util.ResourceLocation;

import java.io.IOException;

public class TextureManager extends GameRegistry<GameSprite> {
  public static TextureManager DEFAULT;

  /*
  Doc?
   */
  public TextureManager() {
    super("textures", GameSprite.class);

    TextureManager.DEFAULT = this;
  }

  public GameSprite getEntry(String group, String item) {
    return super.getEntry(group+":"+item);
  }

  public void addEntry(String group, String id, GameSprite entry) {
    super.addEntry(group+":"+id, entry);
  }

  public void addEntry(String group, String id, ResourceLocation entry) throws IOException {
    super.addEntry(group+":"+id, entry.loadSprite());
  }

  @EventListener
  public static void createTextureManager(RegistryEvent.CreateRegistries event) {
    event.create(new TextureManager());
  }
}
