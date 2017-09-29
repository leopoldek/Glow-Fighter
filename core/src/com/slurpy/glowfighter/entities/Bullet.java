package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.Core;

public class Bullet extends Entity {
	
	public Bullet(Vector2 pos, Vector2 vel, Color color) {
		super(createBodyDef(pos, vel.angleRad()), createParts(color));
		body.setLinearVelocity(vel);
		
	}
	
	public Bullet(BodyDef def, Part[] parts){
		super(def, parts);
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void hit(Entity other){
		Core.physics.queueDestroy(body);
	}
	
	private static Part[] createParts(Color color){
		return new Part[]{
				new LinePart(new Vector2(-0.1f, 0), new Vector2(0.05f, 0), 5, color)
		};
	}
	
	private static BodyDef createBodyDef(Vector2 pos, float rot){
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.active = true;
		def.position.set(pos);
		def.angle = rot;
		def.bullet = true;
		def.fixedRotation = true;
		return def;
	}
}
