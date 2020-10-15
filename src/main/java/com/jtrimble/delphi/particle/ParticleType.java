package com.jtrimble.delphi.particle;

import com.jtrimble.delphi.registry.RegistryEntry;
import com.jtrimble.delphi.util.MapVector;

import java.awt.Color;

public interface ParticleType extends RegistryEntry {
  int determineLifetime();
  double[] initParams();
  MapVector getNewDirection(Particle p);
  float getTransparency(Particle p);
  Color getColor(Particle p, float lightLevel);

  @Override
  default void onRegister() {}
}
