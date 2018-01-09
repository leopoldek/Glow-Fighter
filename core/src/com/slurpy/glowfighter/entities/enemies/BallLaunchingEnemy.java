package com.slurpy.glowfighter.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Category;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.EntityDef;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.traits.Damage;
import com.slurpy.glowfighter.entities.traits.Health;
import com.slurpy.glowfighter.parts.PolygonPart;

public class BallLaunchingEnemy extends Entity implements Damage, Health{
	
	private float health;
	private float timer;

	public BallLaunchingEnemy(Vector2 pos) {
		super(getEntityDef(pos));
		health = 500;
	}

	@Override
	public void update() {
		body.setTransform(body.getPosition(), body.getAngle() + 0.1f * Gdx.graphics.getDeltaTime());
		
		Vector2 force = new Vector2(4f * Gdx.graphics.getDeltaTime(), 0).setAngle(Core.graphics.getCameraPosition().cpy().sub(body.getPosition()).angle());
		body.setLinearVelocity(body.getLinearVelocity().add(force));
		if(body.getLinearVelocity().len2() > 4)body.setLinearVelocity(body.getLinearVelocity().setLength2(4));
		
		if(timer > 1){
			Vector2 vel = new Vector2(6f, 0).rotateRad(body.getAngle());
			Core.entities.addEntity(new SpikeBall(new Vector2(body.getPosition()).add(vel.x / 2, vel.y  / 2), vel, 0.5f, 100f, 20f));
			vel.rotate(60f);
			Core.entities.addEntity(new SpikeBall(new Vector2(body.getPosition()).add(vel.x / 2, vel.y  / 2), vel, 0.5f, 100f, 20f));
			vel.rotate(60f);
			Core.entities.addEntity(new SpikeBall(new Vector2(body.getPosition()).add(vel.x / 2, vel.y  / 2), vel, 0.5f, 100f, 20f));
			vel.rotate(60f);
			Core.entities.addEntity(new SpikeBall(new Vector2(body.getPosition()).add(vel.x / 2, vel.y  / 2), vel, 0.5f, 100f, 20f));
			vel.rotate(60f);
			Core.entities.addEntity(new SpikeBall(new Vector2(body.getPosition()).add(vel.x / 2, vel.y  / 2), vel, 0.5f, 100f, 20f));
			vel.rotate(60f);
			Core.entities.addEntity(new SpikeBall(new Vector2(body.getPosition()).add(vel.x / 2, vel.y  / 2), vel, 0.5f, 100f, 20f));
			timer = 0;
		}else{
			timer += Gdx.graphics.getDeltaTime();
		}
	}

	@Override
	public void hit(Entity other) {
		
	}
	
	@Override
	public float getDamage() {
		return 40f;
	}
	
	@Override
	public void takeDamage(float dmg) {
		health -= dmg;
		if(health < 0){
			delete();
		}
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static EntityDef getEntityDef(Vector2 pos){
		entityDef.pos.set(pos);
		return entityDef;
	}
	
	static{
		Vector2 radiusVector = new Vector2(2f, 0);
		entityDef.polygon = new Vector2[]{
				radiusVector.cpy(),
				radiusVector.rotate(-60f).cpy(),
				radiusVector.rotate(-60f).cpy(),
				radiusVector.rotate(-60f).cpy(),
				radiusVector.rotate(-60f).cpy(),
				radiusVector.rotate(-60f).cpy()
		};
		entityDef.color = Color.ORANGE;
		entityDef.part = new PolygonPart(entityDef.polygon, 0.3f);
		entityDef.category = Category.ENTITY;
		entityDef.team = Team.ENEMY;
		entityDef.bullet = false;
		entityDef.setNoCollisionForce();
		entityDef.bodyType = BodyType.DynamicBody;
	}
}
