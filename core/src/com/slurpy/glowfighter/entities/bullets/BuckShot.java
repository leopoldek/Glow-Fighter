package com.slurpy.glowfighter.entities.bullets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.entities.Category;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.EntityDef;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.traits.Damage;
import com.slurpy.glowfighter.parts.CirclePart;

public class BuckShot extends Entity implements Damage{
	
	private final float damage;
	
	public BuckShot(Vector2 pos, Vector2 vel, Color color, Team team, float damage) {
		super(getEntityDef(pos, vel.angleRad(), color, team));
		body.setLinearVelocity(vel);
		this.damage = damage;
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void hit(Entity other){
		delete();
	}
	
	@Override
	public float getDamage() {
		return damage;
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static EntityDef getEntityDef(Vector2 pos, float rot, Color color, Team team){
		entityDef.pos.set(pos);
		entityDef.rot = rot;
		entityDef.color = color;
		entityDef.team = team;
		return entityDef;
	}
	
	private static float size = 0.1f;
	private static Vector2[] polygon = new Vector2[]{
			new Vector2(size, size),
			new Vector2(size, -size),
			new Vector2(-size, -size),
			new Vector2(-size, size)
	};
	
	static{
		entityDef.polygon = polygon;
		entityDef.part = new CirclePart(new Vector2(), size);
		entityDef.category = Category.BULLET;
		entityDef.bullet = true;
		//entityDef.sensor = true;
		entityDef.bodyType = BodyType.DynamicBody;
	}
}
