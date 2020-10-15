package com.jtrimble.delphi.world;

import static com.jtrimble.delphi.game.Delphi.LOGGER;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jtrimble.delphi.registry.RegistryManager;
import com.jtrimble.delphi.render.SceneImg;
import com.jtrimble.delphi.tile.GameTile;
import com.jtrimble.delphi.tile.TileType;
import com.jtrimble.delphi.tile.WallTile;
import com.jtrimble.delphi.util.GameLogger.LogLevel;
import com.jtrimble.delphi.util.ResourceLocation;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import silverlib.err.ArgFormatException;
import silverlib.geo.PinPoint;

public class GameMap {
  private String resourceName;
  private String renderName;
  private int width, height;
  private ArrayList<WallTile> walls;
  private ArrayList<GameTile> tiles;

//  static GameLogger LOGGER = GameLogger.DEFAULT;
  public static final GameMap TEST = new GameMap();

  public GameMap(String name) throws IOException {

    this.resourceName = name;

    LOGGER.log("Loading Map \""+name+"\"...", "MapLoader");
    LOGGER.log("Base Directory: "+System.getProperty("user.dir"), "MapLoader", LogLevel.DEBUG);

    // Source files
    ResourceLocation metaFile = createRL("meta.yaml");
    ResourceLocation wallFile = createRL("walls.txt");
    ResourceLocation tileFile = createRL("decorations.json");

    // Load meta information
    String[] metaLines;
    try {
      metaLines = metaFile.loadAsText().split("\n");
    } catch (IOException e) {
      LOGGER.log("Unable to load metadata file for world "+name, 1, "MapLoader", LogLevel.ERROR);
      e.printStackTrace();
      throw new RuntimeException();
    }
    HashMap<String, String> metaMap = new HashMap<>();
    for (String line : metaLines) {
      if (!line.contains(":")) {
        continue;
      }

      String cleaned = line.replaceFirst("\\s?:\\s?", ":");
      String[] pts = cleaned.split(":");
      metaMap.put(pts[0], pts[1]);
    }

    this.renderName = metaMap.get("name");
    this.width = Integer.parseInt(metaMap.get("width"));
    this.height = Integer.parseInt(metaMap.get("height"));
    LOGGER.log("Loaded metadata", 1, "MapLoader");

    // Load walls
    this.walls = new ArrayList<>();

    String[] wallLines;
    try {
      wallLines = wallFile.loadAsText().split("\n");
    } catch (IOException e) {
      LOGGER.log("Unable to load wall vector file for world "+name, 1, "MapLoader", LogLevel.ERROR);
      throw new RuntimeException();
    }

    for (String wallDesc : wallLines) {
      if (wallDesc.length() >= 5) {
        try {
          walls.add(WallTile.fromDesc(wallDesc));
        } catch (ArgFormatException e) {
          System.out.println(e.getMessage());
        }
      }
    }

    LOGGER.log("Loaded wall vectors", 1, "MapLoader");

    // Load tiles
    this.tiles = new ArrayList<>();

    JsonArray tileObj;
    try {
      tileObj = tileFile.loadAsJsonArray();
    } catch (IOException e) {
      LOGGER.log("Unable to load decorations file for world "+name, 1, "MapLoader", LogLevel.ERROR);
      throw new RuntimeException();
    }

    for (JsonElement je : tileObj) {
      JsonObject jo = je.getAsJsonObject();

      TileType tt = RegistryManager.main.getRegistry(TileType.class)
          .getEntry(jo.get("id").getAsString());
      this.tiles.add(tt.constructTile(jo));
    }

    LOGGER.log("Loaded decorations", 1, "MapLoader");

    LOGGER.log("Complete!", 1, "MapLoader");
  }

  private GameMap() {
    this.resourceName = "##TEST##";
    this.renderName = "Test";
    this.width = 300;
    this.height = 300;

    this.walls = new ArrayList<>();
    this.tiles = new ArrayList<>();
  }

  private ResourceLocation createRL(String file) {
    return new ResourceLocation(GameMap.class, this.resourceName, file);
  }

  public static String resolveResourceLocation(String[] args) {
    return "maps/"+args[0]+"/"+args[1];
  }

  public SceneImg getTexture() {
    if (resourceName.equals("##TEST##")) {
      return new SceneImg(width, height, Color.WHITE);
    }

    try {
      ResourceLocation textureLoc = createRL("texture.png");
      return textureLoc.loadImage();
    } catch (IOException e) {
      LOGGER.log("Unable to load base texture for world "+getRenderName(), "MapLoader", LogLevel.ERROR);
      throw new RuntimeException();
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public ArrayList<GameTile> getTiles() {
    return tiles;
  }

  public ArrayList<WallTile> getWalls() {
    return walls;
  }

  public String getRenderName() {
    return renderName;
  }

  public boolean canMoveThroughPosition(PinPoint p) {
    for (WallTile wv : walls) {
      if (wv.pointIntersects(p)) {
        return false;
      }
    }

    for (GameTile gt : tiles) {
      if (gt.pointIntersects(p) && gt.doesBlockMovement()) {
        return false;
      }
    }

    return true;
  }

  public boolean intersectsWall(PinPoint p) {
    for (WallTile wv : walls) {
      if (wv.pointIntersects(p)) {
        return true;
      }
    }

    return false;
  }
}
