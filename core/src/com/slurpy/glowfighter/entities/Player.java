package com.slurpy.glowfighter.entities;

import static com.badlogic.gdx.math.MathUtils.atan2;
import static com.badlogic.gdx.math.MathUtils.cos;
import static com.badlogic.gdx.math.MathUtils.sin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.parts.LinePart;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;
import com.slurpy.glowfighter.parts.TrailPart;
import com.slurpy.glowfighter.utils.Action;

public class Player extends Entity {
	
	private static final float speed = 60;
	private static final float maxSpeed = 15;

	public Player(Vector2 pos, float rot) {
		super(getEntityDef(pos, rot));
	}

	@Override
	public void update() {
		body.setTransform(body.getPosition(), atan2(-(Gdx.input.getY() - Gdx.graphics.getHeight()/2), Gdx.input.getX() - Gdx.graphics.getWidth()/2));
		float delta = Gdx.graphics.getDeltaTime();
		
		Vector2 move = new Vector2();
		if(Core.bindings.isActionPressed(Action.moveUp)){
			move.add(0, speed);
		}
		if(Core.bindings.isActionPressed(Action.moveDown)){
			move.add(0, -speed);
		}
		if(Core.bindings.isActionPressed(Action.moveLeft)){
			move.add(-speed, 0);
		}
		if(Core.bindings.isActionPressed(Action.moveRight)){
			move.add(speed, 0);
		}
		
		if(Core.bindings.isActionPressed(Action.moveSlow))move.scl(0.4f);
		
		Vector2 vel = body.getLinearVelocity();
		if(move.isZero()){
			if(vel.len() < speed * delta){
				vel.setZero();
			}else{
				move.set(speed, 0);
				move.setAngle(vel.angle() + 180);
				vel.add(move.scl(delta));
			}
		}else{
			vel.add(move.scl(delta));
			if(vel.len2() > maxSpeed * maxSpeed)vel.setLength(maxSpeed);
		}
		body.setLinearVelocity(vel);
		
		if(Core.bindings.isActionPressed(Action.primary)){
			float angle = body.getAngle();
			Core.entities.addEntity(new Bullet(body.getPosition().cpy().add(cos(angle) * size * 2, sin(angle) * size * 2),
					new Vector2(100, 0).rotateRad(angle + MathUtils.random(-0.1f, 0.1f)), Color.GOLD, Team.FRIENDLY));
		}
	}
	
	@Override
	public void hit(Entity other){
		System.out.println("Hit entity!");
	}
	
	private static EntityDef entityDef = new EntityDef();
	
	private static EntityDef getEntityDef(Vector2 pos, float rot){
		entityDef.pos.set(pos);
		entityDef.rot = rot;
		entityDef.parts = new Part[]{
				new TrailPart(new PolygonPart(polygon, 0.1f), 1f, 0.65f),
				new LinePart(new Vector2(0, 0), new Vector2(-0.3f, 0), 0.1f)
		};
		return entityDef;
	}
	
	private static float size = 0.4f;
	private static Vector2[] polygon = new Vector2[]{new Vector2(size, 0), new Vector2(-size, -size), new Vector2(-size, size)};
	
	static{
		entityDef.polygon = polygon;
		entityDef.parts = new Part[2];
		entityDef.category = Category.ENTITY;
		entityDef.team = Team.FRIENDLY;
		entityDef.bullet = true;
		entityDef.setColor(Color.WHITE);
		entityDef.bodyType = BodyType.DynamicBody;
	}
}
