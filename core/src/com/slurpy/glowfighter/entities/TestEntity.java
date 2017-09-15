package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class TestEntity extends Entity{
	
	public TestEntity(Vector2 pos, float rot){
		super(pos, rot, createParts());
	}
	
	@Override
	public void update(){
		rot += Gdx.graphics.getDeltaTime() * 10;
	}
	
	private static LinePart[] createParts(){
		return new LinePart[]{
			new LinePart(new Vector2(-100, 100), new Vector2(100, 100), 10, Color.GREEN),//top
			new LinePart(new Vector2(-100, -100), new Vector2(-100, 100), 10, Color.GREEN),//left
			new LinePart(new Vector2(100, -100), new Vector2(100, 100), 10, Color.GREEN),//right
			new LinePart(new Vector2(-100, -100), new Vector2(100, -100), 10, Color.GREEN)//bot
		};
	}
}
