package com.slurpy.glowfighter.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.slurpy.glowfighter.parts.Part;

public abstract class Entity {
	
	protected Part[] parts;
	protected Body body;
	protected Color color;
	
	public Entity(Part[] parts, Vector2 body, Color color){
		this.parts = parts;
		
	}
	
	public abstract void update();
	
	public void draw(){
		for(Part part : parts){
			part.draw(body.getPosition(), body.getAngle());
		}
	}
	
	public abstract void hit(Entity other);
}
