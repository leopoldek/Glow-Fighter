package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.parts.LinePart;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;
import com.slurpy.glowfighter.parts.TrailPart;
import com.slurpy.glowfighter.utils.Action;

public class Player extends Entity {
	
	private static final float speed = 60;
	private static final float maxSpeed = 20;

	public Player(Vector2 pos, float rot) {
		super(pos, rot, Color.WHITE.cpy(), createParts(), polygon);
	}

	@Override
	public void update() {
		body.setTransform(body.getPosition(), MathUtils.atan2(-(Gdx.input.getY() - Gdx.graphics.getHeight()/2), Gdx.input.getX() - Gdx.graphics.getWidth()/2));
		
		
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
		
		body.setLinearVelocity(move);
		
		if(Core.bindings.isActionPressed(Action.primary)){
			//Core.entities.addEntity(new Bullet(pos.cpy(), new Vector2(2000, 0).rotate(rot + MathUtils.random(-10, 10))));
		}
	}
	
	@Override
	public void hit(Entity other) {
		
	}
	
	private static Part[] createParts(){
		return new Part[]{
				new TrailPart(new PolygonPart(polygon, 5f), 1f, 0.5f),
				new LinePart(new Vector2(0, 0), new Vector2(1f, 0), 0.2f, Color.WHITE)
		};
	}
	
	private static Vector2[] polygon = new Vector2[]{new Vector2(0.5f, 0), new Vector2(-0.5f, -0.5f), new Vector2(-0.5f, 0.5f)};
}
