package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.DeceptivePart;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;

public class TestEntity extends Entity{
	
	public TestEntity(Vector2 pos, float rot, Color color){
		super(pos, rot, color, createParts(), polygon);
	}
	
	@Override
	public void update(){
		//rot += Gdx.graphics.getDeltaTime() * 10;
	}
	
	private static Part[] createParts(){
		return new Part[]{
				new DeceptivePart(new PolygonPart(polygon, 5), 12, 1.5f, 100, true)
		};
	}
	
	private static Vector2[] polygon = new Vector2[]{
			new Vector2(-100, -100),
			new Vector2(-100, 100),
			new Vector2(100, 100),
			new Vector2(100, -100)
	};

	@Override
	public void hit(Entity other) {
		
	}
}
