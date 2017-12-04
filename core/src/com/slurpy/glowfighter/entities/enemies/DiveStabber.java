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
import com.slurpy.glowfighter.entities.traits.Damage;
import com.slurpy.glowfighter.entities.traits.Health;
import com.slurpy.glowfighter.managers.AssetManager.SoundAsset;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;
import com.slurpy.glowfighter.parts.TrailPart;

public class DiveStabber extends Entity implements Damage, Health{
	
	private static final float delay = 1.3f;
	private static final float radius = 14f;
	private static final Color standby = Color.NAVY;
	private static final Color active = Color.RED;
	
	private float health;
	
	private boolean count = false;
	private float countdown = delay;
	
	private boolean diving = false;
	
	private final Player player;

	public DiveStabber(Vector2 pos, Player player) {
		super(getEntityDef(pos));
		this.player = player;
		health = 160;
	}

	@Override
	public void update() {
		if(diving)return;
		float angle = player.getPosition().sub(body.getPosition()).angleRad();
		body.setTransform(body.getPosition(), angle);
		if(player.getPosition().dst(body.getPosition()) < radius){
			if(!count){
				count = true;
				countdown = delay;
			}
		}else{
			count = false;
			colors[0].set(standby);
		}
		if(count){
			countdown -= Gdx.graphics.getDeltaTime();
			colors[0].set(active).lerp(standby, countdown / delay);
			if(countdown <= 0){
				body.setLinearVelocity(new Vector2(25f, 0).rotateRad(angle));
				Core.audio.playSound2D(SoundAsset.Activated, body.getPosition(), radius + 2);
				colors[0].set(active);
				diving = true;
			}
		}
	}

	@Override
	public void hit(Entity other) {
		if(other instanceof Player || other.category == Category.WALL)delete();
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
		entityDef.parts = new Part[]{
				new TrailPart(new PolygonPart(entityDef.polygon, 0.15f), 0.3f, 1.2f)
		};
		entityDef.setColor(standby.cpy());
		return entityDef;
	}
	
	static{
		entityDef.polygon = new Vector2[]{
				new Vector2(1f, 0),
				new Vector2(0, -0.4f),
				new Vector2(-1f, 0),
				new Vector2(0, 0.4f)
		};
		entityDef.category = Category.ENTITY;
		entityDef.team = Team.ENEMY;
		entityDef.bullet = false;
		entityDef.setNoCollisionForce();
		entityDef.bodyType = BodyType.DynamicBody;
	}
}
