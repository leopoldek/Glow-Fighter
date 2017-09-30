package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.parts.Part;
import com.slurpy.glowfighter.parts.PolygonPart;

public class Wall extends Entity {
	
	public Wall(Vector2 pos, Vector2 size, float rot, Color color) {
		super(pos, rot, color, createParts(size), createPolygon(size));
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void hit(Entity other) {
		
	}
	
	private static Part[] createParts(Vector2 size){
		return new Part[]{
				new PolygonPart(createPolygon(size), 0.5f)
		};
	}
	
	private static Vector2[] createPolygon(Vector2 size){
		float x = size.x / 2;
		float y = size.y / 2;
		return new Vector2[]{
				new Vector2(x, y), 
				new Vector2(x, -y),
				new Vector2(-x, -y),
				new Vector2(-x, y)
		};
	}
}
