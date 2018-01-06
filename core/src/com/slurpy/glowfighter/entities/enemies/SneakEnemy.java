package com.slurpy.glowfighter.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Category;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.EntityDef;
import com.slurpy.glowfighter.entities.Player;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.traits.Damage;
import com.slurpy.glowfighter.entities.traits.KnockbackMultiplier;
import com.slurpy.glowfighter.managers.AssetManager.EffectAsset;
import com.slurpy.glowfighter.parts.DeceptivePart;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;
import com.slurpy.glowfighter.parts.PulsatingPart;

public class SneakEnemy extends Entity implements Damage, KnockbackMultiplier{
	
	private static final float SPEED = 10f;
	
	private final Player player;
	
	public SneakEnemy(Vector2 pos, float rot, Player player) {
		super(getEntityDef(pos, rot));
		body.setLinearVelocity(SPEED, 0);
		this.player = player;
	}
	
	@Override
	public void update() {
		Vector2 playerPos = player.getPosition();
		float angle = playerPos.sub(getPosition()).angleRad();
		angle -= body.getAngle();
		if(angle > MathUtils.PI)angle -= MathUtils.PI2;
		float clamp = 2f * Gdx.graphics.getDeltaTime();
		angle = MathUtils.clamp(angle, -clamp, clamp);
		angle += body.getAngle();
		body.setLinearVelocity(MathUtils.cos(angle) * SPEED, MathUtils.sin(angle) * SPEED);
		body.setTransform(body.getPosition(), angle);
	}
	
	@Override
	public void hit(Entity other) {
		Core.graphics.drawParticle(EffectAsset.EnemyMissleDeath, body.getPosition());
		delete();
	}
	
	@Override
	public float getDamage() {
		return 15f;
	}
	
	@Override
	public float getMultiplier() {
		return 0.5f;
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static float size = 0.1f;
	private static Vector2[] polygon = new Vector2[]{new Vector2(size, 0), new Vector2(-size, -size), new Vector2(-size, size)};
	
	private static EntityDef getEntityDef(Vector2 pos, float rot){
		entityDef.pos.set(pos);
		entityDef.rot = rot;
		entityDef.parts = new Part[]{
				new DeceptivePart(new PulsatingPart(new PolygonPart(polygon, 0.04f), 0.75f, 0.2f, 1f), 6f, 3f, 1f, false)
		};
		entityDef.setColor(Color.MAGENTA.cpy());
		return entityDef;
	}
	
	static{
		entityDef.polygon = polygon;
		entityDef.category = Category.ENTITY;
		entityDef.team = Team.ENEMY;
		entityDef.bullet = false;
		entityDef.bodyType = BodyType.DynamicBody;
	}
}
