package com.slurpy.glowfighter.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.entities.Category;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.EntityDef;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.traits.Damage;
import com.slurpy.glowfighter.entities.traits.Health;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;

public class SpikeBall extends Entity implements Damage, Health{
	
	private float health;
	private final float damage;

	public SpikeBall(Vector2 pos, Vector2 vel, float radius, float health, float damage) {
		super(getEntityDef(pos, radius));
		body.setLinearVelocity(vel);
		this.health = health;
		this.damage = damage;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void hit(Entity other) {
		if(other.team != Team.FRIENDLY)delete();
	}
	
	@Override
	public float getDamage() {
		return damage;
	}
	
	@Override
	public void takeDamage(float dmg) {
		health -= dmg;
		if(health < 0){
			delete();
		}
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static EntityDef getEntityDef(Vector2 pos, float radius){
		entityDef.pos.set(pos);
		Vector2 radiusVector = new Vector2(radius, 0);
		entityDef.polygon = new Vector2[]{
				radiusVector.cpy(),
				radiusVector.rotate(-45f).cpy(),
				radiusVector.rotate(-45f).cpy(),
				radiusVector.rotate(-45f).cpy(),
				radiusVector.rotate(-45f).cpy(),
				radiusVector.rotate(-45f).cpy(),
				radiusVector.rotate(-45f).cpy(),
				radiusVector.rotate(-45f).cpy()
		};
		entityDef.parts = new Part[]{
				new PolygonPart(entityDef.polygon, 0.1f)
		};
		entityDef.setColor(Color.ORANGE.cpy());
		return entityDef;
	}
	
	static{
		entityDef.category = Category.ENTITY;
		entityDef.team = Team.ENEMY;
		entityDef.bullet = false;
		entityDef.setNoCollisionForce();
		entityDef.bodyType = BodyType.DynamicBody;
	}
}
