package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	
	public Vector2 pos;
	public float rot;
	
	protected Part[] parts;
	
	public Entity(Vector2 pos, float rot, Part[] parts){
		this.pos = pos;
		this.rot = rot;
		this.parts = parts;
	}
	
	public abstract void update();
	
	public void draw(){
		for(Part part : parts){
			part.draw(pos, rot);
		}
	}
}
