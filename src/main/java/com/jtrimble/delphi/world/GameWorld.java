package com.jtrimble.delphi.world;

import com.jtrimble.delphi.entity.GameEntity;
import com.jtrimble.delphi.entity.Player;
import com.jtrimble.delphi.particle.Particle;
import com.jtrimble.delphi.tile.GameTile;
import java.util.ArrayList;
import silverlib.geo.PinPoint;

public class GameWorld {
  private GameMap sourceMap;

  private ArrayList<GameEntity> entities;
  private ArrayList<GameTile> tiles;
  private ArrayList<Particle> particles;

  private Player player;

  public GameWorld(GameMap map, Player player) {
    this.sourceMap = map;

    entities = new ArrayList<>();
    entities.add(player);

    this.tiles = map.getTiles();
    this.particles = new ArrayList<>();

    this.player = player;
  }

  public GameWorld(GameMap map, PinPoint playerPosition) {
    this.sourceMap = map;

    this.player = new Player(playerPosition);

    entities = new ArrayList<>();
    entities.add(player);

    this.tiles = map.getTiles();
    this.particles = new ArrayList<>();
  }

  public void tick() {

  }

  public GameMap getMap() {
    return sourceMap;
  }

  public boolean canMoveThroughPosition(PinPoint p) {
    return false;
  }

  public ArrayList<GameEntity> getEntities() {
    return entities;
  }

  public void addParticle(Particle p) {
    this.particles.add(p);
  }

  public int getMapWidth() {
    return this.sourceMap.getWidth();
  }
  public int getMapHeight() {
    return this.sourceMap.getHeight();
  }

  public ArrayList<GameTile> getTiles() {
    return tiles;
  }

  public ArrayList<Particle> getParticles() {
    return particles;
  }

  public Player getPlayer() {
    return player;
  }

  public float getTransparencyAtPoint(PinPoint p) {
    if (sourceMap.intersectsWall(p)) {
      return 0;
    }

    GameTile tap = getTileAtPosition(p);
    if (tap != null) {
      return tap.getTransparency();
    }

    return 1;
  }

  public GameTile getTileAtPosition(PinPoint p) {
    for (GameTile tile : this.tiles) {
      if (tile.pointIntersects(p)) {
        return tile;
      }
    }

    return null;
  }
}
