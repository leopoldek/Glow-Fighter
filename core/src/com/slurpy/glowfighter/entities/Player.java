package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.utils.Action;

public class Player extends Entity {
	
	private static final float speed = 2000;
	private static final float maxSpeed = 1250;
	
	private Vector2 vel = new Vector2();

	public Player(Vector2 pos, float rot) {
		super(pos, rot, createParts());
		//Bind keys
	}

	@Override
	public void update() {
		rot = MathUtils.atan2(-(Gdx.input.getY() - Gdx.graphics.getHeight()/2), Gdx.input.getX() - Gdx.graphics.getWidth()/2) * MathUtils.radiansToDegrees;
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
			if(vel.len() > maxSpeed)vel.setLength(maxSpeed);
		}
		
		pos.add(vel.x * delta, vel.y * delta);
		Core.graphics.look(pos);
	}
	
	private static Part[] createParts(){
		return new Part[]{
				new TrailPolygonPart(new Vector2[]{new Vector2(30, 0), new Vector2(-30, -30), new Vector2(-30, 30)}, 6, Color.WHITE, 1.0f, 50),
				new LinePart(new Vector2(0, 0), new Vector2(10, 0), 4, Color.WHITE)
		};
	}
}
