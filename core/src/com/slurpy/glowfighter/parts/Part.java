package com.slurpy.glowfighter.parts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public abstract class Part {
	
	public boolean visible = true;
	
	public abstract void draw(Vector2 pos, float rot, Color color);
}
