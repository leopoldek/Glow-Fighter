package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.LinePart;
import com.slurpy.glowfighter.parts.Part;

public class Bullet extends Entity {//TODO Make abstract class for all bullets later.
	
	public Bullet(Vector2 pos, Vector2 vel, Color color) {
		super(pos, vel.angleRad(), color, createParts(), polygon);
		body.setLinearVelocity(vel);
		body.setBullet(true);
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void hit(Entity other){
		delete();
	}
	
	private static float height = 0.05f;
	private static float depth = -0.1f;
	private static float width = 0.1f;
	private static Vector2[] polygon = new Vector2[]{
			new Vector2(height, width),
			new Vector2(height, -width),
			new Vector2(depth, -width),
			new Vector2(depth, width)
	};
	private static Part[] createParts(){
		return new Part[]{
				new LinePart(new Vector2(depth * 10, 0), new Vector2(height, 0), width)
		};
	}
}
