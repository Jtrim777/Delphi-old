package com.jtrimble.delphi.gui;

import com.jtrimble.delphi.entity.Player;
import com.jtrimble.delphi.gui.component.InventorySlot;
import com.jtrimble.delphi.item.Inventory;
import silverlib.geo.Point;

public class InventoryMenu extends GameMenu<Inventory> {

  public InventoryMenu(Player user, Inventory context, String name) {
    super(user, context, Type.WINDOW);

    init(name);
  }

  public InventoryMenu(Player user, Inventory context, Inventory coContext, String name) {
    super(user, context, coContext, Type.WINDOW);

    init(name);
  }

  private void init(String name) {
    this.height = 200;
    this.width = (int) Math.ceil((double) context.getSize() / 5) * InventorySlot.SIZE;

    for (int i=0; i<context.getSize(); i++) {
      int tx = ((i/5)*43) + 7;
      int ty = ((i%5)*43) + 20;

      this.addElement(new InventorySlot(new Point(tx, ty), this.context, i));
    }

    System.out.println("Created menu [" + name + "] with size (" + width + ", " + height + ")");
  }

  @Override
  public boolean doesHaltGameplay() {
    return true;
  }

  @Override
  public String getScreenID() {
    int cols = (int) Math.ceil((double) context.getSize() / 5);
    return "inventory" + cols + "x5";
  }

  @Override
  public void onDismiss() {

  }

  //  @EventListener
//  public static void loadScreens(RegistryEvent.Register<SceneImg> event) {
//    for (int i = 2; i <= 4; i++) {
//      ResourceLocation modelSource = new ResourceLocation(null, "screens", "menus",
//          "inventory" + i + "x5.png");
//      try {
//        SceneImg screen = modelSource.loadImage().setID("inventory" + i + "x5");
//
//        event.getRegistry().register(screen);
//      } catch (IOException e) {
//        LOGGER.log("Unable to load texture for screen [inventory" + i + "x5]", "ScreenManager",
//            LogLevel.ERROR);
//        LOGGER.log("Couldn't read file " + modelSource.getAbsolutePath(), "ScreenManager",
//            LogLevel.ERROR);
//      }
//    }
//  }
}
