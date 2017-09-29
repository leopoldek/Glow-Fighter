package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.utils.Action;

public class Player extends Entity {
	
	private static final float speed = 60;
	private static final float maxSpeed = 20;

	public Player(Vector2 pos, float rot) {
		super(createBodyDef(pos, rot), createParts());
		
		PolygonShape shape = new PolygonShape();
		shape.set(polygon);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		//fixtureDef.density = 0.001f;
		body.createFixture(fixtureDef);
		
		shape.dispose();
	}

	@Override
	public void update() {
		body.setTransform(body.getPosition(), MathUtils.atan2(-(Gdx.input.getY() - Gdx.graphics.getHeight()/2), Gdx.input.getX() - Gdx.graphics.getWidth()/2));
		
		//if(Core.bindings.isActionPressed(Action.moveSlow))move.scl(0.4f);
		if(Core.bindings.isActionPressed(Action.moveUp)){
			body.applyForceToCenter(0, speed, true);
		}
		if(Core.bindings.isActionPressed(Action.moveDown)){
			body.applyForceToCenter(0, -speed, true);
		}
		if(Core.bindings.isActionPressed(Action.moveLeft)){
			body.applyForceToCenter(-speed, 0, true);
		}
		if(Core.bindings.isActionPressed(Action.moveRight)){
			body.applyForceToCenter(speed, 0, true);
		}
		
		if(Core.bindings.isActionPressed(Action.primary)){
			//Core.entities.addEntity(new Bullet(pos.cpy(), new Vector2(2000, 0).rotate(rot + MathUtils.random(-10, 10))));
		}
	}
	
	public void postUpdate(){
		Core.graphics.look(body.getPosition());
		Vector2 vel = body.getLinearVelocity();
		body.setLinearVelocity(vel.len2() <= maxSpeed * maxSpeed ? vel : vel.clamp(0, maxSpeed));
	}
	
	private static Part[] createParts(){
		return new Part[]{
				new TrailPolygonPart(polygon, 0.2f, Color.WHITE, 1f, 0.5f)
				//new LinePart(new Vector2(0, 0), new Vector2(1f, 0), 0.2f, Color.WHITE)
		};
	}
	
	private static BodyDef createBodyDef(Vector2 pos, float rot){
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.active = true;
		def.position.set(pos);
		def.angle = rot;
		def.fixedRotation = true;
		def.linearDamping = 2f;
		return def;
	}
	
	private static Vector2[] polygon = new Vector2[]{new Vector2(0.5f, 0), new Vector2(-0.5f, -0.5f), new Vector2(-0.5f, 0.5f)};
}
