package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;

public class CirclePart extends Part{
	
	public Vector2 pos;
	public float radius;
	
	private final Vector2 temp = new Vector2();
	
	public CirclePart(Vector2 pos, float radius) {
		this.pos = pos;
		this.radius = radius;
	}

	@Override
	public void draw(Vector2 pos, float rot, Color color) {
		Core.graphics.drawCircle(temp.set(this.pos).add(pos), radius, color);
	}
}
