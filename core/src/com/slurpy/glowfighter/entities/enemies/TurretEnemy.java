package com.slurpy.glowfighter.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Category;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.EntityDef;
import com.slurpy.glowfighter.entities.Player;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.bullets.LaserShot;
import com.slurpy.glowfighter.entities.traits.Damage;
import com.slurpy.glowfighter.entities.traits.Health;
import com.slurpy.glowfighter.parts.PolygonPart;

public class TurretEnemy extends Entity implements Damage, Health{
	
	private static final float cooldown = 0.20f;
	
	private float accumulator = cooldown;
	
	private float health;
	
	private final Player player;
	private float seekAngle;
	private Vector2 seek;

	public TurretEnemy(Vector2 pos, Player player) {
		super(getEntityDef(pos));
		health = 1000;
		this.player = player;
	}

	@Override
	public void update(){
		seekAngle += 0.55f * Gdx.graphics.getDeltaTime();
		seek = new Vector2(15f, 0).rotateRad(seekAngle);
		seek.add(player.getPosition());
		
		Vector2 velocity = new Vector2(seek);
		velocity.sub(body.getPosition());
		velocity.limit(16f);
		body.setLinearVelocity(velocity);
		body.setTransform(body.getPosition(), velocity.angleRad());
		
		
		accumulator += Gdx.graphics.getDeltaTime();
		if(player.getPosition().dst(body.getPosition()) <= 16f){
			while(accumulator >= cooldown){
				float rot = player.getPosition().sub(body.getPosition()).angleRad();
				Core.entities.addEntity(new LaserShot(body.getPosition(), new Vector2(50, 0).rotateRad(rot), Color.MAGENTA, Team.ENEMY, 5f));
				//Core.audio.playSound(SoundAsset.Shoot, 0.2f);
				accumulator -= cooldown;
			}
		}else{
			if(accumulator > cooldown)accumulator = cooldown;
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
		entityDef.polygon = new Vector2[]{
				new Vector2(1f, 0f),
				new Vector2(0.5f, -0.5f),
				new Vector2(-0.5f, -0.5f),
				new Vector2(-1f, 0f),
				new Vector2(-0.5f, 0.5f),
				new Vector2(0.5f, 0.5f),
		};
		entityDef.part = new PolygonPart(entityDef.polygon, 0.2f);
		entityDef.color = Color.ORANGE;
		entityDef.category = Category.ENTITY;
		entityDef.team = Team.ENEMY;
		entityDef.bullet = false;
		entityDef.setNoCollisionForce();
		entityDef.bodyType = BodyType.DynamicBody;
	}
}
