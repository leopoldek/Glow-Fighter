package com.slurpy.glowfighter.entities.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.ObjectSet;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.entities.Category;
import com.slurpy.glowfighter.entities.Entity;
import com.slurpy.glowfighter.entities.EntityDef;
import com.slurpy.glowfighter.entities.Team;
import com.slurpy.glowfighter.entities.traits.Damage;
import com.slurpy.glowfighter.entities.traits.Health;
import com.slurpy.glowfighter.parts.LinePart;
import com.slurpy.glowfighter.parts.Part;

public class Rocket extends Entity implements Damage{//TODO Make abstract class for all bullets later.
	
	private static final float DELTA = 1600f;
	
	private final float damage;
	private final float speed;
	private final Color postColor;
	private final float radius;
	
	private float acceleration = -400f;
	
	public Rocket(Vector2 pos, Vector2 vel, Color preColor, Color postColor, Team team, float damage, float radius) {
		super(getEntityDef(pos, vel.angleRad(), preColor, team));
		body.setLinearVelocity(vel.cpy().setLength(50f));
		this.damage = damage;
		this.speed = vel.len();
		this.postColor = postColor;
		this.radius = radius;
	}

	@Override
	public void update() {
		Vector2 vel = body.getLinearVelocity();
		float len = vel.len();
		if(acceleration > 0){
			colors[0] = postColor;
			if(len > speed){
				len = speed;
				return;
			}
		}
		acceleration += DELTA * Gdx.graphics.getDeltaTime();
		len += acceleration * Gdx.graphics.getDeltaTime();
		vel.setLength(len);
		body.setLinearVelocity(vel);
	}
	
	@Override
	public void hit(Entity other){
		delete();
		if(acceleration <= 0)return;
		ObjectSet<Entity> entities = Core.physics.getEntitiesInRadius(body.getPosition(), radius);
		for(Entity entity : entities){
			if(entity == this)continue;
			if(entity.team == Team.FRIENDLY)continue;
			entity.hit(this);
			if(entity instanceof Health)((Health)entity).takeDamage(damage);
		}
	}
	
	@Override
	public float getDamage() {
		if(acceleration <= 0){
			return 0;
		}else{
			return damage;
		}
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
	
	private static float height = 0.03f;
	private static float depth = -0.1f;
	private static float width = 0.2f;
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
		//entityDef.sensor = true;
		entityDef.bodyType = BodyType.DynamicBody;
	}
}