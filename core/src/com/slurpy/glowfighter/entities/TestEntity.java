package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.DeceptivePolygonPart;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;

public class TestEntity extends Entity{
	
	public TestEntity(Vector2 pos, float rot, Color color){
		super(pos, rot, createParts(color));
	}
	
	@Override
	public void update(){
		rot += Gdx.graphics.getDeltaTime() * 10;
	}
	
	private static Part[] createParts(Color color){
		return new Part[]{
				new PolygonPart(new Vector2[]{
						new Vector2(-100, -100),
						new Vector2(-100, 100),
						new Vector2(100, 100),
						new Vector2(100, -100)
				}, 5, color),
				new DeceptivePolygonPart(new Vector2[]{
						new Vector2(-50, -50),
						new Vector2(-50, 50),
						new Vector2(50, 50),
						new Vector2(50, -50)
				}, 5, color.cpy().set(1 - color.r, 1 - color.g, 1 - color.b, color.a), 12, 1.5f, 100, false)
		};
	}
}
