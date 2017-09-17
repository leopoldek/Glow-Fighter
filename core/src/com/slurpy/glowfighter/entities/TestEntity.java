package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class TestEntity extends Entity{
	
	public TestEntity(Vector2 pos, float rot, Color color){
		super(pos, rot, createParts(color));
	}
	
	@Override
	public void update(){
		rot += Gdx.graphics.getDeltaTime() * 10;
	}
	
	private static LinePart[] createParts(Color color){
		return new LinePart[]{
				new LinePart(new Vector2(-100, 100), new Vector2(100, 100), 5, color),//top
				new LinePart(new Vector2(-100, -100), new Vector2(-100, 100), 5, color),//left
				new LinePart(new Vector2(100, -100), new Vector2(100, 100), 5, color),//right
				new LinePart(new Vector2(-100, -100), new Vector2(100, -100), 5, color)//bot
		};
	}
}
