package com.jtrimble.delphi.particle;

import com.jtrimble.delphi.event.EventListener;
import com.jtrimble.delphi.event.RegistryEvent;
import com.jtrimble.delphi.registry.RegistryTargetGroup;
import com.jtrimble.delphi.util.MapVector;
import java.awt.Color;

@RegistryTargetGroup
public class ParticleTypes {
  public static final ParticleType SMOKE = new ParticleType() {
    private static final double TARGET_SPEED = 0.1d;
    private static final double TARGET_ANGLE = 3*Math.PI/2.0;

    @Override
    public int determineLifetime() {
      return (int)(Math.random()*85.0)+17;
    }

    @Override
    public double[] initParams() {
      return new double[]{(Math.random()*110)+20};
    }

    @Override
    public MapVector getNewDirection(Particle p) {
      double angDiff = p.dir.angle() - TARGET_ANGLE;

      double speedDiff = p.dir.magnitude() - TARGET_SPEED;

      double newAngle = p.dir.angle();
      double newSpeed = p.dir.magnitude();

      if (Math.abs(angDiff) > 0.06) {
        double adj = (0.05 * -(angDiff/Math.abs(angDiff)));
        newAngle += adj;
      } else {
        double adj = 0.02 * (Math.random()-0.5);;
        newAngle += adj;
      }

      if (Math.abs(speedDiff) > 0.1) {
        newSpeed += 0.08 * (speedDiff/Math.abs(speedDiff));
      }

      return new MapVector(newAngle, newSpeed, true);
    }

    @Override
    public float getTransparency(Particle p) {
      double cvar = Math.pow(0.9, p.lifetime-p.ticksExisted-50)/200;

      return (float)Math.max(1.0-cvar,0);
    }

    @Override
    public Color getColor(Particle p, float lightLevel) {
      int trueGray = (int)((float)p.params[0]*lightLevel);

      return new Color(trueGray,trueGray,trueGray,(int)(getTransparency(p)*255.0));
    }

    @Override
    public String getID() {
      return "smoke";
    }
  };

  public static final ParticleType SPELL = new ParticleType() {
    private static final double TARGET_SPEED = 0.05d;

    @Override
    public int determineLifetime() {
      return (int)(Math.random()*34)+10;
    }

    @Override
    public double[] initParams() {
      return new double[]{(Math.random()*55)+200, (Math.random()*50)+150};
    }

    @Override
    public MapVector getNewDirection(Particle p) {
      double speedDiff = p.dir.magnitude() - TARGET_SPEED;

      double newAngle = p.dir.angle();
      double newSpeed = p.dir.magnitude();

      double adj = 0.02 * (Math.random()-0.5);;
      newAngle += adj;

      if (Math.abs(speedDiff) > 0.05) {
        newSpeed += 0.025 * -(speedDiff/Math.abs(speedDiff));
      }

      return new MapVector(newAngle, newSpeed, true);
    }

    @Override
    public float getTransparency(Particle p) {

      return (float)p.ticksExisted/(float)p.lifetime;
    }

    @Override
    public Color getColor(Particle p, float lightLevel) {
      return new Color((int)p.params[0],150,(int)p.params[1],(int)(getTransparency(p)*255.0));
    }

    @Override
    public String getID() {
      return "spell";
    }
  };

  @EventListener
  public static void registerParticles(RegistryEvent.Register<ParticleType> event) {
    event.getRegistry().scanClass(ParticleTypes.class);
  }
}
