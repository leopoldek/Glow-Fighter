package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;
import com.slurpy.glowfighter.utils.Action;

public class Player extends Entity {

	public Player(Vector2 pos, float rot) {
		super(pos, rot, createParts());
		//Bind keys
	}

	@Override
	public void update() {
		rot = MathUtils.atan2(-(Gdx.input.getY() - Gdx.graphics.getHeight()/2), Gdx.input.getX() - Gdx.graphics.getWidth()/2) * MathUtils.radiansToDegrees;
		
		Vector2 move = new Vector2();
		if(Core.bindings.isActionPressed(Action.moveUp))
			move.add(0, 400);
		if(Core.bindings.isActionPressed(Action.moveLeft))
			move.add(-400, 0);
		if(Core.bindings.isActionPressed(Action.moveDown))
			move.add(0, -400);
		if(Core.bindings.isActionPressed(Action.moveRight))
			move.add(400, 0);
		if(Core.bindings.isActionPressed(Action.moveSlow))move.scl(0.4f);
		pos.add(move.scl(Gdx.graphics.getDeltaTime()));
		
		Core.graphics.look(pos);
	}
	
	private static Part[] createParts(){
		return new Part[]{
				new PolygonPart(new Vector2[]{new Vector2(60, 0), new Vector2(-60, -60), new Vector2(-60, 60)}, 6, Color.WHITE),
				new LinePart(new Vector2(0, 0), new Vector2(10, 0), 4, Color.WHITE)
		};
	}
}
