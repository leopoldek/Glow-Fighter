package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.LinePart;
import com.slurpy.glowfighter.parts.Part;

public class Bullet extends Entity {//TODO Make abstract class for all bullets later.
	
	public Bullet(Vector2 pos, Vector2 vel, Color color, Team team) {
		super(getEntityDef(pos, vel.angleRad(), color, team));
		body.setLinearVelocity(vel);
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void hit(Entity other){
		delete();
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static EntityDef getEntityDef(Vector2 pos, float rot, Color color, Team team){
		entityDef.pos.set(pos);
		entityDef.rot = rot;
		entityDef.parts = new Part[]{new LinePart(new Vector2(depth * 10, 0), new Vector2(height, 0), width)};
		entityDef.setColor(color);
		entityDef.team = team;
		return entityDef;
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
	
	static{
		entityDef.polygon = polygon;
		entityDef.category = Category.BULLET;
		entityDef.bullet = true;
	}
}
