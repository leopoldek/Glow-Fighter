package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.slurpy.glowfighter.Core;

public abstract class Entity {
	
	public Vector2 pos;
	public float rot;
	
	protected float width;
	protected Color color;
	protected Vector2[] parts;
	
	public Entity(Vector2 pos, float rot, float width, Color color, Vector2[] parts){
		this.pos = pos;
		this.rot = rot;
		this.width = width;
		this.color = color;
		this.parts = parts;
	}
	
	public abstract void update();
	
	public void draw(){
		Vector2 start = new Vector2();
		Vector2 end = new Vector2();
		for(int i = 0; i < parts.length; i += 2){
			start.set(parts[i]).rotate(rot).add(pos);
			end.set(parts[i+1]).rotate(rot).add(pos);
			
			Core.graphics.drawLine(start, end, width, color);
		}
	}
}
